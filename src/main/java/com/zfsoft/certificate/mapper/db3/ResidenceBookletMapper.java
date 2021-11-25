package com.zfsoft.certificate.mapper.db3;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zfsoft.certificate.pojo.ResidenceBooklet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 86131
 */
@Mapper
public interface ResidenceBookletMapper extends BaseMapper<ResidenceBooklet> {

    /**
     * 查询户口簿信息
     * 入参即为数据库对应的此证人证件号和姓名字段名而非字段值
     * @param ownerId
     * @param ownerName
     * @return
     */
    List<ResidenceBooklet> getResidenceBookletList(@Param("ownerId") String ownerId, @Param("ownerName") String ownerName);

}
