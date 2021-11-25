package com.zfsoft.certificate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zfsoft.certificate.pojo.ZzWorkStyle;

/**
 * @author 86131
 */
public interface ZzWorkStyleService extends IService<ZzWorkStyle> {

    /**
     * 通过主键(styleId)查询证照类型
     *
     * @param id
     * @return
     */
    ZzWorkStyle getZzWorkStyleById(String id);

}
