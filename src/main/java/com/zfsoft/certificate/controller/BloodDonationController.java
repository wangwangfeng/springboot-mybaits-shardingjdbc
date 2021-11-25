package com.zfsoft.certificate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.zfsoft.certificate.pojo.BloodDonation;
import com.zfsoft.certificate.pojo.base.CertificateBaseBO;
import com.zfsoft.certificate.pojo.base.CertificateReqStrBO;
import com.zfsoft.certificate.pojo.base.Constant;
import com.zfsoft.certificate.service.BloodDonationService;
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
 * 献血证
 *
 * @author 86131
 */
@Api(tags = "献血证制证后台")
@Controller
@Slf4j
public class BloodDonationController {

    @Autowired
    private Constant constant;

    /**
     * 消费线程计数器
     */
    public static AtomicInteger tashCounter = new AtomicInteger();

    @Autowired
    private BloodDonationService bloodDonationService;


    /**
     * 献血证
     *
     * @return
     */
    @ApiOperation(value = "献血证制证", notes = "")
    @RequestMapping(value = "/xxz", method = RequestMethod.GET)
    @ResponseBody
    public String makeHonour() {
        List<BloodDonation> bloodDonationList = bloodDonationService.findAll();
        log.info("献血证任务开始总量为:" + bloodDonationList.size() + "个");
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(bloodDonationList)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (bloodDonationList.size() > tashCounter.get()) {
                BloodDonation entity = bloodDonationList.get(tashCounter.get());
                executor.execute(() -> {
                    try {
                        CertificateBaseBO certificateBaseBO = new CertificateBaseBO();
                        CertificateReqStrBO certificateReqStrBO = new CertificateReqStrBO();
                        certificateBaseBO.setContentCode("11340800335629046P0187");
                        certificateBaseBO.setOwnerId(entity.getZjhm());
                        certificateBaseBO.setOwnerName(entity.getXm());
                        certificateBaseBO.setInfoCode(entity.getXxm());
                        certificateBaseBO.setInfoValidityBegin(DateUtil.format(entity.getCxsj(), "yyyyMMdd"));
                        certificateBaseBO.setInfoValidityEnd(constant.getInfoValidityEnd());
                        Map<String, String> dataMap = new HashMap<>();
                        // 献血日期
                        dataMap.put("xxrq", DateUtil.format(entity.getCxsj(), "yyyy年MM月dd日"));
                        // 语音热线
                        dataMap.put("rx", "");
                        // 血型
                        dataMap.put("xx", StringUtils.handleString(entity.getDaxx()));
                        // 姓名
                        dataMap.put("xm", StringUtils.handleString(entity.getXm()));
                        // 民族
                        dataMap.put("mz", "");
                        // 身份证号
                        dataMap.put("sfzh", StringUtils.handleString(entity.getZjhm()));
                        // 编号
                        dataMap.put("bh", StringUtils.handleString(entity.getXxm()));
                        // 工作单位
                        dataMap.put("gzdw", "");
                        // 家庭住址
                        dataMap.put("jtzz", "");
                        // 本人签名
                        dataMap.put("brqm", "");
                        // 证件类型
                        dataMap.put("zjlx", StringUtils.handleString(entity.getZjlx()));
                        // 献血种类
                        dataMap.put("xxzl", StringUtils.handleString(entity.getXxlx()));
                        // 献血量
                        dataMap.put("xxl", StringUtils.handleString(entity.getCxl().toString()));
                        // 献血编码
                        dataMap.put("xxbm", StringUtils.handleString(entity.getXxm()));
                        // 献血地点
                        if (StrUtil.isNotEmpty(entity.getCxdd())) {
                            if ("cai xue che".equals(entity.getCxdd().trim())) {
                                dataMap.put("xxdd", "采血车");
                            } else if ("jie tou".equals(entity.getCxdd().trim())) {
                                dataMap.put("xxdd", "街头");
                            } else if ("sscaixuedian".equals(entity.getCxdd().trim())) {
                                dataMap.put("xxdd", "宿松采血点");
                            } else if ("other".equals(entity.getCxdd().trim())) {
                                dataMap.put("xxdd", "其它");
                            } else {
                                dataMap.put("xxdd", StringUtils.handleString(entity.getCxdd()));
                            }
                        } else {
                            dataMap.put("xxdd", "");
                        }
                        dataMap.put("xxdd", StringUtils.handleString(entity.getCxdd()));
                        // 发证单位
                        dataMap.put("fzdw", "");
                        // 咨询电话
                        dataMap.put("zxdh", "");
                        certificateBaseBO.setData(dataMap);
                        certificateReqStrBO.setOrgCode("340800");
                        certificateReqStrBO.setOrgName("安庆市卫健委");
                        certificateReqStrBO.setUserName("安庆市卫健委");
                        certificateReqStrBO.setUserCode("340800");
                        certificateReqStrBO.setIdcard("340800");
                        certificateReqStrBO.setBizCode("zzwk");
                        certificateReqStrBO.setBizName("证照文库");
                        certificateBaseBO.setReqStr(certificateReqStrBO);
                        JSONObject jsonObject = new JSONObject(certificateBaseBO);
                        log.info("制证入参:{}", jsonObject.toString());
                        String result = HttpUtil.post(constant.getMakeUrl(), jsonObject.toString());
                        JSONObject object = new JSONObject(result);
                        log.info("制证结果:{}", object.toString());
                        if ("true".equals(object.get("flag"))) {

                        }
                    } catch (Exception e) {
                        log.error(":制证失败:{}", e.getMessage());
                    }
                });
                tashCounter.incrementAndGet();
            }
        }

        return "献血证制证:" + bloodDonationList.size() + ":" + DateUtil.now();
    }

}
