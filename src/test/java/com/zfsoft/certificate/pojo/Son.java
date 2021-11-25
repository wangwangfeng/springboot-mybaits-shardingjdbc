package com.zfsoft.certificate.pojo;

import lombok.Data;

/**
 * @Classname: Son
 * @Description: TODO
 * @Date: 2021/7/23 14:47
 * @author: wwf
 */
@Data
public class Son {

    // 姓名
    private String name;

    // 年龄
    private int age;

    public Son() {

    }

    public Son(String name, int age) {
        this.name = name;
        this.age = age;
    }

}
