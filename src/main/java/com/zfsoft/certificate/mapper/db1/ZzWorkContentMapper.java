package com.zfsoft.certificate.mapper.db1;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zfsoft.certificate.pojo.ZzWorkContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 86131
 */
@Mapper
public interface ZzWorkContentMapper extends BaseMapper<ZzWorkContent> {

    /**
     * 通过证照编码查询证照目录
     * @param code
     * @return
     */
    ZzWorkContent getZzWorkContentByCode(@Param("code") String code);

}
