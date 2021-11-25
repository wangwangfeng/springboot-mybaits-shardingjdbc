package com.zfsoft.certificate.pojo.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Classname: BookValid
 * @Description: TODO
 * @Date: 2021/9/1 13:46
 * @author: wwf
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Excel("书籍校验")
public class BookValid {

    /**
     * 增加数字文本校验
     */
    //@ExcelField("书籍名称")
    //@ExcelNumericValid(operatorType = OperatorType.LESS_THAN, validType = ValidType.TEXT_LENGTH, expr1 = "10", errorContent = "书籍名称不能超过10位")
    private String name;

    /**
     * 增加重复校验
     **/
    //@ExcelField(value = "价格", format = "0.00")
    //@ExcelRepeatValid(errorContent = "不允许输入同样的价格")
    private BigDecimal price;

    /**
     * 增加下拉框
     **/
    //@ExcelField("性别")
    //@ExcelDropdownBox(combobox = {"男", "女"})
    private Gender gender;

    /**
     * 增加日期校验
     **/
    //@ExcelField(value = "出版日期", format = "yyyy-MM-dd")
    //@ExcelDateValid(operatorType = OperatorType.BETWEEN, expr1 = "1980-01-01", expr2 = "2022-01-01", errorContent = "出版日期只允许时间范围在1980-01-01到2022-01-01之间")
    private Date date;

}
