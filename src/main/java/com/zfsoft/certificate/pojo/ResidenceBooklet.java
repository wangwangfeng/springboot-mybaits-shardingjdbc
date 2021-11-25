package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "hkb_new")
public class ResidenceBooklet implements Serializable {

    private static final long serialVersionUID = 8464633505768435098L;

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 区划代码
     */
    @TableField(value = "QHDM")
    private String QHDM;

    /**
     * 持证者id
     */
    @TableField(value = "OWNERID")
    private String OWNERID;

    /**
     * 持证者姓名
     */
    @TableField(value = "OWNERNAME")
    private String OWNERNAME;

    /**
     * 证书编号
     */
    @TableField(value = "INFOCODE")
    private String INFOCODE;

    /**
     * 有效期开始
     */
    @TableField(value = "INFOVALIDITYBEGIN")
    private String INFOVALIDITYBEGIN;

    /**
     * 机构编码
     */
    @TableField(value = "ORGCODE")
    private String ORGCODE;

    /**
     * 机构名称
     */
    @TableField(value = "ORGNAME")
    private String ORGNAME;

    /**
     * 户口簿日期
     */
    @TableField(value = "HKBDATE")
    private String HKBDATE;

    /**
     * 户别
     */
    @TableField(value = "HUBIE")
    private String HUBIE;

    /**
     * 户主姓名
     */
    @TableField(value = "HUZHUXINGMING")
    private String HUZHUXINGMING;

    /**
     * 派出所1
     */
    @TableField(value = "PAICHUSUOMINGCHENG01")
    private String PAICHUSUOMINGCHENG01;

    /**
     * 社区1
     */
    @TableField(value = "SHEQU")
    private String SHEQU;

    /**
     * 承办人1
     */
    @TableField(value = "CHENGBANREN01")
    private String CHENGBANREN01;

    /**
     * 年1
     */
    @TableField(value = "NIAN01")
    private String NIAN01;

    /**
     * 月1
     */
    @TableField(value = "YUE01")
    private String YUE01;

    /**
     * 日1
     */
    @TableField(value = "RI01")
    private String RI01;

    /**
     * 姓名
     */
    @TableField(value = "XINGMING")
    private String XINGMING;

    /**
     * 身份证号
     */
    @TableField(value = "SHENFENZHENHAO")
    private String SHENFENZHENHAO;

    /**
     * 关系
     */
    @TableField(value = "GUANXI")
    private String GUANXI;

    /**
     * 性别
     */
    @TableField(value = "SEX")
    private String SEX;

    /**
     * 民族
     */
    @TableField(value = "MINZU")
    private String MINZU;

    /**
     * 出生日期
     */
    @TableField(value = "CHUSHENGRIQI")
    private String CHUSHENGRIQI;

    /**
     * 年
     */
    @TableField(value = "NIAN")
    private String NIAN;

    /**
     * 月
     */
    @TableField(value = "YUE")
    private String YUE;

    /**
     * 日
     */
    @TableField(value = "RI")
    private String RI;

    /**
     * 曾用名
     */
    @TableField(value = "H_RENAME")
    private String h_RENAME;

    /**
     * 信仰
     */
    @TableField(value = "XINYANG")
    private String XINYANG;

    /**
     * 身高
     */
    @TableField(value = "SHENGAO")
    private String SHENGAO;

    /**
     * 血型
     */
    @TableField(value = "XUEXIN")
    private String XUEXIN;

    /**
     * 文化程度
     */
    @TableField(value = "WENHUACHENGDU")
    private String WENHUACHENGDU;

    /**
     * 婚姻
     */
    @TableField(value = "HUNYIN")
    private String HUNYIN;

    /**
     * 兵役
     */
    @TableField(value = "BINGYI")
    private String BINGYI;

    /**
     * 服务场所
     */
    @TableField(value = "FUWUCHUSUO")
    private String FUWUCHUSUO;

    /**
     * 职业
     */
    @TableField(value = "ZHIYE")
    private String ZHIYE;

    /**
     * 何时何地
     */
    @TableField(value = "HESHIHEDI")
    private String HESHIHEDI;

    /**
     * 何时何地-本地
     */
    @TableField(value = "HESHIHEDI_BENDI")
    private String HESHIHEDI_BENDI;

    /**
     * 户号
     */
    @TableField(value = "HUHAO")
    private String HUHAO;

    /**
     * 户口类别
     */
    @TableField(value = "HUKOULEIBIE")
    private String HUKOULEIBIE;

    /**
     * 派出所
     */
    @TableField(value = "PAICHUSUOMINGCHENG")
    private String PAICHUSUOMINGCHENG;

    /**
     * 承办人
     */
    @TableField(value = "CHENGBANREN")
    private String CHENGBANREN;

    /**
     * 姓名1
     */
    @TableField(value = "XINGMING1")
    private String XINGMING1;

    /**
     * 身份证号1
     */
    @TableField(value = "SHENFENZHENHAO1")
    private String SHENFENZHENHAO1;

    /**
     * 关系1
     */
    @TableField(value = "GUANXI1")
    private String GUANXI1;

    /**
     * 性别1
     */
    @TableField(value = "SEX1")
    private String SEX1;

    /**
     * 民族1
     */
    @TableField(value = "MINZU1")
    private String MINZU1;

    /**
     * 出生日期1
     */
    @TableField(value = "CHUSHENGRIQI1")
    private String CHUSHENGRIQI1;

    /**
     * 年1
     */
    @TableField(value = "NIAN1")
    private String NIAN1;

    /**
     * 月1
     */
    @TableField(value = "YUE1")
    private String YUE1;

    /**
     * 日1
     */
    @TableField(value = "RI1")
    private String RI1;

    /**
     * 曾用名1
     */
    @TableField(value = "H_RENAME1")
    private String h_RENAME1;

    /**
     * 信仰1
     */
    @TableField(value = "XINYANG1")
    private String XINYANG1;

    /**
     * 身高1
     */
    @TableField(value = "SHENGAO1")
    private String SHENGAO1;

    /**
     * 血型1
     */
    @TableField(value = "XUEXIN1")
    private String XUEXIN1;

    /**
     * 文化程度1
     */
    @TableField(value = "WENHUACHENGDU1")
    private String WENHUACHENGDU1;

    /**
     * 婚姻1
     */
    @TableField(value = "HUNYIN1")
    private String HUNYIN1;

    /**
     * 兵役1
     */
    @TableField(value = "BINGYI1")
    private String BINGYI1;

    /**
     * 服务场所1
     */
    @TableField(value = "FUWUCHUSUO1")
    private String FUWUCHUSUO1;

    /**
     * 职业1
     */
    @TableField(value = "ZHIYE1")
    private String ZHIYE1;

    /**
     * 何时何地1
     */
    @TableField(value = "HESHIHEDI1")
    private String HESHIHEDI1;

    /**
     * 何时何地-本地1
     */
    @TableField(value = "HESHIHEDI_BENDI1")
    private String HESHIHEDI_BENDI1;

    /**
     * 户号1
     */
    @TableField(value = "HUHAO1")
    private String HUHAO1;

    /**
     * 户口类别1
     */
    @TableField(value = "HUKOULEIBIE1")
    private String HUKOULEIBIE1;

    /**
     * 派出所1
     */
    @TableField(value = "PAICHUSUOMINGCHENG1")
    private String PAICHUSUOMINGCHENG1;

    /**
     * 承办人1
     */
    @TableField(value = "CHENGBANREN1")
    private String CHENGBANREN1;

    /**
     * 姓名2
     */
    @TableField(value = "XINGMING2")
    private String XINGMING2;

    /**
     * 身份证号2
     */
    @TableField(value = "SHENFENZHENHAO2")
    private String SHENFENZHENHAO2;

    /**
     * 关系2
     */
    @TableField(value = "GUANXI2")
    private String GUANXI2;

    /**
     * 性别2
     */
    @TableField(value = "SEX2")
    private String SEX2;

    /**
     * 民族2
     */
    @TableField(value = "MINZU2")
    private String MINZU2;

    /**
     * 出生日期2
     */
    @TableField(value = "CHUSHENGRIQI2")
    private String CHUSHENGRIQI2;

    /**
     * 年2
     */
    @TableField(value = "NIAN2")
    private String NIAN2;

    /**
     * 月2
     */
    @TableField(value = "YUE2")
    private String YUE2;

    /**
     * 日2
     */
    @TableField(value = "RI2")
    private String RI2;

    /**
     * 曾用名2
     */
    @TableField(value = "H_RENAME2")
    private String h_RENAME2;

    /**
     * 信仰2
     */
    @TableField(value = "XINYANG2")
    private String XINYANG2;

    /**
     * 身高2
     */
    @TableField(value = "SHENGAO2")
    private String SHENGAO2;

    /**
     * 血型2
     */
    @TableField(value = "XUEXIN2")
    private String XUEXIN2;

    /**
     * 文化程度2
     */
    @TableField(value = "WENHUACHENGDU2")
    private String WENHUACHENGDU2;

    /**
     * 婚姻2
     */
    @TableField(value = "HUNYIN2")
    private String HUNYIN2;

    /**
     * 兵役2
     */
    @TableField(value = "BINGYI2")
    private String BINGYI2;

    /**
     * 服务场所2
     */
    @TableField(value = "FUWUCHUSUO2")
    private String FUWUCHUSUO2;

    /**
     * 职业2
     */
    @TableField(value = "ZHIYE2")
    private String ZHIYE2;

    /**
     * 何时何地2
     */
    @TableField(value = "HESHIHEDI2")
    private String HESHIHEDI2;

    /**
     * 何时何地-本地2
     */
    @TableField(value = "HESHIHEDI_BENDI2")
    private String HESHIHEDI_BENDI2;

    /**
     * 户号2
     */
    @TableField(value = "HUHAO2")
    private String HUHAO2;

    /**
     * 户口类别2
     */
    @TableField(value = "HUKOULEIBIE2")
    private String HUKOULEIBIE2;

    /**
     * 派出所2
     */
    @TableField(value = "PAICHUSUOMINGCHENG2")
    private String PAICHUSUOMINGCHENG2;

    /**
     * 承办人2
     */
    @TableField(value = "CHENGBANREN2")
    private String CHENGBANREN2;

    /**
     * 姓名3
     */
    @TableField(value = "XINGMING3")
    private String XINGMING3;

    /**
     * 身份证号3
     */
    @TableField(value = "SHENFENZHENHAO3")
    private String SHENFENZHENHAO3;

    /**
     * 关系3
     */
    @TableField(value = "GUANXI3")
    private String GUANXI3;

    /**
     * 性别3
     */
    @TableField(value = "SEX3")
    private String SEX3;

    /**
     * 民族3
     */
    @TableField(value = "MINZU3")
    private String MINZU3;

    /**
     * 出生日期3
     */
    @TableField(value = "CHUSHENGRIQI3")
    private String CHUSHENGRIQI3;

    /**
     * 年3
     */
    @TableField(value = "NIAN3")
    private String NIAN3;

    /**
     * 月3
     */
    @TableField(value = "YUE3")
    private String YUE3;

    /**
     * 日3
     */
    @TableField(value = "RI3")
    private String RI3;

    /**
     * 曾用名3
     */
    @TableField(value = "H_RENAME3")
    private String h_RENAME3;

    /**
     * 信仰3
     */
    @TableField(value = "XINYANG3")
    private String XINYANG3;

    /**
     * 身高3
     */
    @TableField(value = "SHENGAO3")
    private String SHENGAO3;

    /**
     * 血型3
     */
    @TableField(value = "XUEXIN3")
    private String XUEXIN3;

    /**
     * 文化程度3
     */
    @TableField(value = "WENHUACHENGDU3")
    private String WENHUACHENGDU3;

    /**
     * 婚姻3
     */
    @TableField(value = "HUNYIN3")
    private String HUNYIN3;

    /**
     * 兵役3
     */
    @TableField(value = "BINGYI3")
    private String BINGYI3;

    /**
     * 服务场所3
     */
    @TableField(value = "FUWUCHUSUO3")
    private String FUWUCHUSUO3;

    /**
     * 职业3
     */
    @TableField(value = "ZHIYE3")
    private String ZHIYE3;

    /**
     * 何时何地3
     */
    @TableField(value = "HESHIHEDI3")
    private String HESHIHEDI3;

    /**
     * 何时何地-本地3
     */
    @TableField(value = "HESHIHEDI_BENDI3")
    private String HESHIHEDI_BENDI3;

    /**
     * 户号3
     */
    @TableField(value = "HUHAO3")
    private String HUHAO3;

    /**
     * 户口类别3
     */
    @TableField(value = "HUKOULEIBIE3")
    private String HUKOULEIBIE3;

    /**
     * 派出所3
     */
    @TableField(value = "PAICHUSUOMINGCHENG3")
    private String PAICHUSUOMINGCHENG3;

    /**
     * 承办人3
     */
    @TableField(value = "CHENGBANREN3")
    private String CHENGBANREN3;

    /**
     * 姓名4
     */
    @TableField(value = "XINGMING4")
    private String XINGMING4;

    /**
     * 身份证号4
     */
    @TableField(value = "SHENFENZHENHAO4")
    private String SHENFENZHENHAO4;

    /**
     * 关系4
     */
    @TableField(value = "GUANXI4")
    private String GUANXI4;

    /**
     * 性别4
     */
    @TableField(value = "SEX4")
    private String SEX4;

    /**
     * 民族4
     */
    @TableField(value = "MINZU4")
    private String MINZU4;

    /**
     * 出生日期4
     */
    @TableField(value = "CHUSHENGRIQI4")
    private String CHUSHENGRIQI4;

    /**
     * 年4
     */
    @TableField(value = "NIAN4")
    private String NIAN4;

    /**
     * 月4
     */
    @TableField(value = "YUE4")
    private String YUE4;

    /**
     * 日4
     */
    @TableField(value = "RI4")
    private String RI4;

    /**
     * 曾用名4
     */
    @TableField(value = "H_RENAME4")
    private String h_RENAME4;

    /**
     * 信仰4
     */
    @TableField(value = "XINYANG4")
    private String XINYANG4;

    /**
     * 身高4
     */
    @TableField(value = "SHENGAO4")
    private String SHENGAO4;

    /**
     * 血型4
     */
    @TableField(value = "XUEXIN4")
    private String XUEXIN4;

    /**
     * 文化程度4
     */
    @TableField(value = "WENHUACHENGDU4")
    private String WENHUACHENGDU4;

    /**
     * 婚姻4
     */
    @TableField(value = "HUNYIN4")
    private String HUNYIN4;

    /**
     * 兵役4
     */
    @TableField(value = "BINGYI4")
    private String BINGYI4;

    /**
     * 服务场所4
     */
    @TableField(value = "FUWUCHUSUO4")
    private String FUWUCHUSUO4;

    /**
     * 职业4
     */
    @TableField(value = "ZHIYE4")
    private String ZHIYE4;

    /**
     * 何时何地4
     */
    @TableField(value = "HESHIHEDI4")
    private String HESHIHEDI4;

    /**
     * 何时何地-本地4
     */
    @TableField(value = "HESHIHEDI_BENDI4")
    private String HESHIHEDI_BENDI4;

    /**
     * 户号4
     */
    @TableField(value = "HUHAO4")
    private String HUHAO4;

    /**
     * 户口类别4
     */
    @TableField(value = "HUKOULEIBIE4")
    private String HUKOULEIBIE4;

    /**
     * 派出所4
     */
    @TableField(value = "PAICHUSUOMINGCHENG4")
    private String PAICHUSUOMINGCHENG4;

    /**
     * 承办人4
     */
    @TableField(value = "CHENGBANREN4")
    private String CHENGBANREN4;

    /**
     * 姓名6
     */
    @TableField(value = "XINGMING5")
    private String XINGMING5;

    /**
     * 身份证号6
     */
    @TableField(value = "SHENFENZHENHAO5")
    private String SHENFENZHENHAO5;

    /**
     * 关系6
     */
    @TableField(value = "GUANXI5")
    private String GUANXI5;

    /**
     * 性别6
     */
    @TableField(value = "SEX5")
    private String SEX5;

    /**
     * 民族6
     */
    @TableField(value = "MINZU5")
    private String MINZU5;

    /**
     * 出生日期6
     */
    @TableField(value = "CHUSHENGRIQI5")
    private String CHUSHENGRIQI5;

    /**
     * 年6
     */
    @TableField(value = "NIAN5")
    private String NIAN5;

    /**
     * 月6
     */
    @TableField(value = "YUE5")
    private String YUE5;

    /**
     * 日6
     */
    @TableField(value = "RI5")
    private String RI5;

    /**
     * 曾用名6
     */
    @TableField(value = "H_RENAME5")
    private String h_RENAME5;

    /**
     * 信仰6
     */
    @TableField(value = "XINYANG5")
    private String XINYANG5;

    /**
     * 身高6
     */
    @TableField(value = "SHENGAO5")
    private String SHENGAO5;

    /**
     * 血型6
     */
    @TableField(value = "XUEXIN5")
    private String XUEXIN5;

    /**
     * 文化程度6
     */
    @TableField(value = "WENHUACHENGDU5")
    private String WENHUACHENGDU5;

    /**
     * 婚姻6
     */
    @TableField(value = "HUNYIN5")
    private String HUNYIN5;

    /**
     * 兵役6
     */
    @TableField(value = "BINGYI5")
    private String BINGYI5;

    /**
     * 服务场所6
     */
    @TableField(value = "FUWUCHUSUO5")
    private String FUWUCHUSUO5;

    /**
     * 职业6
     */
    @TableField(value = "ZHIYE5")
    private String ZHIYE5;

    /**
     * 何时何地6
     */
    @TableField(value = "HESHIHEDI5")
    private String HESHIHEDI5;

    /**
     * 何时何地-本地6
     */
    @TableField(value = "HESHIHEDI_BENDI5")
    private String HESHIHEDI_BENDI5;

    /**
     * 户号6
     */
    @TableField(value = "HUHAO5")
    private String HUHAO5;

    /**
     * 户口类别6
     */
    @TableField(value = "HUKOULEIBIE5")
    private String HUKOULEIBIE5;

    /**
     * 派出所6
     */
    @TableField(value = "PAICHUSUOMINGCHENG5")
    private String PAICHUSUOMINGCHENG5;

    /**
     * 承办人6
     */
    @TableField(value = "CHENGBANREN5")
    private String CHENGBANREN5;

    /**
     * 姓名7
     */
    @TableField(value = "XINGMING6")
    private String XINGMING6;

    /**
     * 身份证号7
     */
    @TableField(value = "SHENFENZHENHAO6")
    private String SHENFENZHENHAO6;

    /**
     * 关系7
     */
    @TableField(value = "GUANXI6")
    private String GUANXI6;

    /**
     * 性别7
     */
    @TableField(value = "SEX6")
    private String SEX6;

    /**
     * 民族7
     */
    @TableField(value = "MINZU6")
    private String MINZU6;

    /**
     * 出生日期7
     */
    @TableField(value = "CHUSHENGRIQI6")
    private String CHUSHENGRIQI6;

    /**
     * 年7
     */
    @TableField(value = "NIAN6")
    private String NIAN6;

    /**
     * 月7
     */
    @TableField(value = "YUE6")
    private String YUE6;

    /**
     * 日7
     */
    @TableField(value = "RI6")
    private String RI6;

    /**
     * 曾用名7
     */
    @TableField(value = "H_RENAME6")
    private String h_RENAME6;

    /**
     * 信仰7
     */
    @TableField(value = "XINYANG6")
    private String XINYANG6;

    /**
     * 身高7
     */
    @TableField(value = "SHENGAO6")
    private String SHENGAO6;

    /**
     * 血型7
     */
    @TableField(value = "XUEXIN6")
    private String XUEXIN6;

    /**
     * 文化程度7
     */
    @TableField(value = "WENHUACHENGDU6")
    private String WENHUACHENGDU6;

    /**
     * 婚姻7
     */
    @TableField(value = "HUNYIN6")
    private String HUNYIN6;

    /**
     * 兵役7
     */
    @TableField(value = "BINGYI6")
    private String BINGYI6;

    /**
     * 服务场所7
     */
    @TableField(value = "FUWUCHUSUO6")
    private String FUWUCHUSUO6;

    /**
     * 职业7
     */
    @TableField(value = "ZHIYE6")
    private String ZHIYE6;

    /**
     * 何时何地7
     */
    @TableField(value = "HESHIHEDI6")
    private String HESHIHEDI6;

    /**
     * 何时何地-本地7
     */
    @TableField(value = "HESHIHEDI_BENDI6")
    private String HESHIHEDI_BENDI6;

    /**
     * 户号7
     */
    @TableField(value = "HUHAO6")
    private String HUHAO6;

    /**
     * 户口类别7
     */
    @TableField(value = "HUKOULEIBIE6")
    private String HUKOULEIBIE6;

    /**
     * 派出所7
     */
    @TableField(value = "PAICHUSUOMINGCHENG6")
    private String PAICHUSUOMINGCHENG6;

    /**
     * 承办人7
     */
    @TableField(value = "CHENGBANREN6")
    private String CHENGBANREN6;

    /**
     * 插入日期
     */
    @TableField(value = "insert_date")
    private Date insert_date;

    /**
     * 住址01
     */
    @TableField(value = "ZHUZHI01")
    private String ZHUZHI01;

    /**
     * 出生地
     */
    @TableField(value = "CHUSHENGDI")
    private String CHUSHENGDI;

    /**
     * 籍贯
     */
    @TableField(value = "JIGUAN")
    private String JIGUAN;

    /**
     * 住址
     */
    @TableField(value = "ZHUZHI")
    private String ZHUZHI;

    /**
     * 出生地1
     */
    @TableField(value = "CHUSHENGDI1")
    private String CHUSHENGDI1;

    /**
     * 籍贯1
     */
    @TableField(value = "JIGUAN1")
    private String JIGUAN1;

    /**
     * 住址1
     */
    @TableField(value = "ZHUZHI1")
    private String ZHUZHI1;

    /**
     * 出生地2
     */
    @TableField(value = "CHUSHENGDI2")
    private String CHUSHENGDI2;

    /**
     * 籍贯2
     */
    @TableField(value = "JIGUAN2")
    private String JIGUAN2;

    /**
     * 住址2
     */
    @TableField(value = "ZHUZHI2")
    private String ZHUZHI2;

    /**
     * 出生地3
     */
    @TableField(value = "CHUSHENGDI3")
    private String CHUSHENGDI3;

    /**
     * 籍贯3
     */
    @TableField(value = "JIGUAN3")
    private String JIGUAN3;

    /**
     * 住址3
     */
    @TableField(value = "ZHUZHI3")
    private String ZHUZHI3;

    /**
     * 出生地4
     */
    @TableField(value = "CHUSHENGDI4")
    private String CHUSHENGDI4;

    /**
     * 籍贯4
     */
    @TableField(value = "JIGUAN4")
    private String JIGUAN4;

    /**
     * 住址4
     */
    @TableField(value = "ZHUZHI4")
    private String ZHUZHI4;

    /**
     * 出生地6
     */
    @TableField(value = "CHUSHENGDI5")
    private String CHUSHENGDI5;

    /**
     * 籍贯6
     */
    @TableField(value = "JIGUAN5")
    private String JIGUAN5;

    /**
     * 住址6
     */
    @TableField(value = "ZHUZHI5")
    private String ZHUZHI5;

    /**
     * 出生地7
     */
    @TableField(value = "CHUSHENGDI6")
    private String CHUSHENGDI6;

    /**
     * 籍贯7
     */
    @TableField(value = "JIGUAN6")
    private String JIGUAN6;

    /**
     * 住址7
     */
    @TableField(value = "ZHUZHI6")
    private String ZHUZHI6;

}