package com.github.dapeng.dms.web.vo;

import com.github.dapeng.dms.util.CommonUtil;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 一个微服务下的所有接口方法的信息
 *
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-07 10:55 AM
 */
@Data
public class MetaMethodVo {
    private String serviceName;

    private String methodName;

    private String eventName;

    private String describe;

    public MetaMethodVo(String serviceName, String methodName, String describe) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.describe = describe;
    }

    /*public MetaMethodVo(String methodName, String eventName, String describe) {
        this.methodName = methodName;
        this.eventName = eventName;
        this.describe = describe;
    }*/
}
