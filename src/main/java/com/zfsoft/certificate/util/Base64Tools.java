package com.zfsoft.certificate.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.io.*;

/**
 * @author 86131
 */
@Slf4j
public class Base64Tools {

    /**
     * byte数组 转换为 Base64字符串
     */
    public static String encodeBase64String(byte[] data) {
        return Base64.encodeBase64String(data);
    }

    /**
     * Base64字符串 转换为 byte数组
     */
    public static byte[] decodeBase64(String base64) {
        return Base64.decodeBase64(base64);
    }

    /**
     * 将文件转成base64字符串
     *
     * @param path
     * @return
     */
    public static String encodeBase64File(String path) {
        byte[] buffer = null;
        FileInputStream inputFile = null;
        try {
            File file = new File(path);
            inputFile = new FileInputStream(file);
            buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            return Base64.encodeBase64String(buffer);
        } catch (Exception e) {
            log.error("异常信息:{}", e);
        } finally {
            try {
                inputFile.close();
            } catch (IOException e) {
                log.error("异常信息:{}", e);
            }
        }
        return null;
    }

    /**
     * base64转文件
     *
     * @param strBase64
     * @param outFile
     * @throws IOException
     */
    public static void base64ToFile(String strBase64, String outFile) throws IOException {
        FileOutputStream out = null;
        try {
            byte[] buffer = Base64.decodeBase64(strBase64);
            out = new FileOutputStream(outFile);
            out.write(buffer);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * @param base64Content
     * @param filePath      base64字符串转pdf
     */
    public static void base64StringToPdf(String base64Content, String filePath) {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            byte[] bytes = Base64.decodeBase64(base64Content);
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
            bis = new BufferedInputStream(byteInputStream);
            File file = new File(filePath);
            File path = file.getParentFile();
            if (!path.exists()) {
                path.mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            byte[] buffer = new byte[1024];
            int length = bis.read(buffer);
            while (length != -1) {
                bos.write(buffer, 0, length);
                length = bis.read(buffer);
            }
            bos.flush();
        } catch (Exception e) {
            log.error("异常信息:{}", e);
        } finally {
            try {
                bos.close();
                fos.close();
                bis.close();
            } catch (IOException e) {
                log.error("异常信息:{}", e);
            }

        }
    }

}
