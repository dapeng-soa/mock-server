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
public class MetadataVo {
    private long id;

    private String simpleName;

    private String serviceName;

    private String version;

    private int methodSize;

    private String createAt;

    public MetadataVo(long id, String simpleName, String serviceName, String version, int methodSize, Timestamp createAt) {
        this.id = id;
        this.simpleName = simpleName;
        this.serviceName = serviceName;
        this.version = version;
        this.methodSize = methodSize;
        this.createAt = CommonUtil.longToStringDate(createAt.getTime());
    }


}
