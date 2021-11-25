package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname: Test
 * @Description: TODO
 * @Date: 2021/11/25 11:10
 * @author: wwf
 */
@Data
@TableName(value = "test")
public class Test implements Serializable {

    private static final long serialVersionUID = 8766575387635831762L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 姓名
     */
    private String name;

}