package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db3.ResidenceBookletMapper;
import com.zfsoft.certificate.pojo.ResidenceBooklet;
import com.zfsoft.certificate.service.ResidenceBookletService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 86131
 */
@Service
public class ResidenceBookletServiceImpl extends ServiceImpl<ResidenceBookletMapper, ResidenceBooklet> implements ResidenceBookletService {

    @Resource
    private ResidenceBookletMapper residenceBookletMapper;

    @Override
    public List<ResidenceBooklet> getResidenceBookletList(String ownerId, String ownerName) {
        return residenceBookletMapper.getResidenceBookletList(ownerId, ownerName);
    }

}
