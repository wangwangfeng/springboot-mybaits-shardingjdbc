package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db2.SenileEntityMapper;
import com.zfsoft.certificate.pojo.SenileEntity;
import com.zfsoft.certificate.service.SenileEntityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * //分布式事务
 * //@ShardingTransactionType(TransactionType.XA)
 * //@Transactional
 *
 * @author 86131
 */
@Service
public class SenileEntityServiceImpl extends ServiceImpl<SenileEntityMapper, SenileEntity> implements SenileEntityService {

    @Resource
    private SenileEntityMapper senileEntityMapper;

}
