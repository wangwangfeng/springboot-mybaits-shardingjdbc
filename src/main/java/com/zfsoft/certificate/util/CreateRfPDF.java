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
 * @Description: 生成人防PDF文件
 * @Date: 2021/7/22 9:14
 * @author: wwf
 */
@Slf4j
public class CreateRfPDF {

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
            // 使用Windows字体
            //BaseFont bfChinese = BaseFont.createFont("C:/Windows/Fonts/simfang.ttf",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
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
            new CreateRfPDF().createPDF(document, object);
            document.close();
        } catch (Exception e) {
            log.error("异常信息:{}", e);
        }


        return null;
    }

    // 生成PDF文件
    public void createPDF(Document document, Object object) throws Exception {

        Paragraph paragraph1 = new Paragraph("防空地下室易地建设费缴费告知书", headfont);
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
        str4.append("你单位在（项目地址）");
        str4.append("【项目地址】");
        str4.append("实施");
        str4.append("【项目名称】");
        str4.append("（项目名称），已通过");
        str4.append("【审批部门】");
        str4.append("（审批部门）防空地下室易地建设行政审批。依据《中华人民共和国人民防空法》《安徽省物价局 安徽省财政厅 安徽省人民防空办公室关于降低我省防空地下室易地建设费标准的通知》等规定，你单位应依法缴纳防空地下室易地建设费合计人民币");
        str4.append("【应缴费额】");
        str4.append("元。计征明细情况：应缴费基数");
        str4.append("【应缴费基数】");
        str4.append("，征收标准");
        str4.append("【征收标准】");
        str4.append("，减免性质");
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

        StringBuilder str5 = new StringBuilder();
        str5.append("请你单位于");
        str5.append("【限缴日期】");
        str5.append("前，到项目所在地政务服务大厅税务窗口或税务局办税服务厅办理防空地下室易地建设费缴纳的相关事宜。");
        Paragraph paragraph5 = new Paragraph(str5.toString(), textfont);
        paragraph5.setAlignment(0);
        paragraph5.setFirstLineIndent(22);
        paragraph5.setLeading(20f);
        paragraph5.setSpacingBefore(5f);
        paragraph5.setSpacingAfter(5f);

        Paragraph paragraph6 = new Paragraph("联系人：【联系人姓名】  电话：【联系人电话】", textfont);
        paragraph6.setAlignment(0);
        paragraph6.setFirstLineIndent(22);
        paragraph6.setLeading(20f);
        paragraph6.setSpacingBefore(5f);
        paragraph6.setSpacingAfter(5f);

        Paragraph paragraph7 = new Paragraph("【受理部门】", textfont);
        paragraph7.setAlignment(2);
        paragraph7.setLeading(20f);
        paragraph7.setSpacingBefore(5f);
        paragraph7.setSpacingAfter(5f);

        Paragraph paragraph8 = new Paragraph("【受理日期】", textfont);
        paragraph8.setAlignment(2);
        paragraph8.setLeading(20f);

        document.add(paragraph1);
        document.add(paragraph2);
        document.add(paragraph3);
        document.add(paragraph4);
        document.add(paragraph5);
        document.add(paragraph6);
        document.add(paragraph7);
        document.add(paragraph8);

    }


}
