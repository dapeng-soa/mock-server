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


    public MockService(String serviceName, Timestamp createdAt) {
        this.serviceName = serviceName;
        this.createdAt = createdAt;
    }

    public MockService(String serviceName, long metadataId, Timestamp createdAt) {
        this.serviceName = serviceName;
        this.metadataId = metadataId;
        this.createdAt = createdAt;
    }
}
