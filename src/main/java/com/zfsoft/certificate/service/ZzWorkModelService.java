package com.zfsoft.certificate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zfsoft.certificate.pojo.ZzWorkModel;

/**
 * @Classname: ZzWorkModelService
 * @Description: TODO
 * @Date: 2021/6/10 16:56
 * @author: wwf
 */
public interface ZzWorkModelService extends IService<ZzWorkModel> {

    ZzWorkModel getZzWorkModelByContentCode(String contentCode);

}
