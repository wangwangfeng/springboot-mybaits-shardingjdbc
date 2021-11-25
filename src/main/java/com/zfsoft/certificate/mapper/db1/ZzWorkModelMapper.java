package com.zfsoft.certificate.mapper.db1;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zfsoft.certificate.pojo.ZzWorkModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Classname: ZzWorkModelMapper
 * @Description: TODO
 * @Date: 2021/6/10 16:56
 * @author: wwf
 */
@Mapper
public interface ZzWorkModelMapper extends BaseMapper<ZzWorkModel> {

    ZzWorkModel getZzWorkModelByContentCode(@Param("contentCode") String contentCode);

}