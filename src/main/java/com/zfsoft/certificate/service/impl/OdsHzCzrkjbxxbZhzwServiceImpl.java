package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db3.OdsHzCzrkjbxxbZhzwMapper;
import com.zfsoft.certificate.pojo.OdsHzCzrkjbxxbZhzw;
import com.zfsoft.certificate.service.OdsHzCzrkjbxxbZhzwService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 86131
 */
@Service
public class OdsHzCzrkjbxxbZhzwServiceImpl extends ServiceImpl<OdsHzCzrkjbxxbZhzwMapper, OdsHzCzrkjbxxbZhzw> implements OdsHzCzrkjbxxbZhzwService {

    @Resource
    private OdsHzCzrkjbxxbZhzwMapper odsHzCzrkjbxxbZhzwMapper;

}
