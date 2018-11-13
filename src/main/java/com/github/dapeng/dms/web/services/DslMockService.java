package com.github.dapeng.dms.web.services;

import com.github.dapeng.dms.mock.matchers.validator.JsonSchemaValidator;
import com.github.dapeng.dms.util.Resp;
import com.github.dapeng.dms.web.entity.*;
import com.github.dapeng.dms.web.entity.MockService;
import com.github.dapeng.dms.web.entity.dto.MockDto;
import com.github.dapeng.dms.web.repository.MockMethodRepository;
import com.github.dapeng.dms.web.repository.MockRepository;
import com.github.dapeng.dms.web.util.MockException;
import com.github.dapeng.dms.web.util.MockUtils;
import com.github.dapeng.dms.web.vo.MockMethodVo;
import com.github.dapeng.dms.web.vo.MockServiceVo;
import com.github.dapeng.dms.web.vo.MockVo;
import com.github.dapeng.dms.web.vo.request.*;
import com.github.dapeng.dms.web.vo.response.*;
import com.querydsl.core.QueryResults;
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
import java.util.List;
import java.util.stream.Collectors;

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

    private final MockMethodRepository methodRepository;
    private final MockRepository mockRepository;
    //实体管理者
    private final EntityManager entityManager;
    //JPA查询工厂
    private JPAQueryFactory queryDsl;

    public DslMockService(MockMethodRepository methodRepository, MockRepository mockRepository, EntityManager entityManager) {
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
        //使用 queryDsl 查询
        QMockService qService = QMockService.mockService;
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
        QueryResults<MockService> results = serviceQuery.orderBy(qService.id.asc()).fetchResults();

        // fetch data
        List<MockServiceVo> mockServiceVoList = results.getResults().stream().map(serviceInfo -> {

            long count = queryMockMethodCount(serviceInfo.getId());
            String serviceName = serviceInfo.getServiceName();
            String simpleService = serviceName.substring(serviceName.lastIndexOf(".") + 1);
            return new MockServiceVo(serviceInfo.getId(), serviceInfo.getServiceName(),
                    simpleService, serviceInfo.getVersion(), null, count);
        }).collect(Collectors.toList());

        if (dmsPage != null) {
            DmsPageResp dmsPageResp = new DmsPageResp(dmsPage.getStart(), dmsPage.getLimit(), results.getTotal());
            return Resp.success(new ListServiceResp(mockServiceVoList, dmsPageResp));
        }
        return Resp.success(new ListServiceResp(mockServiceVoList));
    }

    /**
     * query methods
     */
    public QueryMethodResp queryMethodByCondition(QueryMethodReq request) {
        DmsPageReq dmsPage = request.getPageRequest();

        QMockMethod qMethod = QMockMethod.mockMethod;
        JPAQuery<MockMethod> serviceQuery = queryDsl.selectFrom(qMethod);

        if (request.getServiceName() != null) {
            serviceQuery.where(qMethod.service.like("%" + request.getServiceName() + "%"));
        }
        if (request.getMethodName() != null) {
            serviceQuery.where(qMethod.method.eq(request.getMethodName()));
        }

        if (dmsPage != null) {
            serviceQuery.offset(dmsPage.getStart()).limit(dmsPage.getLimit());
        }
        QueryResults<MockMethod> results = serviceQuery.orderBy(qMethod.id.asc()).fetchResults();
        log.info("MockMethod results size: {}", results.getResults().size());

        List<MockMethodVo> mockMethodVoList = results.getResults().stream()
                .map(m -> new MockMethodVo(m.getId(), m.getService(), m.getMethod(), m.getRequestType(), m.getUrl()))
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
        QMockMethod qMethod = QMockMethod.mockMethod;
        return queryDsl
                .selectFrom(qMethod)
                .where(qMethod.serviceId.eq(serviceId))
                .fetchCount();
    }

    public Object queryMetadataById(String service) {
        QMockMetadata qMetadata = QMockMetadata.mockMetadata;
        List<MockMetadata> metadataList = queryDsl
                .selectFrom(qMetadata)
                .where(qMetadata.serviceName.eq(service))
                .fetch();

        return metadataList.get(0);
    }

    public void createMethod(CreateMethodReq request) {
        methodRepository.save(
                new MockMethod(request.getServiceName(),
                        request.getMethodName(),
                        request.getRequestType(),
                        request.getUrl(),
                        new Timestamp(System.currentTimeMillis())));


    }

    public long updateMethod(UpdateMethodReq request) {
        QMockMethod qMethod = QMockMethod.mockMethod;
        return queryDsl.update(qMethod)
                .set(qMethod.service, request.getServiceName())
                .set(qMethod.method, request.getMethodName())
                .set(qMethod.requestType, request.getRequestType())
                .set(qMethod.url, request.getUrl())
                .set(qMethod.createdAt, new Timestamp(System.currentTimeMillis()))
                .where(qMethod.id.eq(request.getId()))
                .execute();
    }

    public long deleteMethod(Long id) {
        QMockMethod qMethod = QMockMethod.mockMethod;
        long count = queryDsl.selectFrom(qMethod).where(qMethod.id.eq(id)).fetchCount();
        if (count == 0) {
            throw new MockException("该Mock规则记录不存在");
        }
        return queryDsl.delete(qMethod).where(qMethod.id.eq(id)).execute();
    }


    /**
     * 根据服务名 和 方法名 查询 Mock Express 数据
     */
    public QueryMockResp listMockExpress(QueryMockReq request) {
        DmsPageReq dmsPage = request.getPageRequest();
        QMock qMock = QMock.mock;
        JPAQuery<Mock> serviceQuery = queryDsl.selectFrom(qMock);
        serviceQuery.where(qMock.methodId.eq(request.getMethodId()));

        if (dmsPage != null) {
            serviceQuery.offset(dmsPage.getStart()).limit(dmsPage.getLimit());
        }
        QueryResults<Mock> results = serviceQuery.orderBy(qMock.id.asc()).fetchResults();

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
        QMockMethod qMethod = QMockMethod.mockMethod;
        return queryDsl.select(Projections.bean(MockMethodFormResp.class, qMethod.service, qMethod.method))
                .from(qMethod)
                .where(qMethod.id.eq(id)).fetchFirst();

    }

    /**
     * 创建 mock 规则
     */
    public void createMockInfo(CreateMockReq request) throws JSONException {
        JsonSchemaValidator.matcher(request.getMockExpress());
        JsonSchemaValidator.matcher(request.getMockData());
        //convert
        String mockCompileJson = MockUtils.convertJsonValueToPatternJson(request.getMockExpress());

        QMockService qService = QMockService.mockService;
        QMockMethod qMethod = QMockMethod.mockMethod;

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
            throw new MockException("不存在或大于1");
        }
    }

    /**
     * update mock info
     */
    public void updateMockInfo(UpdateMockReq request) {
        QMock qMock = QMock.mock;
        JPAUpdateClause mockUpdate = queryDsl.update(qMock);
        if (request.getMockExpress() != null) {
            mockUpdate.set(qMock.mockExpress, request.getMockExpress());
        }
        if (request.getMockData() != null) {
            mockUpdate.set(qMock.data, request.getMockExpress());
        }
        mockUpdate.where(qMock.id.eq(request.getMockId())).execute();
    }




    /*

    public List<MockService> queryServiceByCondition(QueryServiceReq request) {
        //使用 querydsl 查询
        QMockServiceInfo qService = QMockServiceInfo.mockServiceInfo;
        //查询并返回结果集
        JPAQuery<MockService> serviceQuery = queryDsl.selectFrom(qService);

        //该Predicate为querydsl下的类,支持嵌套组装复杂查询条件
//        Predicate predicate = qService.id.longValue().lt(3).and(qService.serviceName.like("shanghai"));
//分页排序
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC,"id"));
        PageRequest pageRequest = new PageRequest(0,10,sort);
        Page<TCity> tCityPage = tCityRepository.findAll(predicate,pageRequest);


        if (request.getSimpleName() != null) {
            serviceQuery.where(qService.serviceName.like(request.getSimpleName()));
        }
        if (request.getServiceId() != null) {
            serviceQuery.where(qService.id.eq(request.getServiceId()));
        }


        if (request.getPageRequest() != null) {
            //
            DmsPageReq dmsPageRequest = request.getPageRequest();
//            Sort sort = new Sort(Sort.Direction.DESC,"createTime"); //创建时间降序排序
            Pageable pageable = PageRequest.of(dmsPageRequest.getStart(), dmsPageRequest.getLimit());
            serviceQuery.orderBy(qService.id.desc());
        }
        return serviceQuery.fetch();
    }


  Sort sort = Sort.by(Sort.Direction.DESC, dmsPage.getSortFields());
//                Predicate predicate = qService.id.longValue().lt(3).and(qService.serviceName.like("shanghai"));
//                serviceQuery.orderBy(new OrderSpecifier<Object>(Order.DESC, dmsPage.getSortFields()));
                pageRequest = PageRequest.of(dmsPage.getStart(), dmsPage.getLimit(), sort);

     */

}
