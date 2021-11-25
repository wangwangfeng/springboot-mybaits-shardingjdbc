package com.zfsoft.certificate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.zfsoft.certificate.pojo.ResidenceBooklet;
import com.zfsoft.certificate.pojo.base.Constant;
import com.zfsoft.certificate.service.ResidenceBookletService;
import com.zfsoft.certificate.thread.task.ConsumerResidenceBooklet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 户口簿制证
 *
 * @author 86131
 */
@Api(tags = "户口簿制证后台")
@Controller
@Slf4j
public class ResidenceBookletController {

    @Autowired
    private Constant constant;

    /**
     * 消费线程计数器
     */
    public static AtomicInteger tashCounter = new AtomicInteger();

    @Autowired
    private ResidenceBookletService residenceBookletService;

    /**
     * 户口簿
     *
     * @param ownerId
     * @param ownerName
     * @return
     */
    @ApiOperation(value = "户口簿制证", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "ownerId", value = "身份证号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "ownerName", value = "姓名", required = true, dataType = "String")
    })
    @RequestMapping(value = "/hkb/{ownerId}/{ownerName}", method = RequestMethod.GET)
    @ResponseBody
    public String make(@PathVariable(name = "ownerId") String ownerId,
                       @PathVariable(name = "ownerName") String ownerName) {

        if (StrUtil.isNotEmpty(ownerId)
                && StrUtil.isNotEmpty(ownerName)) {
            if (!ownerId.contains(constant.getHkbOwnerId())
                    || !ownerName.contains(constant.getHkbOwnerName())) {
                ownerId = constant.getHkbOwnerId();
                ownerName = constant.getHkbOwnerName();
            }
        } else {
            ownerId = constant.getHkbOwnerId();
            ownerName = constant.getHkbOwnerName();
        }
        //主线程作为生产者生产户口簿制证任务
        List<ResidenceBooklet> residenceBooklets = residenceBookletService.getResidenceBookletList(ownerId.trim(), ownerName.trim());
        log.info("户口簿任务开始总量为:{}个", residenceBooklets.size());
        tashCounter.set(0);
        if (CollUtil.isNotEmpty(residenceBooklets)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(constant.getCorePoolSize(), constant.getMaxPoolSize(),
                    constant.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            while (residenceBooklets.size() > tashCounter.get()) {
                executor.execute(new ConsumerResidenceBooklet(residenceBooklets.get(tashCounter.get()), constant, ownerName));
                tashCounter.incrementAndGet();
            }
        }
        return "户口簿制证:" + residenceBooklets.size() + ":" + DateUtil.now();
    }

}
