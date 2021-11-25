package com.zfsoft.certificate.mapper.db1;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zfsoft.certificate.pojo.ZzWorkBusinessMetadata;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Classname: ZzWorkBusinessMetadataMapper
 * @Description: TODO
 * @Date: 2021/6/10 16:32
 * @author: wwf
 */
@Mapper
public interface ZzWorkBusinessMetadataMapper extends BaseMapper<ZzWorkBusinessMetadata> {

    List<ZzWorkBusinessMetadata> getZzWorkBusinessMetadataByStyleId(@Param("styleId") String styleId);

}