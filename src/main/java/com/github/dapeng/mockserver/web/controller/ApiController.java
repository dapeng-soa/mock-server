package com.github.dapeng.mockserver.web.controller;

import com.github.dapeng.core.metadata.Method;
import com.github.dapeng.core.metadata.Service;
import com.github.dapeng.core.metadata.Struct;
import com.github.dapeng.core.metadata.TEnum;
import com.github.dapeng.json.OptimizedMetadata;
import com.github.dapeng.mockserver.entity.Mock;
import com.github.dapeng.mockserver.entity.MockServiceInfo;
import com.github.dapeng.mockserver.services.MockService;
import com.github.dapeng.mockserver.web.util.ServiceAnnotationsUtil;
import com.github.dapeng.mockserver.web.util.ServiceJsonUtil;
import com.github.dapeng.mockserver.web.vo.EventDto;
import com.github.dapeng.mockserver.web.vo.MockServiceVo;
import com.github.dapeng.mockserver.web.vo.MockVo;
import com.github.dapeng.openapi.cache.ServiceCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Api服务Controller
 *
 * @author leihuazhe
 * @date 2018-01-12 20:01
 */
@Controller
@RequestMapping(value = "api")
@Slf4j
public class ApiController {
    private final MockService mockService;

    public ApiController(MockService mockService) {
        this.mockService = mockService;
    }

    @ModelAttribute
    public void populateModel(Model model) {
        model.addAttribute("tagName", "api");
    }

    @RequestMapping(value = "index")
    public String listMockService(Model model) {
        List<MockServiceInfo> mockServiceList = mockService.findMockServiceList();
        List<MockServiceVo> mockServiceVoList = mockServiceList.stream().map(serviceInfo -> {
            List<MockVo> mockVoList = transferMockVo(mockService.findMockByName(serviceInfo.getServiceName()));

            String serviceName = serviceInfo.getServiceName();
            String simpleService = serviceName.substring(serviceName.lastIndexOf("."));
            return new MockServiceVo(serviceInfo.getServiceName(), simpleService, mockVoList);
        }).collect(Collectors.toList());
        model.addAttribute("mockServiceList", mockServiceVoList);
        return "api/mock-list";
    }


    @RequestMapping(value = "service/{serviceName}")
    public String mockMethodList(@PathVariable String serviceName, Model model) {
        List<MockVo> mockVoList = transferMockVo(mockService.findMockByServiceName(serviceName));

        model.addAttribute("mockVoList", mockVoList);

        log.info("请求service列表展示，当前服务实例为 {}");

        return "api/mock-method";
    }

    @RequestMapping(value = "method/{serviceName}/{version}/{methodName}", method = RequestMethod.GET)
    @Transactional(rollbackFor = Exception.class)
    public String method(HttpServletRequest request, @PathVariable String serviceName, @PathVariable String version, @PathVariable String methodName) {
        OptimizedMetadata.OptimizedService optimizedService = ServiceCache.getService(serviceName, version);
        Method seleted = optimizedService.getMethodMap().get(methodName);
        List<Method> methods = optimizedService.getService().getMethods();

        Collections.sort(methods, Comparator.comparing(Method::getName));
        List<EventDto> events = ServiceAnnotationsUtil.findEventsByMethod(seleted);

        request.setAttribute("events", events);
        request.setAttribute("service", optimizedService.getService());
        request.setAttribute("methods", methods);
        request.setAttribute("method", seleted);
        return "api/method";
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
     * 解析枚举信息，转为Json格式
     *
     * @param serviceName 服务名
     * @param version     版本号
     * @return Json 字符串
     * @author maple.lei
     */
    @RequestMapping(value = "enum/{serviceName}/{version}/jsonEnum", method = RequestMethod.GET)
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object getEnumJson(@PathVariable String serviceName, @PathVariable String version) {
        Service service = ServiceCache.getService(serviceName, version).getService();
        Map<String, Object> stringObjectMap = ServiceJsonUtil.executeJson(service);
        return stringObjectMap.get(ServiceJsonUtil.JSONOBJ);

    }

    @RequestMapping(value = "enum/{serviceName}/{version}/jsonEnumString", method = RequestMethod.GET)
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public String getEnumJsonString(@PathVariable String serviceName, @PathVariable String version) {
        Service service = ServiceCache.getService(serviceName, version).getService();
        Map<String, Object> stringObjectMap = ServiceJsonUtil.executeJson(service);

        return (String) stringObjectMap.get(ServiceJsonUtil.JSONSTR);
    }

    /**
     * transfer Mock POJO to MockVo
     */
    private List<MockVo> transferMockVo(List<Mock> mockList) {
        return mockList.stream().map(mock -> {
            String service = mock.getServiceName();
            String simpleService = service.substring(service.lastIndexOf("."));
            return new MockVo(service, simpleService, mock.getMethodName(), mock.getVersion(), mock.getHttpMethod(),
                    mock.getMockExpress(), mock.getData(), mock.getOrdered());
        }).collect(Collectors.toList());
    }
}
