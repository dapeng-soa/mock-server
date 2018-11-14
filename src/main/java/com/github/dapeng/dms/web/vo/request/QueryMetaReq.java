package com.github.dapeng.dms.web.vo.request;


/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-09 1:06 PM
 */
public class QueryMetaReq {
    private Long metadataId;
    private String serviceName;
    private String version;

    private DmsPageReq pageRequest;

    public Long getMetadataId() {
        return metadataId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getVersion() {
        return version;
    }

    public DmsPageReq getPageRequest() {
        return pageRequest;
    }
}
