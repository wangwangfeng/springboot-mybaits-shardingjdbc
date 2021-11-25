package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db6.ZzwkZzwInfo2Mapper;
import com.zfsoft.certificate.pojo.ZzwkZzwInfo2;
import com.zfsoft.certificate.service.ZzwkZzwInfo2Service;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Classname: ZzwkZzwInfo2ServiceImpl
 * @Description: TODO
 * @Date: 2021/7/7 11:06
 * @author: wwf
 */
@Service
public class ZzwkZzwInfo2ServiceImpl extends ServiceImpl<ZzwkZzwInfo2Mapper, ZzwkZzwInfo2> implements ZzwkZzwInfo2Service {

    @Resource
    private ZzwkZzwInfo2Mapper zzwkZzwInfo2Mapper;

}
