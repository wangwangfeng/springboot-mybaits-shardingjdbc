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
import com.zfsoft.certificate.pojo.SenileEntity;
import com.zfsoft.certificate.pojo.ZzWorkCertificate;
import com.zfsoft.certificate.pojo.base.Constant;
import com.zfsoft.certificate.util.IflytekUtils;
import com.zfsoft.certificate.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 证照索引上报相关后台
 *
 * @author 86131
 */
@Api(tags = "证照索引上报后台")
@Slf4j
@Controller
@RequestMapping(value = "/upload")
public class IndexReportController {

    @Autowired
    private Constant constant;

    /**
     * 消费线程计数器
     */
    public static AtomicInteger tashCounter = new AtomicInteger();

    @Autowired
    private ZzWorkCertificateMapper zzWorkCertificateMapper;

    /**
     * 索引重新上报根据ID
     *
     * @param operateType 上报类型
     * @param id          主键
     * @return
     */
    @ApiOperation(value = "根据主键索引重新上报", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "operateType", value = "上报格式", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "id", value = "主键", required = true, dataType = "String")
    })
    @RequestMapping(value = "/id/{operateType}/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String indexReportById(@PathVariable(name = "operateType") String operateType,
                                  @PathVariable(name = "id") String id) {

        try {
            // ZzWorkCertificate zzWorkCertificate = new LambdaQueryChainWrapper<>(zzWorkCertificateMapper).eq(ZzWorkCertificate::getId, id).one();
            ZzWorkCertificate zzWorkCertificate = zzWorkCertificateMapper.selectById(id);
            String result = upload(zzWorkCertificate, operateType);
            JSONObject resObject = JSONObject.parseObject(result);
            if (!resObject.isEmpty()) {
                if ("200".equals(resObject.getString("flag"))) {
                    // 更新上报标识为1
                    boolean row = new LambdaUpdateChainWrapper<>(zzWorkCertificateMapper)
                            .eq(ZzWorkCertificate::getId, zzWorkCertificate.getId())
                            .set(ZzWorkCertificate::getPostFlag, constant.getOne())
                            .update();
                }
            }
            return result;
        } catch (Exception e) {
            log.error("上报异常:{}", e.getMessage());
            return e.getMessage();
        }

    }

    /**
     * 索引重新上报根据上报标识postFlag
     * @param operateType
     * @param postFlag
     */
    @ApiOperation(value = "根据上报标识所有索引重新上报", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "operateType", value = "上报格式", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "postFlag", value = "上报标识", required = true, dataType = "String")
    })
    @RequestMapping(value = "/postFlag/{operateType}/{postFlag}", method = RequestMethod.GET)
    @ResponseBody
    public void indexReportByPostFlag(@PathVariable(name = "operateType") String operateType,
                                      @PathVariable(name = "postFlag") String postFlag) {

        try {
            if (StrUtil.isNotEmpty(postFlag)) {
                if (!"0".equals(postFlag)
                        && !"1".equals(postFlag)) {
                    postFlag = "0";
                }
            } else {
                postFlag = "0";
            }
            /**
             * 查询删除状态是0，制证状态，有效期起小于当前日期，有效期结束大于当前日期的数据
             */
            List<ZzWorkCertificate> zzWorkCertificateList = new LambdaQueryChainWrapper<>(zzWorkCertificateMapper)
                    .eq(ZzWorkCertificate::getPostFlag, postFlag)
                    .eq(ZzWorkCertificate::getDeleteState, "0")
                    .le(ZzWorkCertificate::getInfoValidityBegin, DateUtil.format(new Date(), "yyyyMMdd"))
                    .ge(ZzWorkCertificate::getInfoValidityEnd, DateUtil.format(new Date(), "yyyyMMdd"))
                    .in(ZzWorkCertificate::getCerStatus, Arrays.asList("0", "6", "9"))
                    .orderByDesc(ZzWorkCertificate::getCreateDate)
                    .list();
            tashCounter.set(0);
            if (CollUtil.isNotEmpty(zzWorkCertificateList)) {
                log.info("上报所有数据:{}个", zzWorkCertificateList.size());
                ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                        constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
                while (zzWorkCertificateList.size() > tashCounter.get()) {
                    ZzWorkCertificate zzWorkCertificate = zzWorkCertificateList.get(tashCounter.get());
                    if (zzWorkCertificate != null && StrUtil.isNotEmpty(zzWorkCertificate.getCertificateCode())) {
                        executor.execute(() -> {
                            String result = upload(zzWorkCertificate,operateType);
                            JSONObject resObject = JSONObject.parseObject(result);
                            if (!resObject.isEmpty()) {
                                if ("200".equals(resObject.getString("flag"))) {
                                    // 更新上报标识为1
                                    boolean row = new LambdaUpdateChainWrapper<>(zzWorkCertificateMapper)
                                            .eq(ZzWorkCertificate::getId, zzWorkCertificate.getId())
                                            .set(ZzWorkCertificate::getPostFlag, constant.getOne())
                                            .update();
                                }
                            }
                        });
                    }
                    tashCounter.incrementAndGet();
                }
            }
        } catch (Exception e) {
            log.error("上报异常:{}", e.getMessage());
        }

    }

    /**
     * 索引上报
     *
     * @param zzWorkCertificate
     * @param operateType       操作类型(新增:I;修改:U;删除:D)
     * @return
     */
    public String upload(ZzWorkCertificate zzWorkCertificate, String operateType) {

        try {
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
                if (StrUtil.isNotEmpty(operateType)) {
                    if ("I".equalsIgnoreCase(operateType)) {
                        jsonMap.put("operateType", "I");
                    } else if ("U".equalsIgnoreCase(operateType)) {
                        jsonMap.put("operateType", "U");
                    } else {
                        jsonMap.put("operateType", "U");
                    }
                } else {
                    jsonMap.put("operateType", "U");
                }
                jsonMap.put("systemCode", "aqzzk");
                jsonMap.put("bizId", zzWorkCertificate.getId());
                jsonMap.put("validBegin", zzWorkCertificate.getInfoValidityBegin().substring(0, 4) + "-"
                        + zzWorkCertificate.getInfoValidityBegin().substring(4, 6) + "-"
                        + zzWorkCertificate.getInfoValidityBegin().substring(6, 8));
                jsonMap.put("validEnd", zzWorkCertificate.getInfoValidityEnd().substring(0, 4) + "-"
                        + zzWorkCertificate.getInfoValidityEnd().substring(4, 6) + "-"
                        + zzWorkCertificate.getInfoValidityEnd().substring(6, 8));
                jsonMap.put("dataSource", "2");
                jsonMap.put("makeTime", zzWorkCertificate.getCreateDate() == null ?
                        null : simpleDateFormat.format(zzWorkCertificate.getCreateDate()));
                jsonMap.put("createTime", simpleDateFormat.format(new Date()));
                jsonMap.put("contentMd5", DigestUtils.md5Hex(zzWorkCertificate.getData()));
                // 2020-11-28新增 证件类型(证件类型infoTypeCode：个人（满足18位）111或999，法人（满足18位）001或099；signCount：印章数量)
                jsonMap.put("infoTypeCode", StringUtils.infoTypeCode(zzWorkCertificate.getOwnerId()));
                // 印章数量	非必填，默认0
                jsonMap.put("signCount", "0");
                String jsonStr = JSON.toJSONString(jsonMap);
                //索引上报
                ApiResponse apu = iflytekUtils.key_WEIHU(jsonStr.getBytes(StandardCharsets.UTF_8.name()));
                String result = new String(apu.getBody(), StandardCharsets.UTF_8.name());
                log.info("索引上报返回结果:{}", result);
                return result;
            } else {
                return "数据为空！";
            }
        } catch (Exception e) {
            log.error("异常:{}", e.getMessage());
            return "失败！";
        }
    }

}
