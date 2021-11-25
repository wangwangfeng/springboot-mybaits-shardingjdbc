package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db1.SysAttaMapper;
import com.zfsoft.certificate.pojo.SysAtta;
import com.zfsoft.certificate.service.SysAttaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 86131
 */
@Service
public class SysAttaServiceImpl extends ServiceImpl<SysAttaMapper, SysAtta> implements SysAttaService {

    @Resource
    private SysAttaMapper sysAttaMapper;

    @Override
    public SysAtta getSysAttaByOid(String oid) {
        return sysAttaMapper.getSysAttaByOid(oid);
    }

}
