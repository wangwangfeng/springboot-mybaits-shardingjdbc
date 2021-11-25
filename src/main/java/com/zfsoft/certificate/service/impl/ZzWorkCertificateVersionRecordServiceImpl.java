package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db1.ZzWorkCertificateVersionRecordMapper;
import com.zfsoft.certificate.pojo.ZzWorkCertificateVersionRecord;
import com.zfsoft.certificate.service.ZzWorkCertificateVersionRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Classname: ZzWorkCertificateVersionRecordServiceImpl
 * @Description: TODO
 * @Date: 2021/6/15 9:23
 * @author: wwf
 */
@Service
public class ZzWorkCertificateVersionRecordServiceImpl extends ServiceImpl<ZzWorkCertificateVersionRecordMapper, ZzWorkCertificateVersionRecord> implements ZzWorkCertificateVersionRecordService {

    @Resource
    private ZzWorkCertificateVersionRecordMapper zzWorkCertificateVersionRecordMapper;

}
