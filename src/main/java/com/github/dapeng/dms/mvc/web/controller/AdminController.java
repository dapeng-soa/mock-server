package com.github.dapeng.dms.mvc.web.controller;

import com.github.dapeng.core.metadata.Method;
import com.github.dapeng.core.metadata.Service;
import com.github.dapeng.core.metadata.Struct;
import com.github.dapeng.core.metadata.TEnum;
import com.github.dapeng.dms.mvc.entity.Mock;
import com.github.dapeng.dms.mvc.entity.MockServiceInfo;
import com.github.dapeng.dms.mvc.services.MockService;
import com.github.dapeng.dms.mvc.vo.MockMethodVo;
import com.github.dapeng.dms.mvc.vo.MockServiceVo;
import com.github.dapeng.dms.mvc.vo.MockVo;
import com.github.dapeng.dms.util.Resp;
import com.github.dapeng.dms.util.RespEnum;
import com.github.dapeng.json.OptimizedMetadata;
import com.github.dapeng.openapi.cache.ServiceCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-31 4:13 PM
 */
@Api("Mock数据增删该查相关API")
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final MockService mockService;

    public AdminController(MockService mockService) {
        this.mockService = mockService;
    }

    @ApiOperation(value = "显示目前已有的Mock服务Service")
    @RequestMapping("/listServices")
    public Object listMockService(Model model) {
        List<MockServiceInfo> mockServiceList = mockService.findMockServiceList();
        return mockServiceList.stream().map(serviceInfo -> {
            List<MockVo> mockVoList = transferMockVo(mockService.findMockByServiceId(serviceInfo.getId()));
            String serviceName = serviceInfo.getServiceName();
            String simpleService = serviceName.substring(serviceName.lastIndexOf(".") + 1);
            return new MockServiceVo(serviceInfo.getId(), serviceInfo.getServiceName(), simpleService, mockVoList);
        }).collect(Collectors.toList());
    }


    @ApiOperation(value = "显示指定服务下面的已配置Mock的方法或者接口", notes = "某个Service微服务下的已Mock方法列表")
    @PostMapping(value = "/listMethods")
    public Object listMethodsByService(@RequestParam long serviceId) {
        List<Mock> mockList = mockService.findMockByServiceId(serviceId);
        //lambda List进行分组
        Map<String, List<Mock>> methodMap = mockList.stream().collect(Collectors.groupingBy(Mock::getMethodName));

        List<MockMethodVo> methodVoList = new ArrayList<>();
        methodMap.forEach((method, mocks) -> {
            Mock mock = mocks.get(0);
            MockMethodVo methodVo = new MockMethodVo(mock.getServiceId(), mock.getServiceName(),
                    mock.getMethodName(), mock.getHttpMethod(), "", mocks);
            methodVoList.add(methodVo);
        });
        return methodVoList;
    }






    /**
     * 显示指定 mock-key 下面的 mock 规则
     */

    @GetMapping("/listMethods")
    public Object findMockDataByMockKey(@PathVariable String service, @PathVariable String method,
                                        @PathVariable String version) {

        try {
            return mockService.findMockDataByMockKey(service, method, version);
        } catch (Exception e) {
            log.error("FindMockDataByMockKey Failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Resp.of(RespEnum.ERROR.getCode(),
                    "FindMockDataByMockKey Failed: " + e.getMessage()));
        }
    }

    @ApiOperation(value = "添加某一个方法的mock规则", notes = "注意要精确到一个方法然后进行添加")
    @ApiImplicitParams({@ApiImplicitParam(name = "service", value = "服务名称", dataType = "String"),
            @ApiImplicitParam(name = "method", value = "方法名称", dataType = "String"),
            @ApiImplicitParam(name = "version", value = "版本号", dataType = "String"),
            @ApiImplicitParam(name = "mockExpress", value = "mock匹配表达式", dataType = "String"),
            @ApiImplicitParam(name = "mockData", value = "mock需要返回的数据", dataType = "String")
    })
    @PostMapping("/add")
    public ResponseEntity addMockData(@RequestParam String service, @RequestParam String method, @RequestParam String version,
                                      @RequestParam String mockExpress, @RequestParam String mockData) {
        try {

            mockService.addMockInfo(service, method, version, mockExpress, mockData);
            return ResponseEntity.ok(Resp.of(RespEnum.OK));
        } catch (JSONException e) {
            log.error("Json Schema 解析失败，请检查格式: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Resp.of(RespEnum.ERROR.getCode(),
                            "Json Schema 解析失败，请检查格式: " + e.getMessage()));
        }
    }


    /**
     * 优先级修改。默认规则是 从下往上进行移动。
     * a     =>     b
     * b     =>     a
     *
     * @param frontId 要修改的mock id
     * @param belowId 需要排到某条规则之前的，那条规则的 mock id
     * @return ResponseEntity
     */
    @ApiOperation(value = "修改同一MockKey下的Mock规则优先级", notes = "注意传入两个规则的ID即可，每次只可以修改一个规则的优先级")
    @ApiImplicitParams({@ApiImplicitParam(name = "frontId", value = "修改的规则需要移动的规则id之上的规则ID", dataType = "String"),
            @ApiImplicitParam(name = "belowId", value = "需要修改的规则的Id", dataType = "String")
    })
    @PostMapping("/modify/priority")
    public ResponseEntity modifyMockPriority(@RequestParam long frontId, @RequestParam long belowId) {
        try {
            mockService.modifyMockPriority(frontId, belowId);
            return ResponseEntity.ok(Resp.of(RespEnum.OK));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Resp.of(RespEnum.ERROR.getCode(), e.getMessage()));
        }
    }


    @RequestMapping(value = "findmethod/{serviceName}/{version}/{methodName}", method = RequestMethod.GET)
    @ResponseBody
    public Method findMethod(@PathVariable String serviceName, @PathVariable String version, @PathVariable String methodName) {
        OptimizedMetadata.OptimizedService optimizedService = ServiceCache.getService(serviceName, version);

        return optimizedService.getMethodMap().get(methodName);
    }

    @RequestMapping(value = "struct/{serviceName}/{version}/{ref}", method = RequestMethod.GET)
    @Transactional(rollbackFor = Exception.class)
    public String struct(HttpServletRequest request, @PathVariable String serviceName, @PathVariable String version, @PathVariable String ref) {
        OptimizedMetadata.OptimizedService optimizedService = ServiceCache.getService(serviceName, version);

        request.setAttribute("struct", optimizedService.getOptimizedStructs().get(ref).getStruct());
        request.setAttribute("service", optimizedService.getService());
        request.setAttribute("structs", optimizedService.getService().getStructDefinitions());
        return "api/struct";
    }

    @RequestMapping(value = "findstruct/{serviceName}/{version}/{fullStructName}", method = RequestMethod.GET)
    @ResponseBody
    public Struct findStruct(@PathVariable String serviceName, @PathVariable String version, @PathVariable String fullStructName) {
        OptimizedMetadata.OptimizedService optimizedService = ServiceCache.getService(serviceName, version);

        return optimizedService.getOptimizedStructs().get(fullStructName).getStruct();
    }

    @RequestMapping(value = "findEnum/{serviceName}/{version}/{ref}", method = RequestMethod.GET)
    @ResponseBody
    public TEnum findEnum(@PathVariable String serviceName, @PathVariable String version, @PathVariable String ref) {
        OptimizedMetadata.OptimizedService optimizedService = ServiceCache.getService(serviceName, version);

        return optimizedService.getEnumMap().get(ref);
    }

    /**
     * 返回枚举 json async
     *
     * @param request
     * @param serviceName
     * @param version
     * @param ref
     * @return
     */
    @RequestMapping(value = "enum/{serviceName}/{version}/{ref}", method = RequestMethod.GET)
    @Transactional(rollbackFor = Exception.class)
    public String anEnum(HttpServletRequest request, @PathVariable String serviceName, @PathVariable String version, @PathVariable String ref) {
        OptimizedMetadata.OptimizedService optimizedService = ServiceCache.getService(serviceName, version);

        request.setAttribute("anEnum", optimizedService.getEnumMap().get(ref));

        request.setAttribute("service", optimizedService.getService());
        request.setAttribute("enums", optimizedService.getService().getEnumDefinitions());
        return "api/enum";
    }


    @RequestMapping(value = "test/{serviceName}/{version}/{methodName}", method = RequestMethod.GET)
    @Transactional(rollbackFor = Exception.class)
    public String goTest(HttpServletRequest request, @PathVariable String serviceName, @PathVariable String version, @PathVariable String methodName) {
        OptimizedMetadata.OptimizedService optimizedService = ServiceCache.getService(serviceName, version);

        request.setAttribute("service", optimizedService.getService());
        request.setAttribute("method", optimizedService.getMethodMap().get(methodName));
        request.setAttribute("services", ServiceCache.getServices().values()
                .stream().map(x -> x.getService()).collect(Collectors.toList()));
        return "api/test";
    }

    @RequestMapping(value = "findService/{serviceName}/{version}", method = RequestMethod.GET)
    @ResponseBody
    public Service findService(@PathVariable String serviceName, @PathVariable String version) {
        return ServiceCache.getService(serviceName, version).getService();
    }

    @RequestMapping(value = "findServiceAfterRefresh/{serviceName}/{version}/{refresh}", method = RequestMethod.GET)
    @ResponseBody
    public Service findService(@PathVariable String serviceName, @PathVariable String version, @PathVariable boolean refresh) {
        /*if (refresh) {
            serviceCache.reloadServices();
        }*/
        return ServiceCache.getService(serviceName, version).getService();
    }


    /**
     * transfer Mock POJO to MockVo
     */
    private List<MockVo> transferMockVo(List<Mock> mockList) {
        return mockList.stream().map(mock -> {
            String service = mock.getServiceName();
            String simpleService = service.substring(service.lastIndexOf(".") + 1);
            return new MockVo(service, simpleService, mock.getMethodName(), mock.getVersion(), mock.getHttpMethod(),
                    mock.getMockExpress(), mock.getData(), mock.getSort());
        }).collect(Collectors.toList());
    }
}
