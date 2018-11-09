package com.github.dapeng.dms.web.vo.request;


/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-09 1:06 PM
 */
public class ListServiceRequest {

    private String simpleName;

    private String serviceName;

    private Long serviceId;

    private DmsPageRequest pageRequest;

    public String getSimpleName() {
        return simpleName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public DmsPageRequest getPageRequest() {
        return pageRequest;
    }
}
