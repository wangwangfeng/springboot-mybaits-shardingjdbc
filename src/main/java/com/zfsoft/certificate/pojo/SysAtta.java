package com.zfsoft.certificate.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 86131
 */
@Data
@TableName(value = "t_sys_atta")
public class SysAtta implements Serializable {

    private static final long serialVersionUID = -8280916962065853594L;

    /**
     * 主键
     */
    @TableId(value = "OID", type = IdType.ASSIGN_UUID)
    private String oid;

    /**
     * 附件名称
     */
    @TableField(value = "NAME")
    private String name;

    /**
     * 附件原始名称
     */
    @TableField(value = "ORIGIN_NAME")
    private String originName;

    /**
     * 附件地址
     */
    @TableField(value = "FILE_PATH")
    private String filePath;

    /**
     * 附件扩展名
     */
    @TableField(value = "EXTENSION_NAME")
    private String extensionName;

    /**
     * 上传时间
     */
    @TableField(value = "UPLOAD_DATE")
    private Date uploadDate;

    /**
     * 接收人编号
     */
    @TableField(value = "USER_OID")
    private String userOid;

    /**
     * 删除状态
     */
    @TableField(value = "IS_DELETE")
    private String isDelete;

}
