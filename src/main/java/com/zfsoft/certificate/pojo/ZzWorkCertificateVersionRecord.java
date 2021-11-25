package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Classname: ZzWorkCertificateVersionRecord
 * @Description: TODO
 * @Date: 2021/6/15 9:23
 * @author: wwf
 */
@Data
@TableName(value = "zzwk_certificate_version_record")
public class ZzWorkCertificateVersionRecord implements Serializable {

    private static final long serialVersionUID = -2101140408822735405L;

    @TableId(value = "ID")
    private String id;

    private String certificateId;

    private String certificateCode;

    private String contentCode;

    private String ownerId;

    private String ownerName;

    private String infoCode;

    private String maintenanceType;

    private String maintenanceReason;

    private String version;

    private String maintenanceUserName;

    private String maintenanceUserCode;

    private String maintenanceIdcard;

    private String maintenanceOrgCode;

    private String maintenanceOrgName;

    private String maintenanceBizcode;

    private String maintenanceBizname;

    private Date createDate;

    private String attaOid;

    private String styleId;

    public ZzWorkCertificateVersionRecord(){

    }

    public ZzWorkCertificateVersionRecord(ZzWorkCertificate certificate){
        this.certificateId = certificate.getId();
        this.certificateCode = certificate.getCertificateCode();
        this.contentCode = certificate.getContentCode();
        this.ownerId = certificate.getOwnerId();
        this.ownerName = certificate.getOwnerName();
        this.infoCode = certificate.getInfoCode();
        this.styleId = certificate.getStyleId();
        this.attaOid = certificate.getAttaOid();
        this.version = certificate.getVersion();
    }

}