package com.zfsoft.certificate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.zfsoft.certificate.config.ServerConfig;
import com.zfsoft.certificate.pojo.SysAtta;
import com.zfsoft.certificate.pojo.ZzWorkCertificate;
import com.zfsoft.certificate.pojo.ZzWorkContent;
import com.zfsoft.certificate.pojo.ZzWorkStyle;
import com.zfsoft.certificate.pojo.base.Constant;
import com.zfsoft.certificate.service.SysAttaService;
import com.zfsoft.certificate.service.ZzWorkCertificateService;
import com.zfsoft.certificate.service.ZzWorkContentService;
import com.zfsoft.certificate.service.ZzWorkStyleService;
import com.zfsoft.certificate.util.DownloadUtil;
import com.zfsoft.certificate.util.FileUtils;
import com.zfsoft.certificate.util.HttpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 盖章
 *
 * @author 86131
 */
@Api(tags = "盖章后台")
@Slf4j
@Controller
public class MakeSealController {

    @Autowired
    private Constant constant;

    /**
     * 消费线程计数器
     */
    public static AtomicInteger tashCounter = new AtomicInteger();

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private ZzWorkContentService zzWorkContentService;

    @Autowired
    private ZzWorkStyleService zzWorkStyleService;

    @Autowired
    private ZzWorkCertificateService zzWorkCertificateService;

    @Autowired
    private SysAttaService sysAttaService;

    /**
     * 盖章
     * 未盖章的证照和之前盖章失败的证照：cer_status=0,9
     *
     * @param code 证照编码
     * @return
     */
    @ApiOperation(value = "根据证照类型编码对未盖章或盖章失败的证照进行盖章", notes = "")
    @ApiImplicitParam(paramType = "path", name = "code", value = "证照类型编码", required = true, dataType = "String")
    @GetMapping("/seal/{code}")
    @ResponseBody
    public String seal(@PathVariable(name = "code") String code) {

        if (StrUtil.isNotEmpty(code)) {
            log.info("++++++++++盖章开始++++++++++");
            ZzWorkContent zzWorkContent = zzWorkContentService.getZzWorkContentByCode(code.trim());
            if (zzWorkContent != null) {
                String sealCodeFirst = zzWorkContent.getSealCodeFirst();
                String sealCodeSecond = zzWorkContent.getSealCodeSecond();
                String sealCodeThird = zzWorkContent.getSealCodeThird();
                // 如果两个印章编号都为空则无法盖章
                if (StrUtil.isNotEmpty(sealCodeFirst) || StrUtil.isNotEmpty(sealCodeSecond)) {
                    if (StrUtil.isNotEmpty(zzWorkContent.getStyleId())) {
                        ZzWorkStyle zzWorkStyle = zzWorkStyleService.getZzWorkStyleById(zzWorkContent.getStyleId());
                        if (zzWorkStyle != null) {
                            String ruleInfoFirst = zzWorkStyle.getRuleInfoFirst();
                            String ruleInfoSecond = zzWorkStyle.getRuleInfoSecond();
                            String ruleInfoThird = zzWorkStyle.getRuleInfoThird();
                            // 如果两个印章编位置都为空则无法盖章
                            if (StrUtil.isNotEmpty(ruleInfoFirst) || StrUtil.isNotEmpty(ruleInfoSecond)) {
                                List<ZzWorkCertificate> zzWorkCertificateList = zzWorkCertificateService.getZzWorkCertificateByContentCode(code.trim());
                                log.info("查询到需要盖章的数据总量:{}个", zzWorkCertificateList.size());
                                tashCounter.set(0);
                                if (CollUtil.isNotEmpty(zzWorkCertificateList)) {
                                    ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                                            constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
                                    while (zzWorkCertificateList.size() > tashCounter.get()) {
                                        ZzWorkCertificate zzWorkCertificate = zzWorkCertificateList.get(tashCounter.get());
                                        executor.execute(() -> {
                                            try {
                                                if (StrUtil.isNotEmpty(zzWorkCertificate.getAttaOid())) {
                                                    SysAtta sysAtta = sysAttaService.getSysAttaByOid(zzWorkCertificate.getAttaOid());
                                                    if (sysAtta != null) {
                                                        if (StrUtil.isNotEmpty(sysAtta.getFilePath())) {
                                                            String resultPath = constant.getMakeDiskZ() + sysAtta.getFilePath() + sysAtta.getName();
                                                            File file = new File(resultPath);
                                                            FileInputStream fileInputStream = new FileInputStream(file);
                                                            byte[] buffer = new byte[(int) file.length()];
                                                            fileInputStream.read(buffer);
                                                            fileInputStream.close();
                                                            String modelName = Base64.encodeBase64String(buffer);
                                                            Map<String, String> xmlStr = new HashMap<>();
                                                            xmlStr.put("xmlStr", DownloadUtil.sealAutoPdfZF(modelName, sysAtta.getName(),
                                                                    sealCodeFirst, sealCodeSecond, sealCodeThird,
                                                                    ruleInfoFirst, ruleInfoSecond, ruleInfoThird));
                                                            String result = HttpUtils.post(constant.getMakeSealUrl(), xmlStr);
                                                            // 请求响应解析--xml
                                                            boolean success = DownloadUtil.getXmlAttribute(result, resultPath);
                                                            log.info("端口号:{}-证照编码:{}", serverConfig.getPort(), code);
                                                            log.info("点聚盖章结果:{}-{}", success, zzWorkCertificate.getId());
                                                            // 如果盖章成功且覆盖掉源文件，则证照状态改成6
                                                            if (success) {
                                                                zzWorkCertificateService.updateCerStatusById(zzWorkCertificate.getId(), "6");
                                                            } else {
                                                                zzWorkCertificateService.updateCerStatusById(zzWorkCertificate.getId(), "9");
                                                            }
                                                        } else {
                                                            log.info("附件表中证照路径filePath为空！");
                                                        }
                                                    } else {
                                                        log.info("通过证照数据附件主键查询附件为空！");
                                                    }
                                                } else {
                                                    log.info("通过证照数据获取的证照附件主键为空！");
                                                }
                                            } catch (Exception e) {
                                                log.error("盖章失败:{}", e.getMessage());
                                            }
                                        });
                                        tashCounter.incrementAndGet();
                                    }
                                    log.info("++++++++++盖章结束++++++++++");
                                } else {
                                    log.info("查询到需要盖章的数据为空！");
                                }
                            } else {
                                log.info("两个印章位置都为空则无法盖章！");
                            }
                        } else {
                            log.info("通过证照id查询证照类型(ZzWorkStyle)为空");
                        }
                    } else {
                        log.info("通过证照目录(ZzWorkContent)中的styleId为空！");
                    }
                } else {
                    log.info("两个印章编号都为空则无法盖章！");
                }
            } else {
                log.info("通过证照编码查询证照目录(ZzWorkContent)为空！");
            }
        }
        return "盖章类型：" + code + ":" + DateUtil.now();
    }

}
