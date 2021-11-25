package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Classname: ZzWorkBusinessMetadata
 * @Description: 证照元素表
 * @Date: 2021/6/10 16:32
 * @author: wwf
 */
@Data
@TableName(value = "zzwk_business_metadata")
public class ZzWorkBusinessMetadata implements Serializable {

    private static final long serialVersionUID = -3098604960678517422L;

    @TableId(value = "ID")
    private String id;

    private String metadata;

    private String controlType;

    private String type;

    private Long length;

    private String isRequire;

    private String name;

    private String description;

    private Date createTime;

    private String contentCode;

    private Long version;

    private String relation;

    private String defaultValue;

    private String styleId;

    private String relationQuery;

    private String xyptguid;

    private String xyptbatchguid;

    private Date xyptTimeDxp;

    private Long xyptbatchno;

    private String deleteState;

    private String updateTime;

    private Date jhsjZf;

}