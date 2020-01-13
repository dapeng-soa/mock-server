package com.github.dapeng.dms.web.controller;

import com.github.dapeng.dms.util.Resp;
import com.github.dapeng.dms.util.RespUtil;
import com.github.dapeng.dms.web.entity.Video;
import com.github.dapeng.dms.web.services.DslMockService;
import com.github.dapeng.dms.web.util.AHCExecutor;
import com.github.dapeng.dms.web.vo.request.QueryDetailVideoRequest;
import com.github.dapeng.dms.web.vo.request.QueryVideoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Denim.leihz 2020-01-04 11:03 PM
 */
@RestController
@Slf4j
public class VideoController {

    @Autowired
    private HttpServletRequest request;

    private final DslMockService dslMockService;

    public VideoController(DslMockService dslMockService) {
        this.dslMockService = dslMockService;
    }

    @PostMapping("/admin/listVideos")
    public Object listVideoList(@RequestBody QueryVideoRequest requestVo) {
        try {
            return dslMockService.queryVideoByCondition(requestVo);
        } catch (Exception e) {
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }

    @PostMapping("/out/queryVideoById")
    public Object queryVideoById(@RequestBody QueryDetailVideoRequest requestVo) {
        String remoteAddr = request.getRemoteAddr();
        //http://ip.taobao.com/service/getIpInfo.php?ip=115.159.41.97

        if (remoteAddr.equals("115.159.41.97")) {
            remoteAddr = getRealIp();
        }

        String info = AHCExecutor.execute("http://ip.taobao.com/service/getIpInfo.php?ip=" + remoteAddr);
        log.info("请求视频 ID: {}, key: {}, from IP : {} ,message: \n {}",
                requestVo.getVideoId(), requestVo.getKey(), remoteAddr, info);

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

    private String getRealIp() {
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        log.info("GetRealIp ip: {}", ip);
        return ip;
    }

}
