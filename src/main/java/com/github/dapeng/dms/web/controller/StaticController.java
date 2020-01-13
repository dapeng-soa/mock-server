package com.github.dapeng.dms.web.controller;

import com.github.dapeng.dms.util.Resp;
import com.github.dapeng.dms.util.RespUtil;
import com.github.dapeng.dms.web.entity.Video;
import com.github.dapeng.dms.web.services.DslMockService;
import com.github.dapeng.dms.web.util.AHCExecutor;
import com.github.dapeng.dms.web.vo.request.QueryDetailVideoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Denim.leihz 2020-01-04 11:03 PM
 */
@Controller
@Slf4j
public class StaticController {

    @Autowired
    private HttpServletRequest request;

    private final DslMockService dslMockService;

    public StaticController(DslMockService dslMockService) {
        this.dslMockService = dslMockService;
    }


    @GetMapping("/out/video/{videoId}/{key}")
    //java.lang.IllegalStateException: Optional long parameter 'videoId' is present but cannot be translated into a null value due to being declared as a primitive type. Consider declaring it as object wrapper for the corresponding primitive type.
    //	at org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver.handleNullValue(AbstractNamedValueMethodArgumentResolver.java:244)
    //	at org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver.resolveArgument(AbstractNamedValueMethodArgumentResolver.java:114)
    //	at org.springframework.web.method.support.HandlerMethodArgumentResolverComposite.resolveArgument(HandlerMethodArgumentResolverComposite.java:124)
    //	at org.springframework.web.method.support.InvocableHandlerMethod.getMethodArgumentValues(InvocableHandlerMethod.java:161)
    //	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:131)
    //	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:102)
    //	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:891)
    //	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:797)
    //	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)
    //	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:991)
    //	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:925)
    //	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:974)
    //	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:866)
    public String queryVideoById(@PathVariable Long videoId, @PathVariable String key, Model model) {
        String remoteAddr = request.getRemoteAddr();
        //http://ip.taobao.com/service/getIpInfo.php?ip=115.159.41.97

        if (remoteAddr.equals("115.159.41.97")) {
            remoteAddr = getRealIp();
        }
        QueryDetailVideoRequest requestVo = new QueryDetailVideoRequest();
        requestVo.setVideoId(videoId);
        requestVo.setKey(key);

        String info = AHCExecutor.execute("http://ip.taobao.com/service/getIpInfo.php?ip=" + remoteAddr);
        log.info("请求视频 ID: {}, key: {}, from IP : {} ,message: \n {}",
                requestVo.getVideoId(), requestVo.getKey(), remoteAddr, info);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !authentication.getPrincipal().equals("anonymousUser")) {
            log.info("authentication: {}", authentication);
            Resp resp = doQuery(requestVo);
            model.addAttribute("videoUrl", ((Video) resp.getSuccess()).getVideoUrl());
            return "video";
        }

        Resp resp = doQuery(requestVo);
        if (((Video) resp.getSuccess()).getKey().equals(requestVo.getKey())) {
            model.addAttribute("videoUrl", ((Video) resp.getSuccess()).getVideoUrl());
            return "video";
        } else {
            model.addAttribute("videoUrl", "");
            return "video";
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
