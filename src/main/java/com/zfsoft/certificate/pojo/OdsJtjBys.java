package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname: OdsJtjBys
 * @Description: 毕业证书
 * @Date: 2021/6/9 17:31
 * @author: wwf
 */
@Data
public class OdsJtjBys implements Serializable {

    private static final long serialVersionUID = 937847734314959665L;

    /**
     * 毕业生编号
     */
    @TableId
    private String bysbh;

    /**
     * 姓名
     */
    private String xm;

    /**
     * 身份证号码
     */
    private String sfzh;

    /**
     * 性别
     */
    private String xb;

    /**
     * 入学年
     */
    private String rxn;

    /**
     * 入学月
     */
    private String rxy;

    /**
     * 毕业年
     */
    private String byn;

    /**
     * 毕业月
     */
    private String byy;

    /**
     * 学校
     */
    private String xx;

}
