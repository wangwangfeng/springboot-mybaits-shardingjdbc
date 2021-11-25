package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db6.ZzwkZzwInfo1Mapper;
import com.zfsoft.certificate.pojo.ZzwkZzwInfo1;
import com.zfsoft.certificate.service.ZzwkZzwInfo1Service;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Classname: ZzwkZzwInfo1ServiceImpl
 * @Description: TODO
 * @Date: 2021/7/7 10:44
 * @author: wwf
 */
@Service
public class ZzwkZzwInfo1ServiceImpl extends ServiceImpl<ZzwkZzwInfo1Mapper, ZzwkZzwInfo1> implements ZzwkZzwInfo1Service {

    @Resource
    private ZzwkZzwInfo1Mapper zzwkZzwInfo1Mapper;

}
