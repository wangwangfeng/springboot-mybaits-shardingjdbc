package com.zfsoft.certificate.util;

import com.aspose.words.Document;
import com.zfsoft.certificate.pojo.base.CertificateBaseBO;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Map;

/**
 * @Classname: AsposeUtils
 * @Description: aspose-word for java 工具类
 * 注意字体问题(windows下正常，Linux乱码是缺少对应字体文件导致)
 * 注意：使用Aspose时，每一个模块（words,pdf,cells）都有相同的类，如License类，SaveOptions类，SaveFormat类，功能各不相同
 * @Date: 2021/7/27 13:40
 * @author: wwf
 */
@Slf4j
public class AsposeUtils {

    // 定义公共输出文件夹路径
    private final static String outPath = "D://tests/";

    /**
     * @Description: 不从配置文件读取去除水印
     **/
    public static boolean getLicense() {
        boolean result = false;
        try {
            String licenseXml = "<License>\n" +
                    "   <Data>\n" +
                    "     <Products>\n" +
                    "       <Product>Aspose.Total for Java</Product>\n" +
                    "       <Product>Aspose.Words for Java</Product>\n" +
                    "     </Products>\n" +
                    "     <EditionType>Enterprise</EditionType>\n" +
                    "     <SubscriptionExpiry>20991231</SubscriptionExpiry>\n" +
                    "     <LicenseExpiry>20991231</LicenseExpiry>\n" +
                    "     <SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>\n" +
                    "   </Data>\n" +
                    "   <Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>\n" +
                    " </License>";

            InputStream inputStream = new ByteArrayInputStream(licenseXml.getBytes("UTF-8"));
            com.aspose.words.License license = new com.aspose.words.License();
            license.setLicense(inputStream);
            result = true;
        } catch (Exception e) {
            log.error("异常信息:{}", e);
        }
        return result;
    }

    /**
     * @Description: 验证wordLicense去掉水印
     * @Date: 2021/7/27 13:48
     * @author: wwf
     * @param:
     * @return: boolean
     **/
    public static boolean getWordLicense() {
        boolean result = false;
        try {
            InputStream inputStream = AsposeUtils.class.getResourceAsStream("/templates/license.xml");
            com.aspose.words.License license = new com.aspose.words.License();
            license.setLicense(inputStream);
            result = true;
        } catch (Exception e) {
            log.error("异常信息:{}", e);
        }
        return result;
    }

    // 验证excelLicense去掉水印
/*    public static boolean getExcelLicense() {
        boolean result = false;
        try {
            InputStream inputStream = AsposeUtils.class.getResourceAsStream("/templates/license.xml");
            com.aspose.cells.License license = new com.aspose.cells.License();
            license.setLicense(inputStream);
            result = true;
        } catch (Exception e) {
            log.error("异常信息:{}", e);
        }
        return result;
    }*/

    /**
     * @param fileName 目标文件名称 例：11.pdf
     * @Description: word转pdf
     * @Date: 2021/7/27 14:30
     * @author: wwf
     * @param: inputPath 源文件路径
     * @return: void
     **/
    public static void doc2pdf(String inputPath, String fileName) throws Exception {

        if (!getWordLicense()) {
            throw new Exception("请联系管理员更换asposeLicense签名凭证");
        }

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(new File(inputPath));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("源文件不存在！");
        }

        File file = new File(outPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            long start = System.currentTimeMillis();
            File outFile = new File(outPath + fileName);
            FileOutputStream os = new FileOutputStream(outFile);
            Document document = new Document(inputPath);
            // 方式一：填充书签数据(操作：插入-书签)(不存在的标签不会展示出来)
            //document.getRange().getBookmarks().get("gzswh").setText("安徽省安庆市大观区[2021]第11111111号");
            //document.getRange().getBookmarks().get("jfrmc").setText("上海卓繁有限公司");
            //document.getRange().getBookmarks().get("tyshdm").setText("91310000729355068Q");
            //document.getRange().getBookmarks().get("slrq").setText(DateUtil.now());

            // 方式二：MergeField填充字段(操作：插入-文档部件-域-邮件合并-MergeField)
            // 日期类型默认展示形式-2021/7/28 13:31:31
            // String[] fieldNames = new String[]{"xmdz", "xmmc"};
            // Object[] fieldValues = new Object[]{"合肥市", DateUtil.date()};
            // document.getMailMerge().execute(filedNames, filedValues);

            // 方式二：实体填充数据(不存在的域会展示出来)
            CertificateBaseBO baseBO = new CertificateBaseBO();
            baseBO.setOwnerId("1111");
            baseBO.setOwnerName("张三");
            Map<String, Object> data = StringUtils.getColumnNameValue(baseBO);
            String[] fieldNames = new String[data.size()];
            Object[] values = new Object[data.size()];
            int i = 0;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                fieldNames[i] = entry.getKey();
                values[i] = entry.getValue();
                i++;
            }
            document.getMailMerge().execute(fieldNames, values);

            document.save(os, com.aspose.words.SaveFormat.PDF);
            long end = System.currentTimeMillis();
            log.info("转化耗时:{}", end - start);
        } catch (Exception e) {
            throw new Exception("word转pdf失败！" + e);
        } finally {
            if (inputPath != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("异常信息:{}", e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("异常信息:{}", e);
                }
            }
        }
    }

    /**
     * @Description: excel转pdf
     * @Date: 2021/7/30 17:07
     * @author: wwf
     * @param: inputPath
     * @param fileName
     * @return: void
     **/
/*    public static void excel2pdf(String inputPath, String fileName) throws Exception {

        if (!getExcelLicense()) {
            throw new Exception("请联系管理员更换asposeLicense签名凭证");
        }

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(new File(inputPath));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("源文件不存在！");
        }

        File file = new File(outPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            long start = System.currentTimeMillis();
            File outFile = new File(outPath + fileName);
            FileOutputStream os = new FileOutputStream(outFile);
            Workbook workbook = new Workbook(inputPath);
            workbook.save(os, com.aspose.cells.SaveFormat.PDF);
            long end = System.currentTimeMillis();
            log.info("转化耗时:{}", end - start);
        } catch (Exception e) {
            throw new Exception("excel转pdf失败！" + e);
        } finally {
            if (inputPath != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("异常信息:{}", e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("异常信息:{}", e);
                }
            }
        }
    }*/

    public static void main(String[] args) {
        String inputPath = "D://tests/书签填充.doc";
        String inputPath1 = "D://tests/3.xls";
        String outFileName = "444444.pdf";
        try {
            doc2pdf(inputPath, outFileName);
            //excel2pdf(inputPath1, outFileName);
        } catch (Exception e) {
            log.error("异常信息:{}", e);
        }
    }

}
