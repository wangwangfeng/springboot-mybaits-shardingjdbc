package com.zfsoft.certificate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.zfsoft.certificate.mapper.db3.OdsJjDrvZhzwMapper;
import com.zfsoft.certificate.mapper.db3.OdsJjFrmCodeZhzwMapper;
import com.zfsoft.certificate.pojo.OdsJjDrvZhzw;
import com.zfsoft.certificate.pojo.OdsJjFrmCodeZhzw;
import com.zfsoft.certificate.pojo.base.CertificateBaseBO;
import com.zfsoft.certificate.pojo.base.CertificateReqStrBO;
import com.zfsoft.certificate.pojo.base.Constant;
import com.zfsoft.certificate.util.Base64Tools;
import com.zfsoft.certificate.util.IdcardUtil;
import com.zfsoft.certificate.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
 * 驾驶证制证
 *
 * @author 86131
 */
@Api(tags = "驾驶证制证后台")
@Controller
@Slf4j
public class DriverLicenseController {

    @Autowired
    private Constant constant;

    /**
     * 消费线程计数器
     */
    public static AtomicInteger tashCounter = new AtomicInteger();

    @Autowired
    private OdsJjDrvZhzwMapper odsJjDrvZhzwMapper;

    @Autowired
    private OdsJjFrmCodeZhzwMapper odsJjFrmCodeZhzwMapper;


    /**
     * 驾驶证
     * 获取有效截止日期大于当前日期的1万条数据
     *
     * @return
     */
    @ApiOperation(value = "驾驶证制证", notes = "根据配置文件查询分页")
    @RequestMapping(value = "/jsz", method = RequestMethod.GET)
    @ResponseBody
    public String makeDriverLicense() {

        List<OdsJjDrvZhzw> odsJjDrvZhzws = new LambdaQueryChainWrapper<>(odsJjDrvZhzwMapper)
                .isNotNull(OdsJjDrvZhzw::getPhoto)
                .isNotNull(OdsJjDrvZhzw::getInfovaliditybegin)
                .isNotNull(OdsJjDrvZhzw::getInfovalidityend)
                .gt(OdsJjDrvZhzw::getInfovalidityend, DateUtil.date())
                .orderByDesc(OdsJjDrvZhzw::getModifydate)
                .last("limit " + constant.getLimitBegin() + "," + constant.getLimitEnd())
                .list();
        log.info("驾驶证制证开始总量为:{}个", odsJjDrvZhzws.size());
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(odsJjDrvZhzws)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (odsJjDrvZhzws.size() > tashCounter.get()) {
                OdsJjDrvZhzw odsJjDrvZhzw = odsJjDrvZhzws.get(tashCounter.get());
                log.info("数据身份证号:{}", odsJjDrvZhzw.getInfocode());
                executor.execute(() -> {
                    try {
                        CertificateBaseBO certificateBaseBO = new CertificateBaseBO();
                        CertificateReqStrBO certificateReqStrBO = new CertificateReqStrBO();
                        certificateBaseBO.setContentCode("11340800003110803D0053");
                        certificateBaseBO.setOwnerId(odsJjDrvZhzw.getInfocode());
                        certificateBaseBO.setOwnerName(odsJjDrvZhzw.getName1());
                        certificateBaseBO.setInfoCode(odsJjDrvZhzw.getInfocode());
                        certificateBaseBO.setInfoValidityBegin(DateUtil.format(odsJjDrvZhzw.getInfovaliditybegin(),
                                "yyyyMMdd"));
                        certificateBaseBO.setInfoValidityEnd(DateUtil.format(odsJjDrvZhzw.getInfovalidityend(),
                                "yyyyMMdd"));
                        Map<String, String> dataMap = new HashMap<>();
                        dataMap.put("infoCode", odsJjDrvZhzw.getInfocode());
                        dataMap.put("name1", odsJjDrvZhzw.getName1());
                        // 性别
                        if (StringUtils.isNotEmpty(odsJjDrvZhzw.getSex())) {
                            if ("1".equals(odsJjDrvZhzw.getSex())) {
                                dataMap.put("sex", "男");
                            } else if ("2".equals(odsJjDrvZhzw.getSex())) {
                                dataMap.put("sex", "女");
                            }
                        }
                        // 国家
                        OdsJjFrmCodeZhzw odsJjFrmCodeZhzw = new LambdaQueryChainWrapper<>(odsJjFrmCodeZhzwMapper)
                                .likeRight(OdsJjFrmCodeZhzw::getXtlb, "00")
                                .eq(OdsJjFrmCodeZhzw::getDmlb, "0031")
                                .eq(OdsJjFrmCodeZhzw::getDmz, odsJjDrvZhzw.getCountry())
                                .orderByDesc(OdsJjFrmCodeZhzw::getXyptTimeDxp)
                                .last("limit 1")
                                .one();
                        if (odsJjFrmCodeZhzw != null && StringUtils.isNotEmpty(odsJjFrmCodeZhzw.getDmsm1())) {
                            dataMap.put("country", odsJjFrmCodeZhzw.getDmsm1());
                        }
                        // 地址
                        dataMap.put("address", StringUtils.handleString(odsJjDrvZhzw.getAddress()));
                        dataMap.put("birthday", IdcardUtil.getYearByIdCard(odsJjDrvZhzw.getInfocode()) + "-"
                                + IdcardUtil.getMonthByIdCard(odsJjDrvZhzw.getInfocode()) + "-"
                                + IdcardUtil.getDateByIdCard(odsJjDrvZhzw.getInfocode()));
                        // 初次领证日期格式处理:日月年（08-8月 -19 12.00.00.000000000 上午）---->2019-08-08
                        if (StringUtils.isNotEmpty(odsJjDrvZhzw.getFirstday())) {
                            String[] day = odsJjDrvZhzw.getFirstday().substring(0, 9)
                                    .replaceAll(" ", "")
                                    .replace("月", "").split("-");
                            StringBuffer stringBuffer = new StringBuffer();
                            // 获取当前年份的后两位做判断
                            if (Integer.parseInt(day[2]) > Integer.parseInt(String.valueOf(DateUtil.thisYear())
                                    .substring(2))) {
                                stringBuffer.append("19").append(day[2]);
                            } else {
                                stringBuffer.append("20").append(day[2]);
                            }
                            stringBuffer.append("-");
                            if (day[1].length() == 1) {
                                stringBuffer.append("0").append(day[1]);
                            } else {
                                stringBuffer.append(day[1]);
                            }
                            stringBuffer.append("-");
                            stringBuffer.append(day[0]);
                            dataMap.put("firstDay", stringBuffer.toString());
                        }
                        dataMap.put("type", odsJjDrvZhzw.getType());
                        dataMap.put("beganEnd", DateUtil.format(odsJjDrvZhzw.getInfovaliditybegin(), "yyyy-MM-dd"));
                        dataMap.put("beganEnd1", DateUtil.format(odsJjDrvZhzw.getInfovalidityend(), "yyyy-MM-dd"));
                        if (odsJjDrvZhzw.getPhoto() != null
                                && odsJjDrvZhzw.getPhoto().length > 0) {
                            dataMap.put("photo", Base64Tools.encodeBase64String(odsJjDrvZhzw.getPhoto()));
                        } else {
                            dataMap.put("photo", "");
                        }
                        dataMap.put("infoCode2", odsJjDrvZhzw.getInfocode());
                        dataMap.put("name2", odsJjDrvZhzw.getName1());
                        dataMap.put("dabh", odsJjDrvZhzw.getDabh());
                        dataMap.put("jl2", odsJjDrvZhzw.getJl2());
                        dataMap.put("jl1", odsJjDrvZhzw.getJl1());
                        dataMap.put("barCode", odsJjDrvZhzw.getBarcode());
                        dataMap.put("barNumber", odsJjDrvZhzw.getBarnumber());
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

        return "驾驶证制证:" + odsJjDrvZhzws.size() + ":" + DateUtil.now();
    }

    /**
     * 根据身份证号制证单个驾驶证数据
     *
     * @return
     */
    @ApiOperation(value = "根据身份证号制证驾驶证", notes = "")
    @RequestMapping(value = "/jsz/{ownerId}", method = RequestMethod.GET)
    @ResponseBody
    public String makeDriverLicenseByOwnerId(@PathVariable String ownerId) {

        List<OdsJjDrvZhzw> odsJjDrvZhzws = new LambdaQueryChainWrapper<>(odsJjDrvZhzwMapper)
                .isNotNull(OdsJjDrvZhzw::getPhoto)
                .isNotNull(OdsJjDrvZhzw::getInfovaliditybegin)
                .isNotNull(OdsJjDrvZhzw::getInfovalidityend)
                .eq(OdsJjDrvZhzw::getInfocode, ownerId)
                .gt(OdsJjDrvZhzw::getInfovalidityend, DateUtil.date())
                .orderByDesc(OdsJjDrvZhzw::getModifydate)
                .list();
        log.info("单个驾驶证制证身份证号:{}", ownerId);
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(odsJjDrvZhzws)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (odsJjDrvZhzws.size() > tashCounter.get()) {
                OdsJjDrvZhzw odsJjDrvZhzw = odsJjDrvZhzws.get(tashCounter.get());
                executor.execute(() -> {
                    try {
                        CertificateBaseBO certificateBaseBO = new CertificateBaseBO();
                        CertificateReqStrBO certificateReqStrBO = new CertificateReqStrBO();
                        certificateBaseBO.setContentCode("11340800003110803D0053");
                        certificateBaseBO.setOwnerId(odsJjDrvZhzw.getInfocode());
                        certificateBaseBO.setOwnerName(odsJjDrvZhzw.getName1());
                        certificateBaseBO.setInfoCode(odsJjDrvZhzw.getInfocode());
                        certificateBaseBO.setInfoValidityBegin(DateUtil.format(odsJjDrvZhzw.getInfovaliditybegin(),
                                "yyyyMMdd"));
                        certificateBaseBO.setInfoValidityEnd(DateUtil.format(odsJjDrvZhzw.getInfovalidityend(),
                                "yyyyMMdd"));
                        Map<String, String> dataMap = new HashMap<>();
                        dataMap.put("infoCode", odsJjDrvZhzw.getInfocode());
                        dataMap.put("name1", odsJjDrvZhzw.getName1());
                        // 性别
                        if (StringUtils.isNotEmpty(odsJjDrvZhzw.getSex())) {
                            if ("1".equals(odsJjDrvZhzw.getSex())) {
                                dataMap.put("sex", "男");
                            } else if ("2".equals(odsJjDrvZhzw.getSex())) {
                                dataMap.put("sex", "女");
                            }
                        }
                        // 国家
                        OdsJjFrmCodeZhzw odsJjFrmCodeZhzw = new LambdaQueryChainWrapper<>(odsJjFrmCodeZhzwMapper)
                                .likeRight(OdsJjFrmCodeZhzw::getXtlb, "00")
                                .eq(OdsJjFrmCodeZhzw::getDmlb, "0031")
                                .eq(OdsJjFrmCodeZhzw::getDmz, odsJjDrvZhzw.getCountry())
                                .orderByDesc(OdsJjFrmCodeZhzw::getXyptTimeDxp)
                                .last("limit 1")
                                .one();
                        if (odsJjFrmCodeZhzw != null && StringUtils.isNotEmpty(odsJjFrmCodeZhzw.getDmsm1())) {
                            dataMap.put("country", odsJjFrmCodeZhzw.getDmsm1());
                        }
                        // 地址
                        dataMap.put("address", StringUtils.handleString(odsJjDrvZhzw.getAddress()));
                        dataMap.put("birthday", IdcardUtil.getYearByIdCard(odsJjDrvZhzw.getInfocode()) + "-"
                                + IdcardUtil.getMonthByIdCard(odsJjDrvZhzw.getInfocode()) + "-"
                                + IdcardUtil.getDateByIdCard(odsJjDrvZhzw.getInfocode()));
                        // 初次领证日期格式处理:日月年（08-8月 -19 12.00.00.000000000 上午）---->2019-08-08
                        if (StringUtils.isNotEmpty(odsJjDrvZhzw.getFirstday())) {
                            String[] day = odsJjDrvZhzw.getFirstday().substring(0, 9)
                                    .replaceAll(" ", "")
                                    .replace("月", "").split("-");
                            StringBuffer stringBuffer = new StringBuffer();
                            // 获取当前年份的后两位做判断
                            if (Integer.parseInt(day[2]) > Integer.parseInt(String.valueOf(DateUtil.thisYear())
                                    .substring(2))) {
                                stringBuffer.append("19").append(day[2]);
                            } else {
                                stringBuffer.append("20").append(day[2]);
                            }
                            stringBuffer.append("-");
                            if (day[1].length() == 1) {
                                stringBuffer.append("0").append(day[1]);
                            } else {
                                stringBuffer.append(day[1]);
                            }
                            stringBuffer.append("-");
                            stringBuffer.append(day[0]);
                            dataMap.put("firstDay", stringBuffer.toString());
                        }
                        dataMap.put("type", odsJjDrvZhzw.getType());
                        dataMap.put("beganEnd", DateUtil.format(odsJjDrvZhzw.getInfovaliditybegin(), "yyyy-MM-dd"));
                        dataMap.put("beganEnd1", DateUtil.format(odsJjDrvZhzw.getInfovalidityend(), "yyyy-MM-dd"));
                        if (odsJjDrvZhzw.getPhoto() != null
                                && odsJjDrvZhzw.getPhoto().length > 0) {
                            dataMap.put("photo", Base64Tools.encodeBase64String(odsJjDrvZhzw.getPhoto()));
                        } else {
                            dataMap.put("photo", "");
                        }
                        dataMap.put("infoCode2", odsJjDrvZhzw.getInfocode());
                        dataMap.put("name2", odsJjDrvZhzw.getName1());
                        dataMap.put("dabh", odsJjDrvZhzw.getDabh());
                        dataMap.put("jl2", odsJjDrvZhzw.getJl2());
                        dataMap.put("jl1", odsJjDrvZhzw.getJl1());
                        dataMap.put("barCode", odsJjDrvZhzw.getBarcode());
                        dataMap.put("barNumber", odsJjDrvZhzw.getBarnumber());
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

        return "驾驶证制证身份证号:" + ownerId + ";" + "数据总量为:" + odsJjDrvZhzws.size() + "个;日期" + DateUtil.now();
    }

}
