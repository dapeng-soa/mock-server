package com.github.dapeng.dms.web.services;

import com.github.dapeng.dms.web.entity.MockServiceInfo;
import com.github.dapeng.dms.web.entity.QMockServiceInfo;
import com.github.dapeng.dms.web.repository.MetadataRepository;
import com.github.dapeng.dms.web.repository.MockRepository;
import com.github.dapeng.dms.web.repository.MockServiceRepository;
import com.github.dapeng.dms.web.vo.request.DmsPageReq;
import com.github.dapeng.dms.web.vo.request.ListServiceReq;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

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
     *
     * @return
     */
    public QueryResults<MockServiceInfo> queryByCondition(ListServiceReq request) {
        //使用 querydsl 查询
        QMockServiceInfo qService = QMockServiceInfo.mockServiceInfo;
        //查询并返回结果集
        JPAQuery<MockServiceInfo> serviceQuery = queryFactory.selectFrom(qService);

        if (request.getSimpleName() != null) {
            serviceQuery.where(qService.serviceName.like("%" + request.getSimpleName() + "%"));
        }
        if (request.getServiceId() != null) {
            serviceQuery.where(qService.id.eq(request.getServiceId()));
        }

        if (request.getPageRequest() != null) {
            DmsPageReq dmsPage = request.getPageRequest();
            //分页排序
            PageRequest pageRequest = PageRequest.of(dmsPage.getStart(), dmsPage.getLimit());
            serviceQuery.offset(pageRequest.getOffset()).limit(pageRequest.getPageSize());
        }
        QueryResults<MockServiceInfo> results = serviceQuery.orderBy(qService.id.asc()).fetchResults();
        log.info("results: {}", results.toString());
        return results;
    }








    /*

    public List<MockServiceInfo> queryByCondition(ListServiceReq request) {
        //使用 querydsl 查询
        QMockServiceInfo qService = QMockServiceInfo.mockServiceInfo;
        //查询并返回结果集
        JPAQuery<MockServiceInfo> serviceQuery = queryFactory.selectFrom(qService);

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
