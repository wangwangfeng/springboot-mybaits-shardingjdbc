package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db1.ZzwkCertificateVersionRecordMapper;
import com.zfsoft.certificate.pojo.ZzwkCertificateVersionRecord;
import com.zfsoft.certificate.service.ZzwkCertificateVersionRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 86131
 */
@Service
public class ZzwkCertificateVersionRecordServiceImpl extends ServiceImpl<ZzwkCertificateVersionRecordMapper, ZzwkCertificateVersionRecord> implements ZzwkCertificateVersionRecordService {

    @Resource
    private ZzwkCertificateVersionRecordMapper zzwkCertificateVersionRecordMapper;

}
