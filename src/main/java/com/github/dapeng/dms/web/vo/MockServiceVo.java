package com.github.dapeng.dms.web.vo;

import com.github.dapeng.dms.util.CommonUtil;
import com.github.dapeng.dms.web.entity.MockMetadata;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-01 4:10 PM
 */
@Data
public class MockServiceVo {

    private long id;

    private String serviceName;

    private String simpleName;

    private String version;

    private long metadataId;

    private long mockMethodSize;

    private String createAt;

    public MockServiceVo(long id, String serviceName) {
        this.id = id;
        this.serviceName = serviceName;
        this.simpleName = serviceName.substring(serviceName.lastIndexOf(".") + 1);
        this.mockMethodSize = 0L;
    }

    public MockServiceVo(long id, String serviceName, String simpleName, long mockMethodSize) {
        this.id = id;
        this.serviceName = serviceName;
        this.simpleName = simpleName;
        this.mockMethodSize = mockMethodSize;
    }

    public MockServiceVo(long id, String serviceName, String simpleName, String version,
                         long metadataId, long mockMethodSize, Timestamp createAt) {
        this.id = id;
        this.serviceName = serviceName;
        this.simpleName = simpleName;
        this.version = version;
        this.metadataId = metadataId;
        this.mockMethodSize = mockMethodSize;
        this.createAt = CommonUtil.longToStringDate(createAt.getTime());
    }
}
