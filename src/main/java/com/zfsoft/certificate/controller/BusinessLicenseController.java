package com.zfsoft.certificate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.zfsoft.certificate.mapper.db4.DwdQyyyzzxxMapper;
import com.zfsoft.certificate.pojo.DwdQyyyzzxx;
import com.zfsoft.certificate.pojo.base.CertificateBaseBO;
import com.zfsoft.certificate.pojo.base.CertificateReqStrBO;
import com.zfsoft.certificate.pojo.base.Constant;
import com.zfsoft.certificate.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 营业执照制证(2021-07-14)
 * 省统一制证
 * 为了不动产考核(刷选后符合条件数据：76278(未去掉过期证照))
 * @author 86131
 */
@Api(tags = "营业执照制证后台")
@Controller
@Slf4j
public class BusinessLicenseController {

    @Autowired
    private Constant constant;

    /**
     * 消费线程计数器
     */
    public static AtomicInteger tashCounter = new AtomicInteger();

    @Autowired
    private DwdQyyyzzxxMapper dwdQyyyzzxxMapper;


    /**
     * 营业执照
     *
     * @return
     */
    @ApiOperation(value = "营业执照制证", notes = "根据配置文件查询分页")
    @RequestMapping(value = "/yyzz", method = RequestMethod.GET)
    @ResponseBody
    public String makeBusinessLicense() {

        List<DwdQyyyzzxx> dwdQyyyzzxxes = new LambdaQueryChainWrapper<>(dwdQyyyzzxxMapper)
                .isNotNull(DwdQyyyzzxx::getZch)
                .isNotNull(DwdQyyyzzxx::getMc)
                .isNotNull(DwdQyyyzzxx::getLx)
                .isNotNull(DwdQyyyzzxx::getZs)
                .isNotNull(DwdQyyyzzxx::getFddbr)
                .isNotNull(DwdQyyyzzxx::getZczb)
                .isNotNull(DwdQyyyzzxx::getClrq)
                .isNotNull(DwdQyyyzzxx::getYyqx)
                .isNotNull(DwdQyyyzzxx::getJyfw)
                .ne(DwdQyyyzzxx::getZczb, constant.getZero())
                .orderByDesc(DwdQyyyzzxx::getZch)
                .last("limit " + constant.getLimitBegin() + "," + constant.getLimitEnd())
                .list();
        log.info("营业执照制证开始总量为:{}个", dwdQyyyzzxxes.size());
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(dwdQyyyzzxxes)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (dwdQyyyzzxxes.size() > tashCounter.get()) {
                DwdQyyyzzxx dwdQyyyzzxx = dwdQyyyzzxxes.get(tashCounter.get());
                log.info("数据统一社会信用代码:{}", dwdQyyyzzxx.getZch());
                executor.execute(() -> {
                    try {
                        CertificateBaseBO certificateBaseBO = new CertificateBaseBO();
                        CertificateReqStrBO certificateReqStrBO = new CertificateReqStrBO();
                        certificateBaseBO.setContentCode("11340800328025287E0001");
                        certificateBaseBO.setOwnerId(dwdQyyyzzxx.getZch());
                        certificateBaseBO.setOwnerName(dwdQyyyzzxx.getMc());
                        certificateBaseBO.setInfoCode(dwdQyyyzzxx.getZch());
                        certificateBaseBO.setInfoValidityBegin(DateUtil.format(DateUtil.parse(dwdQyyyzzxx.getClrq()),
                                DatePattern.PURE_DATE_PATTERN));
                        certificateBaseBO.setInfoValidityEnd(DateUtil.format(DateUtil.parse(dwdQyyyzzxx.getYyqx()),
                                DatePattern.PURE_DATE_PATTERN));
                        Map<String, String> dataMap = new HashMap<>();
                        dataMap.put("zch", dwdQyyyzzxx.getZch());
                        dataMap.put("mc", dwdQyyyzzxx.getMc());
                        dataMap.put("lx", dwdQyyyzzxx.getLx());
                        dataMap.put("zs", dwdQyyyzzxx.getZs());
                        dataMap.put("fddbr", dwdQyyyzzxx.getFddbr());
                        // 处理注册资本
                        if (StringUtils.isNotEmpty(dwdQyyyzzxx.getZczb())
                                && dwdQyyyzzxx.getZczb().contains("万元")) {
                            dataMap.put("zczb", dwdQyyyzzxx.getZczb());
                        } else {
                            dataMap.put("zczb", "￥" + dwdQyyyzzxx.getZczb() + "万元");
                        }
                        dataMap.put("clrq", DateUtil.format(DateUtil.parse(dwdQyyyzzxx.getClrq()),
                                DatePattern.CHINESE_DATE_PATTERN));
                        dataMap.put("yyqx", DateUtil.format(DateUtil.parse(dwdQyyyzzxx.getClrq()),
                                DatePattern.CHINESE_DATE_PATTERN)
                                + "至" + DateUtil.format(DateUtil.parse(dwdQyyyzzxx.getYyqx()),
                                DatePattern.CHINESE_DATE_PATTERN));
                        // 处理经营范围
                        dataMap.put("jyfw", dwdQyyyzzxx.getJyfw().replace("*", ""));
                        dataMap.put("djjg", "安庆市市场监督管理局");
                        dataMap.put("nian", String.valueOf(DateUtil.year(DateUtil.parse(dwdQyyyzzxx.getClrq()))));
                        dataMap.put("yue", DateUtil.month(DateUtil.parse(dwdQyyyzzxx.getClrq())) + 1 < 10 ?
                                "0" + String.valueOf(DateUtil.month(DateUtil.parse(dwdQyyyzzxx.getClrq())) + 1)
                                : String.valueOf(DateUtil.month(DateUtil.parse(dwdQyyyzzxx.getClrq())) + 1));
                        dataMap.put("ri", DateUtil.dayOfMonth(DateUtil.parse(dwdQyyyzzxx.getClrq())) < 10 ?
                                "0" + String.valueOf(DateUtil.dayOfMonth(DateUtil.parse(dwdQyyyzzxx.getClrq())))
                                : String.valueOf(DateUtil.dayOfMonth(DateUtil.parse(dwdQyyyzzxx.getClrq()))));
                        certificateBaseBO.setData(dataMap);
                        certificateReqStrBO.setUserName("安庆市数据资源局");
                        certificateReqStrBO.setUserCode("340800");
                        certificateReqStrBO.setIdcard("340800");
                        certificateReqStrBO.setOrgCode("340800");
                        certificateReqStrBO.setOrgName("安庆市数据资源局");
                        certificateReqStrBO.setBizCode("zzwk");
                        certificateReqStrBO.setBizName("证照文库");
                        certificateBaseBO.setReqStr(certificateReqStrBO);
                        JSONObject jsonObject = new JSONObject(certificateBaseBO);
                        String result = HttpUtil.post(constant.getMakeUrl(), jsonObject.toString());
                        JSONObject object = new JSONObject(result);
                        log.info("制证结果:{}", object.toString());
                        if ("true".equals(object.get("flag"))) {

                        }
                    } catch (Exception e) {
                        log.error("制证异常失败:{}", e.getMessage());
                    }
                });
                tashCounter.incrementAndGet();
            }
        }

        return "营业执照制证:" + dwdQyyyzzxxes.size() + ":" + DateUtil.now();
    }

}
