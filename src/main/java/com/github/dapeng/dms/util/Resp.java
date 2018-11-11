package com.github.dapeng.dms.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-31 4:17 PM
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Resp<T> {
    private int status;

    private T success;

    private String responseCode;

    private T responseMsg;

    private Resp(T success) {
        this.status = 1;
        this.success = success;
    }

    private Resp(String responseCode, T responseMsg) {
        this.status = 0;
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
    }

    public static <T> Resp success(T success) {
        return new Resp<>(success);
    }

    public static <T> Resp error(String responseCode, T responseMsg) {
        return new Resp<>(responseCode, responseMsg);
    }

}

//{
//   "success": "*******************收到的消息为： Hello(maple,None) HelloServiceImpl version=2.0.1",
//   "status": 1
//}
