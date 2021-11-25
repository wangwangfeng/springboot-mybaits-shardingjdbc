package com.zfsoft.certificate.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zfsoft.certificate.pojo.ZzWorkCertificate;

import java.util.List;
import java.util.Map;

/**
 * @author 86131
 */
public interface ZzWorkCertificateService extends IService<ZzWorkCertificate> {

    /**
     * 通过证照编码(contentCode)查询证照数据
     *
     * @param contentCode
     * @return
     */
    List<ZzWorkCertificate> getZzWorkCertificateByContentCode(String contentCode);

    /**
     * 根据主键更新证照标识字段
     *
     * @param id
     * @param cerStatus
     */
    void updateCerStatusById(String id, String cerStatus);

    /**
     * 通过证照编码注销历史已注销证照
     * 重新注销
     *
     * @param contentCode
     * @return
     */
    List<ZzWorkCertificate> getZzWorkCertificateMaintenanceByContentCode(String contentCode);

    /**
     * 根据证照编码、证照种类、证照编号、证照证件号查询有效证照数据
     *
     * @param contentCode
     * @param infoCode
     * @param ownerId
     * @return
     * @throws Exception
     */
    List<ZzWorkCertificate> findByContentCodeAndInfo(String contentCode, String infoCode, String ownerId) throws Exception;

    /**
     * @Description: 制证盖章
     * @Date: 2021/6/10 15:59
     * @author: wwf
     * @param: jsonObject
     * @return: java.util.Map<java.lang.String,java.lang.String>
     **/
    Map<String, String> zzMake(JSONObject jsonObject) throws Exception;

}
