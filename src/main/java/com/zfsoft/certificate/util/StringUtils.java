package com.zfsoft.certificate.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author 86131
 */
@Slf4j
public class StringUtils extends StrUtil {

    /**
     * 字符串处理
     * 去空格、换行、引号、反斜杠\、引号等
     *
     * @param str
     * @return
     */
    public static String handleString(String str) {
        if (StrUtil.isNotEmpty(str)) {
            Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
            Matcher matcher = pattern.matcher(str);
            String dest = matcher.replaceAll("");
            return dest.replaceAll("\\\\", "/").replaceAll("\"", "");
        } else {
            return "";
        }
    }

    /**
     * 根据证件号返回调用省级接口证件类型
     *
     * @param ownerId
     * @return
     */
    public static String infoTypeCode(String ownerId) {
        if (StrUtil.isNotEmpty(ownerId)) {
            if (IdcardUtil.validateCard(ownerId)) {
                // 个人证照
                if (ownerId.length() == 18) {
                    return "111";
                } else {
                    return "999";
                }
            } else {
                // 法人证照
                if (ownerId.length() == 18) {
                    return "001";
                } else {
                    return "099";
                }
            }
        } else {
            return "111";
        }
    }

    /**
     * @Description: 生成一个MD5加密字符串
     * @Date: 2021/6/11 17:17
     * @author: wwf
     * @param: str
     * @return: java.lang.String
     **/
    public static String getMD5String(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            log.error("异常信息:{}", e);
            return null;
        }
    }

    /**
     * @Description: word模板填充实体数据
     * (实体里面非字符串类型，该方法可进行处理)
     * @Date: 2021/7/28 14:25
     * @author: wwf
     * @param: object
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     **/
    public static Map<String, Object> getColumnNameValue(Object object) {

        Class<?> objectClass = object.getClass();
        Field[] fields = objectClass.getDeclaredFields();
        Map<String, Object> map = new HashMap<>(fields.length);
        try {
            for (Field field : fields) {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), objectClass);
                Method method = pd.getReadMethod();
                String key = field.getName();
                Object value = method.invoke(object);
                // 获取属性的类型
                String type = field.getGenericType().toString();
                // System.out.println("属性类型:" + type);
                if (value != null) {
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            log.error("异常信息:{}", e);
        }
        return map;
    }

}
