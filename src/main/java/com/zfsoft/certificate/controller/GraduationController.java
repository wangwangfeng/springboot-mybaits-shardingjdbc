package com.zfsoft.certificate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.zfsoft.certificate.mapper.db5.OdsJtjBysMapper;
import com.zfsoft.certificate.pojo.OdsJtjBys;
import com.zfsoft.certificate.pojo.base.CertificateBaseBO;
import com.zfsoft.certificate.pojo.base.CertificateReqStrBO;
import com.zfsoft.certificate.pojo.base.Constant;
import com.zfsoft.certificate.util.IdcardUtil;
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
 * @Classname: GraduationController
 * @Description: 毕业证书制证
 * @Date: 2021/6/9 17:19
 * @author: wwf
 */
@Api(tags = "毕业证书制证后台")
@Controller
@Slf4j
public class GraduationController {

    @Autowired
    private Constant constant;

    /**
     * 消费线程计数器
     */
    public static AtomicInteger tashCounter = new AtomicInteger();

    @Autowired
    private OdsJtjBysMapper odsJtjBysMapper;

    /**
     * @Description: 毕业证书制证
     * 全部盖章：安庆市教育体育局章(印章编码：34080000080955)
     * @Date: 2021/6/10 9:09
     * @author: wwf
     * @param:
     * @return: java.lang.String
     **/
    @ApiOperation(value = "毕业证书制证", notes = "")
    @RequestMapping(value = "/byzs", method = RequestMethod.GET)
    @ResponseBody
    public String makeGraduation() {

        List<OdsJtjBys> odsJtjBysList = new LambdaQueryChainWrapper<>(odsJtjBysMapper)
                .isNotNull(OdsJtjBys::getBysbh)
                .isNotNull(OdsJtjBys::getXm)
                .orderByDesc(OdsJtjBys::getBysbh)
                .last("limit " + constant.getLimitBegin() + "," + constant.getLimitEnd())
                .list();
        log.info("毕业证书制证开始总量为:{}个", odsJtjBysList.size());
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(odsJtjBysList)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (odsJtjBysList.size() > tashCounter.get()) {
                OdsJtjBys odsJtjBys = odsJtjBysList.get(tashCounter.get());
                log.info("数据身份证号:{}", odsJtjBys.getSfzh());
                executor.execute(() -> {
                    try {
                        CertificateBaseBO certificateBaseBO = new CertificateBaseBO();
                        CertificateReqStrBO certificateReqStrBO = new CertificateReqStrBO();
                        certificateBaseBO.setContentCode("1134080000310989690008");
                        certificateBaseBO.setOwnerId(odsJtjBys.getSfzh());
                        certificateBaseBO.setOwnerName(odsJtjBys.getXm());
                        certificateBaseBO.setInfoCode(odsJtjBys.getBysbh());
                        certificateBaseBO.setInfoValidityBegin(odsJtjBys.getByn()
                                + odsJtjBys.getByy() + "30");
                        certificateBaseBO.setInfoValidityEnd(constant.getInfoValidityEnd());
                        Map<String, String> dataMap = new HashMap<>();
                        dataMap.put("photo", "");
                        // 姓名
                        dataMap.put("xm", odsJtjBys.getXm());
                        // 性别
                        dataMap.put("xb", odsJtjBys.getXb());
                        // 出生年
                        dataMap.put("csn", IdcardUtil.getYearByIdCard(odsJtjBys.getSfzh()));
                        // 出生月
                        dataMap.put("csy", IdcardUtil.getMonthByIdCard(odsJtjBys.getSfzh()));
                        // 入学年
                        dataMap.put("rxn", odsJtjBys.getRxn());
                        // 入学月
                        dataMap.put("rxy", odsJtjBys.getRxy());
                        // 毕业年
                        dataMap.put("byn", odsJtjBys.getByn());
                        // 毕业月
                        dataMap.put("byr", odsJtjBys.getByy());
                        // 校长
                        dataMap.put("xz", "");
                        // 年
                        dataMap.put("n", odsJtjBys.getByn());
                        // 月
                        dataMap.put("y", odsJtjBys.getByy());
                        // 日
                        dataMap.put("r", "30");
                        // 证书编号
                        dataMap.put("no", odsJtjBys.getBysbh());
                        // 校名
                        dataMap.put("xm1", odsJtjBys.getXx());
                        // 毕业证字号
                        dataMap.put("zh", odsJtjBys.getByn());
                        // 毕业证第几号
                        dataMap.put("djh", odsJtjBys.getBysbh());
                        // 学校名称
                        dataMap.put("xxmc", odsJtjBys.getXx());
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
        return "毕业证书制证:" + odsJtjBysList.size() + ":" + DateUtil.now();
    }

}
