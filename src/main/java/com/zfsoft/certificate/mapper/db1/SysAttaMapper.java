package com.zfsoft.certificate.mapper.db1;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zfsoft.certificate.pojo.SysAtta;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 86131
 */
@Mapper
public interface SysAttaMapper extends BaseMapper<SysAtta> {

    /**
     * 通过主键查询证照附件
     *
     * @param oid
     * @return
     */
    SysAtta getSysAttaByOid(@Param("oid") String oid);

}
