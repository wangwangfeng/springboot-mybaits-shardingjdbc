package com.zfsoft.certificate.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 文件工具类
 */
@Slf4j
public class FileUtils {

    /**
     * 文件拷贝
     */
    public static int copyStream(InputStream in, OutputStream out)
            throws IOException {
        int result = 0;

        byte[] buf = new byte[4096];
        int numRead;
        while ((numRead = in.read(buf)) != -1) {
            out.write(buf, 0, numRead);
            result += numRead;
        }
        out.flush();
        return result;
    }

    /**
     * 输入流转byte数组
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] getStreamBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copyStream(in, out);
        return out.toByteArray();
    }

    /**
     * 创建目录
     */
    public static void createDir(String dirName) {
        String[] dirList = dirName.split("[//\\\\]");
        String tmp = "";
        for (int i = 0; i < dirList.length; i++) {
            tmp = tmp + dirList[i] + "/";
            File fp = new File(tmp);
            if ((!fp.isDirectory()) && (!fp.isFile())) {
                fp.mkdir();
            }
        }
    }

    /**
     * 删除目录
     */
    public static void deleteDir(String dirName) {
        File fp = new File(dirName);
        File[] aa = fp.listFiles();
        for (int i = 0; i < aa.length; i++) {
            if (aa[i].isFile()) {
                aa[i].delete();
            } else if (aa[i].isDirectory()) {
                deleteDir(aa[i].getPath());
            }
        }
        fp.delete();
    }

    /**
     * 创建指定文件
     * (判断父级文件夹是否存在)
     *
     * @param destFilePath
     * @return
     */
    public static File createFile(String destFilePath) {
        File file = new File(destFilePath);
        if (file.exists()) {
            log.info("创建文件" + destFilePath + "失败，目标文件已存在！");
            return file;
        }
        if (!file.getParentFile().exists()) {
            // 如果目标文件所在的目录不存在，则创建父目录
            if (!file.getParentFile().mkdirs()) {
                log.error("创建目标文件所在目录失败！");
            }
        }
        try {
            if (file.createNewFile()) {
                return file;
            }
        } catch (IOException e) {
            log.error("创建目标文件失败！" + e);
        }
        return file;
    }

    /**
     * 删除文件
     */
    public static void delFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile()) {
            file.delete();
        }
    }

    /**
     * 批量删除文件
     */
    public static void delFile(String[] fileName) {
        if (fileName == null) {
            return;
        }
        for (int i = 0; i < fileName.length; i++) {
            delFile(fileName[i]);
        }
    }

    /**
     * 复制文件
     *
     * @param fromFile
     * @param toFile
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static int copyFile(String fromFile, String toFile)
            throws FileNotFoundException, IOException {
        InputStream is = new FileInputStream(fromFile);
        OutputStream os = new FileOutputStream(toFile);
        int ret = copyStream(is, os);
        is.close();
        os.close();
        return ret;
    }

    public static int copyToFile(InputStream in, String toFile)
            throws FileNotFoundException, IOException {
        OutputStream os = new FileOutputStream(toFile);
        int ret = copyStream(in, os);
        os.close();
        return ret;
    }

    /**
     * 计算文件大小
     */
    static String convertFileSize(long size) {
        int divisor = 1;
        String unit = "bytes";
        if (size >= 1048576L) {
            divisor = 1048576;
            unit = "MB";
        } else if (size >= 1024L) {
            divisor = 1024;
            unit = "KB";
        }
        if (divisor == 1) {
            return size / divisor + " " + unit;
        }
        String aftercomma = String.valueOf(100L * (size % divisor) / divisor);
        if (aftercomma.length() == 1) {
            aftercomma = "0" + aftercomma;
        }
        return size / divisor + "." + aftercomma + " " + unit;
    }

    public static String fileToString(String filePath, String fileName)
            throws Exception {
        String fileAddress = filePath + fileName;
        File dirFile = new File(fileAddress);
        if (!dirFile.exists()) {
            return "NO";
        }
        String line;
        FileInputStream in = new FileInputStream(dirFile);
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(in, StandardCharsets.UTF_8.name()));
        line = reader.readLine();
        while (line != null) {
            buffer.append(line);
            line = reader.readLine();
        }
        in.close();
        return buffer.toString();
    }

    public static String fileToBASE64String(String filePath, String fileName)
            throws Exception {
        String fileAddress = filePath + fileName;
        File dirFile = new File(fileAddress);
        if (!dirFile.exists()) {
            return "NO";
        }
        FileInputStream fis = new FileInputStream(dirFile);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(fis.available());
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = fis.read(b)) != -1) {
            bos.write(b, 0, len);
        }
        String base64String = Base64.encodeBase64String(bos.toByteArray());
        fis.close();
        bos.close();
        return base64String.replaceAll("[\\s*\t\n\r]", "");
    }

    public static String getSuffix(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 判断是否是OFD文件
     *
     * @param file
     * @return
     */
    public static boolean isOFD(File file) {
        return "OFD".equals(getSuffix(file).toUpperCase());
    }

}
