package com.github.dapeng.dms.web.vo.request;


/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-09 1:06 PM
 */
public class QueryMetaMethodReq {
    private Long metadataId;
    private String methodName;
    private String detail;

    private DmsPageReq pageRequest;

    public Long getMetadataId() {
        return metadataId;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getDetail() {
        return detail;
    }

    public DmsPageReq getPageRequest() {
        return pageRequest;
    }
}
