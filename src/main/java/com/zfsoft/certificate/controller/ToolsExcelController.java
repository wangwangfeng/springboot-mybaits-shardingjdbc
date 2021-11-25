package com.zfsoft.certificate.controller;

import com.zfsoft.certificate.pojo.base.Gender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Classname: ToolsExcelController
 * @Description: TODO
 * @Date: 2021/9/1 10:03
 * @author: wwf
 */
@Api(tags = "Excel工具测试后台")
@Slf4j
@RestController
public class ToolsExcelController {

    private final String PATH = "D://test";

    @ApiOperation(value = "测试生成Excel文件1", notes = "")
    @GetMapping("/write")
    public void test(HttpServletResponse response) {
        log.info("开始导出Excel");
/*        BigTitle bigTitle = new BigTitle();
        bigTitle.setAlignment(HorizontalAlignment.CENTER);
        bigTitle.setContent("我是大标题");
        bigTitle.setColor(ExcelColor.BLUE);
        // 需要指定Excel实体为 Book
        // 忽略某列不导出
        ExcelFactory.createWriter(Book.class, response, "性别")
                // 设置标题
                .writeTitle(bigTitle)
                // 设置要导出的数据
                .write(Book.getData())
                .write(Book.getData())
                .write(Book.getData(), false)
                .write(Book.getData(), "Sheet2")
                // 数据刷新到本地excel
                .flushToLocal(PATH);*/
    }

    @ApiOperation(value = "测试生成Excel模板文件2", notes = "")
    @GetMapping("/write2")
    public void test2(HttpServletResponse response) {
/*        ExcelFactory.createWriter(BookValid.class, response)
                // 开启验证
                .valid(true)
                .write(null)
                .flushToLocal(PATH);*/
    }

    @ApiOperation(value = "测试生成多级表头Excel文件3", notes = "")
    @GetMapping("/write3")
    public void test3(HttpServletResponse response) {
/*        ExcelFactory.createWriter(Multi.class, response)
                // 这里需要设置多级表头属性为true，否则是不会自动合并的
                // 会将相邻且值相同的单元格进行合并
                .multiHead(true)
                .write(Multi.getData())
                .flushToLocal(PATH);*/
    }

    @ApiOperation(value = "测试简单模式生成Excel文件4", notes = "")
    @GetMapping("/write4")
    public void test4(HttpServletResponse response) {
/*        ExcelFactory.createSimpleWriter("简单书籍", response, ExcelType.XLSX)
                // 设置表头
                .head(this.getSingleHead())
                .write(this.getData())
                .flushToLocal(PATH);*/
    }


    /**
     * 获取单级表头模拟数据
     **/
    private List<String[]> getSingleHead() {
        List<String[]> list = new ArrayList<>();
        list.add(new String[]{"名称"});
        list.add(new String[]{"价格"});
        list.add(new String[]{"性别"});
        return list;
    }

    /**
     * 获取模拟数据
     */
    private List<List<Object>> getData() {
        List<List<Object>> data = new ArrayList<>();
        data.add(Arrays.asList("小鸭子的故事", 20.12, Gender.WO_MAN));
        data.add(Arrays.asList("格林童话", 58.2, Gender.MAN));
        data.add(Arrays.asList("狼来了", 12.22, Gender.MAN));
        data.add(Arrays.asList("三国演义", 25.8, Gender.MAN));
        return data;
    }


}
