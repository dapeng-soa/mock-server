package com.github.dapeng.dms.web.controller;

import com.github.dapeng.dms.util.Resp;
import com.github.dapeng.dms.util.RespUtil;
import com.github.dapeng.dms.web.entity.Video;
import com.github.dapeng.dms.web.services.DslMockService;
import com.github.dapeng.dms.web.vo.request.QueryDetailVideoRequest;
import com.github.dapeng.dms.web.vo.request.QueryVideoRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Denim.leihz 2020-01-04 11:03 PM
 */
@Api("Video数据")
@RestController
@Slf4j
public class VideoController {

    private final DslMockService dslMockService;

    public VideoController(DslMockService dslMockService) {
        this.dslMockService = dslMockService;
    }

    @ApiOperation(value = "list Videos")
    @PostMapping("/admin/listVideos")
    public Object listVideoList(@RequestBody QueryVideoRequest requestVo) {
        try {
            return dslMockService.queryVideoByCondition(requestVo);
        } catch (Exception e) {
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "QueryVideoById")
    @PostMapping("/out/queryVideoById")
    public Object queryVideoById(@RequestBody QueryDetailVideoRequest requestVo) {
        log.info("请求视频 ID: {}, key: {}", requestVo.getVideoId(), requestVo.getKey());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !authentication.getPrincipal().equals("anonymousUser")) {
            log.info("authentication: {}", authentication);
            return doQuery(requestVo);
        }
        Resp resp = doQuery(requestVo);

        if (((Video) resp.getSuccess()).getKey().equals(requestVo.getKey())) {
            return resp;
        } else {
            return Resp.error("", "");
        }
    }

    private Resp doQuery(QueryDetailVideoRequest requestVo) {
        try {
            return dslMockService.queryVideoById(requestVo);
        } catch (Exception e) {
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }

}
