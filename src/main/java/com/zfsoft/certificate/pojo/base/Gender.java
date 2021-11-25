package com.zfsoft.certificate.pojo.base;

import lombok.Getter;

/**
 * @Classname: Gender
 * @Description: 性别枚举类
 * @Date: 2021/9/1 10:36
 * @author: wwf
 */
@Getter
public enum Gender {

    MAN(1, "男"),
    WO_MAN(2, "女");

    private final int type;
    private final String desc;

    Gender(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static Gender of(int type) {
        for (Gender gender : Gender.values()) {
            if (gender.getType() == type) {
                return gender;
            }
        }
        throw new NullPointerException();
    }

    public static Gender of(String desc) {
        for (Gender gender : Gender.values()) {
            if (gender.getDesc().equals(desc)) {
                return gender;
            }
        }
        return null;
    }

}
