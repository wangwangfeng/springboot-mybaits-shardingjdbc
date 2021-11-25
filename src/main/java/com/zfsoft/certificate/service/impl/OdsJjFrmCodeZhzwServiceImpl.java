package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db3.OdsJjFrmCodeZhzwMapper;
import com.zfsoft.certificate.pojo.OdsJjFrmCodeZhzw;
import com.zfsoft.certificate.service.OdsJjFrmCodeZhzwService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 86131
 */
@Service
public class OdsJjFrmCodeZhzwServiceImpl extends ServiceImpl<OdsJjFrmCodeZhzwMapper, OdsJjFrmCodeZhzw> implements OdsJjFrmCodeZhzwService {

    @Resource
    private OdsJjFrmCodeZhzwMapper odsJjFrmCodeZhzwMapper;

}
