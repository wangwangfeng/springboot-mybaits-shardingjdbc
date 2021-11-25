package com.zfsoft.certificate.util;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.suwell.ofd.custom.agent.HTTPAgent;
import com.suwell.ofd.custom.wrapper.Const.Meta;
import com.suwell.ofd.custom.wrapper.Const.Target;
import com.suwell.ofd.custom.wrapper.Packet;
import com.suwell.ofd.custom.wrapper.model.MarkPosition;
import com.suwell.ofd.custom.wrapper.model.Template;
import com.suwell.ofd.custom.wrapper.model.TextInfo;
import com.zfsoft.certificate.pojo.SysAtta;
import com.zfsoft.certificate.pojo.ZzWorkBusinessMetadata;
import com.zfsoft.certificate.service.SysAttaService;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Classname: HTTPAgentUtil
 * @Description: 数科软件相关工具类
 * @Date: 2021/6/11 10:58
 * @author: wwf
 */
@Component
@Slf4j
public class HTTPAgentUtil {

    @Autowired
    private SysAttaService sysAttaService;
    private static HTTPAgentUtil httpAgentUtil;

    @PostConstruct
    public void init() {
        httpAgentUtil = this;
        httpAgentUtil.sysAttaService = this.sysAttaService;
    }

    /**
     * 单页模板转换，暂不引入加密水印等
     *
     * @param ofdFilePath 模板的使用路径
     * @param xmlFilePath 合成xml数据完整路径
     * @param relSavePath 生成ofd中间段路径
     * @param ofdName     ofd名称
     * @return
     * @throws Exception
     */
    public static SysAtta singleConvert(String ofdFilePath, String xmlFilePath,
                                        String relSavePath, String ofdName) {

        HTTPAgent ha = new HTTPAgent(ConstantUtils.constant.getAgentUrl());

        try {
            String fileName = StringUtils.isNotEmpty(ofdName) ? ofdName : UUID.randomUUID().toString() + ".ofd";
            String fileRelDir = StringUtils.isNotEmpty(relSavePath) ? relSavePath : "temp" + "/" + "ofd" + "/";
            String finalOfdFilePath = ConstantUtils.constant.getBasicPath() + fileRelDir + fileName;
            File f = new File(finalOfdFilePath);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
            }
            Packet packet = new Packet("common.template", Target.OFD);
            InputStream temp = new FileInputStream(new File(ofdFilePath));
            InputStream data = new FileInputStream(new File(xmlFilePath));
            Template t = new Template("单页套转", temp, data);
            packet.data(t);
            packet.metadata(Meta.CUSTOM_DATAS, "test1=" + ConstantUtils.constant.getBasicPath() + relSavePath);
            packet.metadata(Meta.DOC_ID, fileName);
            ha.convert(packet, new FileOutputStream(new File(finalOfdFilePath)));
            SysAtta atta = new SysAtta();
            atta.setIsDelete("N");
            atta.setExtensionName("ofd");
            atta.setFilePath(fileRelDir);
            atta.setName(fileName);
            atta.setOriginName(fileName);
            atta.setUploadDate(new Date());
            httpAgentUtil.sysAttaService.saveOrUpdate(atta);
            return atta;
        } catch (Exception e) {
            log.error("异常信息:{}", e);
        } finally {
            try {
                // 删除合成的xml文件
                FileUtil.del(xmlFilePath);
                ha.close();
            } catch (IOException e) {
                log.error("异常信息:{}", e);
            }
        }
        return null;
    }

    /**
     * 实时多模板合并v-不加密
     */
    public static SysAtta multiPageConvert(String[] ofdPaths, String[] xmlPaths,
                                           String relSavePath, String ofdName,
                                           String imgPath, String firstUnderlayWith,
                                           String waterMark)
            throws Exception {

        HTTPAgent ha = new HTTPAgent(ConstantUtils.constant.getAgentUrl());
        Packet packet = new Packet("common.template", Target.OFD);
        String finalOfdFilePath = null;
        String elecQRCodeSavePath = imgPath;
        if (StringUtils.isNotEmpty(elecQRCodeSavePath)) {
            File elecQRCodeFile = new File(elecQRCodeSavePath);
            if (elecQRCodeFile.exists()) {
                int[] intArr = {1};
                if (StringUtils.isEmpty(firstUnderlayWith)) {
                    firstUnderlayWith = "400";
                }
                float width = Float.parseFloat(firstUnderlayWith) / 10;
                packet.imageMark(new FileInputStream(elecQRCodeFile), "jpg",
                        new MarkPosition(1f, 1f, width, width, intArr));
            }
        }
        String fileName = StringUtils.isNotEmpty(ofdName) ? ofdName : UUID.randomUUID().toString() + ".ofd";
        String fileRelDir = StringUtils.isNotEmpty(relSavePath) ? relSavePath : "temp" + "/" + "ofd" + "/";
        finalOfdFilePath = ConstantUtils.constant.getBasicPath() + fileRelDir + fileName;
        File f = new File(finalOfdFilePath);
        if (!f.exists()) {
            f.getParentFile().mkdirs();
        }
        for (int i = 0; i < ofdPaths.length; i++) {
            if (ofdPaths[i] == null) {
                continue;
            }
            InputStream temp = new FileInputStream(new File(ofdPaths[i]));
            InputStream data = new FileInputStream(new File(xmlPaths[i]));
            Template template = new Template("单页套转", temp, data);
            packet.data(template);
        }

        if (StringUtils.isNotEmpty(waterMark)) {
            packet.textMark(new TextInfo(waterMark, "宋体", 20),
                    new MarkPosition(10, 10, 10, 10, MarkPosition.INDEX_ALL));
        }

        try {
            packet.metadata(Meta.CUSTOM_DATAS, "test1=" + ConstantUtils.constant.getBasicPath() + relSavePath);
            packet.metadata(Meta.DOC_ID, fileName);
            ha.convert(packet, new FileOutputStream(new File(finalOfdFilePath)));
            SysAtta atta = new SysAtta();
            atta.setIsDelete("N");
            atta.setExtensionName("ofd");
            atta.setFilePath(fileRelDir);
            atta.setName(fileName);
            atta.setOriginName(fileName);
            atta.setUploadDate(new Date());
            httpAgentUtil.sysAttaService.saveOrUpdate(atta);
            return atta;
        } catch (Exception e) {
            log.error("异常信息:{}", e);
        } finally {
            try {
                // 删除合成的xml文件
                for (int i = 0; i < xmlPaths.length; i++) {
                    if (xmlPaths[i] == null) {
                        continue;
                    }
                    FileUtil.del(xmlPaths[i]);
                }
                ha.close();
            } catch (IOException e) {
                log.error("异常信息:{}", e);
            }
        }

        return null;
    }

    /**
     * ofd合成组装xml，返回xml完整路径
     * 二期生成xml文件流形式，减少生成的文件(现在合成后删除生成的xml文件)
     * @param list            照面元素
     * @param json            入参元素
     * @param certificateCode 证照流水号
     * @return
     * @throws Exception
     */
    public static String metaDataJSONOfdXml(List<ZzWorkBusinessMetadata> list,
                                            JSONObject json, String certificateCode)
            throws Exception {

        Document doc = DocumentHelper.createDocument();
        doc.addComment("ofdxml");
        Element root = doc.addElement("DataRoot");
        root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");

        for (int i = 0; i < list.size(); i++) {
            ZzWorkBusinessMetadata md = list.get(i);
            String key = md.getName();
            String value = "null".equals(json.getString(key)) ? null : json.getString(key);
            //获取二维码内容转成Base64字符串二维码图片
            StringBuilder stringBuilder = new StringBuilder();
            //不动产权证的附图ID
            //String photoID = "null".equals(String.valueOf(json.get("photo2"))) ? null : String.valueOf(json.get("photo2"));
            if ("ewm".equals(key)) {
                if ("ff80808162e240cf0162fa7d49c9025a".equals(md.getStyleId())) {
                    //中华人民共和国不动产权证书(证照标识-不动产权证书号-附图ID)
                    stringBuilder.append(certificateCode).append("^")
                            .append(json.get("bh1") + "(" + json.get("bh2") + ")" +
                                    json.get("bh3") + "不动产权第" + json.get("bh4") + "号");
                    //.append("^").append(photoID);
                } else if ("ff808081634d86f30163713a2257020f".equals(md.getStyleId())) {
                    //代理记账许可证书(证照标识-许可证书编号-发证机关-发证日期-机构名称-机构代码)
                    stringBuilder.append(certificateCode).append("^")
                            .append(json.get("zsbh")).append("^")
                            .append(json.get("fzjg")).append("^")
                            .append(json.get("year") + "-" + json.get("month")
                                    + "-" + json.get("day")).append("^")
                            .append(json.get("dwmc")).append("^")
                            .append(json.get("tyxydm"));
                } else if ("ff80808166e75bd301671a9a0ced3466".equals(md.getStyleId())) {
                    //烟草专卖零售许可证(证照标识-企业名称-企业类型-许可范围)
                    stringBuilder.append(certificateCode).append("^")
                            .append(json.get("qymc")).append("^")
                            .append(json.get("qylx")).append("^")
                            .append(json.get("xkfw"));
                } else if ("ff80808162e240cf0162fa5ab7090195".equals(md.getStyleId())) {
                    //中华人民共和国建筑工程施工许可证(证照标识-施工许可证编号-工程名称-建设单位-工程项目编号)
                    stringBuilder.append(certificateCode).append("^")
                            .append(json.get("bh")).append("^")
                            .append(json.get("gcmc")).append("^")
                            .append(json.get("jsdw")).append("^")
                            .append(json.get("gcxmbh"));
                }
            } else if ("cxewm".equals(key)) {
                if ("ff808081648f317e0164ef18667803c8".equals(md.getStyleId())) {
                    //不动产登记证明(证照标识-不动产登记证明号)
                    stringBuilder.append(certificateCode).append("^")
                            .append(json.get("zmh1") + "(" + json.get("zmh2") + ")" +
                                    json.get("zmh3") + "不动产证明第" + json.get("djh") + "号");
                }
            }
            if (stringBuilder.length() > 0
                    && !"null".equals(stringBuilder.toString())
                    && !"".equals(stringBuilder.toString())) {
                byte[] bytes = MatrixToImageWriter.getQRCodeImage(stringBuilder.toString(), 110, 110);
                value = Base64Tools.encodeBase64String(bytes);
                //Base64FileUitl.decoderBase64File(value, "D://ewm.png");
            }
            Element dataInfo = root.addElement("DataInfo");
            dataInfo.addAttribute("Name", md.getName());
            if ("photo2".equals(key)) {
                value = null;
            }
            if (ConstantUtils.constant.getTwo().equals(md.getControlType())) {
                if (StringUtils.isEmpty(value)) {
                    continue;
                }
                dataInfo.addAttribute("Value", value);
            } else {
                dataInfo.addAttribute("Value", value);
            }
        }
        String fileDir = ConstantUtils.constant.getBasicPath() + "temp" + "/"
                + "ofd" + "/";
        String finalXmlFilePath = fileDir + UUID.randomUUID() + ".xml";
        File tempFile = new File(fileDir);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        Writer out;
        try {
            File f = new File(finalXmlFilePath);
            out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(f), StandardCharsets.UTF_8.name()));
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(doc);
            writer.close();
        } catch (IOException e) {
            log.error("异常信息:{}", e);
        }

        return finalXmlFilePath;
    }

}
