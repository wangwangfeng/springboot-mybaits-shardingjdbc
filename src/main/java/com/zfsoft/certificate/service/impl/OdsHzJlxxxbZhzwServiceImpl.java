package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db3.OdsHzJlxxxbZhzwMapper;
import com.zfsoft.certificate.pojo.OdsHzJlxxxbZhzw;
import com.zfsoft.certificate.service.OdsHzJlxxxbZhzwService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 86131
 */
@Service
public class OdsHzJlxxxbZhzwServiceImpl extends ServiceImpl<OdsHzJlxxxbZhzwMapper, OdsHzJlxxxbZhzw> implements OdsHzJlxxxbZhzwService {

    @Resource
    private OdsHzJlxxxbZhzwMapper odsHzJlxxxbZhzwMapper;

}
