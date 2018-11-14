package com.github.dapeng.dms.web.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;


/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:43 PM
 */
@Entity
@Table(name = "mock_service")
@Data
public class MockService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "simple_name")
    private String simpleName;

    @Column(name = "service")
    private String serviceName;

    @Column
    private String version;

    @Column(name = "metadata_id")
    private long metadataId;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public MockService(String serviceName, String version) {
        this.serviceName = serviceName;
        this.version = version;
        this.simpleName = this.serviceName.substring(this.serviceName.lastIndexOf(".") + 1);
        this.createdAt = new Timestamp(System.currentTimeMillis());

    }

    public MockService(String simpleName, String serviceName, String version) {
        this.simpleName = simpleName;
        this.serviceName = serviceName;
        this.version = version;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }


}
