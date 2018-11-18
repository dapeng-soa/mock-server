package com.github.dapeng.dms.mock.request;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-18 9:02 PM
 */
public enum RequestType {

    MOCK("MOCK"),
    STATIC_METADATA("STATIC_METADATA"),
    REAL_DATA("REAL_DATA");

    private String name;

    RequestType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
