package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 安庆市常住人口信息系统行政区划信息
 *
 * @author 86131
 */
@Data
@TableName(value = "ods_hz_xzqhb_zhzw")
public class OdsHzXzqhbZhzw implements Serializable {

    private static final long serialVersionUID = -2103936116827576001L;

    /**
     * 代码
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String dm;

    /**
     * 名称
     */
    private String mc;

    /**
     * 中文拼音
     */
    private String zwpy;

    /**
     * 五笔拼音
     */
    private String wbpy;

    /**
     * 备注
     */
    private String bz;

    /**
     * 启用标志
     */
    private String qybz;

    /**
     * 变动类型
     */
    private String bdlx;

    /**
     * 变动时间
     */
    private String bdsj;

    /**
     * 共享库_入库时间
     */
    private Date ywptTimeDxp;

    /**
     * 更新标识
     */
    private String changeFlag;
}