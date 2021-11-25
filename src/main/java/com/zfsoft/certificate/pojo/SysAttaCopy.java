package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统附件复制表
 * 用于一期同步附件到二期FASTDFS中
 *
 * @author 86131
 */
@Data
@TableName(value = "t_sys_atta_copy")
public class SysAttaCopy implements Serializable {

    private static final long serialVersionUID = -7670272405551679121L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String oid;

    /**
     * 附件名称
     */
    private String name;

    /**
     * 附件原始名称
     */
    private String originName;

    /**
     * 附件路径
     */
    private String filePath;

    /**
     * 附件扩展名
     */
    private String extensionName;

    /**
     * 上传时间
     */
    private Date uploadDate;

    /**
     * 所属用户 : 上传用户的编号
     */
    private String userOid;

    /**
     * 删除状态
     */
    private String isDelete;

    private String syncStatus;

}