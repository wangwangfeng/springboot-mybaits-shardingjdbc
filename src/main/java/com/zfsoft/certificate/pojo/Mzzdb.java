package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 民族代码表-新
 *
 * @author 86131
 */
@Data
@TableName(value = "mzzdb")
public class Mzzdb implements Serializable {

    private static final long serialVersionUID = 6527181821378179127L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String xh;

    private String mc;
}