package com.zfsoft.certificate.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.certificate.pojo.ZzWorkCertificate;
import com.zfsoft.certificate.pojo.ZzWorkCertificateLogs;
import com.zfsoft.certificate.service.ZzWorkCertificateLogsService;
import com.zfsoft.certificate.service.ZzWorkCertificateService;
import com.zfsoft.certificate.util.ConstantUtils;
import com.zfsoft.certificate.util.StringUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

/**
 * @Classname: ZzWorkController
 * @Description: 制证相关后台
 * @Date: 2021/6/10 15:46
 * @author: wwf
 */
@Api(tags = "证照制证后台")
@ApiSort(value = 1)
@Controller
@Slf4j
@RequestMapping(value = "/zzWork")
public class ZzWorkController {

    @Autowired
    private ZzWorkCertificateService zzWorkCertificateService;

    @Autowired
    private ZzWorkCertificateLogsService zzWorkCertificateLogsService;

    /**
     * @Description: http://127.0.0.1:8080/zzWork/zzmake/
     **/
    @ApiOperation(value = "证照制证盖章接口", notes = "")
    @ApiOperationSupport(order = 1)
    @ApiImplicitParam(paramType = "body", name = "json", value = "制证JSON参数", required = true)
    @RequestMapping(value = {"/zzmake/"}, method = {RequestMethod.POST}, consumes = {"application/json;charset=utf-8"})
    @ResponseBody
    public Object zzMake(@RequestBody JSONObject json) {

        long start = DateUtil.currentSeconds();
        JSONObject result = new JSONObject();
        ZzWorkCertificateLogs zzWorkCertificateLogs = new ZzWorkCertificateLogs();
        try {
            Map<String, String> map = zzWorkCertificateService.zzMake(json);
            JSONObject dataJson = new JSONObject();
            dataJson.put("code", map.get("code"));
            dataJson.put("id", map.get("id"));
            dataJson.put("attaOid", map.get("attaOid"));
            result.put("result", map.get("result"));
            result.put("flag", map.get("flag"));
            result.put("sealFlag", StringUtils.isEmpty(map.get("sealFlag")) ? "false" : map.get("sealFlag"));
            result.put("data", dataJson);
            if (StringUtils.isNotEmpty(map.get("id"))) {
                ZzWorkCertificate zzWorkCertificate = zzWorkCertificateService.getById(map.get("id"));
                zzWorkCertificateLogs.setAttaOid(zzWorkCertificate.getAttaOid());
                zzWorkCertificateLogs.setCerStatus(zzWorkCertificate.getCerStatus());
                zzWorkCertificateLogs.setCertificateCode(zzWorkCertificate.getCertificateCode());
                zzWorkCertificateLogs.setContentCode(zzWorkCertificate.getContentCode());
                zzWorkCertificateLogs.setContentName(zzWorkCertificate.getContentName());
                zzWorkCertificateLogs.setInfoCode(zzWorkCertificate.getInfoCode());
                zzWorkCertificateLogs.setInfoValidityBegin(zzWorkCertificate.getInfoValidityBegin());
                zzWorkCertificateLogs.setInfoValidityEnd(zzWorkCertificate.getInfoValidityEnd());
                zzWorkCertificateLogs.setOwnerId(zzWorkCertificate.getOwnerId());
                zzWorkCertificateLogs.setOwnerName(zzWorkCertificate.getOwnerName());
                // 获取讯飞上报接口返回值
                String apiResult = map.get("apiResult");
                if (StringUtils.isEmpty(apiResult)) {
                    // 代表上报失败
                    zzWorkCertificate.setPostFlag(ConstantUtils.constant.getZero());
                }
                zzWorkCertificateService.saveOrUpdate(zzWorkCertificate);
            }
        } catch (Exception e) {
            log.error("接口zzmake异常{}", e);
            result.put("result", "系统出现异常！");
            result.put("flag", "false");
            result.put("sealFlag", "false");
        }
        zzWorkCertificateLogs.setFlag(result.getString("flag"));
        zzWorkCertificateLogs.setResult(result.getString("result"));
        zzWorkCertificateLogs.setType("制证");
        zzWorkCertificateLogs.setModifyDate(new Date());
        try {
            zzWorkCertificateLogsService.saveOrUpdate(zzWorkCertificateLogs);
        } catch (Exception e) {
            log.error("接口zzmake保存日志异常{}", e.getMessage());
        }
        System.out.println("制证总计的时间：" + (DateUtil.currentSeconds() - start));
        return result;

    }


}
