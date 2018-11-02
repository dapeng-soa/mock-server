package com.github.dapeng.dms.mvc.entity;

import lombok.Data;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:43 PM
 */
@Data
public class MockContext {

    private final String name;

    private final String expectedJson;

    private final String mockData;

    private final int mockRuleId;

    public MockContext(String name, String expectedJson, String mockData, int mockRuleId) {
        this.name = name;
        this.expectedJson = expectedJson;
        this.mockData = mockData;
        this.mockRuleId = mockRuleId;
    }
}
