package com.github.dapeng.dms.mock.request;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-18 9:02 PM
 */
public enum ResponseType {

    MOCK("MOCK数据"),
    STATIC_METADATA("静态元数据返回数据"),
    REAL_DATA("实时服务数据"),
    NO_DATA("没有数据");

    private String name;

    ResponseType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
