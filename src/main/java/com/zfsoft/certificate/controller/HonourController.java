package com.zfsoft.certificate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.zfsoft.certificate.pojo.HonourEntity;
import com.zfsoft.certificate.pojo.NationEntity;
import com.zfsoft.certificate.pojo.base.CertificateBaseBO;
import com.zfsoft.certificate.pojo.base.CertificateReqStrBO;
import com.zfsoft.certificate.pojo.base.Constant;
import com.zfsoft.certificate.service.HonourEntityService;
import com.zfsoft.certificate.service.NationEntityService;
import com.zfsoft.certificate.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 独生子女光荣证
 *
 * @author 86131
 */
@Api(tags = "独生子女光荣证制证后台")
@Controller
@Slf4j
public class HonourController {

    @Autowired
    private Constant constant;

    /**
     * 消费线程计数器
     */
    public static AtomicInteger tashCounter = new AtomicInteger();

    @Autowired
    private HonourEntityService honourEntityService;

    @Autowired
    private NationEntityService nationEntityService;


    /**
     * 独生子女光荣证
     *
     * @return
     */
    @ApiOperation(value = "独生子女光荣证制证", notes = "")
    @ApiImplicitParam(paramType = "path", name = "type", value = "性别英文", required = true, dataType = "String")
    @RequestMapping(value = "/grz/{type}", method = RequestMethod.GET)
    @ResponseBody
    public String makeHonour(@PathVariable(name = "type") String type) {

        List<HonourEntity> honourEntityList = new ArrayList<>();
        if (StrUtil.isNotEmpty(type)) {
            if (constant.getWoman().equals(type)) {
                honourEntityList = honourEntityService.findAll();
            } else if (constant.getMan().equals(type)) {
                honourEntityList = honourEntityService.findAllPoXm();
            }
        } else {
            honourEntityList = honourEntityService.findAll();
        }
        log.info("光荣证任务开始总量为:{}个", honourEntityList.size());
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(honourEntityList)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (honourEntityList.size() > tashCounter.get()) {
                HonourEntity entity = honourEntityList.get(tashCounter.get());
                executor.execute(() -> {
                    try {
                        CertificateBaseBO certificateBaseBO = new CertificateBaseBO();
                        CertificateReqStrBO certificateReqStrBO = new CertificateReqStrBO();
                        // 根据男方身份证号截取前6位判断区划，为空则根据女方身份证号判断
                        String posfz = entity.getPosfz();
                        String sfz = entity.getSfz();
                        if (StrUtil.isNotEmpty(posfz)) {
                            if (constant.getAqZoneCode().equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340800335629046P0003");
                                certificateReqStrBO.setOrgCode(constant.getAqZoneCode());
                                certificateReqStrBO.setOrgName("安庆市卫生和计划生育委员会");
                            } else if (constant.getWjxZoneCode().equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340827358583060J0117");
                                certificateReqStrBO.setOrgCode(constant.getWjxZoneCode());
                                certificateReqStrBO.setOrgName("望江县卫生和计划生育委员会");
                            } else if (constant.getThxZoneCode().equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340825358594333U0117");
                                certificateReqStrBO.setOrgCode(constant.getThxZoneCode());
                                certificateReqStrBO.setOrgName("太湖县卫生和计划生育委员会");
                            } else if (constant.getHnxZoneCode().equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340822003123436C0117");
                                certificateReqStrBO.setOrgCode(constant.getHnxZoneCode());
                                certificateReqStrBO.setOrgName("怀宁县卫生和计划生育委员会");
                            } else if (constant.getYjqZoneCode().equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340802348747527A0117");
                                certificateReqStrBO.setOrgCode(constant.getYjqZoneCode());
                                certificateReqStrBO.setOrgName("安庆市迎江区卫生和计划生育委员会");
                            } else if (constant.getYxqZoneCode().equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340811343864046B0117");
                                certificateReqStrBO.setOrgCode(constant.getYxqZoneCode());
                                certificateReqStrBO.setOrgName("安庆市宜秀区卫生和计划生育委员会");
                            } else if (constant.getQssZoneCode().equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340824MB0X9278560117");
                                certificateReqStrBO.setOrgCode(constant.getQssZoneCode());
                                certificateReqStrBO.setOrgName("潜山县卫生和计划生育委员会");
                            } else if ("342824".equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340824MB0X9278560117");
                                certificateReqStrBO.setOrgCode("342824");
                                certificateReqStrBO.setOrgName("潜山县卫生和计划生育委员会");
                            } else if (constant.getQssZoneCodeOne().equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340824MB0X9278560117");
                                certificateReqStrBO.setOrgCode(constant.getQssZoneCodeOne());
                                certificateReqStrBO.setOrgName("潜山县卫生和计划生育委员会");
                            } else if (constant.getSsxZoneCode().equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340826MB0W17012E0117");
                                certificateReqStrBO.setOrgCode(constant.getSsxZoneCode());
                                certificateReqStrBO.setOrgName("宿松县卫生和计划生育委员会");
                            } else if (constant.getYxxZoneCode().equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("1134082835515747XB0117");
                                certificateReqStrBO.setOrgCode(constant.getYxxZoneCode());
                                certificateReqStrBO.setOrgName("岳西县卫生和计划生育委员会");
                            } else if (constant.getDgqZoneCode().equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340803343818912J0117");
                                certificateReqStrBO.setOrgCode(constant.getDgqZoneCode());
                                certificateReqStrBO.setOrgName("安庆市大观区卫生和计划生育委员会");
                            } else if (constant.getTcsZoneCode().equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("1134088135858218090117");
                                certificateReqStrBO.setOrgCode(constant.getTcsZoneCode());
                                certificateReqStrBO.setOrgName("桐城市卫生和计划生育委员会");
                            } else {
                                // 所有查不到的放到安庆市
                                certificateBaseBO.setContentCode("11340800335629046P0003");
                                certificateReqStrBO.setOrgCode(constant.getAqZoneCode());
                                certificateReqStrBO.setOrgName("安庆市卫生和计划生育委员会");
                            }
                        } else {
                            if (constant.getAqZoneCode().equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340800335629046P0003");
                                certificateReqStrBO.setOrgCode(constant.getAqZoneCode());
                                certificateReqStrBO.setOrgName("安庆市卫生和计划生育委员会");
                            } else if (constant.getWjxZoneCode().equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340827358583060J0117");
                                certificateReqStrBO.setOrgCode(constant.getWjxZoneCode());
                                certificateReqStrBO.setOrgName("望江县卫生和计划生育委员会");
                            } else if (constant.getThxZoneCode().equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340825358594333U0117");
                                certificateReqStrBO.setOrgCode(constant.getThxZoneCode());
                                certificateReqStrBO.setOrgName("太湖县卫生和计划生育委员会");
                            } else if (constant.getHnxZoneCode().equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340822003123436C0117");
                                certificateReqStrBO.setOrgCode(constant.getHnxZoneCode());
                                certificateReqStrBO.setOrgName("怀宁县卫生和计划生育委员会");
                            } else if (constant.getYjqZoneCode().equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340802348747527A0117");
                                certificateReqStrBO.setOrgCode(constant.getYjqZoneCode());
                                certificateReqStrBO.setOrgName("安庆市迎江区卫生和计划生育委员会");
                            } else if (constant.getYxqZoneCode().equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340811343864046B0117");
                                certificateReqStrBO.setOrgCode(constant.getYxqZoneCode());
                                certificateReqStrBO.setOrgName("安庆市宜秀区卫生和计划生育委员会");
                            } else if (constant.getQssZoneCode().equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340824MB0X9278560117");
                                certificateReqStrBO.setOrgCode(constant.getQssZoneCode());
                                certificateReqStrBO.setOrgName("潜山县卫生和计划生育委员会");
                            } else if ("342824".equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340824MB0X9278560117");
                                certificateReqStrBO.setOrgCode("342824");
                                certificateReqStrBO.setOrgName("潜山县卫生和计划生育委员会");
                            } else if (constant.getQssZoneCodeOne().equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340824MB0X9278560117");
                                certificateReqStrBO.setOrgCode(constant.getQssZoneCodeOne());
                                certificateReqStrBO.setOrgName("潜山县卫生和计划生育委员会");
                            } else if (constant.getSsxZoneCode().equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340826MB0W17012E0117");
                                certificateReqStrBO.setOrgCode(constant.getSsxZoneCode());
                                certificateReqStrBO.setOrgName("宿松县卫生和计划生育委员会");
                            } else if (constant.getYxxZoneCode().equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("1134082835515747XB0117");
                                certificateReqStrBO.setOrgCode(constant.getYxxZoneCode());
                                certificateReqStrBO.setOrgName("岳西县卫生和计划生育委员会");
                            } else if (constant.getDgqZoneCode().equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340803343818912J0117");
                                certificateReqStrBO.setOrgCode(constant.getDgqZoneCode());
                                certificateReqStrBO.setOrgName("安庆市大观区卫生和计划生育委员会");
                            } else if (constant.getTcsZoneCode().equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("1134088135858218090117");
                                certificateReqStrBO.setOrgCode(constant.getTcsZoneCode());
                                certificateReqStrBO.setOrgName("桐城市卫生和计划生育委员会");
                            } else {
                                // 所有查不到的放到安庆市
                                certificateBaseBO.setContentCode("11340800335629046P0003");
                                certificateReqStrBO.setOrgCode(constant.getAqZoneCode());
                                certificateReqStrBO.setOrgName("安庆市卫生和计划生育委员会");
                            }
                        }

                        certificateBaseBO.setOwnerId(StringUtils.handleString(entity.getSfz()));
                        certificateBaseBO.setOwnerName(StringUtils.handleString(entity.getCzr()));
                        certificateBaseBO.setInfoCode(StringUtils.handleString(entity.getGrzh()));
                        if (StrUtil.isNotEmpty(entity.getFzrq())) {
                            certificateBaseBO.setInfoValidityBegin(entity.getFzrq().replaceAll("-", ""));
                        } else {
                            certificateBaseBO.setInfoValidityBegin("20000311");
                        }
                        certificateBaseBO.setInfoValidityEnd(constant.getInfoValidityEnd());
                        Map<String, String> dataMap = new HashMap<>();
                        dataMap.put("photo", "");
                        // 发证单位
                        dataMap.put("fzdw", StringUtils.handleString(entity.getFzdw()));
                        // 发证日期
                        if (StrUtil.isNotEmpty(entity.getFzrq())) {
                            String fzrq = entity.getFzrq().replaceAll("-", "");
                            // 发证日期年
                            dataMap.put("fzrq_n", fzrq.substring(0, 4));
                            // 发证日期_月
                            dataMap.put("fzrq_y", fzrq.substring(4, 6));
                            // 发证日期_日
                            dataMap.put("fzrq_r", fzrq.substring(6, 8));
                        } else {
                            dataMap.put("fzrq_n", "2000");
                            // 发证日期_月
                            dataMap.put("fzrq_y", "03");
                            // 发证日期_日
                            dataMap.put("fzrq_r", "11");
                        }
                        // 独生子女姓名
                        dataMap.put("name", StringUtils.handleString(entity.getXm1()));
                        // 编号
                        dataMap.put("infoCode", StringUtils.handleString(entity.getGrzh()));
                        // 性别
                        if (StrUtil.isNotEmpty(entity.getXb1())) {
                            if ("1".equals(entity.getXb1().trim())) {
                                dataMap.put("gender", "男");
                            } else if ("2".equals(entity.getXb1().trim())) {
                                dataMap.put("gender", "女");
                            }
                        } else {
                            dataMap.put("gender", "");
                        }
                        // 民族(需要查公安民族表)
                        if (StrUtil.isNotEmpty(entity.getMz())) {
                            NationEntity nationEntity = nationEntityService.getNationEntityByCode(entity.getMz());
                            if (nationEntity != null) {
                                dataMap.put("people", nationEntity.getMzmc());
                            } else {
                                dataMap.put("people", "");
                            }
                        } else {
                            dataMap.put("people", "");
                        }
                        // 出生日期
                        if (StrUtil.isNotEmpty(entity.getCsrq())) {
                            String csrq = entity.getCsrq().replaceAll("-", "");
                            // 出生_年
                            dataMap.put("csn", csrq.substring(0, 4));
                            // 出生_月
                            dataMap.put("csy", csrq.substring(4, 6));
                            // 出生_日
                            dataMap.put("csr", csrq.substring(6, 8));
                        } else {
                            dataMap.put("csn", "");
                            dataMap.put("csy", "");
                            dataMap.put("csr", "");
                        }
                        // 母亲姓名
                        dataMap.put("mname", StringUtils.handleString(entity.getXm()));
                        // 母亲工作单位
                        dataMap.put("mdw", StringUtils.handleString(entity.getFwcs()));
                        // 父亲姓名
                        dataMap.put("fname", StringUtils.handleString(entity.getPoxm()));
                        // 父亲工作单位
                        dataMap.put("fdw", StringUtils.handleString(entity.getFwcs1()));
                        // 家庭住址
                        dataMap.put("address", StringUtils.handleString(entity.getXjd()));
                        // 持证人
                        dataMap.put("czr", StringUtils.handleString(entity.getCzr()));
                        // 持证人性别
                        if (StrUtil.isNotEmpty(entity.getXb())) {
                            if ("1".equals(entity.getXb().trim())) {
                                dataMap.put("czrxb", "男");
                            } else if ("2".equals(entity.getXb().trim())) {
                                dataMap.put("czrxb", "女");
                            }
                        } else {
                            dataMap.put("czrxb", "");
                        }
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
                    } catch (Exception e) {
                        log.error("制证异常失败:{}", e.getMessage());
                    }
                });
                tashCounter.incrementAndGet();
            }
        }

        return "光荣证制证:" + honourEntityList.size() + ":" + DateUtil.now();
    }

    /**
     * 独生子女光荣证
     * czr!=xm并且xb=男
     * 需要更换制证男女对应的相关字段内容
     *
     * @return
     */
    @ApiOperation(value = "独生子女光荣证制证接口二", notes = "czr!=xm并且xb=男")
    @RequestMapping(value = "/grz/one", method = RequestMethod.GET)
    @ResponseBody
    public String makeHonourOne() {

        List<HonourEntity> honourEntityList = honourEntityService.findAllPoXmMan();
        log.info("光荣证任务开始总量为:{}个", honourEntityList.size());
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(honourEntityList)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (honourEntityList.size() > tashCounter.get()) {
                HonourEntity entity = honourEntityList.get(tashCounter.get());
                executor.execute(() -> {
                    try {
                        CertificateBaseBO certificateBaseBO = new CertificateBaseBO();
                        CertificateReqStrBO certificateReqStrBO = new CertificateReqStrBO();
                        // 根据男方身份证号截取前6位判断区划，为空则根据女方身份证号判断
                        // 由于本身数据男女反了
                        String posfz = entity.getSfz();
                        String sfz = entity.getPosfz();
                        if (StrUtil.isNotEmpty(posfz)) {
                            if ("340800".equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340800335629046P0003");
                                certificateReqStrBO.setOrgCode("340800");
                                certificateReqStrBO.setOrgName("安庆市卫生和计划生育委员会");
                            } else if ("340827".equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340827358583060J0117");
                                certificateReqStrBO.setOrgCode("340827");
                                certificateReqStrBO.setOrgName("望江县卫生和计划生育委员会");
                            } else if ("340825".equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340825358594333U0117");
                                certificateReqStrBO.setOrgCode("340825");
                                certificateReqStrBO.setOrgName("太湖县卫生和计划生育委员会");
                            } else if ("340822".equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340822003123436C0117");
                                certificateReqStrBO.setOrgCode("340822");
                                certificateReqStrBO.setOrgName("怀宁县卫生和计划生育委员会");
                            } else if ("340802".equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340802348747527A0117");
                                certificateReqStrBO.setOrgCode("340802");
                                certificateReqStrBO.setOrgName("安庆市迎江区卫生和计划生育委员会");
                            } else if ("340811".equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340811343864046B0117");
                                certificateReqStrBO.setOrgCode("340811");
                                certificateReqStrBO.setOrgName("安庆市宜秀区卫生和计划生育委员会");
                            } else if ("340824".equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340824MB0X9278560117");
                                certificateReqStrBO.setOrgCode("340824");
                                certificateReqStrBO.setOrgName("潜山县卫生和计划生育委员会");
                            } else if ("342824".equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340824MB0X9278560117");
                                certificateReqStrBO.setOrgCode("342824");
                                certificateReqStrBO.setOrgName("潜山县卫生和计划生育委员会");
                            } else if ("340882".equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340824MB0X9278560117");
                                certificateReqStrBO.setOrgCode("340882");
                                certificateReqStrBO.setOrgName("潜山县卫生和计划生育委员会");
                            } else if ("340826".equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340826MB0W17012E0117");
                                certificateReqStrBO.setOrgCode("340826");
                                certificateReqStrBO.setOrgName("宿松县卫生和计划生育委员会");
                            } else if ("340828".equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("1134082835515747XB0117");
                                certificateReqStrBO.setOrgCode("340828");
                                certificateReqStrBO.setOrgName("岳西县卫生和计划生育委员会");
                            } else if ("340803".equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340803343818912J0117");
                                certificateReqStrBO.setOrgCode("340803");
                                certificateReqStrBO.setOrgName("安庆市大观区卫生和计划生育委员会");
                            } else if ("340881".equals(posfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("1134088135858218090117");
                                certificateReqStrBO.setOrgCode("340881");
                                certificateReqStrBO.setOrgName("桐城市卫生和计划生育委员会");
                            } else {
                                certificateBaseBO.setContentCode("");
                                certificateReqStrBO.setOrgCode("");
                                certificateReqStrBO.setOrgName("");
                            }
                        } else {
                            if ("340800".equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340800335629046P0003");
                                certificateReqStrBO.setOrgCode("340800");
                                certificateReqStrBO.setOrgName("安庆市卫生和计划生育委员会");
                            } else if ("340827".equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340827358583060J0117");
                                certificateReqStrBO.setOrgCode("340827");
                                certificateReqStrBO.setOrgName("望江县卫生和计划生育委员会");
                            } else if ("340825".equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340825358594333U0117");
                                certificateReqStrBO.setOrgCode("340825");
                                certificateReqStrBO.setOrgName("太湖县卫生和计划生育委员会");
                            } else if ("340822".equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340822003123436C0117");
                                certificateReqStrBO.setOrgCode("340822");
                                certificateReqStrBO.setOrgName("怀宁县卫生和计划生育委员会");
                            } else if ("340802".equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340802348747527A0117");
                                certificateReqStrBO.setOrgCode("340802");
                                certificateReqStrBO.setOrgName("安庆市迎江区卫生和计划生育委员会");
                            } else if ("340811".equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340811343864046B0117");
                                certificateReqStrBO.setOrgCode("340811");
                                certificateReqStrBO.setOrgName("安庆市宜秀区卫生和计划生育委员会");
                            } else if ("340824".equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340824MB0X9278560117");
                                certificateReqStrBO.setOrgCode("340824");
                                certificateReqStrBO.setOrgName("潜山县卫生和计划生育委员会");
                            } else if ("342824".equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340824MB0X9278560117");
                                certificateReqStrBO.setOrgCode("342824");
                                certificateReqStrBO.setOrgName("潜山县卫生和计划生育委员会");
                            } else if ("340882".equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340824MB0X9278560117");
                                certificateReqStrBO.setOrgCode("340882");
                                certificateReqStrBO.setOrgName("潜山县卫生和计划生育委员会");
                            } else if ("340826".equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340826MB0W17012E0117");
                                certificateReqStrBO.setOrgCode("340826");
                                certificateReqStrBO.setOrgName("宿松县卫生和计划生育委员会");
                            } else if ("340828".equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("1134082835515747XB0117");
                                certificateReqStrBO.setOrgCode("340828");
                                certificateReqStrBO.setOrgName("岳西县卫生和计划生育委员会");
                            } else if ("340803".equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("11340803343818912J0117");
                                certificateReqStrBO.setOrgCode("340803");
                                certificateReqStrBO.setOrgName("安庆市大观区卫生和计划生育委员会");
                            } else if ("340881".equals(sfz.substring(0, 6).trim())) {
                                certificateBaseBO.setContentCode("1134088135858218090117");
                                certificateReqStrBO.setOrgCode("340881");
                                certificateReqStrBO.setOrgName("桐城市卫生和计划生育委员会");
                            } else {
                                certificateBaseBO.setContentCode("");
                                certificateReqStrBO.setOrgCode("");
                                certificateReqStrBO.setOrgName("");
                            }
                        }

                        certificateBaseBO.setOwnerId(StringUtils.handleString(entity.getPosfz()));
                        certificateBaseBO.setOwnerName(StringUtils.handleString(entity.getCzr()));
                        certificateBaseBO.setInfoCode(StringUtils.handleString(entity.getGrzh()));
                        if (StrUtil.isNotEmpty(entity.getFzrq())) {
                            certificateBaseBO.setInfoValidityBegin(entity.getFzrq().replaceAll("-", ""));
                        } else {
                            certificateBaseBO.setInfoValidityBegin("20000311");
                        }
                        certificateBaseBO.setInfoValidityEnd(constant.getInfoValidityEnd());
                        Map<String, String> dataMap = new HashMap<>();
                        dataMap.put("photo", "");
                        // 发证单位
                        dataMap.put("fzdw", StringUtils.handleString(entity.getFzdw()));
                        // 发证日期
                        if (StrUtil.isNotEmpty(entity.getFzrq())) {
                            String fzrq = entity.getFzrq().replaceAll("-", "");
                            // 发证日期年
                            dataMap.put("fzrq_n", fzrq.substring(0, 4));
                            // 发证日期_月
                            dataMap.put("fzrq_y", fzrq.substring(4, 6));
                            // 发证日期_日
                            dataMap.put("fzrq_r", fzrq.substring(6, 8));
                        } else {
                            dataMap.put("fzrq_n", "2000");
                            // 发证日期_月
                            dataMap.put("fzrq_y", "03");
                            // 发证日期_日
                            dataMap.put("fzrq_r", "11");
                        }
                        // 独生子女姓名
                        dataMap.put("name", StringUtils.handleString(entity.getXm1()));
                        // 编号
                        dataMap.put("infoCode", StringUtils.handleString(entity.getGrzh()));
                        // 性别
                        if (StrUtil.isNotEmpty(entity.getXb1())) {
                            if ("1".equals(entity.getXb1().trim())) {
                                dataMap.put("gender", "男");
                            } else if ("2".equals(entity.getXb1().trim())) {
                                dataMap.put("gender", "女");
                            }
                        } else {
                            dataMap.put("gender", "");
                        }
                        // 民族(需要查公安民族表)
                        if (StrUtil.isNotEmpty(entity.getMz())) {
                            NationEntity nationEntity = nationEntityService.getNationEntityByCode(entity.getMz());
                            if (nationEntity != null) {
                                dataMap.put("people", nationEntity.getMzmc());
                            } else {
                                dataMap.put("people", "");
                            }
                        } else {
                            dataMap.put("people", "");
                        }
                        // 出生日期
                        if (StrUtil.isNotEmpty(entity.getCsrq())) {
                            String csrq = entity.getCsrq().replaceAll("-", "");
                            // 出生_年
                            dataMap.put("csn", csrq.substring(0, 4));
                            // 出生_月
                            dataMap.put("csy", csrq.substring(4, 6));
                            // 出生_日
                            dataMap.put("csr", csrq.substring(6, 8));
                        } else {
                            dataMap.put("csn", "");
                            dataMap.put("csy", "");
                            dataMap.put("csr", "");
                        }
                        // 母亲姓名+++
                        dataMap.put("mname", StringUtils.handleString(entity.getPoxm()));
                        // 母亲工作单位+++
                        dataMap.put("mdw", StringUtils.handleString(entity.getFwcs1()));
                        // 父亲姓名+++
                        dataMap.put("fname", StringUtils.handleString(entity.getXm()));
                        // 父亲工作单位+++
                        dataMap.put("fdw", StringUtils.handleString(entity.getFwcs()));
                        // 家庭住址
                        dataMap.put("address", StringUtils.handleString(entity.getXjd()));
                        // 持证人
                        dataMap.put("czr", StringUtils.handleString(entity.getCzr()));
                        // 持证人性别
                        dataMap.put("czrxb", "女");
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
                    } catch (Exception e) {
                        log.error("制证异常失败:{}", e.getMessage());
                    }
                });
                tashCounter.incrementAndGet();
            }
        }
        return "光荣证制证:" + honourEntityList.size() + ":" + DateUtil.now();
    }

}
