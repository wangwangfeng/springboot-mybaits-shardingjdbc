package com.zfsoft.certificate.pojo;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Classname: ZzWorkCertificateLogs
 * @Description: TODO
 * @Date: 2021/6/15 9:44
 * @author: wwf
 */
@Data
@TableName(value = "zzwk_certificate_logs")
public class ZzWorkCertificateLogs implements Serializable {

    private static final long serialVersionUID = 4186263351815640781L;

    @TableId(value = "ID")
    private String id;

    private String attaOid;

    private String bizCode;

    private String bizName;

    private String cerStatus;

    private String certificateCode;

    private String contentCode;

    private String contentName;

    private Date createDate;

    private String data;

    private String deleteState;

    private String flag;

    private String idCard;

    private String infoCode;

    private String infoValidityBegin;

    private String infoValidityEnd;

    private String modelId;

    private Date modifyDate;

    private String orgCode;

    private String orgName;

    private String orgTrustCode;

    private String ownerId;

    private String ownerName;

    private String result;

    private String styleId;

    private String type;

    private String userCode;

    private String userName;

    private String version;

}