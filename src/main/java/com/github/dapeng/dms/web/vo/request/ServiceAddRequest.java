package com.github.dapeng.dms.web.vo.request;

import lombok.Data;

import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-08 11:38 AM
 */
@Data
public class ServiceAddRequest {
    private String serviceName;

    private String version;

    private String metadata;

    private List<MethodAddRequest> methodList;

}
