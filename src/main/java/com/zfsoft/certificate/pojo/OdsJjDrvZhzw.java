package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 驾驶证_照面信息
 */
@Data
@TableName(value = "ods_jj_drv_zhzw")
public class OdsJjDrvZhzw implements Serializable {

    private static final long serialVersionUID = -1849316668725174680L;

    /**
     * 档案编号
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String dabh;

    /**
     * 身份证号码
     */
    private String infocode;

    /**
     * 姓名
     */
    private String name1;

    /**
     * 姓名1男2女
     */
    private String sex;

    /**
     * 国籍
     */
    private String country;

    /**
     * 登记住所详细地址
     */
    private String address;

    /**
     * 初次领证日期
     */
    private String firstday;

    /**
     * 准驾车型
     */
    private String type;

    /**
     * 照片
     */
    private byte[] photo;

    /**
     * 记录
     */
    private String jl1;

    /**
     * 记录
     */
    private String jl2;

    /**
     * 条形码
     */
    private String barcode;

    /**
     * 条形码数字
     */
    private String barnumber;

    /**
     * 有效期开始，8位数字日期格式
     */
    private Date infovaliditybegin;

    /**
     * 有效期结束，8位数字日期格式
     */
    private Date infovalidityend;

    /**
     * 修改时间（时间戳字段）
     */
    private Date modifydate;

}