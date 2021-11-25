package com.zfsoft.certificate.mapper.db1;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zfsoft.certificate.pojo.ZzWorkCertificate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 86131
 */
@Mapper
public interface ZzWorkCertificateMapper extends BaseMapper<ZzWorkCertificate> {

    /**
     * 通过证照编码查询证照数据
     * @param contentCode
     * @return
     */
    List<ZzWorkCertificate> getZzWorkCertificateByContentCode(@Param("contentCode") String contentCode);

    /**
     * 根据主键更新证照标识字段
     * @param id
     * @param cerStatus
     */
    void updateCerStatusById(@Param("id") String id, @Param("cerStatus") String cerStatus);

    /**
     * 通过证照编码注销历史已注销证照
     * 重新注销
     * @param contentCode
     * @return
     */
    List<ZzWorkCertificate> getZzWorkCertificateMaintenanceByContentCode(@Param("contentCode") String contentCode);

    /**
     * @Description: TODO
     * @Date: 2021/6/10 16:24
     * @author: wwf
     * @param: contentCode
 * @param infoCode
 * @param ownerId
     * @return: java.util.List<com.zfsoft.certificate.pojo.ZzWorkCertificate>
     **/
    List<ZzWorkCertificate> findByContentCodeAndInfo(@Param("contentCode") String contentCode,
                                               @Param("infoCode") String infoCode,
                                               @Param("ownerId") String ownerId);

}
