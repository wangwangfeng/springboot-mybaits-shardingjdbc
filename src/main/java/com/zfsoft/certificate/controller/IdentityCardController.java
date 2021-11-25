package com.zfsoft.certificate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.zfsoft.certificate.mapper.db3.*;
import com.zfsoft.certificate.pojo.*;
import com.zfsoft.certificate.pojo.base.CertificateBaseBO;
import com.zfsoft.certificate.pojo.base.CertificateReqStrBO;
import com.zfsoft.certificate.pojo.base.Constant;
import com.zfsoft.certificate.util.Base64Tools;
import com.zfsoft.certificate.util.IdcardUtil;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 身份证制证
 *
 * @author 86131
 */
@Api(tags = "身份证制证后台")
@Controller
@Slf4j
public class IdentityCardController {

    @Autowired
    private Constant constant;

    /**
     * 消费线程计数器
     */
    public static AtomicInteger tashCounter = new AtomicInteger();

    @Autowired
    private OdsHzCzrkjbxxbZhzwMapper odsHzCzrkjbxxbZhzwMapper;

    @Autowired
    private MzzdbMapper mzzdbMapper;

    @Autowired
    private OdsHzRyzpxxbZhzwMapper odsHzRyzpxxbZhzwMapper;

    @Autowired
    private OdsHzXzqhbZhzwMapper odsHzXzqhbZhzwMapper;

    @Autowired
    private OdsHzJlxxxbZhzwMapper odsHzJlxxxbZhzwMapper;


    /**
     * 身份证
     * 获取有效截止日期大于当前日期的1万条数据
     * 有效截止日期为空代表长期证照(暂未处理这些)
     *
     * @return
     */
    @ApiOperation(value = "身份证制证", notes = "根据配置文件查询分页")
    @RequestMapping(value = "/sfz", method = RequestMethod.GET)
    @ResponseBody
    public String makeIdentityCard() {

        List<OdsHzCzrkjbxxbZhzw> odsHzCzrkjbxxbZhzws = new LambdaQueryChainWrapper<>(odsHzCzrkjbxxbZhzwMapper)
                .eq(OdsHzCzrkjbxxbZhzw::getRyzt, "0")
                .eq(OdsHzCzrkjbxxbZhzw::getJlbz, "1")
                .eq(OdsHzCzrkjbxxbZhzw::getCxbz, "0")
                .isNotNull(OdsHzCzrkjbxxbZhzw::getGmsfhm)
                .isNotNull(OdsHzCzrkjbxxbZhzw::getXm)
                .isNotNull(OdsHzCzrkjbxxbZhzw::getZpid)
                .isNotNull(OdsHzCzrkjbxxbZhzw::getMlxz)
                .isNotNull(OdsHzCzrkjbxxbZhzw::getYxqxqsrq)
                .isNotNull(OdsHzCzrkjbxxbZhzw::getYxqxjzrq)
                .gt(OdsHzCzrkjbxxbZhzw::getYxqxjzrq, DateUtil.format(DateUtil.date(), "yyyyMMdd"))
                .orderByDesc(OdsHzCzrkjbxxbZhzw::getZhgxsj)
                .last("limit " + constant.getLimitBegin() + "," + constant.getLimitEnd())
                .list();
        log.info("身份证制证开始总量为:{}个", odsHzCzrkjbxxbZhzws.size());
        ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(odsHzCzrkjbxxbZhzws)) {
            while (odsHzCzrkjbxxbZhzws.size() > tashCounter.get()) {
                OdsHzCzrkjbxxbZhzw odsHzCzrkjbxxbZhzw = odsHzCzrkjbxxbZhzws.get(tashCounter.get());
                log.info("数据身份证号:{}", odsHzCzrkjbxxbZhzw.getGmsfhm());
                executor.execute(() -> {
                    try {
                        CertificateBaseBO certificateBaseBO = new CertificateBaseBO();
                        CertificateReqStrBO certificateReqStrBO = new CertificateReqStrBO();
                        if (StringUtils.isNotEmpty(odsHzCzrkjbxxbZhzw.getSsxq())) {
                            /**
                             * 暂时迎江区、大观区、宜秀区都放到安庆市下
                             */
                            if (constant.getAqZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //安庆市340800
                                certificateBaseBO.setContentCode("11340800003110803D0002");
                            } else if (constant.getYjqZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //迎江区340802
                                certificateBaseBO.setContentCode("11340800003110803D0002");
                            } else if (constant.getDgqZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //大观区340803
                                certificateBaseBO.setContentCode("11340800003110803D0002");
                            } else if (constant.getYxqZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //宜秀区340811
                                certificateBaseBO.setContentCode("11340800003110803D0002");
                            } else if (constant.getHnxZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //怀宁县340822
                                certificateBaseBO.setContentCode("1134082200312387410057");
                            } else if (constant.getTcsZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //桐城市340881
                                certificateBaseBO.setContentCode("1134088100312122X70057");
                            } else if (constant.getWjxZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //望江县340827
                                certificateBaseBO.setContentCode("11340827003134952M0057");
                            } else if (constant.getThxZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //太湖县340825
                                certificateBaseBO.setContentCode("1134082500312990XK0000");
                            } else if (constant.getYxxZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //岳西县340828
                                certificateBaseBO.setContentCode("11340828003136907W0057");
                            } else if (constant.getSsxZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //宿松县340826
                                certificateBaseBO.setContentCode("1134082600313207640000");
                            } else if (constant.getQssZoneCodeOne().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                ////潜山市340824/340882
                                certificateBaseBO.setContentCode("11340824003128309X0057");
                            } else if (constant.getQssZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                ////潜山市340824/340882
                                certificateBaseBO.setContentCode("11340824003128309X0057");
                            }
                        }

                        certificateBaseBO.setOwnerId(odsHzCzrkjbxxbZhzw.getGmsfhm());
                        certificateBaseBO.setOwnerName(odsHzCzrkjbxxbZhzw.getXm());
                        certificateBaseBO.setInfoCode(odsHzCzrkjbxxbZhzw.getGmsfhm());
                        certificateBaseBO.setInfoValidityBegin(odsHzCzrkjbxxbZhzw.getYxqxqsrq());
                        certificateBaseBO.setInfoValidityEnd(odsHzCzrkjbxxbZhzw.getYxqxjzrq());
                        Map<String, String> dataMap = new HashMap<>();
                        // 照片(如果根据身份证号查询可以根据最后更新时间取最新的记录)
                        OdsHzRyzpxxbZhzw odsHzRyzpxxbZhzw = new LambdaQueryChainWrapper<>(odsHzRyzpxxbZhzwMapper)
                                .eq(OdsHzRyzpxxbZhzw::getZpid, odsHzCzrkjbxxbZhzw.getZpid())
                                //.orderByDesc(OdsHzRyzpxxbZhzw::getZhgxsj)
                                //.last("limit 1")
                                .one();
                        if (odsHzRyzpxxbZhzw != null
                                && odsHzRyzpxxbZhzw.getZp() != null
                                && odsHzRyzpxxbZhzw.getZp().length > 0) {
                            dataMap.put("photo", Base64Tools.encodeBase64String(odsHzRyzpxxbZhzw.getZp()));
                        } else {
                            dataMap.put("photo", "");
                            log.info("++++身份证照片数据为空++++:{}", odsHzCzrkjbxxbZhzw.getGmsfhm());
                        }
                        dataMap.put("name", StringUtils.handleString(odsHzCzrkjbxxbZhzw.getXm()));
                        // 性别
                        if (StringUtils.isNotEmpty(odsHzCzrkjbxxbZhzw.getXb())) {
                            if ("1".equals(odsHzCzrkjbxxbZhzw.getXb())) {
                                dataMap.put("gender", "男");
                            } else if ("2".equals(odsHzCzrkjbxxbZhzw.getXb())) {
                                dataMap.put("gender", "女");
                            }
                        }
                        // 民族
                        if (StringUtils.isNotEmpty(odsHzCzrkjbxxbZhzw.getMz())) {
                            Mzzdb mzzdb = new LambdaQueryChainWrapper<>(mzzdbMapper)
                                    .eq(Mzzdb::getXh, odsHzCzrkjbxxbZhzw.getMz())
                                    .last("limit 1")
                                    .one();
                            if (mzzdb != null && StringUtils.isNotEmpty(mzzdb.getMc())) {
                                dataMap.put("nation", StringUtils.handleString(mzzdb.getMc()));
                            }
                        }
                        // 根据身份证号获取生日年月日
                        dataMap.put("year", IdcardUtil.getYearByIdCard(odsHzCzrkjbxxbZhzw.getGmsfhm()));
                        dataMap.put("month", IdcardUtil.getMonthByIdCard(odsHzCzrkjbxxbZhzw.getGmsfhm()));
                        dataMap.put("day", IdcardUtil.getDateByIdCard(odsHzCzrkjbxxbZhzw.getGmsfhm()));
                        // 地址：ssxq + jlx + mlph + mlxz
                        StringBuffer stringBuffer = new StringBuffer();
                        if (StringUtils.isNotEmpty(odsHzCzrkjbxxbZhzw.getSsxq())) {
                            OdsHzXzqhbZhzw odsHzXzqhbZhzw = new LambdaQueryChainWrapper<>(odsHzXzqhbZhzwMapper)
                                    .eq(OdsHzXzqhbZhzw::getDm, odsHzCzrkjbxxbZhzw.getSsxq())
                                    .orderByDesc(OdsHzXzqhbZhzw::getYwptTimeDxp)
                                    .last("limit 1")
                                    .one();
                            if (odsHzXzqhbZhzw != null && StringUtils.isNotEmpty(odsHzXzqhbZhzw.getMc())) {
                                stringBuffer.append(odsHzXzqhbZhzw.getMc());
                            }
                        }
                        if (StringUtils.isNotEmpty(odsHzCzrkjbxxbZhzw.getJlx())) {
                            OdsHzJlxxxbZhzw odsHzJlxxxbZhzw = new LambdaQueryChainWrapper<>(odsHzJlxxxbZhzwMapper)
                                    .eq(OdsHzJlxxxbZhzw::getDm0000, odsHzCzrkjbxxbZhzw.getJlx())
                                    .last("limit 1")
                                    .one();
                            if (odsHzJlxxxbZhzw != null && StringUtils.isNotEmpty(odsHzJlxxxbZhzw.getMc0000())) {
                                stringBuffer.append(odsHzJlxxxbZhzw.getMc0000());
                            }
                        }
                        if (StringUtils.isNotEmpty(odsHzCzrkjbxxbZhzw.getMlph())) {
                            stringBuffer.append(odsHzCzrkjbxxbZhzw.getMlph());
                        }
                        if (StringUtils.isNotEmpty(odsHzCzrkjbxxbZhzw.getMlxz())) {
                            stringBuffer.append(odsHzCzrkjbxxbZhzw.getMlxz());
                        }
                        if (stringBuffer.length() > 0) {
                            dataMap.put("address", stringBuffer.toString());
                        }
                        dataMap.put("infoCode", odsHzCzrkjbxxbZhzw.getGmsfhm());
                        dataMap.put("issueName", odsHzCzrkjbxxbZhzw.getQfjg());
                        dataMap.put("begin", DateUtil.format(DateUtil.parse(odsHzCzrkjbxxbZhzw.getYxqxqsrq()), "yyyy.MM.dd"));
                        dataMap.put("end", DateUtil.format(DateUtil.parse(odsHzCzrkjbxxbZhzw.getYxqxjzrq()), "yyyy.MM.dd"));
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
        return "身份证制证:" + odsHzCzrkjbxxbZhzws.size() + ":" + DateUtil.now();
    }

    @ApiOperation(value = "根据单个身份证号制证数据", notes = "")
    @ApiImplicitParam(paramType = "path", name = "owernId", value = "身份证号", required = true, dataType = "String")
    @RequestMapping(value = "/sfz/{owernId}", method = RequestMethod.GET)
    @ResponseBody
    public String makeIdentityCardByOwernId(@PathVariable(name = "owernId") String owernId) {

        List<OdsHzCzrkjbxxbZhzw> odsHzCzrkjbxxbZhzws = new LambdaQueryChainWrapper<>(odsHzCzrkjbxxbZhzwMapper)
                .eq(OdsHzCzrkjbxxbZhzw::getRyzt, "0")
                .eq(OdsHzCzrkjbxxbZhzw::getJlbz, "1")
                .eq(OdsHzCzrkjbxxbZhzw::getCxbz, "0")
                .eq(OdsHzCzrkjbxxbZhzw::getGmsfhm, owernId)
                .isNotNull(OdsHzCzrkjbxxbZhzw::getXm)
                .isNotNull(OdsHzCzrkjbxxbZhzw::getZpid)
                .isNotNull(OdsHzCzrkjbxxbZhzw::getMlxz)
                .isNotNull(OdsHzCzrkjbxxbZhzw::getYxqxqsrq)
                .isNotNull(OdsHzCzrkjbxxbZhzw::getYxqxjzrq)
                .gt(OdsHzCzrkjbxxbZhzw::getYxqxjzrq, DateUtil.format(DateUtil.date(), "yyyyMMdd"))
                .orderByDesc(OdsHzCzrkjbxxbZhzw::getZhgxsj)
                .last("limit " + constant.getLimitBegin() + "," + constant.getLimitEnd())
                .list();
        log.info("身份证制证开始总量为:{}个", odsHzCzrkjbxxbZhzws.size());
        ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(odsHzCzrkjbxxbZhzws)) {
            while (odsHzCzrkjbxxbZhzws.size() > tashCounter.get()) {
                OdsHzCzrkjbxxbZhzw odsHzCzrkjbxxbZhzw = odsHzCzrkjbxxbZhzws.get(tashCounter.get());
                log.info("数据身份证号:{}", odsHzCzrkjbxxbZhzw.getGmsfhm());
                executor.execute(() -> {
                    try {
                        CertificateBaseBO certificateBaseBO = new CertificateBaseBO();
                        CertificateReqStrBO certificateReqStrBO = new CertificateReqStrBO();
                        if (StringUtils.isNotEmpty(odsHzCzrkjbxxbZhzw.getSsxq())) {
                            /**
                             * 暂时迎江区、大观区、宜秀区都放到安庆市下
                             */
                            if (constant.getAqZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //安庆市340800
                                certificateBaseBO.setContentCode("11340800003110803D0002");
                            } else if (constant.getYjqZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //迎江区340802
                                certificateBaseBO.setContentCode("11340800003110803D0002");
                            } else if (constant.getDgqZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //大观区340803
                                certificateBaseBO.setContentCode("11340800003110803D0002");
                            } else if (constant.getYxqZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //宜秀区340811
                                certificateBaseBO.setContentCode("11340800003110803D0002");
                            } else if (constant.getHnxZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //怀宁县340822
                                certificateBaseBO.setContentCode("1134082200312387410057");
                            } else if (constant.getTcsZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //桐城市340881
                                certificateBaseBO.setContentCode("1134088100312122X70057");
                            } else if (constant.getWjxZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //望江县340827
                                certificateBaseBO.setContentCode("11340827003134952M0057");
                            } else if (constant.getThxZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //太湖县340825
                                certificateBaseBO.setContentCode("1134082500312990XK0000");
                            } else if (constant.getYxxZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //岳西县340828
                                certificateBaseBO.setContentCode("11340828003136907W0057");
                            } else if (constant.getSsxZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                //宿松县340826
                                certificateBaseBO.setContentCode("1134082600313207640000");
                            } else if (constant.getQssZoneCodeOne().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                ////潜山市340824/340882
                                certificateBaseBO.setContentCode("11340824003128309X0057");
                            } else if (constant.getQssZoneCode().equals(odsHzCzrkjbxxbZhzw.getSsxq().substring(0, 6).trim())) {
                                ////潜山市340824/340882
                                certificateBaseBO.setContentCode("11340824003128309X0057");
                            }
                        }

                        certificateBaseBO.setOwnerId(odsHzCzrkjbxxbZhzw.getGmsfhm());
                        certificateBaseBO.setOwnerName(odsHzCzrkjbxxbZhzw.getXm());
                        certificateBaseBO.setInfoCode(odsHzCzrkjbxxbZhzw.getGmsfhm());
                        certificateBaseBO.setInfoValidityBegin(odsHzCzrkjbxxbZhzw.getYxqxqsrq());
                        certificateBaseBO.setInfoValidityEnd(odsHzCzrkjbxxbZhzw.getYxqxjzrq());
                        Map<String, String> dataMap = new HashMap<>();
                        // 照片(如果根据身份证号查询可以根据最后更新时间取最新的记录)
                        OdsHzRyzpxxbZhzw odsHzRyzpxxbZhzw = new LambdaQueryChainWrapper<>(odsHzRyzpxxbZhzwMapper)
                                .eq(OdsHzRyzpxxbZhzw::getZpid, odsHzCzrkjbxxbZhzw.getZpid())
                                //.eq(OdsHzRyzpxxbZhzw::getGmsfhm, odsHzCzrkjbxxbZhzw.getGmsfhm())
                                //.orderByDesc(OdsHzRyzpxxbZhzw::getZhgxsj)
                                //.last("limit 1")
                                .one();
                        if (odsHzRyzpxxbZhzw != null
                                && odsHzRyzpxxbZhzw.getZp() != null
                                && odsHzRyzpxxbZhzw.getZp().length > 0) {
                            dataMap.put("photo", Base64Tools.encodeBase64String(odsHzRyzpxxbZhzw.getZp()));
                        } else {
                            dataMap.put("photo", "");
                            log.info("++++身份证照片数据为空++++:{}", odsHzCzrkjbxxbZhzw.getGmsfhm());
                        }
                        dataMap.put("name", StringUtils.handleString(odsHzCzrkjbxxbZhzw.getXm()));
                        // 性别
                        if (StringUtils.isNotEmpty(odsHzCzrkjbxxbZhzw.getXb())) {
                            if ("1".equals(odsHzCzrkjbxxbZhzw.getXb())) {
                                dataMap.put("gender", "男");
                            } else if ("2".equals(odsHzCzrkjbxxbZhzw.getXb())) {
                                dataMap.put("gender", "女");
                            }
                        }
                        // 民族
                        if (StringUtils.isNotEmpty(odsHzCzrkjbxxbZhzw.getMz())) {
                            Mzzdb mzzdb = new LambdaQueryChainWrapper<>(mzzdbMapper)
                                    .eq(Mzzdb::getXh, odsHzCzrkjbxxbZhzw.getMz())
                                    .last("limit 1")
                                    .one();
                            if (mzzdb != null && StringUtils.isNotEmpty(mzzdb.getMc())) {
                                dataMap.put("nation", StringUtils.handleString(mzzdb.getMc()));
                            }
                        }
                        // 根据身份证号获取生日年月日
                        dataMap.put("year", IdcardUtil.getYearByIdCard(odsHzCzrkjbxxbZhzw.getGmsfhm()));
                        dataMap.put("month", IdcardUtil.getMonthByIdCard(odsHzCzrkjbxxbZhzw.getGmsfhm()));
                        dataMap.put("day", IdcardUtil.getDateByIdCard(odsHzCzrkjbxxbZhzw.getGmsfhm()));
                        // 地址：ssxq + jlx + mlph + mlxz
                        StringBuffer stringBuffer = new StringBuffer();
                        if (StringUtils.isNotEmpty(odsHzCzrkjbxxbZhzw.getSsxq())) {
                            OdsHzXzqhbZhzw odsHzXzqhbZhzw = new LambdaQueryChainWrapper<>(odsHzXzqhbZhzwMapper)
                                    .eq(OdsHzXzqhbZhzw::getDm, odsHzCzrkjbxxbZhzw.getSsxq())
                                    .orderByDesc(OdsHzXzqhbZhzw::getYwptTimeDxp)
                                    .last("limit 1")
                                    .one();
                            if (odsHzXzqhbZhzw != null && StringUtils.isNotEmpty(odsHzXzqhbZhzw.getMc())) {
                                stringBuffer.append(odsHzXzqhbZhzw.getMc());
                            }
                        }
                        if (StringUtils.isNotEmpty(odsHzCzrkjbxxbZhzw.getJlx())) {
                            OdsHzJlxxxbZhzw odsHzJlxxxbZhzw = new LambdaQueryChainWrapper<>(odsHzJlxxxbZhzwMapper)
                                    .eq(OdsHzJlxxxbZhzw::getDm0000, odsHzCzrkjbxxbZhzw.getJlx())
                                    .last("limit 1")
                                    .one();
                            if (odsHzJlxxxbZhzw != null && StringUtils.isNotEmpty(odsHzJlxxxbZhzw.getMc0000())) {
                                stringBuffer.append(odsHzJlxxxbZhzw.getMc0000());
                            }
                        }
                        if (StringUtils.isNotEmpty(odsHzCzrkjbxxbZhzw.getMlph())) {
                            stringBuffer.append(odsHzCzrkjbxxbZhzw.getMlph());
                        }
                        if (StringUtils.isNotEmpty(odsHzCzrkjbxxbZhzw.getMlxz())) {
                            stringBuffer.append(odsHzCzrkjbxxbZhzw.getMlxz());
                        }
                        if (stringBuffer.length() > 0) {
                            dataMap.put("address", stringBuffer.toString());
                        }
                        dataMap.put("infoCode", odsHzCzrkjbxxbZhzw.getGmsfhm());
                        dataMap.put("issueName", odsHzCzrkjbxxbZhzw.getQfjg());
                        dataMap.put("begin", DateUtil.format(DateUtil.parse(odsHzCzrkjbxxbZhzw.getYxqxqsrq()), "yyyy.MM.dd"));
                        dataMap.put("end", DateUtil.format(DateUtil.parse(odsHzCzrkjbxxbZhzw.getYxqxjzrq()), "yyyy.MM.dd"));
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
        return "身份证制证:" + odsHzCzrkjbxxbZhzws.size() + ":" + DateUtil.now();
    }

}
