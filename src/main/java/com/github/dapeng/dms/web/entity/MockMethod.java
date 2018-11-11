package com.github.dapeng.dms.web.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;


/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:43 PM
 */
@Entity
@Table(name = "mock_method")
@Data
public class MockMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_id")
    private Long serviceId;

    @Column
    private String service;

    @Column
    private String method;

    @Column(name = "request_type")
    private String requestType;

    @Column
    private String url;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
