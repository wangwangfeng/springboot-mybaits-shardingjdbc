package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db1.ZzWorkCertificateLogsMapper;
import com.zfsoft.certificate.pojo.ZzWorkCertificateLogs;
import com.zfsoft.certificate.service.ZzWorkCertificateLogsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Classname: ZzWorkCertificateLogsServiceImpl
 * @Description: TODO
 * @Date: 2021/6/15 9:44
 * @author: wwf
 */
@Service
public class ZzWorkCertificateLogsServiceImpl extends ServiceImpl<ZzWorkCertificateLogsMapper, ZzWorkCertificateLogs> implements ZzWorkCertificateLogsService {

    @Resource
    private ZzWorkCertificateLogsMapper zzWorkCertificateLogsMapper;

}
