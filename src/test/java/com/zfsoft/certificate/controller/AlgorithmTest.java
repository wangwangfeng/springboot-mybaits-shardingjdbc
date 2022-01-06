package com.zfsoft.certificate.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

/**
 * @Classname: AlgorithmTest
 * @Description: 算法题目
 * @Date: 2021/12/28 9:35
 * @author: wwf
 */
@Slf4j
public class AlgorithmTest {

    /**
     * @Description: 打印100之内的斐波那契数列
     **/
    @Test
    public void main1() {
        print(1, 1, 100);
    }

    /**
     * @Description: 打印前十个斐波那契数列
     **/
    @Test
    public void main2() {
        for (int i = 1; i <= 10; i++) {
            System.out.print(f(i) + ",");
        }
    }

    /**
     * @Description: 输出101-200的素数
     **/
    @Test
    public void main3() {
        for (int i = 101; i <= 200; i++) {
            if (isPrime(i)) {
                System.out.print(i + ",");
            }
        }
    }

    /**
     * @Description: 输出所有水仙花数字
     * 严格意义上来说，只有三位数才能被称为水仙花数
     **/
    @Test
    public void main4() {
        for (int i = 100; i < 1000; i++) {
            // 百位数
            int baiNum = (i / 100) % 10; // 或者  i / 100 也可以
            // 十位数
            int shiNum = (i / 10) % 10;
            // 个位数
            int geNum = i % 10;
            // int shiNum = (i - baiNum*100) / 10;
            // int geNum = (i - baiNum*100 - shiNum*10);
            // baiNum*baiNum*baiNum + shiNum*shiNum*shiNum + geNum*geNum*geNum == i
            if (Math.pow(baiNum, 3) + Math.pow(shiNum, 3) + Math.pow(geNum, 3) == i) {
                System.out.print(i + ",");
            }
        }
    }

    /**
     * @Description: 因素分解
     **/
    @Test
    public void main5() {
        // fenjie1(101);
        fenjie2(28);
    }

    /**
     * @Description: 两个数的最大公约数和最小公倍数
     **/
    @Test
    public void main6() {
        // divisor1(12, 9);
        divisor2(12, 9);
    }

    /**
     * @Description: 求s=a+aa+aaa+aaaa+aa…a的值，其中a是一个数字。例如2+22+222+2222+22222(此时共有5个数相加)
     **/
    @Test
    public void main7() {
        System.out.println("最终结果为：" + no(2, 9));
    }

    /**
     * @Description: 一个数如果恰好等于它的因子之和(不包含本身)，这个数就称为 "完数 "。例如6=1＋2＋3.编程找出1000以内的所有完数。
     * 28因子：1，2，4，7，14
     **/
    @Test
    public void mian8() {
        wanshu();
    }

    /**
     * @Description: 一球从100米高度自由落下，每次落地后反跳回原高度的一半；再落下，求它在第10次落地时，共经过多少米？第10次反弹多高？
     **/
    @Test
    public void mian9() {
        luoti(3);
        System.out.println(sum(3));
        System.out.println(height(3));
    }

    /**
     * @Description: 一个整数，它加上100后是一个完全平方数，再加上168又是一个完全平方数，请问该数是多少
     **/
    @Test
    public void mian10() {
        // fun1();
        fun2();
    }

    /**
     * TODO 以下算法
     **/

    /**
     * @param j   第二项
     * @param max 最大数字
     * @Description: 递归打印给定数之内的斐波那契数列
     * @Date: 2021/12/28 9:40
     * @author: wwf
     * @param: i 第一项
     * @return: void
     **/
    public void print(int i, int j, int max) {
        if (j == 1) {
            System.out.print(i + "," + j + ",");
        }
        int sum = i + j;
        if (sum > max) return;
        System.out.print(sum + ",");
        print(j, sum, max);
    }

    /**
     * @Description: 递归打印前几项斐波那契数列
     * @Date: 2021/12/28 10:07
     * @author: wwf
     * @param: x 第几项
     * @return: int
     **/
    public static int f(int x) {
        if (x == 1 || x == 2) {
            return 1;
        } else {
            return f(x - 1) + f(x - 2);
        }
    }

    /**
     * @Description: 判断一个数是否是素数
     * 判断素数的方法：用一个数分别去除2到sqrt(这个数)，如果能被整除，则表明此数不是素数，反之是素数。
     * @Date: 2021/12/28 10:11
     * @author: wwf
     * @param: x
     * @return: boolean
     **/
    public static boolean isPrime(int x) {
        boolean flag = true;
        // 素数不小于2
        if (x < 2) {
            return false;
        } else {
            for (int i = 2; i <= Math.sqrt(x); i++) {
                // 若能被整除则说明不是素数，返回false
                if (x % i == 0) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * @Description: 因素分解
     * 一个数从2到当前数的一半中，如果这个数能整除它，则可以分解，否则继续
     **/
    public static void fenjie1(int n) {
        for (int i = 2; i < n / 2; i++) {
            if (n % i == 0) {
                System.out.print(i + "*");
                fenjie1(n / i);
            }
        }
        System.out.println(n);
        // 不加正常退出虚拟机的话：比如数字21，可以输出：3*7，7*3，21
        System.exit(0);
    }


    static int k = 2;

    /**
     * @Description: 因素分解
     * 对n进行分解质因数，应先找到一个最小的质数k，然后按下述步骤完成：
     * 1、如果这个质数恰等于n，则说明分解质因数的过程已经结束，打印出即可。
     * 2、如果n>k，但n能被k整除，则应打印出k的值，并用n除以k的商,作为新的正整数你n,重复执行第一步。
     * 3、如果n不能被k整除，则用k+1作为k的值,重复执行第一步。
     **/
    public static void fenjie2(int n) {
        while (k <= n) {
            if (k == n) {
                System.out.print(n);
                break;
            } else if (n % k == 0) {
                System.out.print(k + "*");
                n = n / k;
                fenjie2(n);
                break;
            } else if (n % k != 0) {
                k++;
                fenjie2(n);
                break;
            }
        }
    }

    /**
     * @Description: 计算两个整数的最大公约数和最小公倍数
     * 常规思路：从1到最小的数遍历，判断能同时整除的数，最大的即为最大公约数
     **/
    public static void divisor1(int m, int n) {
        int max = 0, min = 0;
        // m记为最小的数
        if (m > n) {
            int t = m;
            m = n;
            n = t;
        }
        for (int i = 1; i <= m; i++) {
            if (m % i == 0 && n % i == 0) {
                max = i;
            }
        }
        min = m * n / max;
        System.out.println("最大公约数：" + max + "，" + "最小公倍数：" + min);
    }

    /**
     * @Description: 计算两个整数的最大公约数和最小公倍数
     * 辗转相除法
     **/
    public static void divisor2(int m, int n) {
        int z = m * n;
        int max = 0, min = 0;
        // m记为最小的数
        if (m > n) {
            int t = m;
            m = n;
            n = t;
        }
        int r = n % m;
        while (m != 0) {
            r = n % m;
            n = m;
            m = r;
        }
        max = n;
        min = z / n;
        System.out.println("最大公约数：" + max + "，" + "最小公倍数：" + min);
    }

    /**
     * @param n 几个数相加
     * @Description: 求s=a+aa+aaa+aaaa+aa…a的值，其中a是一个数字。例如2+22+222+2222+22222(此时共有5个数相加)
     * @Date: 2021/12/28 15:20
     * @author: wwf
     * @param: a 代表数字
     * @return: long
     **/
    public static long no(int a, int n) {
        // 每个加数
        long num = a;
        // 和
        long sum = 0;
        for (int i = 0; i < n; i++) {
            System.out.print(num + "+");
            sum += num;
            num *= 10;
            num += a;
        }
        return sum;
    }

    /**
     * @Description: 一个数如果恰好等于它的因子之和，这个数就称为 "完数 "。例如6=1＋2＋3.使用java进行编写程序找出1000以内的所有完数。
     * @Date: 2021/12/28 15:36
     * @author: wwf
     * @param:
     * @return: void
     **/
    public static void wanshu() {
        for (int i = 1; i <= 1000; i++) {
            // 计算因子之和
            int temp = 0;
            for (int j = 1; j <= i / 2; j++) {
                if (i % j == 0) {
                    temp += j;
                }
            }
            if (temp == i) {
                System.out.print(i + ",");
            }
        }
    }

    /**
     * @Description: 一球从100米高度自由落下，每次落地后反跳回原高度的一半；再落下，求它在第10次落地时，共经过多少米？第10次反弹多高？
     * @Date: 2021/12/28 15:45
     * @author: wwf
     * @param: times 第几次
     * @return: void
     **/
    public static void luoti(int times) {
        double sum = 100;
        double count = 100;
        for (int i = 1; i < times; i++) {
            count = count / 2;
            sum = sum + count * 2;
        }
        System.out.println("共经过：" + sum);
        System.out.println("反弹高度为：" + count / 2);
    }

    /**
     * @param times 落地次数
     * @return 总行程
     */
    public static double sum(int times) {
        double sum = 100;
        for (int i = 1; i < times; i++) {
            sum += 100 / Math.pow(2, i - 1);
        }
        return sum;
    }

    /**
     * @param times 落地次数
     * @return 第times落地后反弹高度
     */
    public static double height(int times) {
        return 100 / Math.pow(2, times);
    }

    /**
     * @Description: 一个整数，它加上100后是一个完全平方数，再加上168又是一个完全平方数，请问该数是多少
     * @Date: 2021/12/28 17:02
     * @author: wwf
     * @param:
     * @return: void
     **/
    public static void fun1() {
        double num1, num2;
        //循环计算i是否符合要求
        for (int i = 0; i < 100000; i++) {
            //得到i循环的num值
            num1 = Math.sqrt((i + 100));
            num2 = Math.sqrt((i + 268));
            //判断num是否为整数
            if (num1 == (int) num1 && num2 == (int) num2) {
                System.out.print(i + ",");
            }
        }
    }

    /**
     * @Description: 一个整数，它加上100后是一个完全平方数，再加上168又是一个完全平方数，请问该数是多少
     * 分析：
     * 按照给题的思路
     * 假设这个整数是num
     * (num+100) = i * i
     * (num+100+168) = j * j
     * 得到
     * (j * j) - (i * i) = 168
     * (j + i)(j - i) = 168
     * j - i 最小等于 1 时 j 与 i 都不大于 168 并且j > i
     * 等到如下解：
     * @author: wwf
     * @param:
     * @return: void
     **/
    public static void fun2() {
        // i的平方是完全平方数并且i小于168
        for (int i = 0; i < 168; i++) {
            // j的平方是完全平方数,j小于168并且大于i
            for (int j = i + 1; j < 168; j++) {
                //判断是否符合条件
                if (j * j - i * i == 168) {
                    // 整数
                    if (i * i > 100) {
                        System.out.print(i * i - 100 + ",");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入一个数：");
        try {
            int num = scanner.nextInt();
            if (isPrime(num)) {
                System.out.println("这是一个素数！");
            } else {
                System.out.println("这不是素数！");
            }
        } catch (Exception e) {
            System.out.println("异常");
        }
        scanner.close();
    }

}
