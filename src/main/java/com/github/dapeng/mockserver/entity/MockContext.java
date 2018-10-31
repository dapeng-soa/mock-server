package com.github.dapeng.mockserver.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:43 PM
 */
@Data
public class MockContext {

    private String name;

    private String expectedJson;

    private String mockData;

    public MockContext(String name, String expectedJson, String mockData) {
        this.name = name;
        this.expectedJson = expectedJson;
        this.mockData = mockData;
    }
}
