package com.zfsoft.certificate.pojo;

import lombok.Data;

/**
 * @Classname: Person
 * @Description: TODO
 * @Date: 2021/6/29 15:46
 * @author: wwf
 */
@Data
public class Person {

    // 姓名
    private String name;

    // 薪资
    private int salary;

    // 年龄
    private int age;

    //性别
    private String sex;

    // 地区
    private String area;

    public Person(String name, int salary, int age, String sex, String area) {
        this.name = name;
        this.salary = salary;
        this.age = age;
        this.sex = sex;
        this.area = area;
    }

}
