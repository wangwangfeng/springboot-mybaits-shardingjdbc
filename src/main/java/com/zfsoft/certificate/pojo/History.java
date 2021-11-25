package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 86131
 */
@Data
@TableName(value = "history")
public class History implements Serializable {

    private static final long serialVersionUID = 6598118510669155278L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String code;

}