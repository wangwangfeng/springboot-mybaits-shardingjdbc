package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db4.HonourEntityMapper;
import com.zfsoft.certificate.pojo.HonourEntity;
import com.zfsoft.certificate.service.HonourEntityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 86131
 */
@Service
public class HonourEntityServiceImpl extends ServiceImpl<HonourEntityMapper, HonourEntity> implements HonourEntityService {

    @Resource
    private HonourEntityMapper honourEntityMapper;

    @Override
    public List<HonourEntity> findAll() {
        return honourEntityMapper.findAll();
    }

    @Override
    public List<HonourEntity> findAllPoXm() {
        return honourEntityMapper.findAllPoXm();
    }

    @Override
    public List<HonourEntity> findAllPoXmMan() {
        return honourEntityMapper.findAllPoXmMan();
    }

}
