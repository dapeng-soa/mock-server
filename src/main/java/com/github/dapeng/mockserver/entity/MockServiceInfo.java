package com.github.dapeng.mockserver.entity;

import lombok.Data;

import javax.persistence.*;


/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:43 PM
 */
@Entity
@Table(name = "mock_service")
@Data
public class MockServiceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service")
    private String serviceName;

    public MockServiceInfo(String serviceName) {
        this.serviceName = serviceName;
    }
}
