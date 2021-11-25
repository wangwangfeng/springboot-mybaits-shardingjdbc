package com.zfsoft.certificate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.zfsoft.certificate.mapper.db3.OdsJjFrmCodeZhzwMapper;
import com.zfsoft.certificate.mapper.db3.OdsJjXszzmxxZhzwMapper;
import com.zfsoft.certificate.pojo.OdsJjFrmCodeZhzw;
import com.zfsoft.certificate.pojo.OdsJjXszzmxxZhzw;
import com.zfsoft.certificate.pojo.base.CertificateBaseBO;
import com.zfsoft.certificate.pojo.base.CertificateReqStrBO;
import com.zfsoft.certificate.pojo.base.Constant;
import com.zfsoft.certificate.util.Base64Tools;
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
import java.util.concurrent.atomic.AtomicReference;

/**
 * 行驶证制证
 *
 * @author 86131
 */
@Api(tags = "行驶证制证后台")
@Controller
@Slf4j
public class VehicleLicenseController {

    @Autowired
    private Constant constant;

    /**
     * 线程计数器
     */
    public static AtomicInteger atomicInteger = new AtomicInteger();

    @Autowired
    private OdsJjXszzmxxZhzwMapper odsJjXszzmxxZhzwMapper;

    @Autowired
    private OdsJjFrmCodeZhzwMapper odsJjFrmCodeZhzwMapper;


    /**
     * 行驶证
     * 获取有效截止日期大于当前日期的1万条数据
     *
     * @return
     */
    @ApiOperation(value = "行驶证制证", notes = "根据配置文件查询分页")
    @RequestMapping(value = "/xsz", method = RequestMethod.GET)
    @ResponseBody
    public String makeVehicleLicense() {

        List<OdsJjXszzmxxZhzw> odsJjXszzmxxZhzws = new LambdaQueryChainWrapper<>(odsJjXszzmxxZhzwMapper)
                .isNotNull(OdsJjXszzmxxZhzw::getArchivesnum)
                .isNotNull(OdsJjXszzmxxZhzw::getInfovaliditybegin)
                .isNotNull(OdsJjXszzmxxZhzw::getInfovalidityend)
                .gt(OdsJjXszzmxxZhzw::getInfovalidityend, DateUtil.date())
                .orderByDesc(OdsJjXszzmxxZhzw::getModifydate)
                .last("limit " + constant.getLimitBegin() + "," + constant.getLimitEnd())
                .list();
        log.info("行驶证制证开始总量为:{}个", odsJjXszzmxxZhzws.size());
        atomicInteger.set(0);
        if (CollUtil.isNotEmpty(odsJjXszzmxxZhzws)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (odsJjXszzmxxZhzws.size() > atomicInteger.get()) {
                OdsJjXszzmxxZhzw odsJjXszzmxxZhzw = odsJjXszzmxxZhzws.get(atomicInteger.get());
                log.info("数据身份证号:{}", odsJjXszzmxxZhzw.getOwnerid());
                executor.execute(() -> {
                    try {
                        CertificateBaseBO certificateBaseBO = new CertificateBaseBO();
                        CertificateReqStrBO certificateReqStrBO = new CertificateReqStrBO();
                        certificateBaseBO.setContentCode("11340800003110803D0054");
                        certificateBaseBO.setOwnerId(odsJjXszzmxxZhzw.getOwnerid());
                        certificateBaseBO.setOwnerName(odsJjXszzmxxZhzw.getOwnername());
                        certificateBaseBO.setInfoCode("皖" + odsJjXszzmxxZhzw.getInfocode());
                        certificateBaseBO.setInfoValidityBegin(DateUtil.format(odsJjXszzmxxZhzw.getInfovaliditybegin(),
                                "yyyyMMdd"));
                        certificateBaseBO.setInfoValidityEnd(DateUtil.format(odsJjXszzmxxZhzw.getInfovalidityend(),
                                "yyyyMMdd"));
                        Map<String, String> dataMap = new HashMap<>();
                        // 照片	N
                        if (odsJjXszzmxxZhzw.getPhoto() != null
                                && odsJjXszzmxxZhzw.getPhoto().length > 0) {
                            dataMap.put("photo", Base64Tools.encodeBase64String(odsJjXszzmxxZhzw.getPhoto()));
                        } else {
                            dataMap.put("photo", "");
                        }
                        // 证照编号	Y
                        dataMap.put("infoCode", "皖" + odsJjXszzmxxZhzw.getInfocode());
                        // 车辆类型	Y
                        OdsJjFrmCodeZhzw odsJjFrmCodeZhzwCarType = new LambdaQueryChainWrapper<>(odsJjFrmCodeZhzwMapper)
                                .likeRight(OdsJjFrmCodeZhzw::getXtlb, "00")
                                .eq(OdsJjFrmCodeZhzw::getDmlb, "1004")
                                .eq(OdsJjFrmCodeZhzw::getDmz, odsJjXszzmxxZhzw.getCartype())
                                .orderByDesc(OdsJjFrmCodeZhzw::getXyptTimeDxp)
                                .last("limit 1")
                                .one();
                        if (odsJjFrmCodeZhzwCarType != null
                                && StringUtils.isNotEmpty(odsJjFrmCodeZhzwCarType.getDmsm1())) {
                            dataMap.put("carType", odsJjFrmCodeZhzwCarType.getDmsm1());
                        }
                        // 所有人	Y
                        dataMap.put("name", odsJjXszzmxxZhzw.getName());
                        // 地址	Y
                        dataMap.put("address", odsJjXszzmxxZhzw.getAddress());
                        // 使用性质	Y
                        OdsJjFrmCodeZhzw odsJjFrmCodeZhzwUseProperty = new LambdaQueryChainWrapper<>(odsJjFrmCodeZhzwMapper)
                                .likeRight(OdsJjFrmCodeZhzw::getXtlb, "00")
                                .eq(OdsJjFrmCodeZhzw::getDmlb, "1003")
                                .eq(OdsJjFrmCodeZhzw::getDmz, odsJjXszzmxxZhzw.getUseproperty())
                                .orderByDesc(OdsJjFrmCodeZhzw::getXyptTimeDxp)
                                .last("limit 1")
                                .one();
                        if (odsJjFrmCodeZhzwUseProperty != null
                                && StringUtils.isNotEmpty(odsJjFrmCodeZhzwUseProperty.getDmsm1())) {
                            dataMap.put("useProperty", odsJjFrmCodeZhzwUseProperty.getDmsm1());
                        }
                        // 品牌型号	Y
                        dataMap.put("brandModel", odsJjXszzmxxZhzw.getBrandmodel());
                        // 车辆识别代号	Y
                        dataMap.put("carcode", odsJjXszzmxxZhzw.getCarcode());
                        // 发动机号码	Y
                        dataMap.put("engineNumber", odsJjXszzmxxZhzw.getEnginenumber());
                        // 注册日期	Y
                        dataMap.put("registDate", DateUtil.format(odsJjXszzmxxZhzw.getRegistdate(),
                                "yyyy-MM-dd"));
                        // 发证日期	Y
                        dataMap.put("issueDate", DateUtil.format(odsJjXszzmxxZhzw.getIssuedate(),
                                "yyyy-MM-dd"));
                        // 号牌号码	Y
                        dataMap.put("carnumber", "皖" + odsJjXszzmxxZhzw.getCarnumber());
                        // 档案编号 N
                        dataMap.put("archivesNum", odsJjXszzmxxZhzw.getArchivesnum());
                        // 核定载人数    N
                        if (odsJjXszzmxxZhzw.getApprovedload() != null) {
                            dataMap.put("approvedLoad", odsJjXszzmxxZhzw.getApprovedload() + "人");
                        } else {
                            dataMap.put("approvedLoad", "- -");
                        }
                        // 总质量	N
                        if (odsJjXszzmxxZhzw.getTotalmass() != null) {
                            dataMap.put("totalMass", odsJjXszzmxxZhzw.getTotalmass() + "kg");
                        } else {
                            dataMap.put("totalMass", "- -");
                        }
                        // 整备质量	N
                        if (odsJjXszzmxxZhzw.getKerbmass() != null) {
                            dataMap.put("kerbMass", odsJjXszzmxxZhzw.getKerbmass() + "kg");
                        } else {
                            dataMap.put("kerbMass", "- -");
                        }
                        // 核定载质量	N
                        if (odsJjXszzmxxZhzw.getApprovedquality() != null) {
                            dataMap.put("approvedQuality", odsJjXszzmxxZhzw.getApprovedquality() + "kg");
                        } else {
                            dataMap.put("approvedQuality", "- -");
                        }
                        // 外廓尺寸	N
                        if (StringUtils.isNotEmpty(odsJjXszzmxxZhzw.getOverallsize())) {
                            dataMap.put("overallSize", odsJjXszzmxxZhzw.getOverallsize());
                        } else {
                            dataMap.put("overallSize", "- -");
                        }
                        // 准牵引总质量	N
                        if (odsJjXszzmxxZhzw.getTotal() != null) {
                            dataMap.put("total", odsJjXszzmxxZhzw.getTotal() + "kg");
                        } else {
                            dataMap.put("total", "- -");
                        }
                        // 备注	N
                        dataMap.put("note1", odsJjXszzmxxZhzw.getNote1());
                        // 检验记录	N
                        dataMap.put("recode", odsJjXszzmxxZhzw.getRecode());
                        // 记录	N
                        dataMap.put("notes2", odsJjXszzmxxZhzw.getNotes2());
                        // 燃料种类	N
                        OdsJjFrmCodeZhzw odsJjFrmCodeZhzwRlzl = new LambdaQueryChainWrapper<>(odsJjFrmCodeZhzwMapper)
                                .likeRight(OdsJjFrmCodeZhzw::getXtlb, "01")
                                .eq(OdsJjFrmCodeZhzw::getDmlb, "0009")
                                .eq(OdsJjFrmCodeZhzw::getDmz, odsJjXszzmxxZhzw.getRlzl())
                                .orderByDesc(OdsJjFrmCodeZhzw::getXyptTimeDxp)
                                .last("limit 1")
                                .one();
                        if (odsJjFrmCodeZhzwRlzl != null
                                && StringUtils.isNotEmpty(odsJjFrmCodeZhzwRlzl.getDmsm1())) {
                            dataMap.put("rlzl", odsJjFrmCodeZhzwRlzl.getDmsm1());
                        }
                        // 条形码	N
                        dataMap.put("barCode", odsJjXszzmxxZhzw.getBarcode());
                        // 条形码数字	N
                        dataMap.put("barNumber", odsJjXszzmxxZhzw.getBarnumber());
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
                atomicInteger.incrementAndGet();
            }
        }
        return "行驶证制证:" + odsJjXszzmxxZhzws.size() + ":" + DateUtil.now();
    }

    /**
     * 根据身份证号制证单个行驶证数据
     *
     * @return
     */
    @ApiOperation(value = "根据身份证号制证行驶证", notes = "")
    @RequestMapping(value = "/xsz/{ownerId}", method = RequestMethod.GET)
    @ResponseBody
    public String makeVehicleLicenseByOwnerId(@PathVariable String ownerId) {

        List<OdsJjXszzmxxZhzw> odsJjXszzmxxZhzws = new LambdaQueryChainWrapper<>(odsJjXszzmxxZhzwMapper)
                .isNotNull(OdsJjXszzmxxZhzw::getArchivesnum)
                .isNotNull(OdsJjXszzmxxZhzw::getInfovaliditybegin)
                .isNotNull(OdsJjXszzmxxZhzw::getInfovalidityend)
                .eq(OdsJjXszzmxxZhzw::getOwnerid, ownerId)
                .gt(OdsJjXszzmxxZhzw::getInfovalidityend, DateUtil.date())
                .orderByDesc(OdsJjXszzmxxZhzw::getModifydate)
                .list();
        log.info("行驶证制证身份证号为:{}", ownerId);
        atomicInteger.set(0);
        if (CollUtil.isNotEmpty(odsJjXszzmxxZhzws)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (odsJjXszzmxxZhzws.size() > atomicInteger.get()) {
                OdsJjXszzmxxZhzw odsJjXszzmxxZhzw = odsJjXszzmxxZhzws.get(atomicInteger.get());
                executor.execute(() -> {
                    try {
                        CertificateBaseBO certificateBaseBO = new CertificateBaseBO();
                        CertificateReqStrBO certificateReqStrBO = new CertificateReqStrBO();
                        certificateBaseBO.setContentCode("11340800003110803D0054");
                        certificateBaseBO.setOwnerId(odsJjXszzmxxZhzw.getOwnerid());
                        certificateBaseBO.setOwnerName(odsJjXszzmxxZhzw.getOwnername());
                        certificateBaseBO.setInfoCode("皖" + odsJjXszzmxxZhzw.getInfocode());
                        certificateBaseBO.setInfoValidityBegin(DateUtil.format(odsJjXszzmxxZhzw.getInfovaliditybegin(),
                                "yyyyMMdd"));
                        certificateBaseBO.setInfoValidityEnd(DateUtil.format(odsJjXszzmxxZhzw.getInfovalidityend(),
                                "yyyyMMdd"));
                        Map<String, String> dataMap = new HashMap<>();
                        // 照片	N
                        if (odsJjXszzmxxZhzw.getPhoto() != null
                                && odsJjXszzmxxZhzw.getPhoto().length > 0) {
                            dataMap.put("photo", Base64Tools.encodeBase64String(odsJjXszzmxxZhzw.getPhoto()));
                        } else {
                            dataMap.put("photo", "");
                        }
                        // 证照编号	Y
                        dataMap.put("infoCode", "皖" + odsJjXszzmxxZhzw.getInfocode());
                        // 车辆类型	Y
                        OdsJjFrmCodeZhzw odsJjFrmCodeZhzwCarType = new LambdaQueryChainWrapper<>(odsJjFrmCodeZhzwMapper)
                                .likeRight(OdsJjFrmCodeZhzw::getXtlb, "00")
                                .eq(OdsJjFrmCodeZhzw::getDmlb, "1004")
                                .eq(OdsJjFrmCodeZhzw::getDmz, odsJjXszzmxxZhzw.getCartype())
                                .orderByDesc(OdsJjFrmCodeZhzw::getXyptTimeDxp)
                                .last("limit 1")
                                .one();
                        if (odsJjFrmCodeZhzwCarType != null
                                && StringUtils.isNotEmpty(odsJjFrmCodeZhzwCarType.getDmsm1())) {
                            dataMap.put("carType", odsJjFrmCodeZhzwCarType.getDmsm1());
                        }
                        // 所有人	Y
                        dataMap.put("name", odsJjXszzmxxZhzw.getName());
                        // 地址	Y
                        dataMap.put("address", odsJjXszzmxxZhzw.getAddress());
                        // 使用性质	Y
                        OdsJjFrmCodeZhzw odsJjFrmCodeZhzwUseProperty = new LambdaQueryChainWrapper<>(odsJjFrmCodeZhzwMapper)
                                .likeRight(OdsJjFrmCodeZhzw::getXtlb, "00")
                                .eq(OdsJjFrmCodeZhzw::getDmlb, "1003")
                                .eq(OdsJjFrmCodeZhzw::getDmz, odsJjXszzmxxZhzw.getUseproperty())
                                .orderByDesc(OdsJjFrmCodeZhzw::getXyptTimeDxp)
                                .last("limit 1")
                                .one();
                        if (odsJjFrmCodeZhzwUseProperty != null
                                && StringUtils.isNotEmpty(odsJjFrmCodeZhzwUseProperty.getDmsm1())) {
                            dataMap.put("useProperty", odsJjFrmCodeZhzwUseProperty.getDmsm1());
                        }
                        // 品牌型号	Y
                        dataMap.put("brandModel", odsJjXszzmxxZhzw.getBrandmodel());
                        // 车辆识别代号	Y
                        dataMap.put("carcode", odsJjXszzmxxZhzw.getCarcode());
                        // 发动机号码	Y
                        dataMap.put("engineNumber", odsJjXszzmxxZhzw.getEnginenumber());
                        // 注册日期	Y
                        dataMap.put("registDate", DateUtil.format(odsJjXszzmxxZhzw.getRegistdate(),
                                "yyyy-MM-dd"));
                        // 发证日期	Y
                        dataMap.put("issueDate", DateUtil.format(odsJjXszzmxxZhzw.getIssuedate(),
                                "yyyy-MM-dd"));
                        // 号牌号码	Y
                        dataMap.put("carnumber", "皖" + odsJjXszzmxxZhzw.getCarnumber());
                        // 档案编号 N
                        dataMap.put("archivesNum", odsJjXszzmxxZhzw.getArchivesnum());
                        // 核定载人数    N
                        if (odsJjXszzmxxZhzw.getApprovedload() != null) {
                            dataMap.put("approvedLoad", odsJjXszzmxxZhzw.getApprovedload() + "人");
                        } else {
                            dataMap.put("approvedLoad", "- -");
                        }
                        // 总质量	N
                        if (odsJjXszzmxxZhzw.getTotalmass() != null) {
                            dataMap.put("totalMass", odsJjXszzmxxZhzw.getTotalmass() + "kg");
                        } else {
                            dataMap.put("totalMass", "- -");
                        }
                        // 整备质量	N
                        if (odsJjXszzmxxZhzw.getKerbmass() != null) {
                            dataMap.put("kerbMass", odsJjXszzmxxZhzw.getKerbmass() + "kg");
                        } else {
                            dataMap.put("kerbMass", "- -");
                        }
                        // 核定载质量	N
                        if (odsJjXszzmxxZhzw.getApprovedquality() != null) {
                            dataMap.put("approvedQuality", odsJjXszzmxxZhzw.getApprovedquality() + "kg");
                        } else {
                            dataMap.put("approvedQuality", "- -");
                        }
                        // 外廓尺寸	N
                        if (StringUtils.isNotEmpty(odsJjXszzmxxZhzw.getOverallsize())) {
                            dataMap.put("overallSize", odsJjXszzmxxZhzw.getOverallsize());
                        } else {
                            dataMap.put("overallSize", "- -");
                        }
                        // 准牵引总质量	N
                        if (odsJjXszzmxxZhzw.getTotal() != null) {
                            dataMap.put("total", odsJjXszzmxxZhzw.getTotal() + "kg");
                        } else {
                            dataMap.put("total", "- -");
                        }
                        // 备注	N
                        dataMap.put("note1", odsJjXszzmxxZhzw.getNote1());
                        // 检验记录	N
                        dataMap.put("recode", odsJjXszzmxxZhzw.getRecode());
                        // 记录	N
                        dataMap.put("notes2", odsJjXszzmxxZhzw.getNotes2());
                        // 燃料种类	N
                        OdsJjFrmCodeZhzw odsJjFrmCodeZhzwRlzl = new LambdaQueryChainWrapper<>(odsJjFrmCodeZhzwMapper)
                                .likeRight(OdsJjFrmCodeZhzw::getXtlb, "01")
                                .eq(OdsJjFrmCodeZhzw::getDmlb, "0009")
                                .eq(OdsJjFrmCodeZhzw::getDmz, odsJjXszzmxxZhzw.getRlzl())
                                .orderByDesc(OdsJjFrmCodeZhzw::getXyptTimeDxp)
                                .last("limit 1")
                                .one();
                        if (odsJjFrmCodeZhzwRlzl != null
                                && StringUtils.isNotEmpty(odsJjFrmCodeZhzwRlzl.getDmsm1())) {
                            dataMap.put("rlzl", odsJjFrmCodeZhzwRlzl.getDmsm1());
                        }
                        // 条形码	N
                        dataMap.put("barCode", odsJjXszzmxxZhzw.getBarcode());
                        // 条形码数字	N
                        dataMap.put("barNumber", odsJjXszzmxxZhzw.getBarnumber());
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
                atomicInteger.incrementAndGet();
            }
        }
        return "行驶证制证身份证号:" + ownerId + ";" + "数据总量为:" + odsJjXszzmxxZhzws.size() + "个;日期" + DateUtil.now();
    }

}
