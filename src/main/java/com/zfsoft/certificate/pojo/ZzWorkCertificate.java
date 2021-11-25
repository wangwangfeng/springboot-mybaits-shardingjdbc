package com.zfsoft.certificate.pojo;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 86131
 */
@Data
@TableName(value = "zzwk_certificate")
public class ZzWorkCertificate implements Serializable {

    private static final long serialVersionUID = -6872372052634199949L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 证照目录编码
     */
    @TableField(value = "CONTENT_CODE")
    private String contentCode;

    /**
     * 电子证照流水号，是电子证照识别的唯一标识
     */
    @TableField(value = "CERTIFICATE_CODE")
    private String certificateCode;

    /**
     * 证照模板id
     */
    @TableField(value = "MODEL_ID")
    private String modelId;

    /**
     * 证照持证人ID
     */
    @TableField(value = "OWNER_ID")
    private String ownerId;

    /**
     * 证照持证人姓名
     */
    @TableField(value = "OWNER_NAME")
    private String ownerName;

    /**
     * 证件照面编码
     */
    @TableField(value = "INFO_CODE")
    private String infoCode;

    /**
     * 证照有效期限开始时间
     */
    @TableField(value = "INFO_VALIDITY_BEGIN")
    private String infoValidityBegin;

    /**
     * 证照有效期限结束时间
     */
    @TableField(value = "INFO_VALIDITY_END")
    private String infoValidityEnd;

    /**
     * 照面元素JSON数据
     */
    @TableField(value = "DATA")
    private String data;

    /**
     * 制证人姓名
     */
    @TableField(value = "USER_NAME")
    private String userName;


    /**
     * 制证人编码
     */
    @TableField(value = "USER_CODE")
    private String userCode;

    /**
     * 制证人身份标识
     */
    @TableField(value = "ID_CARD")
    private String idCard;

    /**
     * 制证人部门编号
     */
    @TableField(value = "ORG_CODE")
    private String orgCode;

    /**
     * 制证人部门名称
     */
    @TableField(value = "ORG_NAME")
    private String orgName;

    /**
     * 系统标识码（默认为zzwk）
     */
    @TableField(value = "BIZ_CODE")
    private String bizCode;

    /**
     * 系统标识名称（默认为制证文库）
     */
    @TableField(value = "BIZ_NAME")
    private String bizName;

    /**
     * 发证机构的信用代码
     */
    @TableField(value = "ORG_TRUST_CODE")
    private String orgTrustCode;

    /**
     * 证照的版本号
     */
    @TableField(value = "VERSION")
    private String version;

    /**
     * 删除状态 0=可用 1=不可用
     */
    @TableField(value = "DELETE_STATE")
    private String deleteState;

    /**
     * 证照的状态 0=正常 1=挂失 2=撤销 3=吊销 4=注销 5=冻结 6=盖章 9=盖章失败
     */
    @TableField(value = "CER_STATUS")
    private String cerStatus;

    /**
     * 证照对应的材料主键
     */
    @TableField(value = "ATTA_OID")
    private String attaOid;

    /**
     * 证照对应的证照种类
     */
    @TableField(value = "STYLE_ID")
    private String styleId;

    /**
     * 证照目录名称
     */
    @TableField(value = "CONTENT_NAME")
    private String contentName;

    /**
     * 上报标志 1=未上报，null或者为0代表已上报
     */
    @TableField(value = "POST_FLAG")
    private String postFlag;

    /**
     * 证照生成时间
     */
    @TableField(value = "CREATE_DATE")
    private Date createDate;

    public ZzWorkCertificate() {

    }

    public ZzWorkCertificate(JSONObject jsonObject) {
        this.contentCode = String.valueOf(jsonObject.get("contentCode"));
        this.infoCode = String.valueOf(jsonObject.get("infoCode"));
        this.ownerId = String.valueOf(jsonObject.get("ownerId"));
        this.ownerName = String.valueOf(jsonObject.get("ownerName"));
        this.infoValidityBegin = String.valueOf(jsonObject.get("infoValidityBegin"));
        this.infoValidityEnd = String.valueOf(jsonObject.get("infoValidityEnd"));
    }

}
