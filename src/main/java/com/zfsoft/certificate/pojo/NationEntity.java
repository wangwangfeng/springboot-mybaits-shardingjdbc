package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 名族字典表
 *
 * @author 86131
 */
@Data
@TableName(value = "ZF_HZ_MZZD")
public class NationEntity implements Serializable {

    private static final long serialVersionUID = -9141673657716579148L;

    @TableId(value = "MZID", type = IdType.ASSIGN_UUID)
    private String mzid;

    @TableField(value = "MZMC")
    private String mzmc;

}