package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db5.OdsJtjBysMapper;
import com.zfsoft.certificate.pojo.OdsJtjBys;
import com.zfsoft.certificate.service.OdsJtjBysService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Classname: OdsJtjBysServiceImpl
 * @Description: TODO
 * @Date: 2021/6/9 17:29
 * @author: wwf
 */
@Service
public class OdsJtjBysServiceImpl extends ServiceImpl<OdsJtjBysMapper, OdsJtjBys> implements OdsJtjBysService {

    @Resource
    private OdsJtjBysMapper odsJtjBysMapper;

}
