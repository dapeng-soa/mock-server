package com.github.dapeng.dms.util;

import lombok.Getter;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-31 4:19 PM
 */
@Getter
public class RespUtil {

    public static String MOCK_ERROR = "Err-mock-500";
    public static String OK = "OK";

    public enum RespEnum {
        OK("200", "OK"),
        ADMIN_ERROR("Err-Admin-500", "ADMIN_ERROR"),

        ERROR("500", "未知异常"),
        RESPONSE_NULL("500", "Mock和元数据信息均不存在");

        private String code;
        private String msg;

        RespEnum(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }


}
