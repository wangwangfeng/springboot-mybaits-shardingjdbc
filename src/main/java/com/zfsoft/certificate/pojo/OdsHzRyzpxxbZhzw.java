package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 安庆市户籍人口照片信息
 *
 * @author 86131
 */
@Data
@TableName(value = "ods_hz_ryzpxxb_zhzw")
public class OdsHzRyzpxxbZhzw implements Serializable {

    private static final long serialVersionUID = 2818777373872576777L;

    /**
     * 照片ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private BigDecimal zpid;

    /**
     * 人员ID
     */
    private BigDecimal ryid;

    /**
     * 公民身份号码
     */
    private String gmsfhm;

    /**
     * 姓名
     */
    private String xm;

    /**
     * 照片
     */
    private byte[] zp;

    /**
     * 录入时间
     */
    private String lrrq;

    /**
     * 最后更新时间
     */
    private Date zhgxsj;

    /**
     * 时间戳（数据推送时，记录单位前置机数据变化时间（系统时间），包括新增和修改）
     */
    private Date xyptTimeDxp;

    /**
     * 变更标识（1：新增 2：修改 3：删除
     */
    private String changeFlag;


}