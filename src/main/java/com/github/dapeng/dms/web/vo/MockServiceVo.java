package com.github.dapeng.dms.web.vo;

import com.github.dapeng.dms.web.entity.MockMetadata;
import lombok.Data;

import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-01 4:10 PM
 */
@Data
public class MockServiceVo {

    private long serviceId;

    private String service;

    private String simpleName;

    private String version;

    private List<MockMetadata> metadata;

    private long mockMethodSize;

    public MockServiceVo(long serviceId, String service) {
        this.serviceId = serviceId;
        this.service = service;
        this.simpleName = service.substring(service.lastIndexOf(".") + 1);
        this.mockMethodSize = 0L;
    }

    public MockServiceVo(long serviceId, String service, String simpleName, long mockMethodSize) {
        this.serviceId = serviceId;
        this.service = service;
        this.simpleName = simpleName;
        this.mockMethodSize = mockMethodSize;
    }

    public MockServiceVo(long serviceId, String service, String simpleName, String version,
                         List<MockMetadata> metadata, long mockMethodSize) {
        this.serviceId = serviceId;
        this.service = service;
        this.simpleName = simpleName;
        this.version = version;
        this.metadata = metadata;
        this.mockMethodSize = mockMethodSize;
    }
}
