package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db4.DwdQyyyzzxxMapper;
import com.zfsoft.certificate.pojo.DwdQyyyzzxx;
import com.zfsoft.certificate.service.DwdQyyyzzxxService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Classname: DwdQyyyzzxxServiceImpl
 * @Description: TODO
 * @Date: 2021/7/14 9:34
 * @author: wwf
 */
@Service
public class DwdQyyyzzxxServiceImpl extends ServiceImpl<DwdQyyyzzxxMapper, DwdQyyyzzxx> implements DwdQyyyzzxxService {

    @Resource
    private DwdQyyyzzxxMapper dwdQyyyzzxxMapper;

}
