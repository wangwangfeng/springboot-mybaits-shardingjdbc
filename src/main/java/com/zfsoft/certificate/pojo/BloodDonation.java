package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 献血证数据
 *
 * @author 86131
 */
@Data
@TableName(value = "xxz_zz")
public class BloodDonation implements Serializable {

    private static final long serialVersionUID = 4321558177892908479L;

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 姓名
     */
    @TableField(value = "xm")
    private String xm;

    /**
     * 证件类型
     */
    @TableField(value = "zjlx")
    private String zjlx;

    /**
     * 证件号码
     */
    @TableField(value = "zjhm")
    private String zjhm;

    /**
     * 通讯地址
     */
    @TableField(value = "txdz")
    private String txdz;

    /**
     * 档案血型
     */
    @TableField(value = "daxx")
    private String daxx;

    /**
     * 献血类型
     */
    @TableField(value = "xxlx")
    private String xxlx;

    /**
     * 采血量
     */
    @TableField(value = "cxl")
    private Integer cxl;

    /**
     * 献血码
     */
    @TableField(value = "xxm")
    private String xxm;

    /**
     * 采血地点
     */
    @TableField(value = "cxdd")
    private String cxdd;

    /**
     * 采血时间
     */
    @TableField(value = "cxsj")
    private Date cxsj;

}