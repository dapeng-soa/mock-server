package com.github.dapeng.dms.mvc.vo;

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

    private List<MockVo> mockVoList;

    public MockServiceVo(long serviceId, String service, String simpleName, List<MockVo> mockVoList) {
        this.serviceId = serviceId;
        this.service = service;
        this.simpleName = simpleName;
        this.mockVoList = mockVoList;
    }
}
