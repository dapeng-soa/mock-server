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

    @Column(name = "mock_key")
    private String mockKey;

    @Column(name = "service")
    private String serviceName;

    @Column(name = "method")
    private String methodName;

    @Column(name = "version")
    private String version;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "mock_express", columnDefinition = "varchar(1024)")
    private String mockExpress;

    @Column(name = "mock_compile_json", columnDefinition = "varchar(1024)")
    private String mockCompileJson;


    @Column(columnDefinition = "MEDIUMTEXT")
    private String data;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "group_prev_no")
    private Long prevNo;

    @Column(name = "group_next_no")
    private Long nextNo;


    public Mock(String serviceName, String methodName, String version, String httpMethod,
                String mockExpress, String mockCompileJson, String data, Long serviceId) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.version = version;
        this.mockKey = serviceName + Constants.KEY_SEPARATE + methodName + Constants.KEY_SEPARATE + version;
        this.httpMethod = httpMethod;
        this.mockExpress = mockExpress;
        this.mockCompileJson = mockCompileJson;
        this.data = data;
        this.serviceId = serviceId;
        this.prevNo = -1L;
        this.nextNo = 0L;
    }

    public Mock(String serviceName, String methodName, String version, String httpMethod,
                String mockExpress, String mockCompileJson, String data, Long serviceId, long groupPrevNo) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.version = version;
        this.mockKey = serviceName + Constants.KEY_SEPARATE + methodName + Constants.KEY_SEPARATE + version;
        this.httpMethod = httpMethod;
        this.mockExpress = mockExpress;
        this.mockCompileJson = mockCompileJson;
        this.data = data;
        this.serviceId = serviceId;
        this.prevNo = groupPrevNo;
        this.nextNo = 0L;
    }
}
