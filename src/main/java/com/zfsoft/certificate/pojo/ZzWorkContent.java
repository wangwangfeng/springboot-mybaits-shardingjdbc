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
@TableName(value = "zzwk_content")
public class ZzWorkContent implements Serializable {

    private static final long serialVersionUID = -3628735538454997429L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 证照目录编码
     */
    @TableField(value = "CODE")
    private String code;

    /**
     * 目录名称
     */
    @TableField(value = "NAME")
    private String name;

    /**
     * 注册日期
     */
    @TableField(value = "REGISTER_TIME")
    private java.util.Date registerTime;

    /**
     * 描述
     */
    @TableField(value = "DESCRIPTION")
    private String description;

    /**
     * 目录状态 1=未发布 2=已发布
     */
    @TableField(value = "STATE_CODE")
    private String stateCode;

    /**
     * 创建人编码
     */
    @TableField(value = "CREATE_USER_CODE")
    private String createUserCode;

    /**
     * 创建人名称
     */
    @TableField(value = "CREATE_USER_NAME")
    private String createUserName;

    /**
     * 创建人标识
     */
    @TableField(value = "CREATE_USER_IDCARD")
    private String createUserIdcard;

    /**
     * 创建人单位编码
     */
    @TableField(value = "CREATE_ORG_CODE")
    private String createOrgCode;

    /**
     * 创建人单位名称
     */
    @TableField(value = "CREATE_ORG_NAME")
    private String createOrgName;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME")
    private java.util.Date createTime;

    /**
     * 是否需要签章 0=是 1=否
     */
    @TableField(value = "NEED_SIGN")
    private String needSign;

    /**
     * 可办理证照数 0=一个 1=多个
     */
    @TableField(value = "HAVE_MORE")
    private String haveMore;

    /**
     * 签章个数
     */
    @TableField(value = "SIGN_COUNT")
    private String signCount;

    /**
     * 证照业务编码 1=证照变更 2=证照年检 3=证照挂失 4=证照找回
     * 5=证照撤销 6=证照吊销 7=证照注销 8=证照延期 9=证照冻结
     */
    @TableField(value = "CATALOG_BUSINESS")
    private String catalogBusiness;

    /**
     * 主颁发机构代码
     */
    @TableField(value = "MAIN_ISSUE_CODE")
    private String mainIssueCode;

    /**
     * 主颁发机构名称
     */
    @TableField(value = "MAIN_ISSUE_NAME")
    private String mainIssueName;

    /**
     * 颁发机构代码串
     */
    @TableField(value = "ISSUE_CODE_STR")
    private String issueCodeStr;

    /**
     * 颁发机构名称串
     */
    @TableField(value = "ISSUE_NAME_STR")
    private String issueNameStr;

    /**
     * 证照主体类型 1=自然人 2=法人 3=混合 4=其他 5=自然人/法人
     */
    @TableField(value = "MAIN_TYPE_CODE")
    private String mainTypeCode;

    /**
     * 有效期限
     */
    @TableField(value = "VALIDTIME")
    private String validTime;

    /**
     * 创建区域编码
     */
    @TableField(value = "CREATE_AREA_CODE")
    private String createAreaCode;

    /**
     * 证照类型ID
     */
    @TableField(value = "STYLE_ID")
    private String styleId;

    /**
     * 第一个印章编号
     */
    @TableField(value = "SEAL_CODE_FIRST")
    private String sealCodeFirst;

    /**
     * 第二个印章编号
     */
    @TableField(value = "SEAL_CODE_SECOND")
    private String sealCodeSecond;

    /**
     * 第二个印章编号
     */
    @TableField(value = "SEAL_CODE_THIRD")
    private String sealCodeThird;

    /**
     * 删除标识0否1是，交换值
     */
    @TableField(value = "DELETE_STATE")
    private String deleteState;

}
