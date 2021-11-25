package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 代码表
 *
 * @author 86131
 */
@Data
@TableName(value = "ods_jj_frm_code_zhzw")
public class OdsJjFrmCodeZhzw implements Serializable {

    private static final long serialVersionUID = 7124144232146383926L;

    /**
     * 系统类别
     */
    private String xtlb;

    /**
     * 代码类别
     */
    private String dmlb;

    /**
     * 代码值
     */
    @TableId
    private String dmz;

    /**
     * 代码属性1
     */
    private String dmsm1;

    /**
     * 代码属性2
     */
    private String dmsm2;

    /**
     * 代码属性3
     */
    private String dmsm3;

    /**
     * 代码属性4
     */
    private String dmsm4;

    /**
     * 代码属性（0-不允许修改 1-允许修改）
     */
    private String dmsx;

    /**
     * 顺序号
     */
    private Short sxh;

    /**
     * 业务使用对象（空，都能使用
     * 业务使用对象以逗号分隔
     * ）
     */
    private String ywdx;

    /**
     * 状态标记（0-删除 1-正常）
     */
    private String zt;

    /**
     * 传输标记
     */
    private String csbj;

    private Date gxsj;

    /**
     * 时间戳（数据推送时，记录单位前置机数据变化时间（系统时间），包括新增和修改）
     */
    private Date xyptTimeDxp;

    /**
     * 变更标识（1：新增 2：修改 3：删除
     */
    private String changeFlag;

}