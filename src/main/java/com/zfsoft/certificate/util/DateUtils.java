package com.zfsoft.certificate.util;

import cn.hutool.core.date.DatePattern;

import java.text.SimpleDateFormat;

/**
 * @Classname: DateUtils
 * @Description: TODO
 * @Date: 2021/6/10 16:03
 * @author: wwf
 */
public class DateUtils {

    /**
     * 判断输入的yyyyMMdd格式字符串是否是正确的日期格式
     *
     * @param dateString
     * @return
     */
    public static boolean isValidDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat(DatePattern.PURE_DATE_PATTERN);
        try {
            sdf.setLenient(false);
            sdf.parse(dateString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
