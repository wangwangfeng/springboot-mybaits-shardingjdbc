package com.zfsoft.certificate.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.zfsoft.certificate.mapper.db1.*;
import com.zfsoft.certificate.pojo.*;
import com.zfsoft.certificate.pojo.base.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 一期附件处理复制
 * 从证照主表查询符合条件的附件插入附件复制表中
 * 为了正式环境一期附件同步到二期FASTDFS中
 * <p>
 * 附件路径先做处理(统一去掉盘符，确保首目录是:elms_files/**)
 * 这边不需要判断路径文件是否存在，统一在批量导入小程序中加上盘符判断
 * <p>
 * -- elms/ofd/20181217/
 * -- I:/elms_files/ofd_temp/
 * -- I:/elms_files/elms/ofd/11340800003110803D0053/20201209/
 * -- Z:/elms_files/elms/ofd/11340825003129846T0025/20210301/
 *
 * @author 86131
 */
@Api(tags = "一期附件处理后台")
@Slf4j
@Controller
public class AttachCopyController {

    @Autowired
    private Constant constant;

    /**
     * 消费线程计数器
     */
    public static AtomicInteger tashCounter = new AtomicInteger();

    @Autowired
    private ZzWorkCertificateMapper zzWorkCertificateMapper;

    @Autowired
    private SysAttaMapper sysAttaMapper;

    @Autowired
    private SysAttaCopyMapper sysAttaCopyMapper;

    @Autowired
    private ZzwkCertificateLogsMapper zzwkCertificateLogsMapper;

    @Autowired
    private ZzwkCertificateVersionRecordMapper zzwkCertificateVersionRecordMapper;

    @Autowired
    private HistoryMapper historyMapper;

    /**
     * 一期附件处理复制
     * 废弃
     *
     * @return
     */
    @ApiIgnore
    @ApiOperation(value = "一期附件处理复制", notes = "废弃")
    @RequestMapping(value = "/copy", method = RequestMethod.GET)
    @ResponseBody
    public String attachCopy() {

        /**
         * 查询所有符合条件的制证附件数据
         */
        List<ZzWorkCertificate> zzWorkCertificates = new LambdaQueryChainWrapper<>(zzWorkCertificateMapper)
                .eq(ZzWorkCertificate::getDeleteState, "0")
                .in(ZzWorkCertificate::getCerStatus, Arrays.asList("0", "6", "9"))
                .orderByAsc(ZzWorkCertificate::getCreateDate)
                .list();

        log.info("有效证照附件处理的总数据量:" + zzWorkCertificates.size() + "个");
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(zzWorkCertificates)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (zzWorkCertificates.size() > tashCounter.get()) {
                ZzWorkCertificate entity = zzWorkCertificates.get(tashCounter.get());
                if (entity != null) {
                    if (StrUtil.isNotEmpty(entity.getAttaOid())) {
                        executor.execute(() -> {
                            try {
                                /**
                                 * 通过主键查询附件是否存在附件复制表中
                                 * 存在不处理，不存在则插入数据
                                 */
                                SysAttaCopy sysAttaCopy = new LambdaQueryChainWrapper<>(sysAttaCopyMapper)
                                        .eq(SysAttaCopy::getOid, entity.getAttaOid())
                                        .one();
                                if (sysAttaCopy == null) {
                                    SysAtta sysAtta = new LambdaQueryChainWrapper<>(sysAttaMapper)
                                            .eq(SysAtta::getOid, entity.getAttaOid())
                                            .eq(SysAtta::getIsDelete, "N")
                                            .one();
                                    if (sysAtta != null) {
                                        sysAttaCopy = new SysAttaCopy();
                                        BeanUtil.copyProperties(sysAtta, sysAttaCopy);
                                        //N未同步，Y已同步，F同步失败
                                        sysAttaCopy.setSyncStatus("N");
                                        //
                                        String filePath = sysAtta.getFilePath();
                                        if (StrUtil.isNotEmpty(filePath)) {
                                            if (filePath.contains(constant.getDiskZ())) {
                                                sysAttaCopy.setFilePath(filePath.replace(constant.getDiskZ(), ""));
                                            }
                                            // 处理不同系统路径问题 一期附件暂时不需要
                                            //sysAttaCopy.setFilePath(resultFilePath.replaceAll("/", Matcher.quoteReplacement(File.separator)).replaceAll("\\\\", Matcher.quoteReplacement(File.separator)));
                                        }
                                        sysAttaCopyMapper.insert(sysAttaCopy);
                                    }
                                }
                            } catch (Exception e) {
                                log.error("附件处理异常信息:{}", e.getMessage());
                            }
                        });
                    }
                }
                tashCounter.incrementAndGet();
            }
        }

        return "有效证照附件处理的总数据量：" + zzWorkCertificates.size() + ":" + DateUtil.now();
    }

    /**
     * 删除已注销证照数据和附件数据(盘符附件未处理)
     * 按照10万的处理数据
     *
     * @return
     */
    @ApiOperation(value = "删除已注销证照数据和附件数据", notes = "")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public String deleteAttach() {

        /**
         * 查询所有删除状态的制证附件数据
         * cerStatus-0,4,6,9
         * 附件删除成功设置状态为 7， 附件标识为 Y
         */
        List<ZzWorkCertificate> zzWorkCertificates = new LambdaQueryChainWrapper<>(zzWorkCertificateMapper)
                .eq(ZzWorkCertificate::getDeleteState, "1")
                .ne(ZzWorkCertificate::getCerStatus, "7")
                .orderByDesc(ZzWorkCertificate::getCreateDate)
                .last("limit " + constant.getLimitBegin() + "," + constant.getLimitEnd())
                .list();

        log.info("无效证照附件处理的总数据量:" + zzWorkCertificates.size() + "个");
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(zzWorkCertificates)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (zzWorkCertificates.size() > tashCounter.get()) {
                ZzWorkCertificate entity = zzWorkCertificates.get(tashCounter.get());
                if (entity != null) {
                    if (StrUtil.isNotEmpty(entity.getAttaOid())) {
                        executor.execute(() -> {
                            try {
                                SysAtta sysAtta = new LambdaQueryChainWrapper<>(sysAttaMapper)
                                        .eq(SysAtta::getOid, entity.getAttaOid())
                                        .one();
                                if (sysAtta != null) {
                                    String filePath = constant.getMakeDiskZ() + sysAtta.getFilePath() + sysAtta.getName();
                                    File file = new File(filePath);
                                    log.info("附件主键:{}", entity.getAttaOid());
                                    if (file.exists() && file.isFile()) {
                                        file.delete();
                                    }

                                    // 删除证照表数据
                                    //zzWorkCertificateMapper.deleteById(entity.getId());
                                    // 删除日志表数据
                                    LambdaQueryWrapper<ZzwkCertificateLogs> deleteLogs = new LambdaQueryWrapper<ZzwkCertificateLogs>().eq(ZzwkCertificateLogs::getAttaOid, entity.getAttaOid());
                                    zzwkCertificateLogsMapper.delete(deleteLogs);
                                    // 删除维护记录表数据
                                    LambdaQueryWrapper<ZzwkCertificateVersionRecord> deleteVersion = new LambdaQueryWrapper<ZzwkCertificateVersionRecord>().eq(ZzwkCertificateVersionRecord::getAttaOid, entity.getAttaOid());
                                    zzwkCertificateVersionRecordMapper.delete(deleteVersion);
                                    // 删除附件表数据
                                    //sysAttaMapper.deleteById(entity.getAttaOid());

                                    // 删除成功更新附件状态
                                    LambdaUpdateChainWrapper<SysAtta> updateSysAtta = new LambdaUpdateChainWrapper<>(sysAttaMapper);
                                    updateSysAtta.eq(SysAtta::getOid, entity.getAttaOid()).set(SysAtta::getIsDelete, "Y").update();

                                    // 删除成功更新标识
                                    LambdaUpdateChainWrapper<ZzWorkCertificate> updateZzwk = new LambdaUpdateChainWrapper<>(zzWorkCertificateMapper);
                                    updateZzwk.eq(ZzWorkCertificate::getId, entity.getId()).set(ZzWorkCertificate::getCerStatus, "7").update();

                                }

                            } catch (Exception e) {
                                // 删除失败时更新附件状态
                                //LambdaUpdateChainWrapper<SysAtta> updateSysAtta = new LambdaUpdateChainWrapper<>(sysAttaMapper);
                                //updateSysAtta.eq(SysAtta::getOid, entity.getAttaOid()).set(SysAtta::getIsDelete, "Y").update();
                                log.error("无效数据删除异常信息:{}", e.getMessage());
                            }
                        });
                    }
                }
                tashCounter.incrementAndGet();
            }
        }

        return "无效证照附件处理的总数据量：" + zzWorkCertificates.size() + ":" + DateUtil.now();
    }

    /**
     * 删除垃圾附件数据
     * 这个是先手动删除ZzWorkCertificate表数据，然后将对应附件主键复制到history表中
     *
     * @return
     */
    @ApiOperation(value = "删除已注销证照数据和附件数据接口二", notes = "先手动删除证照表数据，然后将附件主键放到History表中再操作")
    @RequestMapping(value = "/del", method = RequestMethod.GET)
    @ResponseBody
    public String deleteAttachHistory() {

        /**
         * 查询所有删除状态的制证附件数据
         */
        List<History> histories = new LambdaQueryChainWrapper<>(historyMapper).list();

        log.info("无效证照附件处理的总数据量:" + histories.size() + "个");
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(histories)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (histories.size() > tashCounter.get()) {
                History entity = histories.get(tashCounter.get());
                if (entity != null) {
                    if (StrUtil.isNotEmpty(entity.getCode())) {
                        executor.execute(() -> {
                            try {
                                // 删除日志表数据
                                LambdaQueryWrapper<ZzwkCertificateLogs> zzwkCertificateLogsLambdaQueryWrapper = new LambdaQueryWrapper<ZzwkCertificateLogs>().eq(ZzwkCertificateLogs::getAttaOid, entity.getCode());
                                zzwkCertificateLogsMapper.delete(zzwkCertificateLogsLambdaQueryWrapper);
                                // 删除维护记录表数据
                                LambdaQueryWrapper<ZzwkCertificateVersionRecord> zzwkCertificateVersionRecordLambdaQueryWrapper = new LambdaQueryWrapper<ZzwkCertificateVersionRecord>().eq(ZzwkCertificateVersionRecord::getAttaOid, entity.getCode());
                                zzwkCertificateVersionRecordMapper.delete(zzwkCertificateVersionRecordLambdaQueryWrapper);
                                // 删除附件表数据
                                sysAttaMapper.deleteById(entity.getCode());

                                // 删除记录附件主键表数据
                                LambdaQueryWrapper<History> historyLambdaQueryWrapper = new LambdaQueryWrapper<History>().eq(History::getCode, entity.getCode());
                                historyMapper.delete(historyLambdaQueryWrapper);
                            } catch (Exception e) {
                                // 删除失败时更新附件状态
                                LambdaUpdateChainWrapper<SysAtta> lambdaUpdateChainWrapper = new LambdaUpdateChainWrapper<>(sysAttaMapper);
                                lambdaUpdateChainWrapper.eq(SysAtta::getOid, entity.getCode()).set(SysAtta::getIsDelete, "Y");
                                lambdaUpdateChainWrapper.update();
                                log.error("无效数据删除异常信息:{}", e.getMessage());
                            }
                        });
                    }
                }
                tashCounter.incrementAndGet();
            }
        }

        return "无效证照附件处理的总数据量：" + histories.size() + ":" + DateUtil.now();
    }

}
