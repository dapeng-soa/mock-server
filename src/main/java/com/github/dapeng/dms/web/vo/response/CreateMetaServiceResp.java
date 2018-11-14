package com.github.dapeng.dms.web.vo.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * {@link JsonInclude https://blog.csdn.net/youthsong/article/details/78801756}
 *
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-09 1:06 PM
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateMetaServiceResp {
    private String simpleName;
    private String serviceName;
    private String version;
    private int methodSize;

    public CreateMetaServiceResp(String simpleName, String serviceName, String version, int methodSize) {
        this.simpleName = simpleName;
        this.serviceName = serviceName;
        this.version = version;
        this.methodSize = methodSize;
    }
}
