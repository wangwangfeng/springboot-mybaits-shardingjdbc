package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 独生子女光荣证
 *
 * @author 86131
 */
@Data
@TableName("dszngrz_zz")
public class HonourEntity implements Serializable {

    private static final long serialVersionUID = -389267138054862170L;

    /**
     * 主键
     */
    @TableId
    private Integer id;

    /**
     * 光荣证号
     */
    private String grzh;

    /**
     * 持证人
     */
    private String czr;

    /**
     * 发证单位
     */
    private String fzdw;

    /**
     * 发证日期
     */
    private String fzrq;

    /**
     * 姓名(数据来自全员，此表无法直接修改字段)
     */
    private String xm;

    /**
     * 性别(数据来自全员，此表无法直接修改字段) 字典 4（性别）
     */
    private String xb;

    /**
     * 出生日期(数据来自全员，此表无法直接修改字段)
     */
    private String csrq;

    /**
     * 身份证号码(数据来自全员，此表无法直接修改字段)
     */
    private String sfz;

    /**
     * 服务处所
     */
    private String fwcs;

    /**
     * 配偶姓名(数据来自全员，此表无法直接修改字段)
     */
    private String poxm;

    /**
     * 配偶出生日期(数据来自全员，此表无法直接修改字段)
     */
    private String pocsrq;

    /**
     * 民族
     */
    private String mz;

    /**
     * 配偶身份证号码(数据来自全员，此表无法直接修改字段)
     */
    private String posfz;

    /**
     * 服务处所
     */
    private String fwcs1;

    /**
     * 姓名
     */
    private String xm1;

    /**
     * 性别
     */
    private String xb1;

    /**
     * 出生日期
     */
    private String csrq1;

    /**
     * 现居地
     */
    private String xjd;
}