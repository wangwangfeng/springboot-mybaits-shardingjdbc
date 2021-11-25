package com.zfsoft.certificate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.iflytek.fsp.shield.java.sdk.model.ApiResponse;
import com.zfsoft.certificate.mapper.db1.ZzWorkCertificateMapper;
import com.zfsoft.certificate.mapper.db2.ZxEntityMapper;
import com.zfsoft.certificate.mapper.db6.ZzwkZzwInfo1Mapper;
import com.zfsoft.certificate.mapper.db6.ZzwkZzwInfo2Mapper;
import com.zfsoft.certificate.pojo.ZxEntity;
import com.zfsoft.certificate.pojo.ZzWorkCertificate;
import com.zfsoft.certificate.pojo.ZzwkZzwInfo1;
import com.zfsoft.certificate.pojo.ZzwkZzwInfo2;
import com.zfsoft.certificate.pojo.base.Constant;
import com.zfsoft.certificate.service.ZzWorkCertificateService;
import com.zfsoft.certificate.util.IflytekUtils;
import com.zfsoft.certificate.util.StringUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 86131
 * 证照注销接口
 * 注销历史数据加索引：INDEX_STYLE_DELETE_STATUS_DATE	`STYLE_ID`, `DELETE_STATE`, `CER_STATUS`, `CREATE_DATE`
 * 注销有效数据按照上面的索引然后去掉排序即可或者加上面的索引去掉CER_STATUS即可
 */
@Api(tags = "证照注销后台")
@ApiSort(value = 2)
@Slf4j
@Controller
public class MaintenanceController {

    @Autowired
    private Constant constant;

    /**
     * 消费线程计数器
     */
    public static AtomicInteger tashCounter = new AtomicInteger();

    @Autowired
    private ZzWorkCertificateMapper zzWorkCertificateMapper;

    @Autowired
    private ZzWorkCertificateService zzWorkCertificateService;

    @Autowired
    private ZzwkZzwInfo1Mapper zzwkZzwInfo1Mapper;

    @Autowired
    private ZzwkZzwInfo2Mapper zzwkZzwInfo2Mapper;

    @Autowired
    private ZxEntityMapper zxEntityMapper;

    /**
     * 历史数据未注销成功
     * 重新注销数据库中删除状态为1和证照状态为4的数据
     *
     * @param code 证照类型编码
     * @return
     */
    @ApiOperation(value = "根据证照类型编码注销历史未注销成功的数据", notes = "")
    @ApiOperationSupport(order = 1)
    @ApiImplicitParam(paramType = "path", name = "code", value = "证照类型编码", required = true, dataType = "String")
    @GetMapping("/maintenance/{code}")
    @ResponseBody
    public String maintenance(@PathVariable(name = "code") String code) {

        //List<ZzWorkCertificate> zzWorkCertificateList = zzWorkCertificateService.getZzWorkCertificateMaintenanceByContentCode(code.trim());
        List<ZzWorkCertificate> zzWorkCertificateList = new LambdaQueryChainWrapper<>(zzWorkCertificateMapper)
                .eq(ZzWorkCertificate::getContentCode, code)
                .eq(ZzWorkCertificate::getDeleteState, constant.getOne())
                .eq(ZzWorkCertificate::getCerStatus, constant.getFour())
                .orderByDesc(ZzWorkCertificate::getCreateDate)
                .last("limit " + constant.getLimitBegin() + "," + constant.getLimitEnd())
                .list();

        if (CollUtil.isNotEmpty(zzWorkCertificateList)) {
            log.info("查询到需要注销的数据总量:{}个", zzWorkCertificateList.size());
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (zzWorkCertificateList.size() > tashCounter.get()) {
                ZzWorkCertificate zzWorkCertificate = zzWorkCertificateList.get(tashCounter.get());
                if (zzWorkCertificate != null
                        && StrUtil.isNotEmpty(zzWorkCertificate.getCertificateCode())) {
                    executor.execute(() -> {
                        try {
                            IflytekUtils iflytekUtils = new IflytekUtils();
                            Map<String, Object> jsonMap = new HashMap<>();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            //持证者证件号码(身份证或统一信用代码)
                            jsonMap.put("ownerId", zzWorkCertificate.getOwnerId());
                            jsonMap.put("contentName", zzWorkCertificate.getContentName());
                            jsonMap.put("infoCode", zzWorkCertificate.getInfoCode());
                            jsonMap.put("code", zzWorkCertificate.getCertificateCode());
                            jsonMap.put("ownerName", zzWorkCertificate.getOwnerName());
                            jsonMap.put("areaCode", "340800");
                            jsonMap.put("version", zzWorkCertificate.getVersion());
                            jsonMap.put("contentCode", zzWorkCertificate.getContentCode());
                            // 操作类型(新增:I;修改:U;删除:D)
                            jsonMap.put("operateType", "D");
                            jsonMap.put("systemCode", "aqzzk");
                            jsonMap.put("bizId", zzWorkCertificate.getId());
                            jsonMap.put("validBegin", zzWorkCertificate.getInfoValidityBegin().substring(0, 4) + "-"
                                    + zzWorkCertificate.getInfoValidityBegin().substring(4, 6) + "-"
                                    + zzWorkCertificate.getInfoValidityBegin().substring(6, 8));
                            jsonMap.put("validEnd", zzWorkCertificate.getInfoValidityEnd().substring(0, 4) + "-"
                                    + zzWorkCertificate.getInfoValidityEnd().substring(4, 6) + "-"
                                    + zzWorkCertificate.getInfoValidityEnd().substring(6, 8));
                            jsonMap.put("dataSource", constant.getTwo());
                            jsonMap.put("makeTime", zzWorkCertificate.getCreateDate() == null ?
                                    null : simpleDateFormat.format(zzWorkCertificate.getCreateDate()));
                            jsonMap.put("createTime", simpleDateFormat.format(new Date()));
                            jsonMap.put("contentMd5", DigestUtils.md5Hex(zzWorkCertificate.getData()));
                            // 2020-11-28新增 证件类型(证件类型infoTypeCode：个人（满足18位）111或999，法人（满足18位）001或099；signCount：印章数量)
                            jsonMap.put("infoTypeCode", StringUtils.infoTypeCode(zzWorkCertificate.getOwnerId()));
                            // 印章数量	非必填，默认0
                            jsonMap.put("signCount", constant.getZero());
                            String jsonStr = JSON.toJSONString(jsonMap);
                            //索引上报
                            ApiResponse apu = iflytekUtils.key_WEIHU(jsonStr.getBytes(StandardCharsets.UTF_8.name()));
                            String result = new String(apu.getBody(), StandardCharsets.UTF_8.name());
                            log.info("索引上报返回结果:{}", result);
                            JSONObject resObject = JSONObject.parseObject(result);
                            if (!resObject.isEmpty()) {
                                if ("200".equals(resObject.getString("flag"))) {
                                    // 注销成功更新数据状态
                                    DecimalFormat countFormat = new DecimalFormat("0000");
                                    String version = countFormat.format(Integer.parseInt(zzWorkCertificate.getVersion()) + 1);
                                    boolean row = new LambdaUpdateChainWrapper<>(zzWorkCertificateMapper)
                                            .eq(ZzWorkCertificate::getId, zzWorkCertificate.getId())
                                            .set(ZzWorkCertificate::getDeleteState, constant.getOne())
                                            .set(ZzWorkCertificate::getCerStatus, constant.getFour())
                                            .set(ZzWorkCertificate::getVersion, version)
                                            .update();
                                }
                            }
                        } catch (Exception e) {
                            log.error("数据主键:{};注销失败:{}", zzWorkCertificate.getId(), e.getMessage());
                        }
                    });
                }
                tashCounter.incrementAndGet();
            }
        } else {
            log.info("查询到需要注销的数据为空！");
        }
        return "重新注销的所有数据:" + zzWorkCertificateList.size() + ":" + DateUtil.now();
    }

    /**
     * 注销某类证照的所有历史数据
     *
     * @param code 证照类型编码
     * @return
     */
    @ApiOperation(value = "根据证照类型编码注销某类证照的所有历史数据", notes = "")
    @ApiOperationSupport(order = 2)
    @ApiImplicitParam(paramType = "path", name = "code", value = "证照类型编码", required = true, dataType = "String")
    @GetMapping("/zx/{code}")
    @ResponseBody
    public String cancelByContentCode(@PathVariable(name = "code") String code) {

        List<ZzWorkCertificate> zzWorkCertificateList = new LambdaQueryChainWrapper<>(zzWorkCertificateMapper)
                .eq(ZzWorkCertificate::getContentCode, code)
                .orderByDesc(ZzWorkCertificate::getCreateDate)
                .last("limit " + constant.getLimitBegin() + "," + constant.getLimitEnd())
                .list();
        log.info("查询到需要注销的数据总量:{}个", zzWorkCertificateList.size());
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(zzWorkCertificateList)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (zzWorkCertificateList.size() > tashCounter.get()) {
                ZzWorkCertificate zzWorkCertificate = zzWorkCertificateList.get(tashCounter.get());
                if (zzWorkCertificate != null
                        && StrUtil.isNotEmpty(zzWorkCertificate.getCertificateCode())) {
                    executor.execute(() -> {
                        try {
                            IflytekUtils iflytekUtils = new IflytekUtils();
                            Map<String, Object> jsonMap = new HashMap<>();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            //持证者证件号码(身份证或统一信用代码)
                            jsonMap.put("ownerId", zzWorkCertificate.getOwnerId());
                            jsonMap.put("contentName", zzWorkCertificate.getContentName());
                            jsonMap.put("infoCode", zzWorkCertificate.getInfoCode());
                            jsonMap.put("code", zzWorkCertificate.getCertificateCode());
                            jsonMap.put("ownerName", zzWorkCertificate.getOwnerName());
                            jsonMap.put("areaCode", "340800");
                            jsonMap.put("version", zzWorkCertificate.getVersion());
                            jsonMap.put("contentCode", zzWorkCertificate.getContentCode());
                            // 操作类型(新增:I;修改:U;删除:D)
                            jsonMap.put("operateType", "D");
                            jsonMap.put("systemCode", "aqzzk");
                            jsonMap.put("bizId", zzWorkCertificate.getId());
                            jsonMap.put("validBegin", zzWorkCertificate.getInfoValidityBegin().substring(0, 4) + "-"
                                    + zzWorkCertificate.getInfoValidityBegin().substring(4, 6) + "-"
                                    + zzWorkCertificate.getInfoValidityBegin().substring(6, 8));
                            jsonMap.put("validEnd", zzWorkCertificate.getInfoValidityEnd().substring(0, 4) + "-"
                                    + zzWorkCertificate.getInfoValidityEnd().substring(4, 6) + "-"
                                    + zzWorkCertificate.getInfoValidityEnd().substring(6, 8));
                            jsonMap.put("dataSource", constant.getTwo());
                            jsonMap.put("makeTime", zzWorkCertificate.getCreateDate() == null ?
                                    null : simpleDateFormat.format(zzWorkCertificate.getCreateDate()));
                            jsonMap.put("createTime", simpleDateFormat.format(new Date()));
                            jsonMap.put("contentMd5", DigestUtils.md5Hex(zzWorkCertificate.getData()));
                            // 2020-11-28新增 证件类型(证件类型infoTypeCode：个人（满足18位）111或999，法人（满足18位）001或099；signCount：印章数量)
                            jsonMap.put("infoTypeCode", StringUtils.infoTypeCode(zzWorkCertificate.getOwnerId()));
                            // 印章数量	非必填，默认0
                            jsonMap.put("signCount", constant.getZero());
                            String jsonStr = JSON.toJSONString(jsonMap);
                            //索引上报
                            ApiResponse apu = iflytekUtils.key_WEIHU(jsonStr.getBytes(StandardCharsets.UTF_8.name()));
                            String result = new String(apu.getBody(), StandardCharsets.UTF_8.name());
                            log.info("索引上报返回结果:{}", result);
                            JSONObject resObject = JSONObject.parseObject(result);
                            if (!resObject.isEmpty()) {
                                if ("200".equals(resObject.getString("flag"))) {
                                    // 注销成功更新数据状态
                                    DecimalFormat countFormat = new DecimalFormat("0000");
                                    String version = countFormat.format(Integer.parseInt(zzWorkCertificate.getVersion()) + 1);
                                    boolean row = new LambdaUpdateChainWrapper<>(zzWorkCertificateMapper)
                                            .eq(ZzWorkCertificate::getId, zzWorkCertificate.getId())
                                            .set(ZzWorkCertificate::getDeleteState, constant.getOne())
                                            .set(ZzWorkCertificate::getCerStatus, constant.getFour())
                                            .set(ZzWorkCertificate::getVersion, version)
                                            .update();
                                }
                            }
                        } catch (Exception e) {
                            log.error("数据主键:{};注销失败:{}", zzWorkCertificate.getId(), e.getMessage());
                        }
                    });
                }
                tashCounter.incrementAndGet();
            }
        } else {
            log.info("查询到需要注销的数据为空！");
        }
        return "重新注销的所有数据:" + zzWorkCertificateList.size() + ":" + DateUtil.now();
    }

    /**
     * @Description: 根据主键注销单个数据
     * @Date: 2021/6/9 16:50
     * @author: wwf
     * @param: styleId
     * @return: java.lang.String
     **/
    @ApiOperation(value = "根据主键注销单个数据", notes = "")
    @ApiImplicitParam(paramType = "path", name = "id", value = "证照主键", required = true, dataType = "String")
    @GetMapping("/zxById/{id}")
    @ResponseBody
    public String cancelById(@PathVariable(name = "id") String id) {

        log.info("注销单条数据开始：+++++");
        ZzWorkCertificate zzWorkCertificate = new LambdaQueryChainWrapper<>(zzWorkCertificateMapper)
                .eq(ZzWorkCertificate::getId, id)
                .one();
        if (zzWorkCertificate != null
                && StrUtil.isNotEmpty(zzWorkCertificate.getCertificateCode())) {
            try {
                IflytekUtils iflytekUtils = new IflytekUtils();
                Map<String, Object> jsonMap = new HashMap<>();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                //持证者证件号码(身份证或统一信用代码)
                jsonMap.put("ownerId", zzWorkCertificate.getOwnerId());
                jsonMap.put("contentName", zzWorkCertificate.getContentName());
                jsonMap.put("infoCode", zzWorkCertificate.getInfoCode());
                jsonMap.put("code", zzWorkCertificate.getCertificateCode());
                jsonMap.put("ownerName", zzWorkCertificate.getOwnerName());
                jsonMap.put("areaCode", "340800");
                jsonMap.put("version", zzWorkCertificate.getVersion());
                jsonMap.put("contentCode", zzWorkCertificate.getContentCode());
                // 操作类型(新增:I;修改:U;删除:D)
                jsonMap.put("operateType", "D");
                jsonMap.put("systemCode", "aqzzk");
                jsonMap.put("bizId", zzWorkCertificate.getId());
                jsonMap.put("validBegin", zzWorkCertificate.getInfoValidityBegin().substring(0, 4) + "-"
                        + zzWorkCertificate.getInfoValidityBegin().substring(4, 6) + "-"
                        + zzWorkCertificate.getInfoValidityBegin().substring(6, 8));
                jsonMap.put("validEnd", zzWorkCertificate.getInfoValidityEnd().substring(0, 4) + "-"
                        + zzWorkCertificate.getInfoValidityEnd().substring(4, 6) + "-"
                        + zzWorkCertificate.getInfoValidityEnd().substring(6, 8));
                jsonMap.put("dataSource", constant.getTwo());
                jsonMap.put("makeTime", zzWorkCertificate.getCreateDate() == null ?
                        null : simpleDateFormat.format(zzWorkCertificate.getCreateDate()));
                jsonMap.put("createTime", simpleDateFormat.format(new Date()));
                jsonMap.put("contentMd5", DigestUtils.md5Hex(zzWorkCertificate.getData()));
                // 2020-11-28新增 证件类型(证件类型infoTypeCode：个人（满足18位）111或999，法人（满足18位）001或099；signCount：印章数量)
                jsonMap.put("infoTypeCode", StringUtils.infoTypeCode(zzWorkCertificate.getOwnerId()));
                // 印章数量	非必填，默认0
                jsonMap.put("signCount", constant.getZero());
                String jsonStr = JSON.toJSONString(jsonMap);
                //索引上报
                ApiResponse apu = iflytekUtils.key_WEIHU(jsonStr.getBytes(StandardCharsets.UTF_8.name()));
                String result = new String(apu.getBody(), StandardCharsets.UTF_8.name());
                log.info("索引上报返回结果:{}", result);
                JSONObject resObject = JSONObject.parseObject(result);
                if (!resObject.isEmpty()) {
                    if ("200".equals(resObject.getString("flag"))) {
                        // 注销成功更新数据状态
                        DecimalFormat countFormat = new DecimalFormat("0000");
                        String version = countFormat.format(Integer.parseInt(zzWorkCertificate.getVersion()) + 1);
                        boolean row = new LambdaUpdateChainWrapper<>(zzWorkCertificateMapper)
                                .eq(ZzWorkCertificate::getId, zzWorkCertificate.getId())
                                .set(ZzWorkCertificate::getDeleteState, constant.getOne())
                                .set(ZzWorkCertificate::getCerStatus, constant.getFour())
                                .set(ZzWorkCertificate::getVersion, version)
                                .update();
                    }
                }
            } catch (Exception e) {
                log.error("数据注销失败:{}", e.getMessage());
            }
        }

        return "注销单个数据:" + DateUtil.now();
    }

    /**
     * @Description: 注销所有证照数据通过styleID
     * @Date: 2021/6/9 16:50
     * @author: wwf
     * @param: styleId
     * @return: java.lang.String
     **/
    @ApiOperation(value = "根据证照类型字段注销所有证照数据", notes = "")
    @ApiImplicitParam(paramType = "path", name = "styleId", value = "证照类型", required = true, dataType = "String")
    @GetMapping("/zxStyleIdAll/{styleId}")
    @ResponseBody
    public String cancelAllByStyleId(@PathVariable(name = "styleId") String styleId) {

        List<ZzWorkCertificate> zzWorkCertificateList = new LambdaQueryChainWrapper<>(zzWorkCertificateMapper)
                .eq(ZzWorkCertificate::getStyleId, styleId)
                .orderByDesc(ZzWorkCertificate::getCreateDate)
                .last("limit " + constant.getLimitBegin() + "," + constant.getLimitEnd())
                .list();
        log.info("查询到需要注销的所有数据总量:{}个", zzWorkCertificateList.size());
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(zzWorkCertificateList)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (zzWorkCertificateList.size() > tashCounter.get()) {
                ZzWorkCertificate zzWorkCertificate = zzWorkCertificateList.get(tashCounter.get());
                if (zzWorkCertificate != null
                        && StrUtil.isNotEmpty(zzWorkCertificate.getCertificateCode())) {
                    executor.execute(() -> {
                        try {
                            IflytekUtils iflytekUtils = new IflytekUtils();
                            Map<String, Object> jsonMap = new HashMap<>();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            //持证者证件号码(身份证或统一信用代码)
                            jsonMap.put("ownerId", zzWorkCertificate.getOwnerId());
                            jsonMap.put("contentName", zzWorkCertificate.getContentName());
                            jsonMap.put("infoCode", zzWorkCertificate.getInfoCode());
                            jsonMap.put("code", zzWorkCertificate.getCertificateCode());
                            jsonMap.put("ownerName", zzWorkCertificate.getOwnerName());
                            jsonMap.put("areaCode", "340800");
                            jsonMap.put("version", zzWorkCertificate.getVersion());
                            jsonMap.put("contentCode", zzWorkCertificate.getContentCode());
                            // 操作类型(新增:I;修改:U;删除:D)
                            jsonMap.put("operateType", "D");
                            jsonMap.put("systemCode", "aqzzk");
                            jsonMap.put("bizId", zzWorkCertificate.getId());
                            jsonMap.put("validBegin", zzWorkCertificate.getInfoValidityBegin().substring(0, 4) + "-"
                                    + zzWorkCertificate.getInfoValidityBegin().substring(4, 6) + "-"
                                    + zzWorkCertificate.getInfoValidityBegin().substring(6, 8));
                            jsonMap.put("validEnd", zzWorkCertificate.getInfoValidityEnd().substring(0, 4) + "-"
                                    + zzWorkCertificate.getInfoValidityEnd().substring(4, 6) + "-"
                                    + zzWorkCertificate.getInfoValidityEnd().substring(6, 8));
                            jsonMap.put("dataSource", constant.getTwo());
                            jsonMap.put("makeTime", zzWorkCertificate.getCreateDate() == null ?
                                    null : simpleDateFormat.format(zzWorkCertificate.getCreateDate()));
                            jsonMap.put("createTime", simpleDateFormat.format(new Date()));
                            jsonMap.put("contentMd5", DigestUtils.md5Hex(zzWorkCertificate.getData()));
                            // 2020-11-28新增 证件类型(证件类型infoTypeCode：个人（满足18位）111或999，法人（满足18位）001或099；signCount：印章数量)
                            jsonMap.put("infoTypeCode", StringUtils.infoTypeCode(zzWorkCertificate.getOwnerId()));
                            // 印章数量	非必填，默认0
                            jsonMap.put("signCount", constant.getZero());
                            String jsonStr = JSON.toJSONString(jsonMap);
                            //索引上报
                            ApiResponse apu = iflytekUtils.key_WEIHU(jsonStr.getBytes(StandardCharsets.UTF_8.name()));
                            String result = new String(apu.getBody(), StandardCharsets.UTF_8.name());
                            log.info("索引上报返回结果:{}", result);
                            JSONObject resObject = JSONObject.parseObject(result);
                            if (!resObject.isEmpty()) {
                                if ("200".equals(resObject.getString("flag"))) {
                                    // 注销成功更新数据状态
                                    DecimalFormat countFormat = new DecimalFormat("0000");
                                    String version = countFormat.format(Integer.parseInt(zzWorkCertificate.getVersion()) + 1);
                                    boolean row = new LambdaUpdateChainWrapper<>(zzWorkCertificateMapper)
                                            .eq(ZzWorkCertificate::getId, zzWorkCertificate.getId())
                                            .set(ZzWorkCertificate::getDeleteState, constant.getOne())
                                            .set(ZzWorkCertificate::getCerStatus, constant.getFour())
                                            .set(ZzWorkCertificate::getVersion, version)
                                            .update();
                                }
                            }
                        } catch (Exception e) {
                            log.error("数据主键:{};注销失败:{}", zzWorkCertificate.getId(), e.getMessage());
                        }
                    });
                }
                tashCounter.incrementAndGet();
            }
        } else {
            log.info("查询到需要注销的数据为空！");
        }
        return "重新注销的所有总量数据:" + zzWorkCertificateList.size() + ":" + DateUtil.now();
    }

    /**
     * @Description: 注销所有有效证照数据通过styleID
     * @Date: 2021/6/9 16:50
     * @author: wwf
     * @param: styleId
     * @return: java.lang.String
     **/
    @ApiOperation(value = "根据证照类型字段注销所有有效证照数据", notes = "")
    @ApiImplicitParam(paramType = "path", name = "styleId", value = "证照类型", required = true, dataType = "String")
    @GetMapping("/zxStyleIdValid/{styleId}")
    @ResponseBody
    public String cancelValidByStyleId(@PathVariable(name = "styleId") String styleId) {

        List<ZzWorkCertificate> zzWorkCertificateList = new LambdaQueryChainWrapper<>(zzWorkCertificateMapper)
                .eq(ZzWorkCertificate::getStyleId, styleId)
                .eq(ZzWorkCertificate::getDeleteState, constant.getZero())
                .ne(ZzWorkCertificate::getCerStatus, constant.getFour())
                .last("limit " + constant.getLimitBegin() + "," + constant.getLimitEnd())
                .list();
        log.info("查询到需要注销的有效数据总量:{}个", zzWorkCertificateList.size());
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(zzWorkCertificateList)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (zzWorkCertificateList.size() > tashCounter.get()) {
                ZzWorkCertificate zzWorkCertificate = zzWorkCertificateList.get(tashCounter.get());
                if (zzWorkCertificate != null
                        && StrUtil.isNotEmpty(zzWorkCertificate.getCertificateCode())) {
                    executor.execute(() -> {
                        try {
                            IflytekUtils iflytekUtils = new IflytekUtils();
                            Map<String, Object> jsonMap = new HashMap<>();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            //持证者证件号码(身份证或统一信用代码)
                            jsonMap.put("ownerId", zzWorkCertificate.getOwnerId());
                            jsonMap.put("contentName", zzWorkCertificate.getContentName());
                            jsonMap.put("infoCode", zzWorkCertificate.getInfoCode());
                            jsonMap.put("code", zzWorkCertificate.getCertificateCode());
                            jsonMap.put("ownerName", zzWorkCertificate.getOwnerName());
                            jsonMap.put("areaCode", "340800");
                            jsonMap.put("version", zzWorkCertificate.getVersion());
                            jsonMap.put("contentCode", zzWorkCertificate.getContentCode());
                            // 操作类型(新增:I;修改:U;删除:D)
                            jsonMap.put("operateType", "D");
                            jsonMap.put("systemCode", "aqzzk");
                            jsonMap.put("bizId", zzWorkCertificate.getId());
                            jsonMap.put("validBegin", zzWorkCertificate.getInfoValidityBegin().substring(0, 4) + "-"
                                    + zzWorkCertificate.getInfoValidityBegin().substring(4, 6) + "-"
                                    + zzWorkCertificate.getInfoValidityBegin().substring(6, 8));
                            jsonMap.put("validEnd", zzWorkCertificate.getInfoValidityEnd().substring(0, 4) + "-"
                                    + zzWorkCertificate.getInfoValidityEnd().substring(4, 6) + "-"
                                    + zzWorkCertificate.getInfoValidityEnd().substring(6, 8));
                            jsonMap.put("dataSource", constant.getTwo());
                            jsonMap.put("makeTime", zzWorkCertificate.getCreateDate() == null ?
                                    null : simpleDateFormat.format(zzWorkCertificate.getCreateDate()));
                            jsonMap.put("createTime", simpleDateFormat.format(new Date()));
                            jsonMap.put("contentMd5", DigestUtils.md5Hex(zzWorkCertificate.getData()));
                            // 2020-11-28新增 证件类型(证件类型infoTypeCode：个人（满足18位）111或999，法人（满足18位）001或099；signCount：印章数量)
                            jsonMap.put("infoTypeCode", StringUtils.infoTypeCode(zzWorkCertificate.getOwnerId()));
                            // 印章数量	非必填，默认0
                            jsonMap.put("signCount", constant.getZero());
                            String jsonStr = JSON.toJSONString(jsonMap);
                            //索引上报
                            ApiResponse apu = iflytekUtils.key_WEIHU(jsonStr.getBytes(StandardCharsets.UTF_8.name()));
                            String result = new String(apu.getBody(), StandardCharsets.UTF_8.name());
                            log.info("索引上报返回结果:{}", result);
                            JSONObject resObject = JSONObject.parseObject(result);
                            if (!resObject.isEmpty()) {
                                if ("200".equals(resObject.getString("flag"))) {
                                    // 注销成功更新数据状态
                                    DecimalFormat countFormat = new DecimalFormat("0000");
                                    String version = countFormat.format(Integer.parseInt(zzWorkCertificate.getVersion()) + 1);
                                    boolean row = new LambdaUpdateChainWrapper<>(zzWorkCertificateMapper)
                                            .eq(ZzWorkCertificate::getId, zzWorkCertificate.getId())
                                            .set(ZzWorkCertificate::getDeleteState, constant.getOne())
                                            .set(ZzWorkCertificate::getCerStatus, constant.getFour())
                                            .set(ZzWorkCertificate::getVersion, version)
                                            .update();
                                }
                            }
                        } catch (Exception e) {
                            log.error("数据主键:{};注销失败:{}", zzWorkCertificate.getId(), e.getMessage());
                        }
                    });
                }
                tashCounter.incrementAndGet();
            }
        } else {
            log.info("查询到需要注销的数据为空！");
        }
        return "重新注销的所有有效数据:" + zzWorkCertificateList.size() + ":" + DateUtil.now();
    }

    /**
     * @Description: 注销所有有效证照数据但注销状态不更新通过styleID
     * @Date: 2021/8/4 13:30
     * @author: wwf
     * @param: styleId
     * @return: java.lang.String
     **/
    @ApiOperation(value = "根据证照类型字段注销所有有效证照数据但注销状态不更新", notes = "")
    @ApiImplicitParam(paramType = "path", name = "styleId", value = "证照类型", required = true, dataType = "String")
    @GetMapping("/zxStyleIdValidBDC/{styleId}")
    @ResponseBody
    public String cancelValidByStyleIdBDC(@PathVariable(value = "styleId") String styleId) {

        List<ZzWorkCertificate> zzWorkCertificateList = new LambdaQueryChainWrapper<>(zzWorkCertificateMapper)
                .eq(ZzWorkCertificate::getStyleId, styleId)
                .eq(ZzWorkCertificate::getDeleteState, constant.getZero())
                .ne(ZzWorkCertificate::getVersion, "999")
                .ne(ZzWorkCertificate::getCerStatus, constant.getFour())
                .last("limit " + constant.getLimitBegin() + "," + constant.getLimitEnd())
                .list();
        log.info("查询到需要注销的证照有效数据总量:{}个", zzWorkCertificateList.size());
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(zzWorkCertificateList)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (zzWorkCertificateList.size() > tashCounter.get()) {
                ZzWorkCertificate zzWorkCertificate = zzWorkCertificateList.get(tashCounter.get());
                if (zzWorkCertificate != null
                        && StrUtil.isNotEmpty(zzWorkCertificate.getCertificateCode())) {
                    executor.execute(() -> {
                        try {
                            IflytekUtils iflytekUtils = new IflytekUtils();
                            Map<String, Object> jsonMap = new HashMap<>();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            //持证者证件号码(身份证或统一信用代码)
                            jsonMap.put("ownerId", zzWorkCertificate.getOwnerId());
                            jsonMap.put("contentName", zzWorkCertificate.getContentName());
                            jsonMap.put("infoCode", zzWorkCertificate.getInfoCode());
                            jsonMap.put("code", zzWorkCertificate.getCertificateCode());
                            jsonMap.put("ownerName", zzWorkCertificate.getOwnerName());
                            jsonMap.put("areaCode", "340800");
                            jsonMap.put("version", zzWorkCertificate.getVersion());
                            jsonMap.put("contentCode", zzWorkCertificate.getContentCode());
                            // 操作类型(新增:I;修改:U;删除:D)
                            jsonMap.put("operateType", "D");
                            jsonMap.put("systemCode", "aqzzk");
                            jsonMap.put("bizId", zzWorkCertificate.getId());
                            jsonMap.put("validBegin", zzWorkCertificate.getInfoValidityBegin().substring(0, 4) + "-"
                                    + zzWorkCertificate.getInfoValidityBegin().substring(4, 6) + "-"
                                    + zzWorkCertificate.getInfoValidityBegin().substring(6, 8));
                            jsonMap.put("validEnd", zzWorkCertificate.getInfoValidityEnd().substring(0, 4) + "-"
                                    + zzWorkCertificate.getInfoValidityEnd().substring(4, 6) + "-"
                                    + zzWorkCertificate.getInfoValidityEnd().substring(6, 8));
                            jsonMap.put("dataSource", constant.getTwo());
                            jsonMap.put("makeTime", zzWorkCertificate.getCreateDate() == null ?
                                    null : simpleDateFormat.format(zzWorkCertificate.getCreateDate()));
                            jsonMap.put("createTime", simpleDateFormat.format(new Date()));
                            jsonMap.put("contentMd5", DigestUtils.md5Hex(zzWorkCertificate.getData()));
                            // 2020-11-28新增 证件类型(证件类型infoTypeCode：个人（满足18位）111或999，法人（满足18位）001或099；signCount：印章数量)
                            jsonMap.put("infoTypeCode", StringUtils.infoTypeCode(zzWorkCertificate.getOwnerId()));
                            // 印章数量	非必填，默认0
                            jsonMap.put("signCount", constant.getZero());
                            String jsonStr = JSON.toJSONString(jsonMap);
                            //索引上报
                            ApiResponse apu = iflytekUtils.key_WEIHU(jsonStr.getBytes(StandardCharsets.UTF_8.name()));
                            String result = new String(apu.getBody(), StandardCharsets.UTF_8.name());
                            log.info("索引上报返回结果:{}", result);
                            JSONObject resObject = JSONObject.parseObject(result);
                            if (!resObject.isEmpty()) {
                                if ("200".equals(resObject.getString("flag"))) {
                                    // 注销成功更新数据版本号为固定值999
                                    boolean row = new LambdaUpdateChainWrapper<>(zzWorkCertificateMapper)
                                            .eq(ZzWorkCertificate::getId, zzWorkCertificate.getId())
                                            .set(ZzWorkCertificate::getVersion, "999")
                                            .update();
                                }
                            }
                        } catch (Exception e) {
                            log.error("数据主键:{};注销失败:{}", zzWorkCertificate.getId(), e.getMessage());
                        }
                    });
                }
                tashCounter.incrementAndGet();
            }
        } else {
            log.info("查询到需要注销的数据为空！");
        }
        return "重新注销的所有不证照有效数据但状态不更新:" + zzWorkCertificateList.size() + ":" + DateUtil.now();
    }

    /**
     * @Description: 注销所有历史注销过的证照数据通过styleID
     * @Date: 2021/6/9 16:50
     * @author: wwf
     * @param: styleId
     * @return: java.lang.String
     **/
    @ApiOperation(value = "根据证照类型字段注销所有历史注销过的证照数据", notes = "")
    @ApiImplicitParam(paramType = "path", name = "styleId", value = "证照类型", required = true, dataType = "String")
    @GetMapping("/zxStyleIdHistory/{styleId}")
    @ResponseBody
    public String cancelHistoryByStyleId(@PathVariable(name = "styleId") String styleId) {

        List<ZzWorkCertificate> zzWorkCertificateList = new LambdaQueryChainWrapper<>(zzWorkCertificateMapper)
                .eq(ZzWorkCertificate::getStyleId, styleId)
                .eq(ZzWorkCertificate::getDeleteState, constant.getOne())
                .eq(ZzWorkCertificate::getCerStatus, constant.getFour())
                .ne(ZzWorkCertificate::getVersion, "888")
                .orderByDesc(ZzWorkCertificate::getCreateDate)
                .last("limit " + constant.getLimitBegin() + "," + constant.getLimitEnd())
                .list();
        log.info("查询到需要注销历史的数据总量:{}个", zzWorkCertificateList.size());
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(zzWorkCertificateList)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (zzWorkCertificateList.size() > tashCounter.get()) {
                ZzWorkCertificate zzWorkCertificate = zzWorkCertificateList.get(tashCounter.get());
                if (zzWorkCertificate != null
                        && StrUtil.isNotEmpty(zzWorkCertificate.getCertificateCode())) {
                    executor.execute(() -> {
                        try {
                            IflytekUtils iflytekUtils = new IflytekUtils();
                            Map<String, Object> jsonMap = new HashMap<>();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            //持证者证件号码(身份证或统一信用代码)
                            jsonMap.put("ownerId", zzWorkCertificate.getOwnerId());
                            jsonMap.put("contentName", zzWorkCertificate.getContentName());
                            jsonMap.put("infoCode", zzWorkCertificate.getInfoCode());
                            jsonMap.put("code", zzWorkCertificate.getCertificateCode());
                            jsonMap.put("ownerName", zzWorkCertificate.getOwnerName());
                            jsonMap.put("areaCode", "340800");
                            jsonMap.put("version", zzWorkCertificate.getVersion());
                            jsonMap.put("contentCode", zzWorkCertificate.getContentCode());
                            // 操作类型(新增:I;修改:U;删除:D)
                            jsonMap.put("operateType", "D");
                            jsonMap.put("systemCode", "aqzzk");
                            jsonMap.put("bizId", zzWorkCertificate.getId());
                            jsonMap.put("validBegin", zzWorkCertificate.getInfoValidityBegin().substring(0, 4) + "-"
                                    + zzWorkCertificate.getInfoValidityBegin().substring(4, 6) + "-"
                                    + zzWorkCertificate.getInfoValidityBegin().substring(6, 8));
                            jsonMap.put("validEnd", zzWorkCertificate.getInfoValidityEnd().substring(0, 4) + "-"
                                    + zzWorkCertificate.getInfoValidityEnd().substring(4, 6) + "-"
                                    + zzWorkCertificate.getInfoValidityEnd().substring(6, 8));
                            jsonMap.put("dataSource", constant.getTwo());
                            jsonMap.put("makeTime", zzWorkCertificate.getCreateDate() == null ?
                                    null : simpleDateFormat.format(zzWorkCertificate.getCreateDate()));
                            jsonMap.put("createTime", simpleDateFormat.format(new Date()));
                            jsonMap.put("contentMd5", DigestUtils.md5Hex(zzWorkCertificate.getData()));
                            // 2020-11-28新增 证件类型(证件类型infoTypeCode：个人（满足18位）111或999，法人（满足18位）001或099；signCount：印章数量)
                            jsonMap.put("infoTypeCode", StringUtils.infoTypeCode(zzWorkCertificate.getOwnerId()));
                            // 印章数量	非必填，默认0
                            jsonMap.put("signCount", constant.getZero());
                            String jsonStr = JSON.toJSONString(jsonMap);
                            //索引上报
                            ApiResponse apu = iflytekUtils.key_WEIHU(jsonStr.getBytes(StandardCharsets.UTF_8.name()));
                            String result = new String(apu.getBody(), StandardCharsets.UTF_8.name());
                            log.info("索引上报返回结果:{}", result);
                            JSONObject resObject = JSONObject.parseObject(result);
                            if (!resObject.isEmpty()) {
                                if ("200".equals(resObject.getString("flag"))) {
                                    // 注销成功更新版本号为固定值999
                                    boolean row = new LambdaUpdateChainWrapper<>(zzWorkCertificateMapper)
                                            .eq(ZzWorkCertificate::getId, zzWorkCertificate.getId())
                                            .set(ZzWorkCertificate::getVersion, "888")
                                            .update();
                                }
                            }
                        } catch (Exception e) {
                            log.error("数据主键:{};注销失败:{}", zzWorkCertificate.getId(), e.getMessage());
                        }
                    });
                }
                tashCounter.incrementAndGet();
            }
        } else {
            log.info("查询到需要注销的数据为空！");
        }
        return "重新注销历史注销的所有数据:" + zzWorkCertificateList.size() + ":" + DateUtil.now();
    }


    /**
     * @Description: 以下为处理讯飞对应身份证数据接口
     **/

    /**
     * @Description: 注销讯飞提供的身份证号的身份证数据(索引未上报的20万数据)
     * @Date: 2021/7/6 16:08
     * @author: wwf
     * @param: styleId
     * @return: java.lang.String
     **/
    @ApiOperation(value = "根据证照类型字段注销讯飞提供的身份证号的身份证数据(索引未上报的20万数据,方法一)", notes = "")
    @ApiImplicitParam(paramType = "path", name = "styleId", value = "证照类型", required = true, dataType = "String")
    @GetMapping("/sfzZx1/{styleId}")
    @ResponseBody
    public String cacelByStyleId1(@PathVariable(name = "styleId") String styleId) {

        List<ZzwkZzwInfo1> zzwkZzwInfo1List = new LambdaQueryChainWrapper<>(zzwkZzwInfo1Mapper)
                //.eq(ZzwkZzwInfo1::getFlag, "0")
                .orderByAsc(ZzwkZzwInfo1::getId)
                .last("limit " + constant.getLimitBegin() + "," + constant.getLimitEnd())
                .list();
        log.info("查询到需要注销的数据总量:{}个", zzwkZzwInfo1List.size());
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(zzwkZzwInfo1List)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (zzwkZzwInfo1List.size() > tashCounter.get()) {
                ZzwkZzwInfo1 zzwkZzwInfo1 = zzwkZzwInfo1List.get(tashCounter.get());
                // 判断身份证号不为空
                if (zzwkZzwInfo1 != null && StrUtil.isNotEmpty(zzwkZzwInfo1.getInfoOwnerId())) {
                    log.info("需要注销的身份证号码:{}", zzwkZzwInfo1.getInfoOwnerId());
                    List<ZzWorkCertificate> zzWorkCertificateList = new LambdaQueryChainWrapper<>(zzWorkCertificateMapper)
                            .eq(ZzWorkCertificate::getStyleId, styleId)
                            .eq(ZzWorkCertificate::getOwnerId, zzwkZzwInfo1.getInfoOwnerId())
                            //.eq(ZzWorkCertificate::getDeleteState, 0)
                            //.ne(ZzWorkCertificate::getCerStatus, 4)
                            .list();
                    if (CollUtil.isNotEmpty(zzWorkCertificateList)) {
                        for (ZzWorkCertificate zzWorkCertificate : zzWorkCertificateList) {
                            executor.execute(() -> {
                                try {
                                    IflytekUtils iflytekUtils = new IflytekUtils();
                                    Map<String, Object> jsonMap = new HashMap<>();
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    //持证者证件号码(身份证或统一信用代码)
                                    jsonMap.put("ownerId", zzWorkCertificate.getOwnerId());
                                    jsonMap.put("contentName", zzWorkCertificate.getContentName());
                                    jsonMap.put("infoCode", zzWorkCertificate.getInfoCode());
                                    jsonMap.put("code", zzWorkCertificate.getCertificateCode());
                                    jsonMap.put("ownerName", zzWorkCertificate.getOwnerName());
                                    jsonMap.put("areaCode", "340800");
                                    jsonMap.put("version", zzWorkCertificate.getVersion());
                                    jsonMap.put("contentCode", zzWorkCertificate.getContentCode());
                                    // 操作类型(新增:I;修改:U;删除:D)
                                    jsonMap.put("operateType", "D");
                                    jsonMap.put("systemCode", "aqzzk");
                                    jsonMap.put("bizId", zzWorkCertificate.getId());
                                    jsonMap.put("validBegin", zzWorkCertificate.getInfoValidityBegin().substring(0, 4) + "-"
                                            + zzWorkCertificate.getInfoValidityBegin().substring(4, 6) + "-"
                                            + zzWorkCertificate.getInfoValidityBegin().substring(6, 8));
                                    jsonMap.put("validEnd", zzWorkCertificate.getInfoValidityEnd().substring(0, 4) + "-"
                                            + zzWorkCertificate.getInfoValidityEnd().substring(4, 6) + "-"
                                            + zzWorkCertificate.getInfoValidityEnd().substring(6, 8));
                                    jsonMap.put("dataSource", constant.getTwo());
                                    jsonMap.put("makeTime", zzWorkCertificate.getCreateDate() == null ?
                                            null : simpleDateFormat.format(zzWorkCertificate.getCreateDate()));
                                    jsonMap.put("createTime", simpleDateFormat.format(new Date()));
                                    jsonMap.put("contentMd5", DigestUtils.md5Hex(zzWorkCertificate.getData()));
                                    // 2020-11-28新增 证件类型(证件类型infoTypeCode：个人（满足18位）111或999，法人（满足18位）001或099；signCount：印章数量)
                                    jsonMap.put("infoTypeCode", StringUtils.infoTypeCode(zzWorkCertificate.getOwnerId()));
                                    // 印章数量	非必填，默认0
                                    jsonMap.put("signCount", constant.getZero());
                                    String jsonStr = JSON.toJSONString(jsonMap);
                                    //索引上报
                                    ApiResponse apu = iflytekUtils.key_WEIHU(jsonStr.getBytes(StandardCharsets.UTF_8.name()));
                                    String result = new String(apu.getBody(), StandardCharsets.UTF_8.name());
                                    log.info("索引上报返回结果:{}", result);
                                    JSONObject resObject = JSONObject.parseObject(result);
                                    if (!resObject.isEmpty()) {
                                        if ("200".equals(resObject.getString("flag"))) {
                                            // 注销成功更新数据状态
                                            DecimalFormat countFormat = new DecimalFormat("0000");
                                            String version = countFormat.format(Integer.parseInt(zzWorkCertificate.getVersion()) + 1);
                                            boolean row = new LambdaUpdateChainWrapper<>(zzWorkCertificateMapper)
                                                    .eq(ZzWorkCertificate::getId, zzWorkCertificate.getId())
                                                    .set(ZzWorkCertificate::getDeleteState, constant.getOne())
                                                    .set(ZzWorkCertificate::getCerStatus, constant.getFour())
                                                    .set(ZzWorkCertificate::getVersion, version)
                                                    .update();
                                            // 更新数梦库标识字段
/*                                            boolean row1 = new LambdaUpdateChainWrapper<>(zzwkZzwInfo1Mapper)
                                                    .eq(ZzwkZzwInfo1::getId, zzwkZzwInfo1.getId())
                                                    .set(ZzwkZzwInfo1::getFlag, "1")
                                                    .update();*/

                                        }
                                    }
                                } catch (Exception e) {
                                    log.error("注销失败:{},注销失败的身份证号:{}", e.getMessage(), zzwkZzwInfo1.getInfoOwnerId());
                                }
                            });
                        }
                    } else {
                        // 未查询到的证照数据，更新数梦库标识字段为2
                        boolean row1 = new LambdaUpdateChainWrapper<>(zzwkZzwInfo1Mapper)
                                .eq(ZzwkZzwInfo1::getId, zzwkZzwInfo1.getId())
                                .set(ZzwkZzwInfo1::getFlag, constant.getTwo())
                                .update();
                    }
                }
                tashCounter.incrementAndGet();
            }

        } else {
            log.info("查询到需要注销的数据为空！");
        }
        return "重新注销的所有数据:" + zzwkZzwInfo1List.size() + ":" + DateUtil.now();

    }

    /**
     * @Description: 注销讯飞提供的身份证号的身份证数据(索引未上报的20万数据)
     * @Date: 2021/7/6 16:08
     * @author: wwf
     * @param: styleId
     * @return: java.lang.String
     **/
    @ApiOperation(value = "根据证照类型字段注销讯飞提供的身份证号的身份证数据(索引未上报的20万数据,方法二)", notes = "")
    @ApiImplicitParam(paramType = "path", name = "styleId", value = "证照类型", required = true, dataType = "String")
    @GetMapping("/sfzZx2/{styleId}")
    @ResponseBody
    public String cacelByStyleId2(@PathVariable(name = "styleId") String styleId) {

        List<ZzwkZzwInfo2> zzwkZzwInfo2List = new LambdaQueryChainWrapper<>(zzwkZzwInfo2Mapper)
                //.eq(ZzwkZzwInfo2::getFlag, "0")
                .orderByAsc(ZzwkZzwInfo2::getId)
                .last("limit " + constant.getLimitBegin() + "," + constant.getLimitEnd())
                .list();
        log.info("查询到需要注销的数据总量:{}个", zzwkZzwInfo2List.size());
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(zzwkZzwInfo2List)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (zzwkZzwInfo2List.size() > tashCounter.get()) {
                ZzwkZzwInfo2 zzwkZzwInfo2 = zzwkZzwInfo2List.get(tashCounter.get());
                // 判断身份证号不为空
                if (zzwkZzwInfo2 != null && StrUtil.isNotEmpty(zzwkZzwInfo2.getInfoOwnerId())) {
                    log.info("需要注销的身份证号码:{}", zzwkZzwInfo2.getInfoOwnerId());
                    List<ZzWorkCertificate> zzWorkCertificateList = new LambdaQueryChainWrapper<>(zzWorkCertificateMapper)
                            .eq(ZzWorkCertificate::getStyleId, styleId)
                            .eq(ZzWorkCertificate::getOwnerId, zzwkZzwInfo2.getInfoOwnerId())
                            //.eq(ZzWorkCertificate::getDeleteState, 0)
                            //.ne(ZzWorkCertificate::getCerStatus, 4)
                            .list();
                    if (CollUtil.isNotEmpty(zzWorkCertificateList)) {
                        for (ZzWorkCertificate zzWorkCertificate : zzWorkCertificateList) {
                            executor.execute(() -> {
                                try {
                                    IflytekUtils iflytekUtils = new IflytekUtils();
                                    Map<String, Object> jsonMap = new HashMap<>();
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    //持证者证件号码(身份证或统一信用代码)
                                    jsonMap.put("ownerId", zzWorkCertificate.getOwnerId());
                                    jsonMap.put("contentName", zzWorkCertificate.getContentName());
                                    jsonMap.put("infoCode", zzWorkCertificate.getInfoCode());
                                    jsonMap.put("code", zzWorkCertificate.getCertificateCode());
                                    jsonMap.put("ownerName", zzWorkCertificate.getOwnerName());
                                    jsonMap.put("areaCode", "340800");
                                    jsonMap.put("version", zzWorkCertificate.getVersion());
                                    jsonMap.put("contentCode", zzWorkCertificate.getContentCode());
                                    // 操作类型(新增:I;修改:U;删除:D)
                                    jsonMap.put("operateType", "D");
                                    jsonMap.put("systemCode", "aqzzk");
                                    jsonMap.put("bizId", zzWorkCertificate.getId());
                                    jsonMap.put("validBegin", zzWorkCertificate.getInfoValidityBegin().substring(0, 4) + "-"
                                            + zzWorkCertificate.getInfoValidityBegin().substring(4, 6) + "-"
                                            + zzWorkCertificate.getInfoValidityBegin().substring(6, 8));
                                    jsonMap.put("validEnd", zzWorkCertificate.getInfoValidityEnd().substring(0, 4) + "-"
                                            + zzWorkCertificate.getInfoValidityEnd().substring(4, 6) + "-"
                                            + zzWorkCertificate.getInfoValidityEnd().substring(6, 8));
                                    jsonMap.put("dataSource", constant.getTwo());
                                    jsonMap.put("makeTime", zzWorkCertificate.getCreateDate() == null ?
                                            null : simpleDateFormat.format(zzWorkCertificate.getCreateDate()));
                                    jsonMap.put("createTime", simpleDateFormat.format(new Date()));
                                    jsonMap.put("contentMd5", DigestUtils.md5Hex(zzWorkCertificate.getData()));
                                    // 2020-11-28新增 证件类型(证件类型infoTypeCode：个人（满足18位）111或999，法人（满足18位）001或099；signCount：印章数量)
                                    jsonMap.put("infoTypeCode", StringUtils.infoTypeCode(zzWorkCertificate.getOwnerId()));
                                    // 印章数量	非必填，默认0
                                    jsonMap.put("signCount", constant.getZero());
                                    String jsonStr = JSON.toJSONString(jsonMap);
                                    //索引上报
                                    ApiResponse apu = iflytekUtils.key_WEIHU(jsonStr.getBytes(StandardCharsets.UTF_8.name()));
                                    String result = new String(apu.getBody(), StandardCharsets.UTF_8.name());
                                    log.info("索引上报返回结果:{}", result);
                                    JSONObject resObject = JSONObject.parseObject(result);
                                    if (!resObject.isEmpty()) {
                                        if ("200".equals(resObject.getString("flag"))) {
                                            // 注销成功更新数据状态
                                            DecimalFormat countFormat = new DecimalFormat("0000");
                                            String version = countFormat.format(Integer.parseInt(zzWorkCertificate.getVersion()) + 1);
                                            boolean row = new LambdaUpdateChainWrapper<>(zzWorkCertificateMapper)
                                                    .eq(ZzWorkCertificate::getId, zzWorkCertificate.getId())
                                                    .set(ZzWorkCertificate::getDeleteState, constant.getOne())
                                                    .set(ZzWorkCertificate::getCerStatus, constant.getFour())
                                                    .set(ZzWorkCertificate::getVersion, version)
                                                    .update();

                                            // 更新数梦库标识字段
/*                                            boolean row1 = new LambdaUpdateChainWrapper<>(zzwkZzwInfo2Mapper)
                                                    .eq(ZzwkZzwInfo2::getId, zzwkZzwInfo2.getId())
                                                    .set(ZzwkZzwInfo2::getFlag, "1")
                                                    .update();*/
                                        }
                                    }
                                } catch (Exception e) {
                                    log.error("注销失败:{},注销失败的身份证号:{}", e.getMessage(), zzwkZzwInfo2.getInfoOwnerId());
                                }
                            });
                        }
                    } else {
                        // 未查询到的证照数据，更新数梦库标识字段为2
                        boolean row1 = new LambdaUpdateChainWrapper<>(zzwkZzwInfo2Mapper)
                                .eq(ZzwkZzwInfo2::getId, zzwkZzwInfo2.getId())
                                .set(ZzwkZzwInfo2::getFlag, constant.getTwo())
                                .update();
                    }

                }
                tashCounter.incrementAndGet();
            }

        } else {
            log.info("查询到需要注销的数据为空！");
        }
        return "重新注销的所有数据:" + zzwkZzwInfo2List.size() + ":" + DateUtil.now();

    }

    /**
     * @Description: 讯飞根据身份证号注销历史有效身份证证照数据(20210819提供给讯飞使用)
     * http://59.203.216.189:9008/zxSfz/{身份证号}
     * @Date: 2021/8/11 13:55
     * @author: wwf
     * @param: ownerId
     * @return: java.lang.String
     **/
    @ApiOperation(value = "根据身份证号注销历史有效身份证证照数据", notes = "")
    @ApiImplicitParam(paramType = "path", name = "ownerId", value = "身份证号", required = true, dataType = "String")
    @GetMapping("/zxSfz/{ownerId}")
    @ResponseBody
    public String cancelSfzByOwnerId(@PathVariable String ownerId) {

        List<ZzWorkCertificate> zzWorkCertificateList = new LambdaQueryChainWrapper<>(zzWorkCertificateMapper)
                .eq(ZzWorkCertificate::getStyleId, "ff80808162e240cf0162fa7878e50259")
                .eq(ZzWorkCertificate::getDeleteState, constant.getZero())
                .eq(ZzWorkCertificate::getOwnerId, ownerId.trim())
                .ne(ZzWorkCertificate::getCerStatus, constant.getFour())
                .list();
        log.info("查询到根据身份证号:{},需要注销的有效总量:{}个", ownerId, zzWorkCertificateList.size());
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(zzWorkCertificateList)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (zzWorkCertificateList.size() > tashCounter.get()) {
                ZzWorkCertificate zzWorkCertificate = zzWorkCertificateList.get(tashCounter.get());
                if (zzWorkCertificate != null
                        && StrUtil.isNotEmpty(zzWorkCertificate.getCertificateCode())) {
                    executor.execute(() -> {
                        try {
                            IflytekUtils iflytekUtils = new IflytekUtils();
                            Map<String, Object> jsonMap = new HashMap<>();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            //持证者证件号码(身份证或统一信用代码)
                            jsonMap.put("ownerId", zzWorkCertificate.getOwnerId());
                            jsonMap.put("contentName", zzWorkCertificate.getContentName());
                            jsonMap.put("infoCode", zzWorkCertificate.getInfoCode());
                            jsonMap.put("code", zzWorkCertificate.getCertificateCode());
                            jsonMap.put("ownerName", zzWorkCertificate.getOwnerName());
                            jsonMap.put("areaCode", "340800");
                            jsonMap.put("version", zzWorkCertificate.getVersion());
                            jsonMap.put("contentCode", zzWorkCertificate.getContentCode());
                            // 操作类型(新增:I;修改:U;删除:D)
                            jsonMap.put("operateType", "D");
                            jsonMap.put("systemCode", "aqzzk");
                            jsonMap.put("bizId", zzWorkCertificate.getId());
                            jsonMap.put("validBegin", zzWorkCertificate.getInfoValidityBegin().substring(0, 4) + "-"
                                    + zzWorkCertificate.getInfoValidityBegin().substring(4, 6) + "-"
                                    + zzWorkCertificate.getInfoValidityBegin().substring(6, 8));
                            jsonMap.put("validEnd", zzWorkCertificate.getInfoValidityEnd().substring(0, 4) + "-"
                                    + zzWorkCertificate.getInfoValidityEnd().substring(4, 6) + "-"
                                    + zzWorkCertificate.getInfoValidityEnd().substring(6, 8));
                            jsonMap.put("dataSource", constant.getTwo());
                            jsonMap.put("makeTime", zzWorkCertificate.getCreateDate() == null ?
                                    null : simpleDateFormat.format(zzWorkCertificate.getCreateDate()));
                            jsonMap.put("createTime", simpleDateFormat.format(new Date()));
                            jsonMap.put("contentMd5", DigestUtils.md5Hex(zzWorkCertificate.getData()));
                            // 2020-11-28新增 证件类型(证件类型infoTypeCode：个人（满足18位）111或999，法人（满足18位）001或099；signCount：印章数量)
                            jsonMap.put("infoTypeCode", StringUtils.infoTypeCode(zzWorkCertificate.getOwnerId()));
                            // 印章数量	非必填，默认0
                            jsonMap.put("signCount", constant.getZero());
                            String jsonStr = JSON.toJSONString(jsonMap);
                            //索引上报
                            ApiResponse apu = iflytekUtils.key_WEIHU(jsonStr.getBytes(StandardCharsets.UTF_8.name()));
                            String result = new String(apu.getBody(), StandardCharsets.UTF_8.name());
                            log.info("索引上报返回结果:{}", result);
                            JSONObject resObject = JSONObject.parseObject(result);
                            if (!resObject.isEmpty()) {
                                if ("200".equals(resObject.getString("flag"))) {
                                    // 注销成功更新数据状态
                                    DecimalFormat countFormat = new DecimalFormat("0000");
                                    String version = countFormat.format(Integer.parseInt(zzWorkCertificate.getVersion()) + 1);
                                    boolean row = new LambdaUpdateChainWrapper<>(zzWorkCertificateMapper)
                                            .eq(ZzWorkCertificate::getId, zzWorkCertificate.getId())
                                            .set(ZzWorkCertificate::getDeleteState, constant.getOne())
                                            .set(ZzWorkCertificate::getCerStatus, constant.getFour())
                                            .set(ZzWorkCertificate::getVersion, version)
                                            .update();
                                }
                            }
                        } catch (Exception e) {
                            log.error("数据主键:{};注销失败:{}", zzWorkCertificate.getId(), e.getMessage());
                        }
                    });
                }
                tashCounter.incrementAndGet();
            }
        } else {
            log.info("查询到根据身份证号:{},需要注销的数据为空！", ownerId);
        }
        return "根据身份证号[" + ownerId + "]需要注销的有效总量数据：" + zzWorkCertificateList.size() + ":" + DateUtil.now();
    }

    @ApiOperation(value = "根据数据库记录查询注销的数据", notes = "")
    @GetMapping("/xfZx")
    @ResponseBody
    public String cancel() {

        List<ZxEntity> zxEntityList = new LambdaQueryChainWrapper<>(zxEntityMapper)
                //.last("limit " + constant.getLimitBegin() + "," + constant.getLimitEnd())
                .isNull(ZxEntity::getSf)
                .list();
        log.info("查询到需要注销的有效数据总量:{}个", zxEntityList.size());
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(zxEntityList)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (zxEntityList.size() > tashCounter.get()) {
                ZxEntity entity = zxEntityList.get(tashCounter.get());
                if (entity != null
                        && StrUtil.isNotEmpty(entity.getId())) {
                    executor.execute(() -> {
                        try {
                            ZzWorkCertificate zzWorkCertificate = new LambdaQueryChainWrapper<>(zzWorkCertificateMapper)
                                    .eq(ZzWorkCertificate::getId, entity.getId())
                                    .one();
                            if (zzWorkCertificate != null) {
                                IflytekUtils iflytekUtils = new IflytekUtils();
                                Map<String, Object> jsonMap = new HashMap<>();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                //持证者证件号码(身份证或统一信用代码)
                                jsonMap.put("ownerId", zzWorkCertificate.getOwnerId());
                                jsonMap.put("contentName", zzWorkCertificate.getContentName());
                                jsonMap.put("infoCode", zzWorkCertificate.getInfoCode());
                                jsonMap.put("code", zzWorkCertificate.getCertificateCode());
                                jsonMap.put("ownerName", zzWorkCertificate.getOwnerName());
                                jsonMap.put("areaCode", "340800");
                                jsonMap.put("version", zzWorkCertificate.getVersion());
                                jsonMap.put("contentCode", zzWorkCertificate.getContentCode());
                                // 操作类型(新增:I;修改:U;删除:D)
                                jsonMap.put("operateType", "D");
                                jsonMap.put("systemCode", "aqzzk");
                                jsonMap.put("bizId", zzWorkCertificate.getId());
                                jsonMap.put("validBegin", zzWorkCertificate.getInfoValidityBegin().substring(0, 4) + "-"
                                        + zzWorkCertificate.getInfoValidityBegin().substring(4, 6) + "-"
                                        + zzWorkCertificate.getInfoValidityBegin().substring(6, 8));
                                jsonMap.put("validEnd", zzWorkCertificate.getInfoValidityEnd().substring(0, 4) + "-"
                                        + zzWorkCertificate.getInfoValidityEnd().substring(4, 6) + "-"
                                        + zzWorkCertificate.getInfoValidityEnd().substring(6, 8));
                                jsonMap.put("dataSource", constant.getTwo());
                                jsonMap.put("makeTime", zzWorkCertificate.getCreateDate() == null ?
                                        null : simpleDateFormat.format(zzWorkCertificate.getCreateDate()));
                                jsonMap.put("createTime", simpleDateFormat.format(new Date()));
                                jsonMap.put("contentMd5", DigestUtils.md5Hex(zzWorkCertificate.getData()));
                                // 2020-11-28新增 证件类型(证件类型infoTypeCode：个人（满足18位）111或999，法人（满足18位）001或099；signCount：印章数量)
                                jsonMap.put("infoTypeCode", StringUtils.infoTypeCode(zzWorkCertificate.getOwnerId()));
                                // 印章数量	非必填，默认0
                                jsonMap.put("signCount", constant.getZero());
                                String jsonStr = JSON.toJSONString(jsonMap);
                                //索引上报
                                ApiResponse apu = iflytekUtils.key_WEIHU(jsonStr.getBytes(StandardCharsets.UTF_8.name()));
                                String result = new String(apu.getBody(), StandardCharsets.UTF_8.name());
                                log.info("索引上报返回结果:{}", result);
                                JSONObject resObject = JSONObject.parseObject(result);
                                if (!resObject.isEmpty()) {
                                    if ("200".equals(resObject.getString("flag"))) {
                                        // 注销成功更新数据状态
                                        DecimalFormat countFormat = new DecimalFormat("0000");
                                        String version = countFormat.format(Integer.parseInt(zzWorkCertificate.getVersion()) + 1);
                                        boolean row = new LambdaUpdateChainWrapper<>(zzWorkCertificateMapper)
                                                .eq(ZzWorkCertificate::getId, zzWorkCertificate.getId())
                                                .set(ZzWorkCertificate::getDeleteState, constant.getOne())
                                                .set(ZzWorkCertificate::getCerStatus, constant.getFour())
                                                .set(ZzWorkCertificate::getVersion, version)
                                                .update();
                                        new LambdaUpdateChainWrapper<>(zxEntityMapper)
                                                .eq(ZxEntity::getId, entity.getId())
                                                .set(ZxEntity::getSf, "S")
                                                .update();
                                    } else {
                                        new LambdaUpdateChainWrapper<>(zxEntityMapper)
                                                .eq(ZxEntity::getId, entity.getId())
                                                .set(ZxEntity::getSf, "S")
                                                .set(ZxEntity::getResult, result)
                                                .update();
                                    }
                                }
                            } else {
                                new LambdaUpdateChainWrapper<>(zxEntityMapper)
                                        .eq(ZxEntity::getId, entity.getId())
                                        .set(ZxEntity::getSf, "F")
                                        .update();
                            }

                        } catch (Exception e) {
                            log.error("数据主键:{};注销失败:{}", entity.getId(), e.getMessage());
                        }
                    });
                }
                tashCounter.incrementAndGet();
            }
        }
        return "注销的所有数据:" + zxEntityList.size() + ":" + DateUtil.now();
    }

}
