package com.github.dapeng.dms.web.vo.request;


/**
 * list 服务下的所有方法的请求
 *
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-09 1:06 PM
 */
public class QueryMockReq {

    private Long methodId;

    private DmsPageReq pageRequest;

    public Long getMethodId() {
        return methodId;
    }

    public DmsPageReq getPageRequest() {
        return pageRequest;
    }

}
