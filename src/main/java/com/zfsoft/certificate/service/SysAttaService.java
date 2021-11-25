package com.zfsoft.certificate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zfsoft.certificate.pojo.SysAtta;

/**
 * @author 86131
 */
public interface SysAttaService extends IService<SysAtta> {

    /**
     * 通过主键查询证照附件
     *
     * @param oid
     * @return
     */
    SysAtta getSysAttaByOid(String oid);

}
