package com.github.dapeng.dms.web.controller;

import com.github.dapeng.dms.util.CommonUtil;
import com.github.dapeng.dms.web.services.DslMockService;
import com.github.dapeng.dms.web.services.MetadataService;
import com.github.dapeng.dms.util.Resp;
import com.github.dapeng.dms.util.RespUtil;
import com.github.dapeng.dms.web.vo.request.*;
import com.github.dapeng.dms.web.vo.response.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    private final DslMockService dslMockService;

    private final MetadataService metadataService;

    /**
     * Spring Dependency Injection for Construction
     */
    public AdminController(DslMockService dslMockService, MetadataService metadataService) {
        this.dslMockService = dslMockService;
        this.metadataService = metadataService;
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

    @ApiOperation(value = "获取已存在的所有服务全称")
    @PostMapping("/listServicesName")
    public Object listMockServiceName(@RequestBody Map<String, Long> params) {
        try {
            Long id = params.get("id");
            List<String> serviceNameList = dslMockService.listMockServiceName(id);
            return Resp.success(serviceNameList);
        } catch (Exception e) {
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

    @ApiOperation(value = "根据条件查询所有接口信息列表")
    @PostMapping(value = "/listInterfacesName")
    public Object listInterfacesName(@RequestBody Map<String, String> params) {
        try {
            String serviceName = CommonUtil.notNullRet(params.get("serviceName"), "请求接口列表时，服务全称必须指定.");
            List<String> methodNames = dslMockService.listMockInterfacesName(serviceName);
            return Resp.success(methodNames);
        } catch (Exception e) {
            log.error("listInterfacesName Error: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "根据条件 List Mock Express")
    @PostMapping(value = "/listMockExpress")
    public Object listMockExpress(@RequestBody QueryMockReq request) {
        try {
            CommonUtil.notNull(request.getMethodId(), "请求MockExpress规则时,methodId 不能为空");
            QueryMockResp resp = dslMockService.listMockExpress(request);
            return Resp.success(resp);
        } catch (Exception e) {
            log.error("listMockExpress Error: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "元数据信息查询")
    @PostMapping("/listMetadata")
    public Object queryMetadataByCondition(@RequestBody QueryMetaReq request) {
        try {
            QueryMetaResp metaResp = dslMockService.queryMetadataByCondition(request);
            return Resp.success(metaResp);
        } catch (Exception e) {
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "元数据详情之接口方法查询")
    @PostMapping("/listMetaDetailMethod")
    public Object queryMetaDetailMethod(@RequestBody QueryMetaMethodReq request) {
        try {
            QueryMetaMethodResp metaMethodResp = metadataService.queryMetaDetailMethod(request);
            return Resp.success(metaMethodResp);
        } catch (Exception e) {
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }


    /**
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * create                                注意唯一性校验
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    @ApiOperation(value = "添加一个Mock基础服务")
    @PostMapping("/createService")
    public Object createService(@RequestBody CreateServiceReq request) {
        try {
            CommonUtil.notNull(request.getServiceName(), "服务全称不能为空");
            CommonUtil.notNull(request.getVersion(), "版本信息不能为空");
            dslMockService.createService(request);
            return Resp.success();
        } catch (Exception e) {
            log.error("createService Error: {}", e.getMessage());
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

    @ApiOperation(value = "添加某一个方法的mock规则")
    @PostMapping("/createMockInfo")
    public Object createMockInfo(@RequestBody CreateMockReq request) {
        try {
            dslMockService.createMockInfo(request);
            return Resp.success(RespUtil.OK);
        } catch (Exception e) {
            log.error("createMockInfo: Json Schema 解析失败，请检查格式: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "一键创建Mock规则")
    @PostMapping("/createMockOnce")
    public Object createMockOnce(@RequestBody CreateOnceReq request) {
        try {
            long methodId = dslMockService.createMockOnce(request);
            return Resp.success(methodId);
        } catch (Exception e) {
            log.error("createMockInfo: Json Schema 解析失败，请检查格式: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }

    /**
     * update
     */
    @ApiOperation(value = "修改Mock服务信息")
    @PostMapping("/updateService")
    public Object updateService(@RequestBody UpdateServiceReq request) {
        try {
            dslMockService.updateService(request);
            return Resp.success(RespUtil.OK);
        } catch (Exception e) {
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "修改服务下的接口Method")
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

    @ApiOperation(value = "根据ID修改某一个方法的mock规则")
    @PostMapping("/updateMockInfo")
    public Object updateMockInfo(@RequestBody UpdateMockReq request) {
        try {
            dslMockService.updateMockInfo(request);
//            mockService.addMockInfo(service, method, version, mockExpress, mockData);
            return Resp.success(RespUtil.OK);
        } catch (Exception e) {
            log.error("updateMockInfo Error: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }


    /**
     * delete
     */
    @ApiOperation(value = "根据id删除服务")
    @PostMapping(value = "/deleteService")
    public Object deleteService(@RequestBody Map<String, String> params) {
        try {
            String id = params.get("id");
            CommonUtil.notNull(id, "根据ID删除服务时，ID不能为空。");
            dslMockService.deleteService(Long.valueOf(id));
            return Resp.success();
        } catch (Exception e) {
            log.error("deleteService Error: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }


    @ApiOperation(value = "根据id删除接口")
    @PostMapping(value = "/deleteInterface")
    public Object deleteMethod(@RequestBody Map<String, String> params) {
        try {
            String id = params.get("id");
            CommonUtil.notNull(id);
            dslMockService.deleteMethod(Long.valueOf(id));
            return Resp.success();
        } catch (Exception e) {
            log.error("deleteInterface Error: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "根据ID删除某一个mock规则")
    @PostMapping("/deleteMockInfo")
    public Object deleteMockInfo(@RequestBody Map<String, String> requestMap) {
        try {
            String id = requestMap.get("id");
            CommonUtil.notNull(id, "删除Mock规则时,传入id不能为空");
            dslMockService.deleteMockInfo(Long.valueOf(id));
            return Resp.success();
        } catch (Exception e) {
            log.error("deleteMockInfo Error: {}", e.getMessage());
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }


    /**
     * 其他接口
     */
    @ApiOperation(value = "根据methodId查询 service and method name")
    @PostMapping(value = "/getMockMethodForm")
    public Object getMockMethodForm(@RequestBody Map<String, String> requestMap) {
        try {
            String id = requestMap.get("id");
            CommonUtil.notNull(id, "根据methodId查询getMockMethodForm时，id不能为空.");
            MockMethodFormResp resp = dslMockService.getMockMethodForm(Long.valueOf(id));
            return Resp.success(resp);
        } catch (Exception e) {
            log.error("listMockExpress Error: {}", e.getMessage());
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

    @ApiOperation(value = "修改同一MockKey下的Mock规则优先级")
    @PostMapping("/updatePriority")
    public Object updateMockPriority(@RequestBody UpdatePriorityReq request) {
        try {
            dslMockService.updateMockPriority(request);
            return Resp.success(RespUtil.OK);
        } catch (Exception e) {
            return Resp.error(RespUtil.MOCK_ERROR, e.getMessage());
        }
    }
}
