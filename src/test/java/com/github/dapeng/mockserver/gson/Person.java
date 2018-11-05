package com.github.dapeng.mockserver.gson;

import lombok.Data;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-03 12:16 AM
 */
@Data
public class Person {
    private String name;
    private String nickName;
    private int age;
    private String describe;


    public Person(String name, String nickName, int age, String describe) {
        this.name = name;
        this.nickName = nickName;
        this.age = age;
        this.describe = describe;
    }
}
