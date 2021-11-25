package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db3.OdsJjDrvZhzwMapper;
import com.zfsoft.certificate.pojo.OdsJjDrvZhzw;
import com.zfsoft.certificate.service.OdsJjDrvZhzwService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 86131
 */
@Service
public class OdsJjDrvZhzwServiceImpl extends ServiceImpl<OdsJjDrvZhzwMapper, OdsJjDrvZhzw> implements OdsJjDrvZhzwService {

    @Resource
    private OdsJjDrvZhzwMapper odsJjDrvZhzwMapper;

}
