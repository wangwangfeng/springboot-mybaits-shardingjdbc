package com.zfsoft.certificate.util;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.certificate.pojo.SysAtta;
import com.zfsoft.certificate.service.SysAttaService;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

/**
 * @Classname: DownloadUtil
 * @Description: TODO
 * @Date: 2021/6/11 10:10
 * @author: wwf
 */
@Slf4j
@Component
public class DownloadUtil {

    @Autowired
    private SysAttaService sysAttaService;
    private static DownloadUtil downloadUtil;

    @PostConstruct
    public void init() {
        downloadUtil = this;
        downloadUtil.sysAttaService = this.sysAttaService;
    }

    /**
     * 获取附件的完整路径
     * 存在返回正确路径，找不到文件的返回空
     *
     * @param attaOid
     * @return
     * @throws Exception
     */
    public static String getSysAttaAbsFilePathByAttaOid(String attaOid) throws Exception {

        SysAtta sysAtta = downloadUtil.sysAttaService.getSysAttaByOid(attaOid);
        if (sysAtta == null) {
            return null;
        }

        String filePath = sysAtta.getFilePath();
        String fileName = sysAtta.getName();

        String downloadFilePath = "";
        if (StringUtils.isNotEmpty(filePath)) {
            // 默认盘符使用Z
            downloadFilePath = ConstantUtils.constant.getBasicPath() + filePath + fileName;
            log.info("存在否savePath路径是:{}", downloadFilePath);
        }

        return downloadFilePath;
    }

    /**
     * 获取模板ZIP的解压文件夹路径
     *
     * @param attaOid
     * @return
     * @throws Exception
     */
    public static String getSysAttaAbsFileParentPathByAttaOid(String attaOid) throws Exception {

        SysAtta sysAtta = downloadUtil.sysAttaService.getSysAttaByOid(attaOid);
        if (sysAtta == null) {
            return null;
        }
        String filePath = sysAtta.getFilePath();
        String downloadFilePath = "";
        if (StringUtils.isNotEmpty(filePath)) {
            downloadFilePath = ConstantUtils.constant.getBasicPath() + filePath;
        }
        return downloadFilePath;
    }

    /**
     * @Description: 印章刻制参数拼接
     * @author: wwf
     * @param: modelName
     * @param attaName
     * @param sealCodeFirst
     * @param sealCodeSecond
     * @param sealCodeThird
     * @param ruleInfoFirst
     * @param ruleInfoSecond
     * @param ruleInfoThird
     * @return: java.lang.String
     **/
    public static String sealAutoPdfZF(String modelName, String attaName,
                                       String sealCodeFirst, String sealCodeSecond, String sealCodeThird,
                                       String ruleInfoFirst, String ruleInfoSecond, String ruleInfoThird) {

        StringBuilder str = new StringBuilder();
        str.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
        str.append("<SEAL_DOC_REQUEST>");
        //基础数据  id 密码
        str.append("<BASE_DATA>");
        str.append("<SYS_ID>zfsoft</SYS_ID>");
        str.append("<SYS_PWD>zfsoft123</SYS_PWD>");
        str.append("</BASE_DATA>");
        //扩展信息
        str.append("<META_DATA>");
        //是否模板合并
        str.append("<IS_MERGER>" + false + "</IS_MERGER>");
        str.append("</META_DATA>");
        //文件列表  FILE_LIST 节点
        str.append("<FILE_LIST>");
        //子节点（可以有多个）
        str.append("<TREE_NODE>");
        //文档名称
        str.append("<FILE_NO>" + attaName + "</FILE_NO>");
        //应用场景base64
        str.append("<CJ_TYPE>base64</CJ_TYPE>");

        str.append("<MODEL_NAME>" + modelName + "</MODEL_NAME>");
        //是否模板合并
        str.append("<IS_MERGER>" + false + "</IS_MERGER>");
        //是否添加二维码
        str.append("<IS_CODEBAR>" + false + "</IS_CODEBAR>");
        //文档类型
        String docType = "ofd";
        str.append("<DOC_TYPE>" + docType + "</DOC_TYPE>");
        //规则类型0：按规则号盖章，1：按规则信息盖章
        str.append("<RULE_TYPE>1</RULE_TYPE>");
        //规则
        str.append("<RULE_LIST>");
        str.append("<RULE_NODE>");
        //文字覆盖
        //str.append("<RULE_INFO>AUTO_ADD:0,0,1,1,255, 注册日期)|(4,</RULE_INFO>");
        //绝对坐标
        str.append("<RULE_INFO>" + ruleInfoFirst + "</RULE_INFO>");
        str.append("<SEAL_CODE>" + sealCodeFirst + "</SEAL_CODE>");

        str.append("</RULE_NODE>");
        if (StrUtil.isNotEmpty(ruleInfoSecond) && StrUtil.isNotEmpty(sealCodeSecond)) {
            str.append("<RULE_NODE>");
            str.append("<RULE_INFO>" + ruleInfoSecond + "</RULE_INFO>");
            str.append("<SEAL_CODE>" + sealCodeSecond + "</SEAL_CODE>");
            str.append("</RULE_NODE>");
        }
        if (StrUtil.isNotEmpty(sealCodeThird) && StrUtil.isNotEmpty(ruleInfoThird)) {
            str.append("<RULE_NODE>");
            str.append("<RULE_INFO>" + ruleInfoThird + "</RULE_INFO>");
            str.append("<SEAL_CODE>" + sealCodeThird + "</SEAL_CODE>");
            str.append("</RULE_NODE>");
        }
        str.append(" </RULE_LIST>");
        str.append("</TREE_NODE>");
        str.append("</FILE_LIST>");
        str.append("</SEAL_DOC_REQUEST>");

        return str.toString();
    }

    /**
     * 对请求盖章得到的响应xml内容解析
     *
     * @param xml
     * @param path
     * @return
     */
    public static boolean getXmlAttribute(String xml, String path) {
        boolean success = false;
        try {
            //将字符串转成xml
            Document doc = DocumentHelper.parseText(xml);
            //获取根节点
            Element rootElt = doc.getRootElement();
            //获取FILE_LIST节点
            Iterator fileList = rootElt.elementIterator("FILE_LIST");
            while (fileList.hasNext()) {
                Element abkRecord = (Element) fileList.next();
                //获取FILE节点
                Iterator file = abkRecord.elementIterator("FILE");
                while (file.hasNext()) {
                    Element fileRecord = (Element) file.next();
                    //FILE_MSG是否成功
                    String fileMsg = fileRecord.elementTextTrim("FILE_MSG");
                    //盖章后文件下载路径
                    String fileUrl = fileRecord.elementTextTrim("FILE_URL");

                    if (fileMsg.contains("成功")) {
                        String url = fileUrl.replace("ESS", "yzyy");
                        //固定下载盖章后文件到Z盘符
                        String result = path;
                        //if (path.contains("I:")) {
                        //result = path.replaceFirst("I:", "Z:");
                        //System.out.println("原来："+ path);
                        //FileUtils.delFile(path);
                        //}
                        //log.info("最终下载盖章的路径:" + result);
                        //String re = FileUtils.createFile(result).getPath();
                        //this.downloadFile(url, re);
                        downloadFile(url, result);
                        success = true;
                    } else {
                        success = false;
                    }
                }
            }
        } catch (Exception e) {
            log.error("异常信息:{}", e);
        }
        return success;
    }

    /**
     * 下载盖章后的文件
     *
     * @param urlPath
     * @param path
     * @throws Exception
     */
    public static void downloadFile(String urlPath, String path) throws Exception {

        File file = new File(path);
        //判断文件是否存在，不存在则创建文件
        if (!file.exists()) {
            file.createNewFile();
        }
        URL url = new URL(urlPath);
        HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
        urlCon.setConnectTimeout(6000);
        urlCon.setReadTimeout(6000);
        int code = urlCon.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK) {
            log.error("文件读取失败!");
            throw new Exception("文件读取失败");
        }
        DataInputStream in = new DataInputStream(urlCon.getInputStream());
        DataOutputStream out = new DataOutputStream(new FileOutputStream(path));
        byte[] buffer = new byte[2048];
        int count = 0;
        while ((count = in.read(buffer)) > 0) {
            out.write(buffer, 0, count);
        }
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {
            log.error("异常信息:{}", e);
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }


}
