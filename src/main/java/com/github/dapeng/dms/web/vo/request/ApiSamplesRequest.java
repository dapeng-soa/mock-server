package com.github.dapeng.dms.web.vo.request;


/**
 * list 服务下的所有方法的请求
 *
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-09 1:06 PM
 */
public class ApiSamplesRequest {

    private String serviceName;

    private String methodName;

    private String version;


    public String getServiceName() {
        return serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getVersion() {
        return version;
    }

}
