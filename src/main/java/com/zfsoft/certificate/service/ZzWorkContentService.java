package com.zfsoft.certificate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zfsoft.certificate.pojo.ZzWorkContent;

/**
 * @author 86131
 */
public interface ZzWorkContentService extends IService<ZzWorkContent> {

    /**
     * 通过证照编码查询证照目录
     *
     * @param code
     * @return
     */
    ZzWorkContent getZzWorkContentByCode(String code);

}
