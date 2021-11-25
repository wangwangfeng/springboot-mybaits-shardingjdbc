package com.zfsoft.certificate.pojo.base;

import lombok.Data;

import java.util.Map;

/**
 * @author 86131
 */
@Data
public class CertificateBaseBO {

    private String contentCode;

    private String ownerId;

    private String ownerName;

    private String infoCode;

    private String infoValidityBegin;

    private String infoValidityEnd;

    private Map<String,String> data;

    private CertificateReqStrBO reqStr;

}
