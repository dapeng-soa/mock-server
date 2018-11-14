package com.github.dapeng.dms.web.vo.request;


/**
 * list 服务下的所有方法的请求
 *
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-09 1:06 PM
 */
public class UpdateServiceReq {

    private Long id;

    private String simpleName;

    private String serviceName;

    private String version;

    public Long getId() {
        return id;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getVersion() {
        return version;
    }
}
