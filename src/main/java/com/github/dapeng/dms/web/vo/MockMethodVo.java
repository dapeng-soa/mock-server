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
    private long id;

    private String simpleService;

    private String serviceName;

    private String method;

    private String requestType;

    private String url;

    private long mockSize;

    public MockMethodVo(long id, String serviceName, String method, String requestType, String url, long mockSize) {
        this.id = id;
        this.simpleService = serviceName.substring(serviceName.lastIndexOf(".") + 1);
        this.serviceName = serviceName;
        this.method = method;
        this.requestType = requestType;
        this.url = url;
        this.mockSize = mockSize;
    }

}
