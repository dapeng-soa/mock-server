package com.github.dapeng.dms.mvc.web.vo;

import lombok.Data;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-01 4:11 PM
 */
@Data
public class MockVo {

    private String service;

    private String simpleName;

    private String method;

    private String version;

    private String httpMethod;

    private String mockExpress;

    private String data;

    private long preNo;
    private long nextNo;

    public MockVo(String service, String simpleName, String method,
                  String version, String httpMethod, String mockExpress,
                  String data, long preNo, long nextNo) {
        this.service = service;
        this.simpleName = simpleName;
        this.method = method;
        this.version = version;
        this.httpMethod = httpMethod;
        this.mockExpress = mockExpress;
        this.data = data;

        this.preNo = preNo;
        this.nextNo = nextNo;
    }
}
