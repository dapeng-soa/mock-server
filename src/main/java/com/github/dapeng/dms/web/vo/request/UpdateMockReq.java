package com.github.dapeng.dms.web.vo.request;


/**
 * list 服务下的所有方法的请求
 *
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-09 1:06 PM
 */
public class UpdateMockReq {

    private Long mockId;

    private String mockExpress;

    private String mockData;

    public Long getMockId() {
        return mockId;
    }

    public String getMockExpress() {
        return mockExpress;
    }

    public String getMockData() {
        return mockData;
    }
}
