package com.zfsoft.certificate.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.certificate.pojo.Person;
import com.zfsoft.certificate.pojo.Son;
import com.zfsoft.certificate.pojo.SysAtta;
import com.zfsoft.certificate.util.CreateRfPDF;
import com.zfsoft.certificate.util.OkHttpUtils;
import com.zfsoft.certificate.util.PDFUtils;
import com.zfsoft.certificate.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import org.joda.time.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @Classname: JavaTest
 * @Description: 注意使用单元测试的情况下：
 * src 的类，最好不要以Test结尾或者跟单元测试同名的测试类，
 * 这样做并不规范，而且容易导致编辑器的误解，
 * 以为该类是测试类，从而导致意想不到的错误
 * @Date: 2021/4/23 16:15
 * @author: wwf
 */
@Slf4j
public class JavaTest {

    private static final String PATH = "D://test";

    public static void main(String[] args) {
        String ss = "{\n" +
                "\t\"contentCode\": \"1134082500312990XK0000\",\n" +
                "\t\"ownerName\": \"孟超\",\n" +
                "\t\"infoValidityEnd\": \"20391022\",\n" +
                "\t\"data\": {\n" +
                "\t\t\"address\": \"安徽省太湖县徐桥镇桥西村孟岭组０６号\",\n" +
                "\t\t\"gender\": \"男\",\n" +
                "\t\t\"month\": \"04\",\n" +
                "\t\t\"issueName\": \"太湖县公安局\",\n" +
                "\t\t\"nation\": \"汉族\",\n" +
                "\t\t\"year\": \"1992\",\n" +
                "\t\t\"name\": \"孟超\",\n" +
                "\t\t\"infoCode\": \"340825199204010279\",\n" +
                "\t\t\"end\": \"2039.10.22\",\n" +
                "\t\t\"day\": \"01\",\n" +
                "\t\t\"begin\": \"2019.10.22\"\n" +
                "\t},\n" +
                "\t\"infoCode\": \"340825199204010279\",\n" +
                "\t\"ownerId\": \"340825199204010279\",\n" +
                "\t\"infoValidityBegin\": \"20191022\",\n" +
                "\t\"reqStr\": {\n" +
                "\t\t\"orgName\": \"安庆市数据资源局\",\n" +
                "\t\t\"bizName\": \"证照文库\",\n" +
                "\t\t\"orgCode\": \"340800\",\n" +
                "\t\t\"bizCode\": \"zzwk\",\n" +
                "\t\t\"idcard\": \"340800\",\n" +
                "\t\t\"userName\": \"安庆市数据资源局\",\n" +
                "\t\t\"userCode\": \"340800\"\n" +
                "\t}\n" +
                "}";
        JSONObject jsonObject = JSONObject.parseObject(ss);
        JSONObject dataObj = new JSONObject((Map) jsonObject.get("data"));
        System.out.println(String.valueOf(dataObj.get("photo")));
        System.out.println(dataObj.getString("photo"));
        System.out.println(dataObj.get("photo"));
        if (("null".equals(dataObj.getString("photo")))
                || (StringUtils.isEmpty(dataObj.getString("photo")))) {
            System.out.println("--111--");
        } else {
            System.out.println(222);
        }
        if (("null".equals(String.valueOf(dataObj.get("photo"))))
                || (StringUtils.isEmpty(dataObj.getString("photo")))) {
            System.out.println("--333--");
        } else {
            System.out.println(444);
        }

        JSONObject data = jsonObject.getJSONObject("data");
        System.out.println(dataObj.getString("name"));
    }

    /**
     * @param number
     * @Description: 字符串分割
     * @Date: 2021/6/29 14:36
     * @author: wwf
     * @param: str
     * @return: java.lang.String
     **/
    public static String getJmxzParam(String str, String number) {

        if (StringUtils.isNotEmpty(str)) {
            String[] result = str.split("[|]");
            if ("1".equals(number)) {
                return result[0].trim();
            } else if ("2".equals(number)) {
                return result[1].trim();
            } else if ("3".equals(number)) {
                return result[2].trim();
            }
        }
        return "";
    }

    /**
     * @param name
     * @Description: 反射通过字段名获取实体对应字段值(字符串格式)
     * @Date: 2021/5/19 9:16
     * @author: wwf
     * @param: object
     * @return: java.lang.String
     **/
    public static String getGetMethod(Object object, String name) throws Exception {
        Method[] m = object.getClass().getMethods();
        String result = "";
        for (int i = 0; i < m.length; i++) {
            if (("get" + name).toLowerCase().equals(m[i].getName().toLowerCase())) {
                Object o = m[i].invoke(object);
                if (o != null) {
                    if (o instanceof Date) {
                        result = DateUtil.format((Date) o, "yyyy-MM-dd");
                    } else if (o instanceof BigDecimal) {
                        DecimalFormat df = new DecimalFormat("#,##0.00");
                        result = df.format(o);
                    } else {
                        result = o.toString();
                    }
                }
            }
        }
        return result;
    }

    /**
     * @param j   第二项
     * @param max 最大数
     * @Description: 递归打印斐波那契数列
     * @Date: 2021/7/2 14:49
     * @author: wwf
     * @param: i 第一项
     * @return: void
     **/
    public void print(int i, int j, int max) {
        // 如果j为1, 说明是前两项, 因为前两项都是1, 提前打印出来
        if (j == 1) {
            System.out.print(i + "," + j + ",");
        }
        int sum = i + j;
        if (sum > max) return;
        System.out.print(sum + ",");
        print(j, sum, max);

    }

    /**
     * @Description: java反射获取对应字段值相关
     **/
    @Test
    public void main1() {
        SysAtta entity = new SysAtta();
        entity.setOid("1111");
        entity.setName("22222");
        entity.setUploadDate(new Date());

        //Field[] fields = entity.getClass().getDeclaredFields();

        try {
            String oid = getGetMethod(entity, "试试撒");
            System.out.println(oid);
            String name = getGetMethod(entity, "name");
            System.out.println(name);
            String date = getGetMethod(entity, "uploaddate");
            System.out.println(date);
        } catch (Exception e) {
            log.error("异常信息:{}", e);
        }

    }

    /**
     * @Description: 字符串截取相关
     **/
    @Test
    public void main2() {
        String ss = "0099011701 | 享受政府优惠政策建设的廉租房、经济适用房等居民住房，减半收取防空地下室易地建设费 | 《国家计划发展委员会 财政部 国家国防动员委员会 建设部印发关于规范防空地下室易地建设收费的规定的通知》 计价格〔2000〕474号第四条第（一）款";
        System.out.println(getJmxzParam(ss, "1"));
        System.out.println(getJmxzParam(ss, "2"));
        System.out.println(getJmxzParam(ss, "3"));
    }

    /**
     * @Description: joda-time使用(线程安全)
     **/
    @Test
    public void main3() {

        DateTime dateTime = new DateTime();
        System.out.println(dateTime);
        // 获取当前日期
        System.out.println(DateTime.now());
        // 获取当前日期毫秒数
        System.out.println(DateTime.now().getMillis());
        // 格式化当前日期
        System.out.println(dateTime.toString("yyyy-MM-dd"));

        Date date = dateTime.toDate();
        System.out.println(date);

        // 如果业务只需要日期或者时间
        LocalDate localDate = new LocalDate();
        LocalTime localTime = new LocalTime();
        System.out.println(localDate);
        System.out.println(localTime);

        // 与时区无关
        LocalDateTime localDateTime = LocalDateTime.now();


        //计算两个日期间隔的天数
        LocalDate start = new LocalDate(2020, 10, 1);
        LocalDate end = new LocalDate(2021, 10, 1);
        int days = Days.daysBetween(start, end).getDays();
        System.out.println(days);

    }

    /**
     * @Description: 使用pdf模板生成pdf文件
     **/
    @Test
    public void main4() {
        Map<String, String> formMap = new HashMap<>();
        formMap.put("tyshxydm", "A1111111111111111");
        formMap.put("fddbr", "张三");
        formMap.put("jjlx", "国有经济");
        formMap.put("dz", "安徽省安庆市大观区希望小学");
        formMap.put("yzbm", "340800");
        formMap.put("dh", "13145653326");
        formMap.put("zxyy", "注销原因啊啊啊啊啊啊啊啊啊啊啊啊");
        formMap.put("bycl", "保有储量为啥呢100kt为啥");
        Map<String, String> imagePathMap = new HashMap<>();
        imagePathMap.put("image", "D:\\test\\01.png");
        PDFUtils.generatorPdf(
                "D:\\test\\ckzx.pdf",
                "D:\\test\\ckzx" + RandomUtil.randomNumber() + ".pdf",
                formMap,
                imagePathMap
        );
        System.out.println("成功");
    }

    @Test
    public void main41() {
        Map<String, String> formMap = new HashMap<>();
        formMap.put("gzswh", "安徽省安庆市大观区[2021]RF34082519930106");
        formMap.put("xmmc", "安徽省安庆市");
        Map<String, String> imagePathMap = new HashMap<>();
        //imagePathMap.put("image", "D:\\test\\01.png");
        PDFUtils.generatorPdf(
                "D:\\test\\rfces.pdf",
                "D:\\test\\rf" + RandomUtil.randomNumber() + ".pdf",
                formMap,
                imagePathMap
        );
        System.out.println("成功");
    }

    /**
     * @Description: java8中的Stream
     **/
    @Test
    public void main5() {

        // 通过 java.util.Collection.stream() 方法用集合创建流
        List<String> list = Arrays.asList("a", "b", "c");
        // 顺序流
        Stream<String> stream = list.stream();
        // 并行流
        Stream<String> parallelStream = list.parallelStream();

        // 使用java.util.Arrays.stream(T[] array)方法用数组创建流
        int[] array = {1, 2, 3, 4};
        IntStream intStream = Arrays.stream(array);

        // 使用Stream的静态方法：of()、iterate()、generate()
        Stream<Integer> stream1 = Stream.of(1, 2, 3, 4);

        Stream<Integer> stream2 = Stream.iterate(0, (x) -> x + 3).limit(4);
        stream2.forEach(System.out::println); // 输出结果：0 3 6 9

        Stream<Double> stream3 = Stream.generate(Math::random).limit(3);
        stream3.forEach(System.out::println);

        // 遍历/匹配（foreach/find/match）
        List<Integer> list1 = Arrays.asList(7, 6, 9, 3, 8, 2, 1);
        list1.stream().filter(x -> x > 6).forEach(System.out::println);
        // 匹配第一个
        Optional<Integer> findFirst = list1.stream().filter(x -> x > 6).findFirst();
        System.out.println("匹配第一个值：" + findFirst.get());
        // 匹配任意（适用于并行流）
        Optional<Integer> findAny = list1.parallelStream().filter(x -> x > 6).findAny();
        System.out.println("匹配任意一个值：" + findAny.get());
        // 是否包含符合特定条件的元素
        boolean anyMatch = list1.stream().anyMatch(x -> x < 6);
        System.out.println("是否存在小于6的值：" + anyMatch);

        // 筛选（filter）
        List<Person> personList = new ArrayList<>();
        personList.add(new Person("Tom", 8900, 23, "male", "New York"));
        personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
        personList.add(new Person("Lily", 7800, 21, "female", "Washington"));
        personList.add(new Person("Anni", 8200, 24, "female", "New York"));
        personList.add(new Person("Owen", 9500, 25, "male", "New York"));
        personList.add(new Person("Alisa", 7900, 26, "female", "New York"));
        List<Person> people = personList.stream().filter(x -> x.getSalary() > 9000)
                .collect(Collectors.toList());
        System.out.println("工资高于9000的员工集合:" + people.toString());
        List<String> fiterList = personList.stream().filter(x -> x.getSalary() > 8000)
                .map(Person::getName).collect(Collectors.toList());
        System.out.println("高于8000的员工姓名：" + fiterList);

        // 聚合（max/min/count)
        List<String> list2 = Arrays.asList("adnm", "admmt", "pot", "xbangd", "weoujgsd");
        Optional<String> stringMax = list2.stream().max(Comparator.comparing(String::length));
        System.out.println("最长的字符串：" + stringMax.get());

        // 自然排序
        Optional<Integer> integer1 = list1.stream().max(Integer::compareTo);
        // 自定义排序
        Optional<Integer> integer2 = list1.stream().max((o1, o2) -> o1.compareTo(o2));
        System.out.println("自然排序的最大值：" + integer1.get());
        System.out.println("自定义排序的最大值：" + integer2.get());

        Optional<Person> max3 = personList.stream().max(Comparator.comparingInt(Person::getSalary));
        System.out.println("员工工资最大值：" + max3.get().getSalary());

        long count = list1.stream().filter(x -> x > 6).count();
        System.out.println("list中大于6的元素个数：" + count);

        // 映射(map/flatMap)
        String[] strArr = {"abcd", "bcdd", "defde", "fTr"};
        List<String> stringList = Arrays.stream(strArr).map(String::toUpperCase).collect(Collectors.toList());
        System.out.println("每个元素大写：" + stringList);

        // 将员工的薪资全部增加1000
        // 方法一：不改变原来员工集合的方式
        List<Person> personListNew = personList.stream().map(person -> {
            Person personNew = new Person(person.getName(), 0, 0, null, null);
            personNew.setSalary(person.getSalary() + 1000);
            return personNew;
        }).collect(Collectors.toList());
        System.out.println("一次改动前：" + personList.get(0).getName() + "-->" + personList.get(0).getSalary());
        System.out.println("一次改动后：" + personListNew.get(0).getName() + "-->" + personListNew.get(0).getSalary());
        // 方法二：改变原来员工集合的方式
        List<Person> personListNew2 = personList.stream().map(person -> {
            person.setSalary(person.getSalary() + 0);
            return person;
        }).collect(Collectors.toList());
        System.out.println("二次改动前：" + personList.get(0).getName() + "-->" + personListNew.get(0).getSalary());
        System.out.println("二次改动后：" + personListNew2.get(0).getName() + "-->" + personListNew.get(0).getSalary());

        // 将两个字符数组合并成一个新的字符数组。
        List<String> list3 = Arrays.asList("m,k,l,a", "1,3,5,7");
        List<String> listNew = list3.stream().flatMap(s -> {
            // 将每个元素转换成一个stream
            String[] split = s.split(",");
            Stream<String> s2 = Arrays.stream(split);
            return s2;
        }).collect(Collectors.toList());
        System.out.println("处理前的集合：" + list3 + "--集合长度：" + list3.size());
        System.out.println("处理后的集合：" + listNew + "--集合长度：" + listNew.size());

        // 归约(reduce)
        // 归约，也称缩减，顾名思义，是把一个流缩减成一个值，能实现对集合求和、求乘积和求最值操作。
        List<Integer> list4 = Arrays.asList(1, 3, 2, 8, 11, 4);
        // 求和方式1
        Optional<Integer> integerSum = list4.stream().reduce((x, y) -> x + y);
        // 求和方式2
        Optional<Integer> integerSum1 = list4.stream().reduce(Integer::sum);
        // 求和方式3
        Integer integerSum2 = list4.stream().reduce(0, Integer::sum);
        System.out.println("list求和：" + integerSum.get() + "," + integerSum1.get() + "," + integerSum2);

        // 求乘积
        Optional<Integer> product = list4.stream().reduce((x, y) -> x * y);
        System.out.println("list求积：" + product.get());

        // 求最大值方式1
        Optional<Integer> max4 = list4.stream().reduce((x, y) -> x > y ? x : y);
        // 求最大值方式2
        Integer max5 = list4.stream().reduce(1, Integer::max);
        System.out.println("list求最大值：" + max4.get() + "," + max5);


        // 求工资之和方式1：
        Optional<Integer> sumSalary = personList.stream().map(Person::getSalary).reduce(Integer::sum);
        // 求工资之和方式2：
        Integer sumSalary2 = personList.stream().reduce(0, (sum, p) -> sum += p.getSalary(),
                (sum1, sum2) -> sum1 + sum2);
        // 求工资之和方式3：
        Integer sumSalary3 = personList.stream().reduce(0, (sum, p) -> sum += p.getSalary(),
                Integer::sum);

        // 求最高工资方式1：
        Integer maxSalary = personList.stream().reduce(0, (max, p) ->
                max > p.getSalary() ? max : p.getSalary(), Integer::max);
        // 求最高工资方式2：
        Integer maxSalary2 = personList.stream().reduce(0, (max, p) ->
                max > p.getSalary() ? max : p.getSalary(), (max1, max2) -> max1 > max2 ? max1 : max2);

        System.out.println("工资之和：" + sumSalary.get() + "," + sumSalary2 + "," + sumSalary3);
        System.out.println("最高工资：" + maxSalary + "," + maxSalary2);

        // 归集(toList/toSet/toMap)
        List<Integer> list5 = Arrays.asList(1, 6, 3, 4, 6, 7, 9, 6, 20);
        List<Integer> listNew5 = list5.stream().filter(x -> x % 2 == 0).collect(Collectors.toList());
        Set<Integer> set = list5.stream().filter(x -> x % 2 == 0).collect(Collectors.toSet());

        Map<?, Person> map = personList.stream().filter(p -> p.getSalary() > 8000)
                .collect(Collectors.toMap(Person::getName, p -> p));
        System.out.println("toList:" + listNew5);
        System.out.println("toSet:" + set);
        System.out.println("toMap:" + map);

        // 统计(count/averaging)

        // 求总数
        Long count1 = personList.stream().collect(Collectors.counting());
        Long count2 = personList.stream().count();
        // 求平均工资
        Double average = personList.stream().collect(Collectors.averagingDouble(Person::getSalary));
        // 求最高工资
        Optional<Integer> max = personList.stream().map(Person::getSalary).collect(Collectors.maxBy(Integer::compare));
        Optional<Integer> max1 = personList.stream().map(Person::getSalary).max(Integer::compare);
        // 求工资之和
        Integer sum = personList.stream().collect(Collectors.summingInt(Person::getSalary));
        Integer sum1 = personList.stream().mapToInt(Person::getSalary).sum();
        // 一次性统计所有信息
        DoubleSummaryStatistics collect = personList.stream().collect(Collectors.summarizingDouble(Person::getSalary));

        System.out.println("员工总数：" + count1 + ":" + count2);
        System.out.println("员工平均工资：" + average);
        System.out.println("员工最高工资：" + max.get() + ":" + max1.get());
        System.out.println("员工工资总和：" + sum + ":" + sum1);
        System.out.println("员工工资所有统计：" + collect);

        // 分组(partitioningBy/groupingBy)
        // 将员工按薪资是否高于8000分组
        Map<Boolean, List<Person>> part = personList.stream().collect(Collectors.partitioningBy(x -> x.getSalary() > 8000));
        // 将员工按性别分组
        Map<String, List<Person>> group = personList.stream().collect(Collectors.groupingBy(Person::getSex));
        // 将员工先按性别分组，再按地区分组
        Map<String, Map<String, List<Person>>> group2 = personList.stream().collect(Collectors.groupingBy(Person::getSex, Collectors.groupingBy(Person::getArea)));
        System.out.println("员工按薪资是否大于8000分组情况：" + part);
        System.out.println("员工按性别分组情况：" + group);
        System.out.println("员工按性别、地区：" + group2);

        // 接合(joining)
        String names = personList.stream().map(p -> p.getName()).collect(Collectors.joining(","));
        System.out.println("所有员工的姓名：" + names);
        List<String> list6 = Arrays.asList("A", "B", "C");
        String string = list6.stream().collect(Collectors.joining("-"));
        String string1 = String.join("-", list6);
        System.out.println("拼接后的字符串：" + string);

        // 归约(reducing)
        Integer sums = personList.stream().collect(Collectors.reducing(0, Person::getSalary, (i, j) -> (i + j - 5000)));
        System.out.println("员工扣税薪资总和：" + sums);

        // stream的reduce
        Optional<Integer> sum2 = personList.stream().map(Person::getSalary).reduce(Integer::sum);
        System.out.println("员工薪资总和：" + sum2.get());

        // 排序(sorted)

        // 按工资升序排序（自然排序）
        List<String> newList = personList.stream()
                .sorted(Comparator.comparing(Person::getSalary))
                .map(Person::getName)
                .collect(Collectors.toList());
        // 按工资倒序排序
        List<String> newList2 = personList.stream()
                .sorted(Comparator.comparing(Person::getSalary).reversed())
                .map(Person::getName)
                .collect(Collectors.toList());
        // 先按工资再按年龄升序排序
        List<String> newList3 = personList.stream()
                .sorted(Comparator.comparing(Person::getSalary).thenComparing(Person::getAge))
                .map(Person::getName)
                .collect(Collectors.toList());
        // 先按工资再按年龄自定义排序（降序）
        List<String> newList4 = personList.stream().sorted((p1, p2) -> {
            if (p1.getSalary() == p2.getSalary()) {
                return p2.getAge() - p1.getAge();
            } else {
                return p2.getSalary() - p1.getSalary();
            }
        }).map(Person::getName).collect(Collectors.toList());

        System.out.println("按工资升序排序：" + newList);
        System.out.println("按工资降序排序：" + newList2);
        System.out.println("先按工资再按年龄升序排序：" + newList3);
        System.out.println("先按工资再按年龄自定义降序排序：" + newList4);

        // 提取/组合

        String[] arr1 = {"a", "b", "c", "d"};
        String[] arr2 = {"d", "e", "f", "g"};

        Stream<String> stringStream = Stream.of(arr1);
        Stream<String> stringStream2 = Stream.of(arr2);
        // concat:合并两个流 distinct：去重
        List<String> stringList1 = Stream.concat(stringStream, stringStream2).distinct().collect(Collectors.toList());
        // limit：限制从流中获得前n个数据
        List<Integer> collect1 = Stream.iterate(1, x -> x + 2).limit(10).collect(Collectors.toList());
        // skip：跳过前n个数据
        List<Integer> collect2 = Stream.iterate(1, x -> x + 2).skip(1).limit(5).collect(Collectors.toList());

        System.out.println("流合并：" + stringList1);
        System.out.println("limit：" + collect1);
        System.out.println("skip：" + collect2);


    }

    /**
     * @Description: 打印某个数之内的斐波那契数列
     **/
    @Test
    public void main6() {
        print(1, 1, 1000);
    }

    @Test
    public void main7() {
        Date date = DateUtil.parse("2008-09-10 00:00:00.0");
        System.out.println(DateUtil.format(date, DatePattern.PURE_DATE_PATTERN));
        System.out.println(DateUtil.year(date));
        System.out.println(DateUtil.month(date) + 1 < 10 ?
                "0" + String.valueOf(DateUtil.month(date) + 1) : String.valueOf(DateUtil.month(date) + 1));
        System.out.println(DateUtil.dayOfMonth(date) < 10 ?
                "0" + String.valueOf(DateUtil.dayOfMonth(date)) : String.valueOf(DateUtil.dayOfMonth(date)));

        System.out.println(DateUtil.today());
        System.out.println(StrUtil.replace(DateUtil.today(), "-", ""));

    }

    /**
     * @Description: freemarker模板生成
     **/
    @Test
    public void main8() {
/*        Configuration configuration = new Configuration();
        Writer out = null;
        try {
            // 获取模板路径
            configuration.setDirectoryForTemplateLoading(new File("src/main/resources"));
            Map<String, Object> map = new HashMap<>(2);
            map.put("test", "测试");
            map.put("date", new Date());
            Template template = configuration.getTemplate("fk.ftl");
            // 生成文件路径
            File resultFile = new File( "D://fk.html");
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFile)));
            // step6 输出文件
            template.process(map, out);
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^fk.html 文件创建成功 !");
        } catch (Exception e) {
            log.error("异常信息:{}", e);
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e1) {
                log.error("异常信息:{}", e1);
            }
        }*/
    }

    @Test
    public void main9() {
        String pdfPath = "D://test//rf22.pdf";
        CreateRfPDF.exportPDF(null, pdfPath);

        String pdfPath1 = "D://test//sl22.pdf";
        //CreateSlPDF.exportPDF(null, pdfPath1);
    }

    /**
     * @Description: 去掉集合中无效实体元素
     **/
    @Test
    public void main10() {
        List<Person> personList = new ArrayList<>();
        personList.add(new Person("Tom", 8900, 23, "male", "New York"));
        personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
        personList.add(new Person("Lily", 7800, 21, "female", "Washington"));
        personList.add(new Person("Anni", 8200, 24, "female", "New York"));
        personList.add(new Person("Owen", 9500, 25, "male", "New York"));
        personList.add(new Person("Alisa", 7900, 26, "female", "New York"));
        List<Son> fiterList = personList.stream().map(person -> {
            Son son = new Son(person.getName(), person.getAge());
            return son;
        }).collect(Collectors.toList());
        System.out.println("去掉集合中无效实体元素：" + fiterList);

        Map<String, List<Person>> group = personList.stream().collect(Collectors.groupingBy(Person::getSex));
        System.out.println(group);

    }

    @Test
    public void main11() {
        Son son = new Son();
        son.setName("张三");
        son.setAge(30);
        StringUtils.getColumnNameValue(son);
    }

    /**
     * @Description: 字段分组后合并
     * 方法一：直接mysql分组后使用GROUP_CONCAT函数
     * 方法二：直接查询所有集合，后台循环后放到Map里面处理
     * 方法三：如下
     **/
    @Test
    public void main12() {
        List<Son> list = new ArrayList<>();
        list.add(new Son("张二", 10));
        list.add(new Son("李四", 20));
        list.add(new Son("张三", 10));

        List<Son> studentList = new ArrayList<>();
        list.parallelStream().collect(Collectors.groupingBy(Son::getAge, Collectors.toList()))
                .forEach((id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new Son(a.getName() + "," + b.getName(), a.getAge())).ifPresent(studentList::add);
                });

        System.out.println(studentList);
    }

    /**
     * @Description: 数字计算
     **/
    @Test
    public void main13() {
        String a = "23.000";
        BigDecimal num1 = new BigDecimal(a);
        BigDecimal re = num1.multiply(BigDecimal.valueOf(1000000000));
        System.out.println(re);
        // 去掉多余的零以及非科学计数法
        System.out.println(re.stripTrailingZeros().toPlainString());

        System.out.println(NumberUtil.isNumber(a));
        System.out.println(NumberUtil.mul("12.00", "1000"));
        System.out.println(NumberUtil.mul("12.2300", "2.32"));

    }

    /**
     * @Description: 高校压缩位图相关
     **/
    @Test
    public void main14() {

/*        RoaringBitmap r1 = RoaringBitmap.bitmapOf(1,2,3,4,6,9,199);
        // 返回第三个数字 结果是4
        System.out.println(r1.select(3));
        // 增加一个数字
        r1.add(1000);
        r1.add(3);
        // 返回某个数的索引 结果是3
        System.out.println(r1.rank(3));
        // 判断里面是否包含某个数
        System.out.println(r1.contains(4));
        System.out.println(r1.contains(5));
        System.out.println(r1);

        RoaringBitmap r2 = new RoaringBitmap();
        r2.add(10l, 20l);
        System.out.println(r2);
        // 查看存储了多少个值
        System.out.println(r2.getLongCardinality());

        // 合并两个
        RoaringBitmap r3 = RoaringBitmap.or(r1, r2);
        System.out.println(r3);
        System.out.println(r3.getLongCardinality());

        // r1和r2进行位运算,并将结果赋值给r1
        r1.or(r2);
        System.out.println(r1);

        System.out.println(r1.equals(r3));

        for (int i : r3) {
            System.out.print(i);
        }
        System.out.println("\n");
        System.out.println("测试遍历");

        // 下面方式遍历更快
        r2.forEach((Consumer<? super Integer>) System.out::println);
        System.out.println("\n");
        System.out.println("测试遍历");
        r2.forEach((IntConsumer) i -> System.out.println(i));*/

    }

    /**
     * @Description: JDK8中的StringJoiner
     **/
    @Test
    public void main15() {
        StringJoiner stringJoiner = new StringJoiner(":", "[", "]");
        System.out.println(stringJoiner.toString()); // []
        stringJoiner.add("---");
        System.out.println(stringJoiner.toString()); // [---]
        stringJoiner.add("+++");
        System.out.println(stringJoiner.toString()); // [---:+++]

    }

    /**
     * @Description: 数据库文档工具
     * 【screw、DBCHM、TableGo】
     **/
    @Test
    public void main16() {

/*        //数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/aq-elms?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("123456");
        //设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        DataSource dataSource = new HikariDataSource(hikariConfig);

        //生成配置
        EngineConfig engineConfig = EngineConfig.builder()
                //生成文件路径
                .fileOutputDir(PATH)
                //打开目录
                .openOutputDir(false)
                //文件类型
                .fileType(EngineFileType.HTML)
                //生成模板实现
                .produceType(EngineTemplateType.freemarker)
                .build();

        //忽略表
        List<String> ignoreTableName = Collections.singletonList("test");
        //忽略表前缀
        List<String> ignorePrefix = Collections.singletonList("test_");
        //忽略表后缀
        List<String> ignoreSuffix = Arrays.asList("_test");

        ProcessConfig processConfig = ProcessConfig.builder()
                //指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
                //根据名称指定表生成
                .designatedTableName(new ArrayList<>())
                //根据表前缀生成
                .designatedTablePrefix(new ArrayList<>())
                //根据表后缀生成
                .designatedTableSuffix(new ArrayList<>())
                //忽略表名
                .ignoreTableName(ignoreTableName)
                //忽略表前缀
                .ignoreTablePrefix(ignorePrefix)
                //忽略表后缀
                .ignoreTableSuffix(ignoreSuffix).build();
        //配置
        Configuration config = Configuration.builder()
                //版本
                .version("1.0.0")
                //描述
                .description("数据库设计文档生成")
                //数据源
                .dataSource(dataSource)
                //生成配置
                .engineConfig(engineConfig)
                //生成配置
                .produceConfig(processConfig)
                .build();
        //执行生成
        new DocumentationExecute(config).execute();*/

    }

    /**
     * @Description: equals判断建议使用 Objects.equals(a, b)
     **/
    @Test
    public void main17() {
        String a = "11";
        String b = "";
        System.out.println(Objects.equals(a, b));
        System.out.println(a.equals(b));

        String a1 = "999998";
        int s = Integer.valueOf(a1) + 1;
        System.out.println(Integer.valueOf(a1) + 1);
        System.out.println(s);
        System.out.println(Integer.toString(s));

        BigDecimal bigDecimal = new BigDecimal(a1);
        BigDecimal a2 = new BigDecimal("1");
        BigDecimal c = bigDecimal.add(a2);
        System.out.println(c.toString());

    }

    @Test
    public void main18() {

        // get请求，方法顺序按照这种方式，切记选择post/get一定要放在倒数第二，同步或者异步倒数第一，才会正确执行
        String result = OkHttpUtils.builder().url("http://59.203.216.189:9008/zxById/1")
                // 有参数的话添加参数，可多个
                //.addParam("id", "1")
                // 也可以添加多个
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .get()
                // 可选择是同步请求还是异步请求
                //.async();
                .sync();
        System.out.println("注销结果-" + result);

        // post请求，分为两种，一种是普通表单提交，一种是json提交
/*        OkHttpUtils.builder().url("请求地址，http/https都可以")
                // 有参数的话添加参数，可多个
                .addParam("参数名", "参数值")
                .addParam("参数名", "参数值")
                // 也可以添加多个
                .addHeader("Content-Type", "application/json; charset=utf-8")
                // 如果是true的话，会类似于postman中post提交方式的raw，用json的方式提交，不是表单
                // 如果是false的话传统的表单提交
                .post(true)
                .sync();*/

        // 选择异步有两个方法，一个是带回调接口，一个是直接返回结果
        String ss = OkHttpUtils.builder().url("http://59.203.216.188:9008/elms/api/zzWork/zzDownload/")
                .addParam("id", "aab4efaef5c72e976d9090685cb7ec63")
                .addParam("format", "ofd")
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .post(true)
                .async();
        System.out.println("返回结果-" + ss);

        OkHttpUtils.builder().url("http://59.203.216.188:9008/elms/api/zzWork/zzDownload/")
                .addParam("id", "aab4efaef5c72e976d9090685cb7ec63")
                .addParam("format", "ofd")
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .post(true).async(new OkHttpUtils.ICallBack() {
            @Override
            public void onSuccessful(Call call, String data) {
                // 请求成功后的处理
                System.out.println("请求成功");
                System.out.println(data);
            }

            @Override
            public void onFailure(Call call, String errorMsg) {
                // 请求失败后的处理
                System.out.println("请求失败");
                System.out.println(errorMsg);
            }
        });

    }

}
