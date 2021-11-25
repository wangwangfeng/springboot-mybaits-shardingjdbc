package com.zfsoft.certificate.pojo.base;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 常量类
 * 从常量配置文件获取所有值
 *
 * @author 86131
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "constant")
@PropertySource("classpath:constant.properties")
@Data
public class Constant {

    /**
     * 分页查询条数
     */
    private String limitBegin;
    private String limitEnd;

    /**
     * 线程池相关参数
     */
    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Long keepAliveSeconds;

    /**
     * 制证相关配置地址
     */
    private String makeSealUrl;
    private String agentUrl;
    private String makeDiskZ;
    private String makeUrl;
    private String zzDownload;

    // 制证有效期结束日期为永久
    private String infoValidityEnd;

    /**
     * 制证区划代码(前六位)
     */
    // 安庆市
    private String aqZoneCode;
    // 市辖区
    private String sxqZoneCode;
    //迎江区
    private String yjqZoneCode;
    //大观区
    private String dgqZoneCode;
    //宜秀区
    private String yxqZoneCode;
    //经开区
    private String jkqZoneCode;
    //怀宁县
    private String hnxZoneCode;
    //太湖县
    private String thxZoneCode;
    //宿松县
    private String ssxZoneCode;
    //望江县
    private String wjxZoneCode;
    //岳西县
    private String yxxZoneCode;
    //桐城市
    private String tcsZoneCode;
    //潜山市
    private String qssZoneCode;
    private String qssZoneCodeOne;

    /**
     * 户口簿制证入参规范
     */
    private String hkbOwnerId;
    private String hkbOwnerName;

    /**
     * 老年证类型60-65-70
     */
    private String lnzSixty;
    private String lnzSixtyFive;
    private String lnzSeventy;

    private String man;
    private String woman;

    private String diskZ;

    /**
     * 附件盘符路径：默认 Z:/elms_files/
     **/
    private String basicPath;

    private String zero;
    private String one;
    private String two;
    private String three;
    private String four;
    private String five;

    private String iflytekHost;
    private String iflytekPort;

}
