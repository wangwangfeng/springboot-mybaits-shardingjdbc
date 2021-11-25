package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 安庆市户籍人口基本信息
 */
@Data
@TableName(value = "ods_hz_czrkjbxxb_zhzw")
public class OdsHzCzrkjbxxbZhzw implements Serializable {

    private static final long serialVersionUID = 3499829645366375719L;

    /**
     * 人员内部ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String rynbid;

    /**
     * 照片ID
     */
    private BigDecimal zpid;

    /**
     * 姓名
     */
    private String xm;

    /**
     * 性别
     */
    private String xb;

    /**
     * 民族
     */
    private String mz;

    /**
     * 街路巷
     */
    private String jlx;

    /**
     * 门（楼）牌号
     */
    private String mlph;

    /**
     * 门（楼）详址
     */
    private String mlxz;

    /**
     * 公民身份号码
     */
    private String gmsfhm;

    /**
     * 签发机关
     */
    private String qfjg;

    /**
     * 有效期限起始日期
     */
    private String yxqxqsrq;

    /**
     * 有效期限截止日期
     */
    private String yxqxjzrq;

    /**
     * 省市县（区）
     */
    private String ssxq;

    /**
     * 最后更新时间
     */
    private Date zhgxsj;

    /**
     * 人员状态
     */
    private String ryzt;

    /**
     * 记录标志
     * 0-历史数据；1-新数据
     */
    private String jlbz;

    /**
     * 冲销标志
     */
    private String cxbz;

}