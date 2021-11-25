package com.zfsoft.certificate.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;

/**
 * @Classname: CreateRFPDF
 * @Description: 生成水利PDF文件
 * @Date: 2021/7/22 9:14
 * @author: wwf
 */
@Slf4j
public class CreateSlPDF {

    // 定义全局的字体静态变量
    private static Font headfont;
    private static Font textfont;
    // 最大宽度
    private static int maxWidth = 520;

    // 静态代码块
    static {
        try {
            // 不同字体（这里定义为同一种字体：包含不同字号、不同style）
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            headfont = new Font(bfChinese, 14, Font.BOLD);
            textfont = new Font(bfChinese, 12, Font.NORMAL);

        } catch (Exception e) {
            log.error("异常信息:{}", e);
        }
    }

    public static String exportPDF(Object object, String pdfPath) {

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            document.open();
            new CreateSlPDF().createPDF(document, object);
            document.close();
        } catch (Exception e) {
            log.error("异常信息:{}", e);
        }


        return null;
    }

    // 生成PDF文件
    public void createPDF(Document document, Object object) throws Exception {

        Paragraph paragraph1 = new Paragraph("水土保持补偿费缴费告知书", headfont);
        paragraph1.setAlignment(1);
        //行间距
        paragraph1.setLeading(20f);
        //设置段落上空白
        paragraph1.setSpacingBefore(5f);
        //设置段落下空白
        paragraph1.setSpacingAfter(10f);

        String str2 = "告知书文号";
        Paragraph paragraph2 = new Paragraph(str2, textfont);
        paragraph2.setAlignment(1);
        paragraph2.setLeading(20f);
        paragraph2.setSpacingBefore(5f);
        paragraph2.setSpacingAfter(5f);

        String str3 = "【缴费人名称】(统一社会信用代码【统一社会信用代码】）：";
        Paragraph paragraph3 = new Paragraph(str3, textfont);
        paragraph3.setAlignment(0);
        paragraph3.setLeading(20f);
        paragraph3.setSpacingBefore(5f);
        paragraph3.setSpacingAfter(5f);


        StringBuilder str4 = new StringBuilder();
        str4.append("你单位在");
        str4.append("【项目地址】");
        str4.append("实施");
        str4.append("【项目名称】");
        str4.append("，已通过");
        str4.append("【审批部门】");
        str4.append("水土保持行政许可，许可文号");
        str4.append("【许可文号】");
        str4.append("。依据《中华人民共和国水土保持法》《安徽省水土保持补偿费征收使用管理实施办法》《安徽省物价局 安徽省财政厅 安徽省水利厅关于我省水土保持补偿费收费标准的通知》《安徽省物价局 安徽省财政厅转发国家发展改革委 财政部关于降低电信网码号资源占用费等部分行政事业性收费标准的通知》等规定，你单位应依法缴纳水土保持补偿费人民币");
        str4.append("【应缴费额】");
        str4.append("元。计征明细情况：应缴费基数");
        str4.append("【应缴费基数】");
        str4.append("h㎡，征收标准");
        str4.append("【征收标准】");
        str4.append("元/㎡，减免性质");
        str4.append("【减免性质】");
        str4.append("，减免费额");
        str4.append("【减免费额】");
        str4.append("。");
        Paragraph paragraph4 = new Paragraph(str4.toString(), textfont);
        paragraph4.setAlignment(0);
        //设置首行缩进
        paragraph4.setFirstLineIndent(22);
        paragraph4.setLeading(20f);
        paragraph4.setSpacingBefore(5f);
        paragraph4.setSpacingAfter(5f);

        Paragraph paragraph5 = new Paragraph("请你单位在开工前（开办一般性生产建设项目的在项目开工前、开采矿产资源处于建设期的在建设活动开始前），到项目所在地政务服务大厅税务窗口或税务局办税服务厅办理水土保持补偿费缴纳的相关事宜。", textfont);
        paragraph5.setAlignment(0);
        paragraph5.setFirstLineIndent(22);
        paragraph5.setLeading(20f);
        paragraph5.setSpacingBefore(5f);
        paragraph5.setSpacingAfter(5f);

        Paragraph paragraph6 = new Paragraph("开采矿产资源的，除上述建设期水土保持补偿费外，你单位在项目投产后，需在季度终了后15日内，按季度向项目所在地税务部门自行申报缴纳矿产资源开采期水土保持补偿费。征收标准：井下开采类项目按销售额1‰计征，露天开采类项目按销售额1.5‰，已实行资源税改革的煤炭企业减半收费。", textfont);
        paragraph6.setAlignment(0);
        paragraph6.setFirstLineIndent(22);
        paragraph6.setLeading(20f);
        paragraph6.setSpacingBefore(5f);
        paragraph6.setSpacingAfter(5f);

        Paragraph paragraph7 = new Paragraph("联系人：【联系人姓名】  电话：【联系人电话】", textfont);
        paragraph7.setAlignment(0);
        paragraph7.setFirstLineIndent(22);
        paragraph7.setLeading(20f);
        paragraph7.setSpacingBefore(5f);
        paragraph7.setSpacingAfter(5f);

        Paragraph paragraph8 = new Paragraph("【受理部门】", textfont);
        paragraph8.setAlignment(2);
        paragraph8.setLeading(20f);
        paragraph8.setSpacingBefore(5f);
        paragraph8.setSpacingAfter(5f);

        Paragraph paragraph9 = new Paragraph("【受理日期】", textfont);
        paragraph9.setAlignment(2);
        paragraph9.setLeading(20f);

        document.add(paragraph1);
        document.add(paragraph2);
        document.add(paragraph3);
        document.add(paragraph4);
        document.add(paragraph5);
        document.add(paragraph6);
        document.add(paragraph7);
        document.add(paragraph8);
        document.add(paragraph9);

    }


}
