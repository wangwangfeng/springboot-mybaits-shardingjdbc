package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db1.ZzWorkBusinessMetadataMapper;
import com.zfsoft.certificate.pojo.ZzWorkBusinessMetadata;
import com.zfsoft.certificate.service.ZzWorkBusinessMetadataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname: ZzWorkBusinessMetadataServiceImpl
 * @Description: TODO
 * @Date: 2021/6/10 16:32
 * @author: wwf
 */
@Service
public class ZzWorkBusinessMetadataServiceImpl extends ServiceImpl<ZzWorkBusinessMetadataMapper, ZzWorkBusinessMetadata> implements ZzWorkBusinessMetadataService {

    @Resource
    private ZzWorkBusinessMetadataMapper zzWorkBusinessMetadataMapper;

    @Override
    public List<ZzWorkBusinessMetadata> getZzWorkBusinessMetadataByStyleId(String styleId) {
        return zzWorkBusinessMetadataMapper.getZzWorkBusinessMetadataByStyleId(styleId);
    }
}
