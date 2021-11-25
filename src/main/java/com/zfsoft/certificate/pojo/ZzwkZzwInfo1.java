package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname: ZzwkZzwInfo1
 * @Description: TODO
 * @Date: 2021/7/7 10:44
 * @author: wwf
 */
@Data
@TableName(value = "zzwk_zzw_info_1")
public class ZzwkZzwInfo1 implements Serializable {

    private static final long serialVersionUID = 5052688665892555745L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 持有者名称
     */
    private String infoOwnerName;

    /**
     * 持有者编码（身份证或组织结构所有人身份证）
     */
    private String infoOwnerId;

    /**
     * 证照类型名称
     */
    private String styleName;

    /**
     * 注销标识：默认0，注销成功更新为1
     */
    private String flag;

}