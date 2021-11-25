package com.zfsoft.certificate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zfsoft.certificate.pojo.HonourEntity;

import java.util.List;

/**
 * @author 86131
 */
public interface HonourEntityService extends IService<HonourEntity> {

    /**
     * 查询所有独生子女光荣证
     * czr=xm并且xb=女
     *
     * @return
     */
    List<HonourEntity> findAll();

    /**
     * 查询所有独生子女光荣证
     * czr!=xm并且xb=女
     *
     * @return
     */
    List<HonourEntity> findAllPoXm();

    /**
     * 查询所有独生子女光荣证
     * czr!=xm并且xb=男
     * 需要更换制证男女对应的相关字段内容
     *
     * @return
     */
    List<HonourEntity> findAllPoXmMan();

}
