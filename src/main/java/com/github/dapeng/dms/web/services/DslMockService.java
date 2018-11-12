package com.github.dapeng.dms.web.services;

import com.github.dapeng.dms.util.Resp;
import com.github.dapeng.dms.web.entity.*;
import com.github.dapeng.dms.web.entity.MockService;
import com.github.dapeng.dms.web.repository.MockMethodRepository;
import com.github.dapeng.dms.web.util.MockException;
import com.github.dapeng.dms.web.vo.MockMethodVo;
import com.github.dapeng.dms.web.vo.MockServiceVo;
import com.github.dapeng.dms.web.vo.request.*;
import com.github.dapeng.dms.web.vo.response.DmsPageResp;
import com.github.dapeng.dms.web.vo.response.ListServiceResp;
import com.github.dapeng.dms.web.vo.response.QueryMethodResp;
import com.github.dapeng.dms.web.vo.response.QueryMockResp;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

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
    //实体管理者
    private final EntityManager entityManager;
    //JPA查询工厂
    private JPAQueryFactory queryDsl;

    public DslMockService(MockMethodRepository methodRepository, EntityManager entityManager) {
        this.methodRepository = methodRepository;
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
     *
     * @param request
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
     *
     * @param serviceId serviceID
     */
    private long queryMockMethodCount(long serviceId) {
        QMock qMock = QMock.mock;
        return queryDsl
                .selectFrom(qMock)
                .where(qMock.serviceId.eq(serviceId))
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
     *
     * @param service serviceName
     * @param method  methodName
     */
    /*public QueryMockResp listMockExpress(String service, String method) {
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




        QMock qMock = QMock.mock;
        List<Mock> fetch = queryDsl.selectFrom(qMock)
                .where(qMock.serviceName.eq(service).and(qMock.methodName.eq(method)))
                .fetch();


        return null;
    }*/




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
