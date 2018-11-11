package com.github.dapeng.dms.web.services;

import com.github.dapeng.dms.util.Resp;
import com.github.dapeng.dms.web.entity.*;
import com.github.dapeng.dms.web.entity.MockService;
import com.github.dapeng.dms.web.repository.MetadataRepository;
import com.github.dapeng.dms.web.repository.MockRepository;
import com.github.dapeng.dms.web.repository.MockServiceRepository;
import com.github.dapeng.dms.web.vo.MockMethodVo;
import com.github.dapeng.dms.web.vo.MockServiceVo;
import com.github.dapeng.dms.web.vo.MockVo;
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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.PageRequest;
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

    private final MockRepository mockRepository;
    private final MockServiceRepository mockServiceRepository;
    private final MetadataRepository metadataRepository;
    //实体管理者
    private final EntityManager entityManager;
    //JPA查询工厂
    private JPAQueryFactory queryFactory;

    public DslMockService(MockRepository mockRepository, MockServiceRepository mockServiceRepository,
                          MetadataRepository metadataRepository, EntityManager entityManager) {
        this.mockRepository = mockRepository;
        this.mockServiceRepository = mockServiceRepository;
        this.metadataRepository = metadataRepository;
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
        //使用 querydsl 查询
        QMockService qService = QMockService.mockService;
        //查询并返回结果集
        JPAQuery<MockService> serviceQuery = queryFactory.selectFrom(qService);

        if (request.getSimpleName() != null) {
            serviceQuery.where(qService.serviceName.like("%" + request.getSimpleName() + "%"));
        }
        if (request.getServiceId() != null) {
            serviceQuery.where(qService.id.eq(request.getServiceId()));
        }

        if (request.getPageRequest() != null) {
            DmsPageReq dmsPage = request.getPageRequest();
            //分页排序
//            PageRequest pageRequest = PageRequest.of(dmsPage.getStart(), dmsPage.getLimit());
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

        if (request.getPageRequest() != null) {
            DmsPageReq page = request.getPageRequest();
            DmsPageResp dmsPageResp = new DmsPageResp(page.getStart(), page.getLimit(), results.getTotal());
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
        QMockMethod qMethod = QMockMethod.mockMethod;
        JPAQuery<MockMethod> serviceQuery = queryFactory.selectFrom(qMethod);

        if (request.getServiceName() != null) {
            serviceQuery.where(qMethod.service.like("%" + request.getServiceName() + "%"));
        }
        if (request.getMethodName() != null) {
            serviceQuery.where(qMethod.method.eq(request.getMethodName()));
        }

        if (request.getPageRequest() != null) {
            DmsPageReq dmsPage = request.getPageRequest();
            //分页排序
            PageRequest pageRequest = PageRequest.of(dmsPage.getStart(), dmsPage.getLimit());
            serviceQuery.offset(pageRequest.getOffset()).limit(pageRequest.getPageSize());
        }
        QueryResults<MockMethod> results = serviceQuery.orderBy(qMethod.id.asc()).fetchResults();
        log.info("MockMethod results: {}", results.toString());

        List<MockMethodVo> mockMethodVoList = results.getResults().stream()
                .map(m -> new MockMethodVo(m.getId(), m.getService(), m.getMethod(), m.getRequestType(), m.getUrl()))
                .collect(Collectors.toList());

        if (request.getPageRequest() != null) {
            DmsPageResp dmsPageResp = new DmsPageResp(results.getOffset(), results.getLimit(), results.getTotal());
            return new QueryMethodResp(mockMethodVoList, dmsPageResp);
        }
        return new QueryMethodResp(mockMethodVoList);
    }

    public long queryMockMethodCount(long serviceId) {
        QMock qMock = QMock.mock;
        return queryFactory
                .selectFrom(qMock)
                .where(qMock.serviceId.eq(serviceId))
                .fetchCount();
    }


    /**
     * transfer Mock POJO to MockVo
     */
    private List<MockVo> transferMockVo(List<Mock> mockList) {
        return mockList.stream().map(mock -> {
            String service = mock.getServiceName();
            String simpleService = service.substring(service.lastIndexOf(".") + 1);
            return new MockVo(mock.getId(), mock.getServiceName(), mock.getMethodName(), mock.getVersion(),
                    mock.getHttpMethod(), mock.getMockExpress(), mock.getData(), mock.getServiceId(), mock.getSort());
        }).collect(Collectors.toList());
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
