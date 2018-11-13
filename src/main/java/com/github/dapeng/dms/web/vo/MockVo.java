package com.github.dapeng.dms.web.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-01 4:11 PM
 */
@Data
public class MockVo {
    @ApiModelProperty("当前Mock规则唯一ID")
    private Long id;

//    @ApiModelProperty("服务名称")
//    private String serviceName;
//
//    @ApiModelProperty("方法名称")
//    private String methodName;

//    @ApiModelProperty("版本信息")
//    private String version;

//    @ApiModelProperty("Http请求方法(GET/POST/PUT/DELETE)")
//    private String httpMethod;

    @ApiModelProperty("Mock预期表达式")
    private String mockExpress;


    @ApiModelProperty("Mock返回数据")
    private String data;
//
//    @ApiModelProperty("服务编号")
//    private Long serviceId;

    @ApiModelProperty("排序号")
    private Long sort;


    public MockVo(Long id, String mockExpress, String data, Long sort) {
        this.id = id;
        this.mockExpress = mockExpress;
        this.data = data;
        this.sort = sort;
    }
}
