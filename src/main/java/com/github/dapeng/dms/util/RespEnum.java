package com.github.dapeng.dms.util;

import lombok.Getter;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-31 4:19 PM
 */
@Getter
public enum RespEnum {

    OK(200, "OK"),

    ERROR(500, "未知异常");

    private int code;
    private String msg;

    RespEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
