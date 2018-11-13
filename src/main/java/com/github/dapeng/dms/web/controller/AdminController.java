package com.github.dapeng.dms.web.controller;

import com.github.dapeng.dms.util.RestUtil;
import com.github.dapeng.dms.web.services.DslMockService;
import com.github.dapeng.dms.web.vo.MockServiceVo;
import com.github.dapeng.dms.web.vo.MockVo;
import com.github.dapeng.dms.util.Resp;
import com.github.dapeng.dms.util.RespUtil;
import com.github.dapeng.dms.web.vo.request.*;
import com.github.dapeng.dms.web.vo.response.QueryMethodResp;
import com.github.dapeng.dms.web.vo.response.QueryMockResp;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * todo 后期使用 Aspect 切面简化代码 并优化。
 *
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-31 4:13 PM
 */
@Api("Mock数据增删该查相关API")
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final com.github.dapeng.dms.web.services.MockService mockService;

    private final DslMockService dslMockService;

    public AdminController(com.github.dapeng.dms.web.services.MockService mockService, DslMockService dslMockService) {
        this.mockService = mockService;
        this.dslMockService = dslMockService;
    }

    @ApiOperation(value = "根据条件 List Service")
    @PostMapping("/listServices")
    public Object listMockService(@RequestBody QueryServiceReq requestVo) {
        try {
            return dslMockService.queryServiceByCondition(requestVo);
        } catch (Exception e) {
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "根据条件 List Service")
    @PostMapping("/queryMetadata")
    public Object queryMetadataById(String service) {
        try {
            return dslMockService.queryMetadataById(service);
        } catch (Exception e) {
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }


    @ApiOperation(value = "添加一个Mock服务", notes = "注意服务名包全名")
    @ApiResponse(code = 200, message = "返回添加成功后的MockServiceVo对象")
    @PostMapping("/addService")
    public Object addServiceInfo(@RequestBody ServiceAddRequest request) {
        try {
            MockServiceVo serviceVo = mockService.addServiceInfo(request);
            return Resp.success(serviceVo);
        } catch (Exception e) {
            log.error("addService Error: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }


    @ApiOperation(value = "根据条件 List Method")
    @PostMapping(value = "/listInterfaces")
    public Object listMethodsByService(@RequestBody QueryMethodReq request) {
        try {
            QueryMethodResp resp = dslMockService.queryMethodByCondition(request);
            return Resp.success(resp);
        } catch (Exception e) {
            log.error("addService Error: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "增加服务下的接口 Method")
    @PostMapping(value = "/createInterface")
    public Object createMethod(@RequestBody CreateMethodReq request) {
        try {
            dslMockService.createMethod(request);
            return Resp.success();
        } catch (Exception e) {
            log.error("createInterface Error: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "修改服务下的接口 Method")
    @PostMapping(value = "/updateInterface")
    public Object updateMethod(@RequestBody UpdateMethodReq request) {
        try {
            dslMockService.updateMethod(request);
            return Resp.success();
        } catch (Exception e) {
            log.error("updateInterface Error: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除接口")
    @PostMapping(value = "/deleteInterface")
    public Object deleteMethod(@RequestBody Map<String, String> params) {
        try {
            String id = params.get("id");
            RestUtil.notNull(id);
            dslMockService.deleteMethod(Long.valueOf(id));
            return Resp.success();
        } catch (Exception e) {
            log.error("deleteInterface Error: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "根据条件 List Mock Express")
    @PostMapping(value = "/listMockExpress")
    public Object listMockExpress(@RequestBody QueryMockReq request) {
        try {
            RestUtil.notNull(request.getMethodId(), "请求MockExpress规则时,methodId 不能为空");
            QueryMockResp resp = dslMockService.listMockExpress(request);
            return Resp.success(resp);
        } catch (Exception e) {
            log.error("listMockExpress Error: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }


    @ApiOperation(value = "添加某一个方法的mock规则", notes = "注意要精确到一个方法然后进行添加")
    @ApiImplicitParams({@ApiImplicitParam(name = "service", value = "服务名称", dataType = "String"),
            @ApiImplicitParam(name = "method", value = "方法名称", dataType = "String"),
            @ApiImplicitParam(name = "version", value = "版本号", dataType = "String"),
            @ApiImplicitParam(name = "mockExpress", value = "mock匹配表达式", dataType = "String"),
            @ApiImplicitParam(name = "mockData", value = "mock需要返回的数据", dataType = "String")
    })
    @PostMapping("/addMock")
    public Object addMockData(@RequestParam String service, @RequestParam String method, @RequestParam String version,
                              @RequestParam String mockExpress, @RequestParam String mockData) {
        try {
            mockService.addMockInfo(service, method, version, mockExpress, mockData);
            return Resp.success(RespUtil.OK);
        } catch (JSONException e) {
            log.error("Json Schema 解析失败，请检查格式: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
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
    public Object modifyMockPriority(@RequestParam long frontId, @RequestParam long belowId) {
        try {
            mockService.modifyMockPriority(frontId, belowId);
            return Resp.success(RespUtil.OK);
        } catch (Exception e) {
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }


    @ApiOperation(value = "修改Mock服务信息", notes = "注意传入MockServiceVo Json形式，ID不要改变")
    @PostMapping("/modify/service")
    public Object updateService(@RequestBody MockServiceVo mockServiceVo) {
        try {
            mockService.updateService(mockServiceVo);
            return Resp.success(RespUtil.OK);
        } catch (Exception e) {
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "修改Mock服务信息", notes = "注意传入MockServiceVo Json形式，ID不要改变")
    @PostMapping("/modify/mock")
    public Object updateMock(@RequestBody MockVo mockVo) {
        try {
            mockService.updateMock(mockVo);
            return Resp.success(RespUtil.OK);
        } catch (Exception e) {
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }
}
