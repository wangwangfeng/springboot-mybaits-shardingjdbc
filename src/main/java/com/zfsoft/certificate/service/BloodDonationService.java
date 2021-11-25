package com.zfsoft.certificate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zfsoft.certificate.pojo.BloodDonation;

import java.util.List;

/**
 * @author 86131
 */
public interface BloodDonationService extends IService<BloodDonation> {

    /**
     * 查询献血证所有数据
     * @return
     */
    List<BloodDonation> findAll();

}
