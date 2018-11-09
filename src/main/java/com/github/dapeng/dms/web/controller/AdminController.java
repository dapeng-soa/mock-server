package com.github.dapeng.dms.web.controller;

import com.github.dapeng.dms.dto.MockServiceDto;
import com.github.dapeng.dms.web.entity.Mock;
import com.github.dapeng.dms.web.entity.MockMetadata;
import com.github.dapeng.dms.web.entity.MockServiceInfo;
import com.github.dapeng.dms.web.services.DslMockService;
import com.github.dapeng.dms.web.services.MockService;
import com.github.dapeng.dms.web.vo.MockMethodVo;
import com.github.dapeng.dms.web.vo.MockServiceVo;
import com.github.dapeng.dms.web.vo.MockVo;
import com.github.dapeng.dms.util.Resp;
import com.github.dapeng.dms.util.RespEnum;
import com.github.dapeng.dms.web.vo.request.ListServiceRequest;
import com.github.dapeng.dms.web.vo.request.ServiceAddRequest;
import com.github.dapeng.dms.web.vo.response.DmsPageResp;
import com.github.dapeng.dms.web.vo.response.ListServiceResp;
import com.querydsl.core.QueryResults;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    private final DslMockService dslMockService;

    public AdminController(MockService mockService, DslMockService dslMockService) {
        this.mockService = mockService;
        this.dslMockService = dslMockService;
    }


    // ------------服务单元------------------------- //
    //                                             //
    //                                            //
    //------------------------------------------ //
    @ApiOperation(value = "显示目前已有的Mock服务Service")
    @PostMapping("/listServices")
    public Object listMockService(@RequestBody ListServiceRequest requestVo) {
        QueryResults<MockServiceInfo> results = dslMockService.queryByCondition(requestVo);
        DmsPageResp dmsPageResp = new DmsPageResp(results.getOffset(), results.getLimit(), results.getTotal());
        // fetch data
        List<MockServiceVo> mockServiceVoList = results.getResults().stream().map(serviceInfo -> {
            List<MockVo> mockList = transferMockVo(mockService.findMockByServiceId(serviceInfo.getId()));
            String serviceName = serviceInfo.getServiceName();
            String simpleService = serviceName.substring(serviceName.lastIndexOf(".") + 1);
            //metadataList
            List<MockMetadata> metadataList = mockService.findMetadataByServiceName(serviceInfo.getServiceName());
            return new MockServiceVo(serviceInfo.getId(), serviceInfo.getServiceName(), simpleService, metadataList, mockList);
        }).collect(Collectors.toList());
        return new ListServiceResp(mockServiceVoList, dmsPageResp);
    }

    @ApiOperation(value = "添加一个Mock服务", notes = "注意服务名包全名")
    @ApiResponse(code = 200, message = "返回添加成功后的MockServiceVo对象")
    @PostMapping("/addService")
    public Object addServiceInfo(@RequestBody ServiceAddRequest request) {
        try {
            return mockService.addServiceInfo(request);
        } catch (Exception e) {
            log.error("addService Error: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Resp.of(RespEnum.ERROR.getCode(), "addServiceInfo failed: " + e.getMessage()));
        }
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


    @ApiOperation(value = "添加某一个方法的mock规则", notes = "注意要精确到一个方法然后进行添加")
    @ApiImplicitParams({@ApiImplicitParam(name = "service", value = "服务名称", dataType = "String"),
            @ApiImplicitParam(name = "method", value = "方法名称", dataType = "String"),
            @ApiImplicitParam(name = "version", value = "版本号", dataType = "String"),
            @ApiImplicitParam(name = "mockExpress", value = "mock匹配表达式", dataType = "String"),
            @ApiImplicitParam(name = "mockData", value = "mock需要返回的数据", dataType = "String")
    })
    @PostMapping("/addMock")
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


    @ApiOperation(value = "修改Mock服务信息", notes = "注意传入MockServiceVo Json形式，ID不要改变")
    @PostMapping("/modify/service")
    public Object updateService(@RequestBody MockServiceVo mockServiceVo) {
        try {
            mockService.updateService(mockServiceVo);
            return ResponseEntity.ok(Resp.of(RespEnum.OK));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Resp.of(RespEnum.ERROR.getCode(), e.getMessage()));
        }
    }

    @ApiOperation(value = "修改Mock服务信息", notes = "注意传入MockServiceVo Json形式，ID不要改变")
    @PostMapping("/modify/mock")
    public Object updateMock(@RequestBody MockVo mockVo) {
        try {
            mockService.updateMock(mockVo);
            return ResponseEntity.ok(Resp.of(RespEnum.OK));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Resp.of(RespEnum.ERROR.getCode(), e.getMessage()));
        }
    }


    /**
     * transfer Mock POJO to MockVo
     */
    private List<MockVo> transferMockVo(List<Mock> mockList) {
        return mockList.stream().map(mock -> {
            String service = mock.getServiceName();
            String simpleService = service.substring(service.lastIndexOf(".") + 1);
            return new MockVo(mock.getId(), mock.getServiceName(), mock.getMethodName(), mock.getVersion(),
                    mock.getHttpMethod(), mock.getMockExpress(), mock.getData(), mock.getServiceId(), mock.getSort());
        }).collect(Collectors.toList());
    }
}
