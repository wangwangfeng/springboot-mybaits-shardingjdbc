package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 86131
 */
@Data
@TableName(value = "zzwk_style")
public class ZzWorkStyle implements Serializable {

    private static final long serialVersionUID = -2001684045137542505L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 类型编码
     */
    @TableField(value = "CODE")
    private String code;

    /**
     * 类型名称
     */
    @TableField(value = "NAME")
    private String name;

    /**
     * 证照种类代码
     */
    @TableField(value = "TYPE_CODE")
    private String typeCode;


    /**
     * 证照种类名称
     */
    @TableField(value = "TYPE_NAME")
    private String typeName;


    /**
     * 电子证照类别 1=证件 2=执照 3=批文 4=牌照 5=证明 6=鉴定报告 7=办事结果
     */
    @TableField(value = "TYPE")
    private String type;

    /**
     * 定义机构信用代码
     */
    @TableField(value = "ORG_TRUST_CODE")
    private String orgTrustCode;

    /**
     * 定义机构名称
     */
    @TableField(value = "ORG_NAME")
    private String orgName;

    /**
     * 定义机构级别 1=国家级 2=省级 3=市级 4=县/区级 5=乡镇/街道级 6=村/社区级 7=其他
     */
    @TableField(value = "ORG_LEVEL")
    private String orgLevel;

    /**
     * 颁证级别 1=国家级 2=省级 3=市级 4=县/区级 5=乡镇/街道级 6=村/社区级 7=其他
     */
    @TableField(value = "ISSUE_LEVEL")
    private String issueLevel;

    /**
     * 是否多个持证主体 Y=是 N=否
     */
    @TableField(value = "MANY_TO_ONE")
    private String manyToOne;

    /**
     * 证照类别
     */
    @TableField(value = "CATEGORY")
    private String cateGory;

    /**
     * 创建人单位编码
     */
    @TableField(value = "OPERATE_ORG_ID")
    private String operateOrgId;

    /**
     * 创建人名称
     */
    @TableField(value = "OPERATE_NAME")
    private String operateName;

    /**
     * 创建人编码
     */
    @TableField(value = "OPERATE_ID")
    private String operateId;

    /**
     * 操作时间
     */
    @TableField(value = "OPERATE_TIME")
    private String operateTime;
    /**
     * 第一个绝对地址
     */
    @TableField(value = "RULE_INFO_FIRST")
    private String ruleInfoFirst;

    /**
     * 第二个绝对地址
     */
    @TableField(value = "RULE_INFO_SECOND")
    private String ruleInfoSecond;

    /**
     * 第三个绝对地址
     */
    @TableField(value = "RULE_INFO_THIRD")
    private String ruleInfoThird;

}
