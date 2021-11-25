package com.zfsoft.certificate.pojo.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname: Book
 * @Description: TODO
 * @Date: 2021/9/1 11:28
 * @author: wwf
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Excel("书籍")
public class Book {

    //@ExcelField(value = "名称", required = true)
    private String name;

    //@ExcelField(value = "价格", format = "0.00")
    private BigDecimal price;

    //@ExcelField("性别")
    private Gender gender;

    /**
     * 获取模拟数据
     *
     * @return List<Book>
     */
    public static List<Book> getData() {
        List<Book> books = new ArrayList<>();
        Book book1 = new Book("童话世界", new BigDecimal("20.2"), Gender.MAN);
        Book book2 = new Book("脑筋急转弯", new BigDecimal("33.4"), Gender.WO_MAN);
        Book book3 = new Book("鲁滨逊漂流记", new BigDecimal("18.8"), Gender.MAN);
        books.add(book1);
        books.add(book2);
        books.add(book3);
        return books;
    }


}
