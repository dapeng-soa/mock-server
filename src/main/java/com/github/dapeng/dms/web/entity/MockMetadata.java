package com.github.dapeng.dms.web.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-08 10:54 AM
 */
@Entity
@Table(name = "mock_metadata")
@Data
@ApiModel("Mock服务元信息")
public class MockMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /*@ApiModelProperty("服务ID")
    @Column(name = "service_id")
    private long serviceId;*/

    @ApiModelProperty("服务名称")
    @Column(name = "service_name")
    private String serviceName;

    @ApiModelProperty("元信息(XML形式存储)")
    @Column
    private String metadata;

    @ApiModelProperty("元数据版本信息")
    @Column
    private String version;

    @ApiModelProperty("元数据类型")
    @Column
    private int type;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    /**
     * 新增元信息
     */
    public MockMetadata(String serviceName, String metadata, String version, int type, Timestamp createdAt) {
//        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.metadata = metadata;
        this.version = version;
        this.type = type;
        this.createdAt = createdAt;
    }

    /**
     * 修改版本
     */
    public MockMetadata(String serviceName, String metadata, String version, int type) {
        this.serviceName = serviceName;
        this.metadata = metadata;
        this.version = version;
        this.type = type;
    }
}
