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
@Data
public class MockMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "simple_name")
    private String simpleName;

    @Column(name = "service_name")
    private String serviceName;

    @Column
    private String version;

    @Column
    private String metadata;

    @Column
    private int type;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public MockMetadata(String simpleName, String serviceName, String version, String metadata) {
        this.simpleName = simpleName;
        this.serviceName = serviceName;
        this.version = version;
        this.metadata = metadata;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
}
