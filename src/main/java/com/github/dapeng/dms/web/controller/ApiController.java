package com.github.dapeng.dms.web.controller;

import com.github.dapeng.dms.mock.metadata.MetadataUtils;
import com.github.dapeng.dms.web.services.MockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * ApiController
 *
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-31 4:13 PM
 */
@Api("Mock 服务API请求接口")
@RestController
@RequestMapping(value = "/api")
@Slf4j
public class ApiController {

    private final MockService mockService;

    public ApiController(MockService mockService) {
        this.mockService = mockService;
    }

    /**
     * 1
     */
    @ApiOperation(value = "前端请求后台API接口1，基于@RequestParam", notes = "类似于服务网关一样，这里请求一致，不携带API-KEY")
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
    @ApiOperation(value = "前端请求后台API接口2，基于@RequestParam", notes = "类似于服务网关一样，这里请求一致，不携带API-KEY")
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
    @ApiOperation(value = "前端请求后台API接口2，基于RESTFUL形式请求", notes = "类似于服务网关一样，这里请求一致，携带API-KEY")
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
    @ApiOperation(value = "前端请求后台API接口2，基于RESTFUL形式请求", notes = "类似于服务网关一样，这里请求一致，携带API-KEY")
    @PostMapping(value = "/{serviceName}/{version}/{methodName}/{apiKey}")
    public String authRest1(@PathVariable String serviceName,
                            @PathVariable String version,
                            @PathVariable String methodName,
                            @RequestParam String parameter,
                            HttpServletRequest request) {
        return processRequest(serviceName,
                version, methodName, parameter, request);

    }

    private String processRequest(String serviceName, String version, String methodName,
                                  String parameter, HttpServletRequest request) {
        //1.mock
        String mockJsonResp = mockService.mock(serviceName, version, methodName, parameter, request);
        if (mockJsonResp != null) {
            return mockJsonResp;
        }
        //2.获取元数据的假数据信息
        String metadataResp = MetadataUtils.getServiceResponse(serviceName, methodName, version);

        if (metadataResp != null) {
            return metadataResp;
        }
        return "Internal Server Error,Mock和元数据信息均不存在";
    }
}