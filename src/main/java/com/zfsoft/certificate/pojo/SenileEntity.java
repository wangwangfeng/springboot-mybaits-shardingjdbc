package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 老年证
 * @author 86131
 */
@Data
@TableName("senile")
public class SenileEntity implements Serializable {

    private static final long serialVersionUID = 1800132859471057846L;

    @TableId
    private String bh;

    private String xm;

    private String xb;

    private String mz;

    private String sfzh;

    private String csrq;

    private String jzdz;

    private String fzjg;

    private String type;

    private String flag;

}