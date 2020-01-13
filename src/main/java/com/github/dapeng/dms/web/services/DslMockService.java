package com.github.dapeng.dms.web.services;

import com.github.dapeng.dms.util.Resp;
import com.github.dapeng.dms.web.entity.*;
import com.github.dapeng.dms.web.vo.request.*;
import com.github.dapeng.dms.web.vo.response.*;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.*;

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


    //实体管理者
    private final EntityManager entityManager;
    //JPA查询工厂
    private JPAQueryFactory queryDsl;

    //2020.1.4更新m -rf
    private static final QVideo qVideo = QVideo.video;

//    private static final QVi qMetadata = QMockMetadata.mockMetadata;

    public DslMockService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        queryDsl = new JPAQueryFactory(entityManager);
        log.info("init JPAQueryFactory successfully");
    }


    public Resp queryVideoByCondition(QueryVideoRequest request) {
        DmsPageReq dmsPage = request.getPageRequest();
        //查询并返回结果集
        JPAQuery<Video> videoQuery = queryDsl.selectFrom(qVideo);

        if (StringUtils.isNotBlank(request.getVideoName())) {
            videoQuery.where(qVideo.videoName.like("%" + request.getVideoName() + "%"));
        }

        if (dmsPage != null) {
            videoQuery.offset(dmsPage.getStart()).limit(dmsPage.getLimit());
        }
        QueryResults<Video> results = videoQuery.orderBy(qVideo.createdAt.desc()).fetchResults();

        // fetch data
        List<Video> videoList = results.getResults();

        if (dmsPage != null) {
            DmsPageResp dmsPageResp = new DmsPageResp(dmsPage.getStart(), dmsPage.getLimit(), results.getTotal());
            return Resp.success(new ListVideoResponse(videoList, dmsPageResp));
        }
        return Resp.success(new ListVideoResponse(videoList));
    }


    public Resp queryVideoById(QueryDetailVideoRequest request) {
        //查询并返回结果集
        Video video = queryDsl.selectFrom(qVideo)
                .where(qVideo.id.eq(request.getVideoId()))
                .fetchFirst();

        if (video != null) {
            return Resp.success(video);
        }
        return Resp.success(new Video("Unknown", "Unknown"));
    }
}
