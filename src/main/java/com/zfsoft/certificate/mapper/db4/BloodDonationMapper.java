package com.zfsoft.certificate.mapper.db4;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zfsoft.certificate.pojo.BloodDonation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 86131
 */
@Mapper
public interface BloodDonationMapper extends BaseMapper<BloodDonation> {

    /**
     * 查询献血证所有数据
     * @return
     */
    List<BloodDonation> findAll();

}