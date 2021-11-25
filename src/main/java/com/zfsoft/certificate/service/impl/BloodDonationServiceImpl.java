package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db4.BloodDonationMapper;
import com.zfsoft.certificate.pojo.BloodDonation;
import com.zfsoft.certificate.service.BloodDonationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author 86131
 */
@Service
public class BloodDonationServiceImpl extends ServiceImpl<BloodDonationMapper, BloodDonation> implements BloodDonationService {

    @Resource
    private BloodDonationMapper bloodDonationMapper;

    @Override
    public List<BloodDonation> findAll() {
        return bloodDonationMapper.findAll();
    }

}
