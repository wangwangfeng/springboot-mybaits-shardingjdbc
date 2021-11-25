package com.zfsoft.certificate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zfsoft.certificate.pojo.ZzWorkBusinessMetadata;

import java.util.List;

/**
 * @Classname: ZzWorkBusinessMetadataService
 * @Description: TODO
 * @Date: 2021/6/10 16:32
 * @author: wwf
 */
public interface ZzWorkBusinessMetadataService extends IService<ZzWorkBusinessMetadata> {

    List<ZzWorkBusinessMetadata> getZzWorkBusinessMetadataByStyleId(String styleId);

}
