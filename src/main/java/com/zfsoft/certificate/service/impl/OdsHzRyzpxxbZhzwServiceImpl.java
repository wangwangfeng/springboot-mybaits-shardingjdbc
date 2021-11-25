package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db3.OdsHzRyzpxxbZhzwMapper;
import com.zfsoft.certificate.pojo.OdsHzRyzpxxbZhzw;
import com.zfsoft.certificate.service.OdsHzRyzpxxbZhzwService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 86131
 */
@Service
public class OdsHzRyzpxxbZhzwServiceImpl extends ServiceImpl<OdsHzRyzpxxbZhzwMapper, OdsHzRyzpxxbZhzw> implements OdsHzRyzpxxbZhzwService {

    @Resource
    private OdsHzRyzpxxbZhzwMapper odsHzRyzpxxbZhzwMapper;

}
