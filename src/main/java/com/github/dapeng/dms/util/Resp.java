package com.github.dapeng.dms.util;

import lombok.Data;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-31 4:17 PM
 */
@Data
public class Resp<T> {
    private int code;
    private String msg;
    private T context;

    private Resp(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Resp(int code, String msg, T context) {
        this.code = code;
        this.context = context;
        this.msg = msg;
    }

    public static Resp of(int code, String msg) {
        return new Resp(code, msg);
    }

    public static <T> Resp of(int code, String msg, T context) {
        return new Resp(code, msg, context);
    }

    public static Resp of(RespEnum respEnum) {
        return new Resp(respEnum.getCode(), respEnum.getMsg());
    }
}

