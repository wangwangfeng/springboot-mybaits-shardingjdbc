package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 安庆市常住人口信息系统街路巷信息
 *
 * @author 86131
 */
@Data
@TableName(value = "ods_hz_jlxxxb_zhzw")
public class OdsHzJlxxxbZhzw implements Serializable {

    private static final long serialVersionUID = 2564928395441072398L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String dm0000;

    private String mc0000;

    private String zt0000;

    private BigDecimal px0000;

}