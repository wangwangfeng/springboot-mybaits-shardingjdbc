package com.zfsoft.certificate.util;

import com.zfsoft.certificate.pojo.base.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Classname: ConstantUtils
 * @Description: 常量工具类
 * @Date: 2021/6/15 14:34
 * @author: wwf
 */
@Component
public class ConstantUtils {

    public static Constant constant;

    @Autowired
    public void setConstant(Constant constant) {
        ConstantUtils.constant = constant;
    }

}
