package com.zfsoft.certificate.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.zfsoft.certificate.mapper.db1.HistoryMapper;
import com.zfsoft.certificate.mapper.db2.SenileEntityMapper;
import com.zfsoft.certificate.mapper.db7.TestMapper;
import com.zfsoft.certificate.pojo.History;
import com.zfsoft.certificate.pojo.SenileEntity;
import com.zfsoft.certificate.pojo.Test;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname: TestController
 * @Description: TODO
 * @Date: 2021/11/25 10:10
 * @author: wwf
 */
@Slf4j
@RestController
public class TestController {

    @Autowired
    private HistoryMapper historyMapper;

    @Autowired
    private SenileEntityMapper senileEntityMapper;

    @Autowired
    private TestMapper testMapper;

    @GetMapping(value = "/find")
    public String find() {

        log.info("开始查询数据");
        History history = new LambdaQueryChainWrapper<>(historyMapper)
                .eq(History::getCode, "1")
                .one();
        log.info("查询DB1数据:" + history.getCode());

        SenileEntity senileEntity = new LambdaQueryChainWrapper<>(senileEntityMapper)
                .eq(SenileEntity::getBh, "1")
                .one();
        log.info("查询DB2数据:" + senileEntity.getXm());

        Test test = new LambdaQueryChainWrapper<>(testMapper)
                .eq(Test::getId, "1")
                .one();
        log.info("查询DB3数据:" + test.getName());

        return DateUtil.now();
    }

    @GetMapping(value = "/update")
    @Transactional
    //@ShardingTransactionType(TransactionType.XA)
    public String update() {

        boolean b = new LambdaUpdateChainWrapper<>(historyMapper)
                .eq(History::getCode, "1")
                .set(History::getCode, "2")
                .update();

/*        boolean b1 = new LambdaUpdateChainWrapper<>(senileEntityMapper)
                .eq(SenileEntity::getBh, "1")
                .set(SenileEntity::getXm, "李四2")
                .update();*/

        boolean b1 = new LambdaUpdateChainWrapper<>(testMapper)
                .eq(Test::getId, "1")
                .set(Test::getName, "李四")
                .update();

        int s = 1/0;

        return DateUtil.now() + "__" + b + "__" + b1;
    }


}
