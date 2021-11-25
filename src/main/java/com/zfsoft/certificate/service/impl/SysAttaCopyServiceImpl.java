package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db1.SysAttaCopyMapper;
import com.zfsoft.certificate.pojo.SysAttaCopy;
import com.zfsoft.certificate.service.SysAttaCopyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysAttaCopyServiceImpl extends ServiceImpl<SysAttaCopyMapper, SysAttaCopy> implements SysAttaCopyService {

    @Resource
    private SysAttaCopyMapper sysAttaCopyMapper;

}
