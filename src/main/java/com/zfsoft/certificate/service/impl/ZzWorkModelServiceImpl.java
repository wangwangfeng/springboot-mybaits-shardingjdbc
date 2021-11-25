package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db1.ZzWorkModelMapper;
import com.zfsoft.certificate.pojo.ZzWorkModel;
import com.zfsoft.certificate.service.ZzWorkModelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Classname: ZzWorkModelServiceImpl
 * @Description: TODO
 * @Date: 2021/6/10 16:56
 * @author: wwf
 */
@Service
public class ZzWorkModelServiceImpl extends ServiceImpl<ZzWorkModelMapper, ZzWorkModel> implements ZzWorkModelService {

    @Resource
    private ZzWorkModelMapper zzWorkModelMapper;

    @Override
    public ZzWorkModel getZzWorkModelByContentCode(String contentCode) {
        return zzWorkModelMapper.getZzWorkModelByContentCode(contentCode);
    }
}
