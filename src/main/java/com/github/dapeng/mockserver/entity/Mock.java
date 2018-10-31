package com.github.dapeng.mockserver.entity;

import lombok.Data;

import javax.persistence.*;


/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:43 PM
 */
@Entity
@Table(name = "mock_data")
@Data
public class Mock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String name;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "mock_express", columnDefinition = "varchar(1024)")
    private String mockExpress;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String data;

    @Column
    private Integer ordered;

    public Mock(String name, String httpMethod, String mockExpress, String data, Integer ordered) {
        this.name = name;
        this.httpMethod = httpMethod;
        this.mockExpress = mockExpress;
        this.data = data;
        this.ordered = ordered;
    }
}
