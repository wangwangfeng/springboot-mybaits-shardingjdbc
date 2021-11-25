package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db1.ZzwkCertificateLogsMapper;
import com.zfsoft.certificate.pojo.ZzwkCertificateLogs;
import com.zfsoft.certificate.service.ZzwkCertificateLogsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author 86131
 */
@Service
public class ZzwkCertificateLogsServiceImpl extends ServiceImpl<ZzwkCertificateLogsMapper, ZzwkCertificateLogs> implements ZzwkCertificateLogsService {

    @Resource
    private ZzwkCertificateLogsMapper zzwkCertificateLogsMapper;

}
