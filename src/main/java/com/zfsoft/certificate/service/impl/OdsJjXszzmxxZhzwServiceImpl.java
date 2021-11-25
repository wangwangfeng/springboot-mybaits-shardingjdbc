package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db3.OdsJjXszzmxxZhzwMapper;
import com.zfsoft.certificate.pojo.OdsJjXszzmxxZhzw;
import com.zfsoft.certificate.service.OdsJjXszzmxxZhzwService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 86131
 */
@Service
public class OdsJjXszzmxxZhzwServiceImpl extends ServiceImpl<OdsJjXszzmxxZhzwMapper, OdsJjXszzmxxZhzw> implements OdsJjXszzmxxZhzwService {

    @Resource
    private OdsJjXszzmxxZhzwMapper odsJjXszzmxxZhzwMapper;

}
