package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db1.ZzWorkStyleMapper;
import com.zfsoft.certificate.pojo.ZzWorkStyle;
import com.zfsoft.certificate.service.ZzWorkStyleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 86131
 */
@Service
public class ZzWorkStyleServiceImpl extends ServiceImpl<ZzWorkStyleMapper, ZzWorkStyle> implements ZzWorkStyleService {

    @Resource
    private ZzWorkStyleMapper zzWorkStyleMapper;

    @Override
    public ZzWorkStyle getZzWorkStyleById(String id) {
        return zzWorkStyleMapper.getZzWorkStyleById(id);
    }

}
