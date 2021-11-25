package com.zfsoft.certificate.util;

import cn.hutool.core.collection.CollUtil;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @Classname: PDFUtils
 * @Description: PDF操作工具类
 * @Date: 2021/6/29 9:34
 * @author: wwf
 */
@Slf4j
public class PDFUtils {

    /**
     * @Description: 根据PDF模板生产PDF文件
     * @Date: 2021/6/29 9:35
     * @author: wwf
     * @param: templatePath pdf模板文件位置
     * @param targetPath 生产pdf文件位置
     * @param formMap pdf要插入表单元素
     * @param imagePathMap pdf要插入图片位置
     * @return: void
     **/
    public static void generatorPdf(String templatePath, String targetPath,
                                    Map<String, String> formMap, Map<String, String> imagePathMap) {

        try (InputStream input = new FileInputStream(templatePath)) {
            PdfReader reader = new PdfReader(input);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(targetPath));
            // 让pdf不可再编辑
            stamper.setFormFlattening(true);
            // 提取pdf中的表单
            AcroFields form = stamper.getAcroFields();
            for (String key : formMap.keySet()) {
                form.setField(key, formMap.get(key));
            }

            if (CollUtil.isNotEmpty(imagePathMap)) {
                for (String key : imagePathMap.keySet()) {
                    // 通过域名获取所在页和坐标，左下角为起点
                    List<AcroFields.FieldPosition> imageFieldPositions = form.getFieldPositions(key);
                    if (CollUtil.isNotEmpty(imageFieldPositions)) {
                        int pageNo = imageFieldPositions.get(0).page;
                        Rectangle rect = imageFieldPositions.get(0).position;
                        // 读图片
                        Image image = Image.getInstance(imagePathMap.get(key));
                        // 根据域的大小缩放图片
                        image.scaleToFit(rect.getWidth(), rect.getHeight());
                        image.setAbsolutePosition(rect.getLeft(), rect.getBottom());
                        // 获取操作的页面
                        PdfContentByte canvas = stamper.getOverContent(pageNo);
                        canvas.addImage(image);
                    }
                }
            }

            stamper.close();
            reader.close();
        } catch (Exception e) {
            log.error("异常信息:{}", e);
        }

    }

}
