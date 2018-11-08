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

    private List<MockMetadata> metadata;

    private List<MockVo> mockVoList;

    public MockServiceVo(long serviceId, String service) {
        this.serviceId = serviceId;
        this.service = service;
        this.simpleName = service.substring(service.lastIndexOf(".") + 1);
        this.mockVoList = null;
    }

    public MockServiceVo(long serviceId, String service, String simpleName, List<MockVo> mockVoList) {
        this.serviceId = serviceId;
        this.service = service;
        this.simpleName = simpleName;
        this.mockVoList = mockVoList;
    }

    public MockServiceVo(long serviceId, String service, String simpleName,
                         List<MockMetadata> metadata, List<MockVo> mockVoList) {
        this.serviceId = serviceId;
        this.service = service;
        this.simpleName = simpleName;
        this.metadata = metadata;
        this.mockVoList = mockVoList;
    }
}
