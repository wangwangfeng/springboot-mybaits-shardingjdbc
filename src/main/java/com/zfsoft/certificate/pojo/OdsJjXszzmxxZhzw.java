package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 行驶证_照面信息
 *
 * @author 86131
 */
@Data
@TableName(value = "ods_jj_xszzmxx_zhzw")
public class OdsJjXszzmxxZhzw implements Serializable {

    private static final long serialVersionUID = 1938868350881932150L;

    /**
     * 序号
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String xh;

    /**
     * 机动车相片
     */
    private byte[] photo;

    /**
     * 号牌号码
     */
    private String infocode;

    /**
     * 车辆类型
     */
    private String cartype;

    /**
     * 所有人
     */
    private String name;

    /**
     * 住址
     */
    private String address;

    /**
     * 使用性质
     */
    private String useproperty;

    /**
     * 品牌型号
     */
    private String brandmodel;

    /**
     * 车辆识别代号
     */
    private String carcode;

    /**
     * 发动机号码
     */
    private String enginenumber;

    /**
     * 注册日期
     */
    private Date registdate;

    /**
     * 发证日期
     */
    private Date issuedate;

    /**
     * 号牌号码
     */
    private String carnumber;

    /**
     * 档案编号
     */
    private String archivesnum;

    /**
     * 核定载人数
     */
    private Integer approvedload;

    /**
     * 总质量
     */
    private Integer totalmass;

    /**
     * 整备质量
     */
    private Integer kerbmass;

    /**
     * 核定载质量
     */
    private Integer approvedquality;

    /**
     * 外廓尺寸
     */
    private String overallsize;

    /**
     * 准牵引总质量
     */
    private Integer total;

    /**
     * 备注
     */
    private String note1;

    /**
     * 检验记录
     */
    private String recode;

    /**
     * 记录
     */
    private String notes2;

    /**
     * 燃料种类
     */
    private String rlzl;

    /**
     * 条形码
     */
    private String barcode;

    /**
     * 条形码数字
     */
    private String barnumber;

    /**
     * 最后更新时间
     */
    private Date zhgxsj;

    /**
     * 证照名称
     */
    private String content;

    /**
     * 拥有者证件号码
     */
    private String ownerid;

    /**
     * 拥有者姓名
     */
    private String ownername;

    /**
     * 有效期开始，8位数字日期格式
     */
    private Date infovaliditybegin;

    /**
     * 有效期结束，8位数字日期格式
     */
    private Date infovalidityend;

    /**
     * 行政区划
     */
    private String orgcode;

    /**
     * 数据首次推送时间
     */
    private Date createdate;

    /**
     * 修改时间（时间戳字段）
     */
    private Date modifydate;

}