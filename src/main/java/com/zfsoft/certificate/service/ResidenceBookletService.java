package com.zfsoft.certificate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zfsoft.certificate.pojo.ResidenceBooklet;

import java.util.List;

/**
 * @author 86131
 */
public interface ResidenceBookletService extends IService<ResidenceBooklet> {

    /**
     * 查询户口簿信息
     * 入参即为数据库对应的此证人证件号和姓名字段名而非字段值
     * @param ownerId
     * @param ownerName
     * @return
     */
    List<ResidenceBooklet> getResidenceBookletList(String ownerId, String ownerName);

}
