package com.github.dapeng.dms.web.vo;

import com.github.dapeng.dms.web.entity.Mock;
import lombok.Data;

import java.util.List;

/**
 * 一个微服务下的所有接口方法的信息
 *
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-07 10:55 AM
 */
@Data
public class MockMethodVo {
    private long serviceId;

    private String serviceName;

    private String simpleName;

    private String methodName;

    private String requestType;

    private String url;

    private List<Mock> mockList;


    public MockMethodVo(long serviceId, String serviceName, String methodName, String requestType, String url, List<Mock> mockList) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.simpleName = serviceName.substring(serviceName.lastIndexOf(".") + 1);
        this.methodName = methodName;
        this.requestType = requestType;
        this.url = String.format("/api/%s/%s/%s", serviceName, "1.0.0", methodName);
        this.mockList = mockList;

    }
}
