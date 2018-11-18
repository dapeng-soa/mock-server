package com.github.dapeng.dms.web.services;

import com.github.dapeng.core.metadata.Method;
import com.github.dapeng.dms.mock.matchers.validator.JsonSchemaValidator;
import com.github.dapeng.dms.mock.metadata.MetaMemoryCache;
import com.github.dapeng.dms.util.CommonUtil;
import com.github.dapeng.dms.util.Resp;
import com.github.dapeng.dms.web.entity.*;
import com.github.dapeng.dms.web.entity.MockService;
import com.github.dapeng.dms.web.entity.dto.MockDto;
import com.github.dapeng.dms.web.repository.MockMethodRepository;
import com.github.dapeng.dms.web.repository.MockRepository;
import com.github.dapeng.dms.web.repository.MockServiceRepository;
import com.github.dapeng.dms.web.util.MockException;
import com.github.dapeng.dms.web.util.MockUtils;
import com.github.dapeng.dms.web.vo.MetadataVo;
import com.github.dapeng.dms.web.vo.MockMethodVo;
import com.github.dapeng.dms.web.vo.MockServiceVo;
import com.github.dapeng.dms.web.vo.MockVo;
import com.github.dapeng.dms.web.vo.request.*;
import com.github.dapeng.dms.web.vo.response.*;
import com.github.dapeng.json.OptimizedMetadata;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.dapeng.dms.util.Constants.DEFAULT_SORT_NUM;

/**
 * shuold start transaction if use update/delete/create
 * <a href="https://zhuanlan.zhihu.com/p/24778422?refer=dreawer">JPA QueryDsl 操作</a>
 *
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-09 4:29 PM
 */
@Service
@Slf4j
@Transactional
public class DslMockService implements InitializingBean {


    private final MockServiceRepository serviceRepository;
    private final MockMethodRepository methodRepository;
    private final MockRepository mockRepository;
    //实体管理者
    private final EntityManager entityManager;
    //JPA查询工厂
    private JPAQueryFactory queryDsl;

    private static final QMockService qService = QMockService.mockService;
    private static final QMockMethod qMethod = QMockMethod.mockMethod;
    private static final QMock qMock = QMock.mock;
    private static final QMockMetadata qMetadata = QMockMetadata.mockMetadata;

    public DslMockService(MockServiceRepository serviceRepository, MockMethodRepository methodRepository, MockRepository mockRepository, EntityManager entityManager) {
        this.serviceRepository = serviceRepository;
        this.methodRepository = methodRepository;
        this.mockRepository = mockRepository;
        this.entityManager = entityManager;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        queryDsl = new JPAQueryFactory(entityManager);
        log.info("init JPAQueryFactory successfully");
    }


    /**
     * 查询全部数据并根据id倒序
     */
    public Resp queryServiceByCondition(QueryServiceReq request) {
        DmsPageReq dmsPage = request.getPageRequest();
        //查询并返回结果集
        JPAQuery<MockService> serviceQuery = queryDsl.selectFrom(qService);

        if (StringUtils.isNotBlank(request.getSimpleName())) {
            serviceQuery.where(qService.serviceName.like("%" + request.getSimpleName() + "%"));
        }
        if (StringUtils.isNotBlank(request.getVersion())) {
            serviceQuery.where(qService.version.eq(request.getVersion()));
        }
        if (request.getServiceId() != null) {
            serviceQuery.where(qService.id.eq(request.getServiceId()));
        }

        if (dmsPage != null) {
            serviceQuery.offset(dmsPage.getStart()).limit(dmsPage.getLimit());
        }
        QueryResults<MockService> results = serviceQuery.orderBy(qService.createdAt.desc()).fetchResults();

        // fetch data
        List<MockServiceVo> mockServiceVoList = results.getResults().stream().map(info -> {
            long count = queryMockMethodCount(info.getId());
            String serviceName = info.getServiceName();
            String simpleService = serviceName.substring(serviceName.lastIndexOf(".") + 1);

            return new MockServiceVo(info.getId(), info.getServiceName(), simpleService,
                    info.getVersion(), info.getMetadataId(), count, info.getCreatedAt());
        }).collect(Collectors.toList());

        if (dmsPage != null) {
            DmsPageResp dmsPageResp = new DmsPageResp(dmsPage.getStart(), dmsPage.getLimit(), results.getTotal());
            return Resp.success(new ListServiceResp(mockServiceVoList, dmsPageResp));
        }
        return Resp.success(new ListServiceResp(mockServiceVoList));
    }

    /**
     * 获取所有服务全称
     */
    public List<String> listMockServiceName(Long id) {
        if (id != null) {
            log.info("请求listMockServiceName id:{}", id);
            return queryDsl.select(qService.serviceName).from(qService).where(qService.id.eq(id)).fetch();
        }
        Set<String> serviceSet = MetaMemoryCache.getFullServiceMap().values().stream()
                .map(s -> CommonUtil.combineMeta(s.getService().namespace, s.getService().name))
                .collect(Collectors.toSet());
        List<String> dbServiceList = queryDsl.select(qService.serviceName).from(qService).fetch();
        serviceSet.addAll(dbServiceList);
        return new ArrayList<>(serviceSet);
    }

    public List<String> listMockInterfacesName(String serviceName) {
        Set<String> methodSet = new HashSet<>();
        String metadataKey = CommonUtil.combine(serviceName, "1.0.0");
        OptimizedMetadata.OptimizedService metaService = MetaMemoryCache.getFullServiceMap().get(metadataKey);
        if (metaService != null) {
            List<String> methodNameList = metaService.getMethodMap().values().stream().map(Method::getName).collect(Collectors.toList());
            methodSet.addAll(methodNameList);
        }
        List<String> dbMethodList = queryDsl.select(qMethod.method).from(qMethod).where(qMethod.service.eq(serviceName)).fetch();
        methodSet.addAll(dbMethodList);
        return new ArrayList<>(methodSet);
    }


    /**
     * query methods
     */
    public QueryMethodResp queryMethodByCondition(QueryMethodReq request) {
        DmsPageReq dmsPage = request.getPageRequest();
        JPAQuery<MockMethod> serviceQuery = queryDsl.selectFrom(qMethod);
        if (request.getServiceId() != null) {
            serviceQuery.where(qMethod.serviceId.eq(request.getServiceId()));
        } else {
            if (request.getServiceName() != null) {
                serviceQuery.where(qMethod.service.like("%" + request.getServiceName() + "%"));
            }
            if (request.getMethodName() != null) {
                serviceQuery.where(qMethod.method.eq(request.getMethodName()));
            }
        }
        if (dmsPage != null) {
            serviceQuery.offset(dmsPage.getStart()).limit(dmsPage.getLimit());
        }
        QueryResults<MockMethod> results = serviceQuery.orderBy(qMethod.id.asc()).fetchResults();
        log.info("MockMethod results size: {}", results.getResults().size());

        List<MockMethodVo> mockMethodVoList = results.getResults().stream()
                .map(m -> {
                    long mockSize = queryDsl.selectFrom(qMock).where(qMock.methodId.eq(m.getId())).fetchCount();
                    return new MockMethodVo(m.getId(), m.getService(), m.getMethod(), m.getRequestType(), m.getUrl(), mockSize);
                })
                .collect(Collectors.toList());
        if (dmsPage != null) {
            DmsPageResp dmsPageResp = new DmsPageResp(dmsPage.getStart(), dmsPage.getLimit(), results.getTotal());
            return new QueryMethodResp(mockMethodVoList, dmsPageResp);
        }
        return new QueryMethodResp(mockMethodVoList);
    }

    /**
     * count
     */
    private long queryMockMethodCount(long serviceId) {
        return queryDsl
                .selectFrom(qMethod)
                .where(qMethod.serviceId.eq(serviceId))
                .fetchCount();
    }

    public Object queryMetadataById(String service) {
        List<MockMetadata> metadataList = queryDsl
                .selectFrom(qMetadata)
                .where(qMetadata.serviceName.eq(service))
                .fetch();

        return metadataList.get(0);
    }

    public void createMethod(CreateMethodReq request) {
        //是否已存在
        MockMethod mockMethod = queryDsl.selectFrom(qMethod)
                .where(qMethod.service.eq(request.getServiceName())
                        .and(qMethod.method.eq(request.getMethodName())))
                .fetchFirst();

        if (mockMethod != null) {
            throw new MockException("当前服务下已创建当前接口，请勿重复创建");
        }
        MockService mockService = queryDsl.selectFrom(qService).where(qService.serviceName.eq(request.getServiceName())).fetchFirst();
        if (mockService != null) {
            methodRepository.save(
                    new MockMethod(mockService.getId(), request.getServiceName(),
                            request.getMethodName(),
                            request.getRequestType(),
                            request.getUrl(),
                            new Timestamp(System.currentTimeMillis())));
        } else {
            throw new MockException("新增接口没有已对应的服务，请先创建Mock服务...");
        }
    }

    public void updateMethod(UpdateMethodReq request) {
        queryDsl.update(qMethod)
                .set(qMethod.service, request.getServiceName())
                .set(qMethod.method, request.getMethodName())
                .set(qMethod.requestType, request.getRequestType())
                .set(qMethod.url, request.getUrl())
                .where(qMethod.id.eq(request.getId()))
                .execute();
    }

    public void deleteMethod(Long id) {
        long count = queryDsl.selectFrom(qMethod).where(qMethod.id.eq(id)).fetchCount();
        if (count == 0) {
            throw new MockException("该Mock规则记录不存在");
        }
        queryDsl.delete(qMethod).where(qMethod.id.eq(id)).execute();
    }


    /**
     * 根据服务名 和 方法名 查询 Mock Express 数据
     */
    public QueryMockResp listMockExpress(QueryMockReq request) {
        DmsPageReq dmsPage = request.getPageRequest();
        JPAQuery<Mock> serviceQuery = queryDsl.selectFrom(qMock);
        serviceQuery.where(qMock.methodId.eq(request.getMethodId()));

        if (dmsPage != null) {
            serviceQuery.offset(dmsPage.getStart()).limit(dmsPage.getLimit());
        }
        QueryResults<Mock> results = serviceQuery.orderBy(qMock.sort.asc()).fetchResults();

        log.info("listMockExpress results size: {}", results.getResults().size());

        List<MockVo> mockList = results.getResults().stream()
                .map(m -> new MockVo(m.getId(), m.getMockExpress(), m.getData(), m.getSort()))
                .collect(Collectors.toList());

        if (dmsPage != null) {
            DmsPageResp dmsPageResp = new DmsPageResp(dmsPage.getStart(), dmsPage.getLimit(), results.getTotal());
            return new QueryMockResp(mockList, dmsPageResp);
        }

        return new QueryMockResp(mockList);
    }

    /**
     * 根据methodId查询 service and method name
     */
    public MockMethodFormResp getMockMethodForm(Long id) {
        return queryDsl.select(Projections.bean(MockMethodFormResp.class, qMethod.service, qMethod.method))
                .from(qMethod)
                .where(qMethod.id.eq(id)).fetchFirst();

    }

    /**
     * 创建 mock 基础服务,并关联已有元数据服务.
     */
    public void createService(CreateServiceReq request) {
        MockService mockService = queryDsl
                .selectFrom(qService)
                .where(qService.serviceName.eq(request.getServiceName())
                        .and(qService.version.eq(request.getVersion())))
                .fetchFirst();
        if (mockService != null) {
            throw new MockException("新增的服务已存在,请勿重复创建相同名称的服务");
        }
        Long metadataId = queryDsl.select(qMetadata.id).from(qMetadata).where(qMetadata.serviceName.eq(request.getServiceName())).fetchFirst();
        if (metadataId == null) metadataId = 0L;

        MockService service;
        if (request.getSimpleName() != null) {
            service = new MockService(request.getSimpleName(), request.getServiceName(), request.getVersion(), metadataId);
        } else {
            service = new MockService(request.getServiceName(), request.getVersion(), metadataId);
        }
        serviceRepository.save(service);
    }

    /**
     * 创建 mock 规则
     */
    public void createMockInfo(CreateMockReq request) throws JSONException {
        JsonSchemaValidator.matcher(request.getMockExpress());
        JsonSchemaValidator.matcher(request.getMockData());
        //convert
        String mockCompileJson = MockUtils.convertJsonValueToPatternJson(request.getMockExpress());

        List<MockDto> mockDtoList = queryDsl.select(Projections.bean(MockDto.class, qService.serviceName.as("service"), qMethod.method,
                qService.version, qMethod.requestType, qMethod.id.as("methodId"))).from(qService)
                .leftJoin(qMethod)
                .on(qService.id.eq(qMethod.serviceId))
                .where(qMethod.id.eq(request.getMethodId())).fetch();
        if (mockDtoList.size() == 1) {
            MockDto dto = mockDtoList.get(0);
            String mockKey = MockUtils.combineMockKey(dto.getService(), dto.getMethod(), dto.getVersion());
            QMock qMock = QMock.mock;
            Mock latestMock = queryDsl.selectFrom(qMock).where(qMock.mockKey.eq(mockKey)).orderBy(qMock.sort.desc()).fetchFirst();

            if (latestMock != null) {
                Mock newMock = new Mock(dto.getService(), dto.getMethod(), dto.getVersion(), dto.getRequestType(),
                        request.getMockExpress(), mockCompileJson, request.getMockData(), dto.getMethodId(),
                        latestMock.getSort() + DEFAULT_SORT_NUM, new Timestamp(System.currentTimeMillis()));
                mockRepository.save(newMock);
            } else {
                Mock newMock = new Mock(dto.getService(), dto.getMethod(), dto.getVersion(), dto.getRequestType(), request.getMockExpress(),
                        mockCompileJson, request.getMockData(), dto.getMethodId(), DEFAULT_SORT_NUM, new Timestamp(System.currentTimeMillis()));
                mockRepository.save(newMock);
            }
        } else {
            throw new MockException("指定的接口没有对应的服务信息");
        }
    }

    /**
     * update mock info
     */
    public void updateMockInfo(UpdateMockReq request) {
        JPAUpdateClause mockUpdate = queryDsl.update(qMock);
        if (request.getMockExpress() != null) {
            mockUpdate.set(qMock.mockExpress, request.getMockExpress());
        }
        if (request.getMockData() != null) {
            mockUpdate.set(qMock.data, request.getMockData());
        }
        mockUpdate.where(qMock.id.eq(request.getMockId())).execute();
    }

    /**
     * 删除服务
     */
    public void deleteService(Long id) {
        queryDsl.delete(qService).where(qService.id.eq(id)).execute();
    }

    public void deleteMockInfo(Long id) {
        long executeId = queryDsl.delete(qMock).where(qMock.id.eq(id)).execute();
        if (executeId != 1) {
            throw new MockException("根据Id删除Mock规则异常,可能数据并不存在");
        }
    }

    public QueryMetaResp queryMetadataByCondition(QueryMetaReq request) {
        DmsPageReq dmsPage = request.getPageRequest();
        //不查 metadata，提升效率
        JPAQuery<Tuple> metaQuery = queryDsl.select(
                qMetadata.id,
                qMetadata.simpleName,
                qMetadata.serviceName,
                qMetadata.version,
                qMetadata.type,
                qMetadata.createdAt,
                qMetadata.updatedAt
        ).from(qMetadata);

        if (request.getMetadataId() != null) {
            metaQuery.where(qMetadata.id.eq(request.getMetadataId()));
        } else {
            if (request.getServiceName() != null) {
                metaQuery.where(qMetadata.serviceName.like("%" + request.getServiceName() + "%"));
            }
            if (request.getVersion() != null) {
                metaQuery.where(qMetadata.version.eq(request.getVersion()));
            }
        }
        if (dmsPage != null) {
            metaQuery.offset(dmsPage.getStart()).limit(dmsPage.getLimit());
        }
        QueryResults<Tuple> queryResults = metaQuery.orderBy(qMetadata.createdAt.desc()).fetchResults();
        List<MetadataVo> metadataVoList = queryResults.getResults().stream().map(t -> {
            Long id = t.get(qMetadata.id);
            String simpleName = t.get(qMetadata.simpleName);
            String serviceName = t.get(qMetadata.serviceName);
            String version = t.get(qMetadata.version);
            int type = t.get(qMetadata.type);
            Timestamp createdAt = t.get(qMetadata.createdAt);
            Timestamp updatedAt = t.get(qMetadata.updatedAt);
            String metaKey = CommonUtil.combine(serviceName, version);
            OptimizedMetadata.OptimizedService optimizedService = MetaMemoryCache.getFullServiceMap().get(metaKey);
            int methodSize = 0;
            if (optimizedService != null) {
                methodSize = optimizedService.getService().methods.size();
            }
            return new MetadataVo(id, simpleName, serviceName, version, methodSize, createdAt);
        }).collect(Collectors.toList());
        log.info("Metadata service results size: {}", metadataVoList.size());

        //获取 metadata 的返回 List 信息
        /*List<MetadataVo> metadataVoList = metadataEntityList.stream()
                .map(m -> {
                    String serviceNmae = m.getServiceName();
                    String version = m.getVersion();
                    String metaKey = CommonUtil.combine(serviceNmae, version);
                    OptimizedMetadata.OptimizedService optimizedService = MetaMemoryCache.getFullServiceMap().get(metaKey);
                    int methodSize = 0;
                    if (optimizedService != null) {
                        methodSize = optimizedService.getService().methods.size();
                    }
                    return new MetadataVo(m.getId(), m.getSimpleName(), serviceNmae, version, methodSize, m.getCreatedAt());
                }).collect(Collectors.toList());*/

        if (dmsPage != null) {
            DmsPageResp dmsPageResp = new DmsPageResp(dmsPage.getStart(), dmsPage.getLimit(), queryResults.getTotal());
            return new QueryMetaResp(metadataVoList, dmsPageResp);
        }
        return new QueryMetaResp(metadataVoList);
    }

    public void updateMockPriority(UpdatePriorityReq request) {
        long begin = 1000L;
        List<Long> priorityList = request.getPriorityList();
        for (Long value : priorityList) {
            queryDsl.update(qMock).set(qMock.sort, begin++).where(qMock.id.eq(value)).execute();
        }
    }

    public void updateService(UpdateServiceReq request) {
        MockService existedService = queryDsl.selectFrom(qService).where(qService.id.eq(request.getId())).fetchFirst();
        if (existedService != null) {
            existedService.setSimpleName(request.getSimpleName());
            existedService.setServiceName(request.getServiceName());
            existedService.setVersion(request.getVersion());

            if (existedService.getMetadataId() != 0L) {
                MockMetadata existMeta = queryDsl.selectFrom(qMetadata)
                        .where(qMetadata.serviceName.eq(request.getServiceName())
                                .and(qMetadata.version.eq(request.getVersion()))).fetchFirst();
                if (existMeta != null) {
                    existedService.setMetadataId(existMeta.getId());
                } else {
                    existedService.setMetadataId(0L);
                }
            }
            serviceRepository.save(existedService);
        }
    }


}
