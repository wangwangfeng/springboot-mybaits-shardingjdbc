package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db1.ZzWorkContentMapper;
import com.zfsoft.certificate.pojo.ZzWorkContent;
import com.zfsoft.certificate.service.ZzWorkContentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 86131
 */
@Service
public class ZzWorkContentServiceImpl extends ServiceImpl<ZzWorkContentMapper, ZzWorkContent> implements ZzWorkContentService {

    @Resource
    private ZzWorkContentMapper zzWorkContentMapper;

    @Override
    public ZzWorkContent getZzWorkContentByCode(String code) {
        return zzWorkContentMapper.getZzWorkContentByCode(code);
    }

}
