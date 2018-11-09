package com.github.dapeng.dms.web.vo.request;


/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-09 1:06 PM
 */
public class ListServiceReq {

    private String simpleName;

    private String serviceName;

    private Long serviceId;

    private DmsPageReq pageRequest;

    public String getSimpleName() {
        return simpleName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public DmsPageReq getPageRequest() {
        return pageRequest;
    }
}
