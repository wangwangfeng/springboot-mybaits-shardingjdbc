package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 证照维护版本记录实体
 *
 * @author 86131
 */
@Data
@TableName(value = "zzwk_certificate_version_record")
public class ZzwkCertificateVersionRecord implements Serializable {

    private static final long serialVersionUID = 4873918042567878999L;

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

}