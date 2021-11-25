package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname: ZxEntity
 * @Description: 证照注销处理表
 * @Date: 2021/11/11 11:59
 * @author: wwf
 */
@Data
@TableName("zx")
public class ZxEntity implements Serializable {

    private static final long serialVersionUID = 6067628811297434474L;


    private String code;

    private String name;

    @TableId
    private String id;

    private String sf;

    private String result;

}
