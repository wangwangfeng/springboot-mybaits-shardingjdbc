package com.zfsoft.certificate.mapper.db3;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zfsoft.certificate.pojo.NationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 86131
 */
@Mapper
public interface NationEntityMapper extends BaseMapper<NationEntity> {

    /**
     * 根据民族编码获取民族名称
     * @param code
     * @return
     */
    NationEntity getNationEntityByCode(@Param("code") String code);

}