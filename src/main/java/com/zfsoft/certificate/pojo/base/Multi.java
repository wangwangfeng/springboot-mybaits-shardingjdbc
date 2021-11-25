package com.zfsoft.certificate.pojo.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname: Multi
 * @Description: TODO
 * @Date: 2021/9/1 14:03
 * @author: wwf
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Excel(value = "旅行社出游情况", type = ExcelType.XLSX)
public class Multi {

    //@ExcelField({"旅行社", "旅行社", "旅行社"})
    private String tour;

    //@ExcelField(value = {"国内", "门票消费金额", "门票消费金额"}, format = "0.00")
    private BigDecimal inlandMoney;

    //@ExcelField({"国内", "A类(张)", "全票"})
    private Integer ticketA;

    //@ExcelField({"国内", "A类(张)", "半票"})
    private Integer ticketAA;

    //@ExcelField({"国内", "B类(张)", "全票"})
    private Integer ticketB;

    //@ExcelField({"国内", "B类(张)", "半票"})
    private Integer ticketBB;

    //@ExcelField(value = {"境外", "门票消费金额", "门票消费金额"}, format = "0.00")
    private BigDecimal foreignMoney;

    //@ExcelField({"境外", "C类(张)", "全票"})
    private Integer ticketC;

    //@ExcelField({"境外", "C类(张)", "半票"})
    private Integer ticketCC;

    //@ExcelField({"境外", "D类(张)", "全票"})
    private Integer ticketD;

    //@ExcelField({"境外", "D类(张)", "半票"})
    private Integer ticketDD;

    /**
     * 获取模拟数据
     *
     * @return List<Book>
     */
    public static List<Multi> getData() {
        List<Multi> multiList = new ArrayList<>();
        for (int i = 0; i < 70000; i++) {
            multiList.add(new Multi("团队A", new BigDecimal("7000.98"), 12, 22, 11, 33, new BigDecimal("10000.99"), 22, 23, 12, 5));
        }
        // multiList.add(new Multi("团队B", new BigDecimal("6288"), 7, 16, 21, 9, new BigDecimal("2789"), 9, 10, 4, 5));
        return multiList;
    }

}
