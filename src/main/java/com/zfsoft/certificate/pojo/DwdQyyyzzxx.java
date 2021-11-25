package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname: DwdQyyyzzxx
 * @Description: TODO
 * @Date: 2021/7/14 9:34
 * @author: wwf
 */
@Data
@TableName(value = "dwd_qyyyzzxx")
public class DwdQyyyzzxx implements Serializable {

    private static final long serialVersionUID = 1322515164062386205L;

    /**
     * 统一社会信用代码
     */
    @TableId
    private String zch;

    /**
     * 企业名称
     */
    private String mc;

    /**
     * 企业类型属性
     */
    private String lx;

    /**
     * 注册地址
     */
    private String zs;

    /**
     * 法定代表人
     */
    private String fddbr;

    /**
     * 注册/开办资金金额
     */
    private String zczb;

    /**
     * 经营期限自
     */
    private String clrq;

    /**
     * 经营期限至
     */
    private String yyqx;

    /**
     * 经营范围
     */
    private String jyfw;

    /**
     * 登记机构名称
     */
    private String djjg;

    private String djn;

    private String djy;

    private String djr;

    private String rwm;

    /**
     * 统计登记证编码
     */
    private String zsbh;

    /**
     * 场所地址
     */
    private String jycs;

    /**
     * 法定代表人
     */
    private String fzr;

    private String hhqx;

    private String zyswhhr;

}