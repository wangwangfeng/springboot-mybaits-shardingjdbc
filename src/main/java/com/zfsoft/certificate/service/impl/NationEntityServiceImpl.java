package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db3.NationEntityMapper;
import com.zfsoft.certificate.pojo.NationEntity;
import com.zfsoft.certificate.service.NationEntityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 86131
 */
@Service
public class NationEntityServiceImpl extends ServiceImpl<NationEntityMapper, NationEntity> implements NationEntityService {

    @Resource
    private NationEntityMapper nationEntityMapper;

    @Override
    public NationEntity getNationEntityByCode(String code) {
        return nationEntityMapper.getNationEntityByCode(code);
    }

}
