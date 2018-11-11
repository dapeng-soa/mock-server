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

    private String method;

    private String requestType;

    private String url;

    public MockMethodVo(long id, String simpleService, String method, String requestType, String url) {
        this.id = id;
        this.simpleService = simpleService.substring(simpleService.lastIndexOf(".") + 1);
        this.method = method;
        this.requestType = requestType;
        this.url = url;
//        this.url = String.format("/api/%s/%s/%s", serviceName, "1.0.0", methodName);
    }

}
