package com.github.dapeng.dms.web.services;

import com.github.dapeng.dms.util.Resp;
import com.github.dapeng.dms.web.entity.*;
import com.github.dapeng.dms.web.entity.MockService;
import com.github.dapeng.dms.web.vo.MockMethodVo;
import com.github.dapeng.dms.web.vo.MockServiceVo;
import com.github.dapeng.dms.web.vo.request.DmsPageReq;
import com.github.dapeng.dms.web.vo.request.QueryMethodReq;
import com.github.dapeng.dms.web.vo.request.QueryServiceReq;
import com.github.dapeng.dms.web.vo.response.DmsPageResp;
import com.github.dapeng.dms.web.vo.response.ListServiceResp;
import com.github.dapeng.dms.web.vo.response.QueryMethodResp;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <a href="https://zhuanlan.zhihu.com/p/24778422?refer=dreawer">JPA QueryDsl 操作</a>
 *
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-09 4:29 PM
 */
@Service
@Slf4j
public class DslMockService implements InitializingBean {

    //实体管理者
    private final EntityManager entityManager;
    //JPA查询工厂
    private JPAQueryFactory queryFactory;

    public DslMockService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        queryFactory = new JPAQueryFactory(entityManager);
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
        JPAQuery<MockService> serviceQuery = queryFactory.selectFrom(qService);

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
        JPAQuery<MockMethod> serviceQuery = queryFactory.selectFrom(qMethod);

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
        return queryFactory
                .selectFrom(qMock)
                .where(qMock.serviceId.eq(serviceId))
                .fetchCount();
    }

    public Object queryMetadataById(String service) {
        QMockMetadata qMetadata = QMockMetadata.mockMetadata;
        List<MockMetadata> metadataList = queryFactory
                .selectFrom(qMetadata)
                .where(qMetadata.serviceName.eq(service))
                .fetch();

        return metadataList.get(0);
    }




    /*

    public List<MockService> queryServiceByCondition(QueryServiceReq request) {
        //使用 querydsl 查询
        QMockServiceInfo qService = QMockServiceInfo.mockServiceInfo;
        //查询并返回结果集
        JPAQuery<MockService> serviceQuery = queryFactory.selectFrom(qService);

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
