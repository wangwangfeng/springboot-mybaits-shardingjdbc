package com.zfsoft.certificate.pojo;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Classname: ZzWorkModel
 * @Description: 证照模板表
 * @Date: 2021/6/10 16:56
 * @author: wwf
 */
@Data
@TableName(value = "zzwk_model")
public class ZzWorkModel implements Serializable {

    private static final long serialVersionUID = -1847949210819710826L;

    @TableId(value = "ID")
    private String id;

    private String name;

    private Date createTime;

    private String applyRange;

    private String description;

    private String contentCode;

    private String origin;

    private String modelId;

    private String modelCode;

    private Long version;

    private String userArea;

    private byte[] model;

    private String status;

    private String type;

    private String styleId;

    private String xyptguid;

    private String xyptbatchguid;

    private Date xyptTimeDxp;

    private Long xyptbatchno;

    private String deleteState;

    private String updateTime;

    private Date jhsjZf;

    private String attaOid;

}