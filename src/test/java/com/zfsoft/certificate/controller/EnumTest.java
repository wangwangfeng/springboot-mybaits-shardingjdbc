package com.zfsoft.certificate.controller;

import java.util.EnumMap;
import java.util.EnumSet;

/**
 * @Classname: EnumTest
 * @Description: 枚举类相关知识
 * @Date: 2021/6/9 9:22
 * @author: wwf
 */
public enum EnumTest {

    SUN("1"),

    MON("2");

    private String key;

    // 获取值
    public String getKey() {
        return this.key;
    }

    // 构造方法
    private EnumTest(String key) {
        this.key = key;
    }

    public static void main(String[] args) {
        // 枚举set集合不能重复
        EnumSet<EnumTest> enumSet = EnumSet.allOf(EnumTest.class);
        for (EnumTest enumTest : enumSet) {
            System.out.println(enumTest);
            System.out.println(enumTest.getKey());
        }

        // 枚举map集合中key只能为枚举类型
        EnumMap<EnumTest, String> enumMap = new EnumMap<>(EnumTest.class);
        enumMap.put(SUN, SUN.getKey());
        enumMap.put(MON, MON.getKey());

        System.out.println("+++++++++");

        // EnumTest.values()方法输出所有枚举类型值
        EnumTest[] values = EnumTest.values();
        for (EnumTest enumTest : EnumTest.values()) {
            System.out.println(enumTest.getKey());
        }

        System.out.println("++++++++++++");

        System.out.println(EnumTest.SUN);
        System.out.println(EnumTest.valueOf("SUN"));

        System.out.println(EnumTest.SUN.key);
        System.out.println(EnumTest.SUN.getKey());

        System.out.println(EnumTest.SUN.name());

        System.out.println(EnumTest.SUN.ordinal());

        EnumTest enumTest = EnumTest.SUN;
        switch (enumTest) {
            case SUN:
                System.out.println("it's sun");
                break;
            case MON:
                System.out.println("it's mon");
                break;
        }


    }


}
