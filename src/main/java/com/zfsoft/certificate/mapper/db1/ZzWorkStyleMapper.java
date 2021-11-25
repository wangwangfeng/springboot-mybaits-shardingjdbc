package com.zfsoft.certificate.mapper.db1;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zfsoft.certificate.pojo.ZzWorkStyle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 86131
 */
@Mapper
public interface ZzWorkStyleMapper extends BaseMapper<ZzWorkStyle> {

    /**
     * 通过主键查询证照类型
     * @param id
     * @return
     */
    ZzWorkStyle getZzWorkStyleById(@Param("id") String id);

}
