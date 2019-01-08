package com.github.dapeng.dms.web.controller;

import com.github.dapeng.dms.mock.metadata.MetaMemoryCache;
import com.github.dapeng.dms.mock.metadata.MetadataUtils;
import com.github.dapeng.dms.mock.request.ResponseType;
import com.github.dapeng.dms.util.CommonUtil;
import com.github.dapeng.dms.util.Pair;
import com.github.dapeng.dms.util.Resp;
import com.github.dapeng.dms.util.RespUtil;
import com.github.dapeng.dms.web.services.ApiService;
import com.github.dapeng.dms.web.vo.request.ApiSamplesRequest;
import com.github.dapeng.dms.web.vo.request.ApiSiteRequest;
import com.github.dapeng.dms.web.vo.response.ApiSiteResponse;
import com.github.dapeng.json.OptimizedMetadata;
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

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
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


    /**
     * 5. for api test
     */
    @ApiOperation(value = "Mock Server 测试站点")
    @PostMapping(value = "/requestForApi")
    public Object apiSite(@RequestBody ApiSiteRequest apiRequest, HttpServletRequest request) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            String serviceName = CommonUtil.notNullRet(apiRequest.getServiceName(), "API请求，服务名不能为空");
            String methodName = CommonUtil.notNullRet(apiRequest.getMethodName(), "API请求，方法名不能为空");
            String version = CommonUtil.notNullRet(apiRequest.getVersion(), "API请求，版本号不能为空");
            String parameter = CommonUtil.notNullRet(apiRequest.getParameter(), "API请求，parameter不能为空");
            Pair<ResponseType, String> responsePair = doProcessRequest(serviceName, version, methodName, parameter, request);
            ApiSiteResponse response = new ApiSiteResponse(responsePair.getKey().getName(), responsePair.getValue());
            return Resp.success(response);
        } catch (Exception e) {
            log.error("apiSite Error: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/getRequestSamples")
    public Object getRequestSamples(@RequestBody ApiSamplesRequest samplesRequest) {
        try {
            String serviceName = CommonUtil.notNullRet(samplesRequest.getServiceName(), "API请求，服务名不能为空");
            String methodName = CommonUtil.notNullRet(samplesRequest.getMethodName(), "API请求，方法名不能为空");
            String version = CommonUtil.notNullRet(samplesRequest.getVersion(), "API请求，版本号不能为空");

            //2.获取元数据的假数据信息
            OptimizedMetadata.OptimizedService service = MetaMemoryCache.getFullServiceMap().get(CommonUtil.combine(serviceName, version));
            String requestSamples = MetadataUtils.getMethodRequestJson(service, serviceName, version, methodName);
            if (requestSamples != null) {
                log.info("request samples: {}", requestSamples);
                return Resp.success(requestSamples);
            }
            return Resp.success("{}");
        } catch (Exception e) {
            log.error("apiSite Error: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }


    private String processRequest(String serviceName, String version, String methodName,
                                  String parameter, HttpServletRequest request) {
        Pair<ResponseType, String> responsePair = doProcessRequest(serviceName, version, methodName, parameter, request);
        log.info("=== processRequest返回数据类型：{}", responsePair.getKey().getName());
        return responsePair.getValue();
    }

    private Pair<ResponseType, String> doProcessRequest(String serviceName, String version, String methodName,
                                                        String parameter, HttpServletRequest request) {
        //1.mock
        String mockJsonResp = apiService.doMock(serviceName, version, methodName, parameter, request);
        if (mockJsonResp != null) {
            String response = addType(mockJsonResp, ResponseType.MOCK.getName());
            log.info("response: {}", response);
            return new Pair<>(ResponseType.MOCK, response);
        }
        //2.获取元数据的假数据信息
        OptimizedMetadata.OptimizedService service = MetaMemoryCache.getFullServiceMap().get(CommonUtil.combine(serviceName, version));
        String mockMetadataResp = MetadataUtils.getMethodResponseJson(service, serviceName, version, methodName, ResponseType.STATIC_METADATA);
        if (mockMetadataResp != null) {
            log.info("response: {}", mockMetadataResp);
            return new Pair<>(ResponseType.STATIC_METADATA, mockMetadataResp);
        }
        //3.获取元数据数据信息
        String metadataResp = MetadataUtils.getServiceResponse(serviceName, methodName, version);
        if (metadataResp != null) {
            log.info("response: {}", metadataResp);
            return new Pair<>(ResponseType.REAL_DATA, metadataResp);
        }
        return new Pair<>(ResponseType.NO_DATA, "Internal Server Error,Mock和元数据信息均不存在");
    }

    private String addType(String json, String type) {
        StringBuilder builder = new StringBuilder();
        String substring = json.substring(0, json.lastIndexOf("}"));
        builder.append(substring);
        builder.append(",\"requestType\":\"").append(type).append("\"");
        builder.append("}");
        return builder.toString();
    }
}
