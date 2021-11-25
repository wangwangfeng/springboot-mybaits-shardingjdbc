package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db3.MzzdbMapper;
import com.zfsoft.certificate.pojo.Mzzdb;
import com.zfsoft.certificate.service.MzzdbService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 86131
 */
@Service
public class MzzdbServiceImpl extends ServiceImpl<MzzdbMapper, Mzzdb> implements MzzdbService {

    @Resource
    private MzzdbMapper mzzdbMapper;

}
