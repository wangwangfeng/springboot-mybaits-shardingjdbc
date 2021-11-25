package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db3.OdsHzXzqhbZhzwMapper;
import com.zfsoft.certificate.pojo.OdsHzXzqhbZhzw;
import com.zfsoft.certificate.service.OdsHzXzqhbZhzwService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 86131
 */
@Service
public class OdsHzXzqhbZhzwServiceImpl extends ServiceImpl<OdsHzXzqhbZhzwMapper, OdsHzXzqhbZhzw> implements OdsHzXzqhbZhzwService {

    @Resource
    private OdsHzXzqhbZhzwMapper odsHzXzqhbZhzwMapper;

}
