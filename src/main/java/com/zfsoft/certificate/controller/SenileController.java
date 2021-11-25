package com.zfsoft.certificate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.zfsoft.certificate.mapper.db2.SenileEntityMapper;
import com.zfsoft.certificate.pojo.SenileEntity;
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
 * 老年证制证
 * @author 86131
 */
@Api(tags = "老年证制证后台")
@Controller
@Slf4j
public class SenileController {

    @Autowired
    private Constant constant;

    /**
     * 线程计数器
     */
    public static AtomicInteger atomicInteger = new AtomicInteger();

    @Autowired
    private SenileEntityMapper senileEntityMapper;


    /**
     * 老年证
     *
     * @return
     */
    @ApiOperation(value = "老年证制证", notes = "根据配置文件查询分页")
    @RequestMapping(value = "/lnz", method = RequestMethod.GET)
    @ResponseBody
    public String makeSenile() {

        /**
         * SELECT * FROM senile where flag = '1' ORDER BY bh DESC
         */
        List<SenileEntity> senileEntityList = new LambdaQueryChainWrapper<>(senileEntityMapper)
                .eq(SenileEntity::getFlag, "1")
                .orderByDesc(SenileEntity::getBh)
                .list();
        log.info("户口簿任务开始总量为:{}个", senileEntityList.size());
        atomicInteger.set(0);
        if (CollUtil.isNotEmpty(senileEntityList)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (senileEntityList.size() > atomicInteger.get()) {
                SenileEntity senileEntity = senileEntityList.get(atomicInteger.get());
                executor.execute(() -> {
                    try {
                        CertificateBaseBO certificateBaseBO = new CertificateBaseBO();
                        CertificateReqStrBO certificateReqStrBO = new CertificateReqStrBO();
                        if (senileEntity.getType() != null) {
                            if (constant.getLnzSixty().equals(senileEntity.getType().substring(0, 2).trim())) {
                                certificateBaseBO.setContentCode("11340800335629046P0184");
                                certificateReqStrBO.setOrgCode("340800");
                                certificateReqStrBO.setOrgName("安庆市卫生和计划生育委员会");
                            } else if (constant.getLnzSixtyFive().equals(senileEntity.getType().substring(0, 2).trim())) {
                                certificateBaseBO.setContentCode("11340800335629046P0185");
                                certificateReqStrBO.setOrgCode("340800");
                                certificateReqStrBO.setOrgName("安庆市卫生和计划生育委员会");
                            } else if (constant.getLnzSeventy().equals(senileEntity.getType().substring(0, 2).trim())) {
                                certificateBaseBO.setContentCode("11340800335629046P0186");
                                certificateReqStrBO.setOrgCode("340800");
                                certificateReqStrBO.setOrgName("安庆市卫生和计划生育委员会");
                            } else {
                                certificateBaseBO.setContentCode("");
                                certificateReqStrBO.setOrgCode("");
                                certificateReqStrBO.setOrgName("");
                            }
                        }

                        certificateBaseBO.setOwnerId(senileEntity.getSfzh());
                        certificateBaseBO.setOwnerName(senileEntity.getXm());
                        certificateBaseBO.setInfoCode(senileEntity.getBh());
                        certificateBaseBO.setInfoValidityBegin("20201226");
                        certificateBaseBO.setInfoValidityEnd(constant.getInfoValidityEnd());
                        Map<String, String> dataMap = new HashMap<>();
                        dataMap.put("photo", "");
                        dataMap.put("bh", StringUtils.handleString(senileEntity.getBh()));
                        dataMap.put("xm", StringUtils.handleString(senileEntity.getXm()));
                        dataMap.put("xb", StringUtils.handleString(senileEntity.getXb()));
                        dataMap.put("mz", StringUtils.handleString(senileEntity.getMz()));
                        dataMap.put("sfzh", StringUtils.handleString(senileEntity.getSfzh()));
                        dataMap.put("csrq", StringUtils.handleString(senileEntity.getCsrq()));
                        dataMap.put("xx", "");
                        dataMap.put("jzdz", StringUtils.handleString(senileEntity.getFzjg() + senileEntity.getJzdz()));
                        dataMap.put("jjlxr", "");
                        dataMap.put("lxdh", "");
                        dataMap.put("bz", "");
                        dataMap.put("fzjg", StringUtils.handleString(senileEntity.getFzjg() + "卫建委"));
                        dataMap.put("fzrq", "2020年12月26日");
                        dataMap.put("ewm", "");
                        certificateBaseBO.setData(dataMap);
                        certificateReqStrBO.setUserName("安庆市数据资源局");
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
                            boolean row = new LambdaUpdateChainWrapper<>(senileEntityMapper)
                                    .eq(SenileEntity::getBh, senileEntity.getBh())
                                    .set(SenileEntity::getFlag, "0")
                                    .update();
                            log.info("更新数据结果：{}", row);
                        }
                    } catch (Exception e) {
                        log.error("制证异常失败:{}", e.getMessage());
                    }
                });
                atomicInteger.incrementAndGet();
            }
        }
        return "老年证制证:" + senileEntityList.size() + ":" + DateUtil.now();
    }

}
