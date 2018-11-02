package com.github.dapeng.dms.mvc.entity;

import com.github.dapeng.dms.util.Constants;
import lombok.Data;

import javax.persistence.*;


/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:43 PM
 */
@Entity
@Table(name = "mock_data")
@Data
public class Mock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service")
    private String serviceName;

    @Column(name = "method")
    private String methodName;

    @Column(name = "version")
    private String version;

    @Column(name = "mock_key")
    private String mockKey;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "mock_express", columnDefinition = "varchar(1024)")
    private String mockExpress;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String data;

    @Column
    private Integer ordered;

    @Column(name = "service_id")
    private Long serviceId;

    public Mock(String serviceName, String methodName, String version, String httpMethod,
                String mockExpress, String data, Integer ordered) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.version = version;
        this.mockKey = serviceName + Constants.KEY_SEPARATE + methodName + Constants.KEY_SEPARATE + version;
        this.httpMethod = httpMethod;
        this.mockExpress = mockExpress;
        this.data = data;
        this.ordered = ordered;
    }

    public Mock(String serviceName, String methodName, String version, String httpMethod,
                String mockExpress, String data, Integer ordered, Long serviceId) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.version = version;
        this.mockKey = serviceName + Constants.KEY_SEPARATE + methodName + Constants.KEY_SEPARATE + version;
        this.httpMethod = httpMethod;
        this.mockExpress = mockExpress;
        this.data = data;
        this.ordered = ordered;
        this.serviceId = serviceId;
    }
}
