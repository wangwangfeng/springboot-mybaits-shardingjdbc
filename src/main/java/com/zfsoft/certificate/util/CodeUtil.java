package com.zfsoft.certificate.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 根据部门统一社会信用代码生成证照流水号
 */
public class CodeUtil {

    public static String produceCode(String zzmcdm, String version) {

        Random random = new Random();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            result.append(random.nextInt(10));
        }
        StringBuilder orginCode = new StringBuilder("1.2.156.3003.2");
        orginCode.append(".").append(zzmcdm);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String ymd = sdf.format(new Date());
        orginCode.append(".").append(ymd);
        orginCode.append(result);
        orginCode.append(".").append(version);
        String code = orginCode.toString().substring(15).replace(".", "");
        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Map<String, Integer> mapStr = new HashMap<>();
        Map<Integer, String> mapInt = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            mapStr.put(String.valueOf(str.charAt(i)), i);
            mapInt.put(i, String.valueOf(str.charAt(i)));
        }
        int m = 36;
        int sum = 0;
        for (int i = 0; i < code.length(); i++) {
            if (i == 0) {
                sum = m + mapStr.get(String.valueOf(code.charAt(i)));
            } else {
                sum = sum + mapStr.get(String.valueOf(code.charAt(i)));
            }
            sum = sum % m == 0 ? m : sum % m;
            sum = sum * 2;
            sum = sum % (m + 1);
        }
        int check = 0;
        if (sum <= 1) {
            check = 1 - sum;
        } else {
            check = m + 1 - sum;
        }
        orginCode.append(".").append(mapInt.get(check));
        return orginCode.toString();
    }

}
