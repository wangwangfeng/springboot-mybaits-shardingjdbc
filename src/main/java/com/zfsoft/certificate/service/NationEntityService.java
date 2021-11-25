package com.zfsoft.certificate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zfsoft.certificate.pojo.NationEntity;

/**
 * @author 86131
 */
public interface NationEntityService extends IService<NationEntity> {

    /**
     * 根据民族编码获取民族名称
     * @param code
     * @return
     */
    NationEntity getNationEntityByCode(String code);

}
