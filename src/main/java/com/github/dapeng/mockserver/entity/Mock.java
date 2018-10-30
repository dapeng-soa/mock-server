package com.github.dapeng.mockserver.entity;

import javax.persistence.*;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:43 PM
 */
@Entity
@Table(name = "mock_data")
public class Mock {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column(name = "mock_express")
    private String mockExpress;

    @Column
    private String data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMockExpress() {
        return mockExpress;
    }

    public void setMockExpress(String mockExpress) {
        this.mockExpress = mockExpress;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
