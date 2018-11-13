package com.github.dapeng.dms.web.entity;

import com.github.dapeng.dms.util.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;


/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:43 PM
 */
@Entity
@Table(name = "mock_data")
@Data
@ApiModel("Mock规则基本信息")
public class Mock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty("Mock规则唯一Key,service+method+version")
    @Column(name = "mock_key")
    private String mockKey;

    @ApiModelProperty("服务名称")
    @Column(name = "service")
    private String serviceName;

    @ApiModelProperty("方法名称")
    @Column(name = "method")
    private String methodName;

    @ApiModelProperty("版本信息")
    @Column(name = "version")
    private String version;

    @ApiModelProperty("Http请求方法(GET/POST/PUT/DELETE)")
    @Column(name = "http_method")
    private String httpMethod;

    @ApiModelProperty("Mock预期表达式")
    @Column(name = "mock_express", columnDefinition = "varchar(1024)")
    private String mockExpress;

    @org.springframework.data.annotation.Transient
    @Column(name = "mock_compile_json", columnDefinition = "varchar(1024)")
    private String mockCompileJson;

    @ApiModelProperty("Mock返回数据")
    @Column(columnDefinition = "MEDIUMTEXT")
    private String data;

    @ApiModelProperty("接口（方法)编号")
    @Column(name = "method_id")
    private Long methodId;

    @ApiModelProperty("排序号")
    @Column
    private Long sort;


    public Mock(String serviceName, String methodName, String version, String httpMethod,
                String mockExpress, String mockCompileJson, String data, Long methodId) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.version = version;
        this.mockKey = serviceName + Constants.KEY_SEPARATE + methodName + Constants.KEY_SEPARATE + version;
        this.httpMethod = httpMethod;
        this.mockExpress = mockExpress;
        this.mockCompileJson = mockCompileJson;
        this.data = data;
        this.methodId = methodId;
        this.sort = 1000L;
    }

    public Mock(String serviceName, String methodName, String version, String httpMethod,
                String mockExpress, String mockCompileJson, String data, Long methodId, long sort) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.version = version;
        this.mockKey = serviceName + Constants.KEY_SEPARATE + methodName + Constants.KEY_SEPARATE + version;
        this.httpMethod = httpMethod;
        this.mockExpress = mockExpress;
        this.mockCompileJson = mockCompileJson;
        this.data = data;
        this.methodId = methodId;
        this.sort = sort;
    }


}
