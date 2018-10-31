package com.github.dapeng.mockserver.controller;

import com.github.dapeng.mockserver.services.MockService;
import com.github.dapeng.openapi.utils.PostUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 1,2 两种形式是不带鉴权的请求形式
 * <p>
 * 3,4 是带鉴权的请求形式
 *
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-31 9:46 AM
 */
@RestController
@RequestMapping("/api")
public class MockController {
    private final MockService mockService;

    public MockController(MockService mockService) {
        this.mockService = mockService;
    }

    /**
     * 1
     */
    @PostMapping
    public String rest(@RequestParam String serviceName,
                       @RequestParam String version,
                       @RequestParam String methodName,
                       @RequestParam String parameter,
                       HttpServletRequest request) {
        return processRequest(serviceName,
                version, methodName, parameter, request);
    }

    /**
     * 2
     */
    @PostMapping(value = "/{serviceName}/{version}/{methodName}")
    public String rest1(@PathVariable String serviceName,
                        @PathVariable String version,
                        @PathVariable String methodName,
                        @RequestParam String parameter,
                        HttpServletRequest request) {
        return processRequest(serviceName,
                version, methodName, parameter, request);

    }

    /**
     * 3
     */
    @PostMapping(value = "/{apiKey}")
    public String authRest(@RequestParam String serviceName,
                           @RequestParam String version,
                           @RequestParam String methodName,
                           @RequestParam String parameter,
                           HttpServletRequest request) {
        return processRequest(serviceName,
                version, methodName, parameter, request);
    }

    /**
     * 4
     */
    @PostMapping(value = "/{serviceName}/{version}/{methodName}/{apiKey}")
    public String authRest1(@PathVariable String serviceName,
                            @PathVariable String version,
                            @PathVariable String methodName,
                            @RequestParam String parameter,
                            HttpServletRequest request) {
        return processRequest(serviceName,
                version, methodName, parameter, request);

    }


    private String processRequest(String serviceName,
                                  String version,
                                  String methodName,
                                  String parameter,
                                  HttpServletRequest request) {
        //1.mock
        String mockJsonResp = mockService.mock(serviceName, version, methodName, parameter, request);
        if (mockJsonResp != null) {
            return mockJsonResp;
        }
        //2.获取元数据的假数据信息
        return PostUtil.post(serviceName, version, methodName, parameter, request);
    }
}
