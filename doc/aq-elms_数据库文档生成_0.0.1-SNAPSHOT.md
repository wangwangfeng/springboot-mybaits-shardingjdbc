# 数据库文档

**数据库名：** aq-elms

**文档版本：** 0.0.1-SNAPSHOT

**文档描述：** 数据库文档生成

| 表名                  | 说明       |
| :---: | :---: |
| [t_ahs_function_allot](#t_ahs_function_allot) | 服务项目分发表 |
| [t_sys_atta](#t_sys_atta) | 系统附件表 |
| [zzwk_content](#zzwk_content) |  |
| [zzwk_flow_log](#zzwk_flow_log) |  |
| [zzwk_index_meta](#zzwk_index_meta) |  |
| [zzwk_material_style](#zzwk_material_style) | 材料类型 |
| [zzwk_material_style_rel](#zzwk_material_style_rel) | 材料类型关联证照类型表 |
| [zzwk_org_trustcode](#zzwk_org_trustcode) |  |
| [zzwk_style](#zzwk_style) |  |

**表名：** <a id="t_ahs_function_allot">t_ahs_function_allot</a>

**说明：** 服务项目分发表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | OID |   varchar   | 32 |   0    |    N     |  Y   |       | 主键  |
|  2   | FUNCTION_OID |   varchar   | 64 |   0    |    Y     |  N   |       | 事项主键  |
|  3   | DISTRICT_OID |   varchar   | 64 |   0    |    Y     |  N   |       | 区划主键  |
|  4   | TERMINAL_TYPE |   varchar   | 16 |   0    |    Y     |  N   |       | 设备类型  |
|  5   | TIP |   varchar   | 255 |   0    |    Y     |  N   |       | 弹窗  |
|  6   | SORT |   int   | 10 |   0    |    Y     |  N   |       | 排序  |
|  7   | ABLE_STATUS |   varchar   | 2 |   0    |    Y     |  N   |       | 启禁用状态Y:启用N:禁用  |
|  8   | DELETE_STATUS |   varchar   | 2 |   0    |    Y     |  N   |       | 删除状态Y:删除N:未删除  |
|  9   | CREATE_DATE |   datetime   | 19 |   0    |    Y     |  N   |       | 创建时间  |
|  10   | MODIFY_DATE |   datetime   | 19 |   0    |    Y     |  N   |       | 修改时间  |

**表名：** <a id="t_sys_atta">t_sys_atta</a>

**说明：** 系统附件表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | OID |   varchar   | 32 |   0    |    N     |  Y   |       | 主键  |
|  2   | NAME |   varchar   | 200 |   0    |    N     |  N   |       | 附件名称  |
|  3   | ORIGIN_NAME |   varchar   | 400 |   0    |    N     |  N   |       | 附件原始名称  |
|  4   | FILE_PATH |   varchar   | 300 |   0    |    Y     |  N   |       | 附件路径  |
|  5   | EXTENSION_NAME |   varchar   | 50 |   0    |    Y     |  N   |       | 附件扩展名  |
|  6   | UPLOAD_DATE |   datetime   | 19 |   0    |    Y     |  N   |       | 上传时间  |
|  7   | USER_OID |   varchar   | 32 |   0    |    Y     |  N   |       | 所属用户:上传用户的编号  |
|  8   | IS_DELETE |   varchar   | 1 |   0    |    Y     |  N   |       | 删除状态  |

**表名：** <a id="zzwk_content">zzwk_content</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | ID |   varchar   | 32 |   0    |    N     |  Y   |       |   |
|  2   | CODE |   varchar   | 32 |   0    |    Y     |  N   |       |   |
|  3   | VERSION |   decimal   | 11 |   0    |    Y     |  N   |       |   |
|  4   | NAME |   varchar   | 128 |   0    |    Y     |  N   |       |   |
|  5   | THUMB |   blob   | 65535 |   0    |    Y     |  N   |       |   |
|  6   | TYPE_CODE |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  7   | ORG_CODE |   varchar   | 32 |   0    |    Y     |  N   |       |   |
|  8   | ORG_NAME |   varchar   | 256 |   0    |    Y     |  N   |       |   |
|  9   | REGISTER_TIME |   timestamp   | 26 |   0    |    N     |  N   |   CURRENT_TIMESTAMP(6)    |   |
|  10   | VALIDTIME |   decimal   | 11 |   0    |    Y     |  N   |       |   |
|  11   | DESCRIPTION |   varchar   | 4000 |   0    |    Y     |  N   |       |   |
|  12   | STATE_CODE |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  13   | CREATE_USER_CODE |   varchar   | 128 |   0    |    Y     |  N   |       |   |
|  14   | CREATE_USER_NAME |   varchar   | 256 |   0    |    Y     |  N   |       |   |
|  15   | CREATE_USER_IDCARD |   varchar   | 128 |   0    |    Y     |  N   |       |   |
|  16   | CREATE_ORG_CODE |   varchar   | 32 |   0    |    Y     |  N   |       |   |
|  17   | CREATE_ORG_NAME |   varchar   | 512 |   0    |    Y     |  N   |       |   |
|  18   | CREATE_TIME |   timestamp   | 26 |   0    |    N     |  N   |   0000-00-0000:00:00.000000    |   |
|  19   | PATER_CODE |   varchar   | 32 |   0    |    Y     |  N   |       |   |
|  20   | SOURCE_CODE |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  21   | PROCESS_STATE |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  22   | CHANGE_STATE |   varchar   | 2 |   0    |    Y     |  N   |       |   |
|  23   | NEED_SIGN |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  24   | HAVE_MORE |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  25   | SIGN_RULE |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  26   | SPACEPAGE_MAKE |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  27   | MANY_TO_ONE |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  28   | LISENCE_TYPE |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  29   | CLASSIFYID |   varchar   | 32 |   0    |    Y     |  N   |       |   |
|  30   | REVOKE_FLAG |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  31   | CANCLE_FLAG |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  32   | NJ_FLAG |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  33   | CHANGE_FLAG |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  34   | LOST_FLAG |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  35   | FIND_FLAG |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  36   | LOGOUT_FLAG |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  37   | TYPE |   varchar   | 10 |   0    |    Y     |  N   |       |   |
|  38   | CATEGORY |   varchar   | 20 |   0    |    Y     |  N   |       |   |
|  39   | ISSUE_LEVEL |   varchar   | 20 |   0    |    Y     |  N   |       |   |
|  40   | SIGN_COUNT |   varchar   | 2 |   0    |    Y     |  N   |       |   |
|  41   | CODE_FORMAT |   varchar   | 500 |   0    |    Y     |  N   |       |   |
|  42   | CATALOG_BUSINESS |   varchar   | 20 |   0    |    Y     |  N   |       |   |
|  43   | CER_CODE |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  44   | CER_NAME |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  45   | MAIN_ISSUE_CODE |   varchar   | 20 |   0    |    Y     |  N   |       |   |
|  46   | MAIN_ISSUE_NAME |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  47   | ISSUE_CODE_STR |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  48   | ISSUE_NAME_STR |   varchar   | 600 |   0    |    Y     |  N   |       |   |
|  49   | CER_TYPE_CODE |   varchar   | 50 |   0    |    Y     |  N   |       |   |
|  50   | CER_TYPE_NAME |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  51   | CER_CATALOG_CODE |   varchar   | 50 |   0    |    Y     |  N   |       |   |
|  52   | CER_CATALOG_NAME |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  53   | MAIN_TYPE_CODE |   varchar   | 50 |   0    |    Y     |  N   |       |   |
|  54   | MAIN_TYPE_NAME |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  55   | VALIDTIME_STR |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  56   | XYPTGUID |   varchar   | 36 |   0    |    Y     |  N   |       |   |
|  57   | XYPTBATCHGUID |   varchar   | 36 |   0    |    Y     |  N   |       |   |
|  58   | XYPT_TIME_DXP |   timestamp   | 26 |   0    |    N     |  N   |   0000-00-0000:00:00.000000    |   |
|  59   | XYPTBATCHNO |   decimal   | 15 |   0    |    Y     |  N   |       |   |
|  60   | CREATE_AREA_CODE |   varchar   | 6 |   0    |    Y     |  N   |       |   |
|  61   | STYLE_ID |   varchar   | 32 |   0    |    Y     |  N   |       |   |
|  62   | DELETE_STATE |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  63   | UPDATE_TIME |   varchar   | 14 |   0    |    Y     |  N   |       |   |
|  64   | JHSJ_ZF |   datetime   | 19 |   0    |    Y     |  N   |       |   |
|  65   | SEAL_CODE_FIRST |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  66   | SEAL_CODE_SECOND |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  67   | SEAL_CODE_THIRD |   varchar   | 100 |   0    |    Y     |  N   |       |   |

**表名：** <a id="zzwk_flow_log">zzwk_flow_log</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | ID |   varchar   | 32 |   0    |    N     |  Y   |       |   |
|  2   | OPERATE_TYPE |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  3   | OPERATE_NAME |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  4   | OPERATE_ID |   varchar   | 32 |   0    |    Y     |  N   |       |   |
|  5   | OPERATE_TIME |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  6   | DESCRIBE |   varchar   | 400 |   0    |    Y     |  N   |       |   |
|  7   | CONTENT_ID |   varchar   | 32 |   0    |    Y     |  N   |       |   |
|  8   | XYPTGUID |   varchar   | 36 |   0    |    Y     |  N   |       |   |
|  9   | XYPTBATCHGUID |   varchar   | 36 |   0    |    Y     |  N   |       |   |
|  10   | XYPT_TIME_DXP |   timestamp   | 26 |   0    |    N     |  N   |   CURRENT_TIMESTAMP(6)    |   |
|  11   | XYPTBATCHNO |   decimal   | 15 |   0    |    Y     |  N   |       |   |
|  12   | DELETE_STATE |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  13   | UPDATE_TIME |   varchar   | 14 |   0    |    Y     |  N   |       |   |
|  14   | JHSJ_ZF |   datetime   | 19 |   0    |    Y     |  N   |       |   |

**表名：** <a id="zzwk_index_meta">zzwk_index_meta</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | ID |   varchar   | 32 |   0    |    N     |  Y   |       |   |
|  2   | OWNER_ID |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  3   | CHANGE_RECORD |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  4   | ONLINE_QUERY_URL |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  5   | ONLINE_CHECK_URL |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  6   | ORIGIN |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  7   | ISSUE_ORG_CODE |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  8   | CONTENT_NAME |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  9   | INFO_VALIDITY_END |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  10   | ISSUE_ORG_NAME |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  11   | INFO_CODE |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  12   | CODE |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  13   | OWNER_NAME |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  14   | MAKE_TIME |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  15   | INFO_VALIDITY_BEGIN |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  16   | AREA_CODE |   varchar   | 10 |   0    |    Y     |  N   |       |   |
|  17   | VERSION |   decimal   | 11 |   0    |    Y     |  N   |       |   |
|  18   | CONTENT_CODE |   varchar   | 3272 |   0    |    Y     |  N   |       |   |
|  19   | XYPTGUID |   varchar   | 36 |   0    |    Y     |  N   |       |   |
|  20   | XYPTBATCHGUID |   varchar   | 36 |   0    |    Y     |  N   |       |   |
|  21   | XYPT_TIME_DXP |   timestamp   | 26 |   0    |    N     |  N   |   CURRENT_TIMESTAMP(6)    |   |
|  22   | XYPTBATCHNO |   decimal   | 15 |   0    |    Y     |  N   |       |   |

**表名：** <a id="zzwk_material_style">zzwk_material_style</a>

**说明：** 材料类型

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | ID |   varchar   | 32 |   0    |    N     |  Y   |       | 标识ID  |
|  2   | CODE |   varchar   | 100 |   0    |    Y     |  N   |       | 材料类型编码  |
|  3   | NAME |   varchar   | 400 |   0    |    Y     |  N   |       | 材料类型名称  |
|  4   | OWNER_TYPE |   varchar   | 2 |   0    |    Y     |  N   |       | 持有者类型  |
|  5   | ORIGIN_CHANNEL |   varchar   | 2 |   0    |    Y     |  N   |       | 来源渠道  |
|  6   | MANAGE_UNIT |   varchar   | 200 |   0    |    Y     |  N   |       | 管辖单位  |
|  7   | IS_UNIQUE |   varchar   | 2 |   0    |    Y     |  N   |       | 是否唯一（Y:是；N:否）  |
|  8   | STATUS |   varchar   | 2 |   0    |    Y     |  N   |       | 状态（0：启用；1：停用；2：注销）  |
|  9   | DELETE_STATE |   varchar   | 2 |   0    |    Y     |  N   |       | 删除状态（0：未删除；1：已删除）  |
|  10   | CREATE_TIME |   varchar   | 14 |   0    |    Y     |  N   |       | 创建时间  |
|  11   | UPDATE_TIME |   varchar   | 14 |   0    |    Y     |  N   |       | 更新时间  |
|  12   | REMARK |   varchar   | 1000 |   0    |    Y     |  N   |       | 材料描述  |

**表名：** <a id="zzwk_material_style_rel">zzwk_material_style_rel</a>

**说明：** 材料类型关联证照类型表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | ID |   varchar   | 32 |   0    |    N     |  Y   |       | 标识ID  |
|  2   | CLASSIFY |   varchar   | 2 |   0    |    Y     |  N   |       | 分类（证照）  |
|  3   | CERT_STYLE_NAME |   varchar   | 400 |   0    |    Y     |  N   |       | 证照类型名称  |
|  4   | CERT_STYLE_CODE |   varchar   | 22 |   0    |    Y     |  N   |       | 证照类型编码  |
|  5   | MATERIAL_STYLE_ID |   varchar   | 32 |   0    |    Y     |  N   |       | 材料类型ID  |
|  6   | DELETE_STATE |   varchar   | 2 |   0    |    Y     |  N   |       | 删除状态（0：未删除；1：已删除）  |
|  7   | CREATE_TIME |   varchar   | 14 |   0    |    Y     |  N   |       | 创建时间  |
|  8   | MATERIAL_STYLE_CODE |   varchar   | 100 |   0    |    Y     |  N   |       | 材料类型编码  |
|  9   | MATERIAL_STYLE_NAME |   varchar   | 400 |   0    |    Y     |  N   |       | 材料类型名称  |
|  10   | STATUS |   varchar   | 2 |   0    |    Y     |  N   |       | 可用状态（1可用/0不可用）  |
|  11   | MODIFY_DATE |   datetime   | 19 |   0    |    Y     |  N   |       | 修改时间  |

**表名：** <a id="zzwk_org_trustcode">zzwk_org_trustcode</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | ID |   varchar   | 32 |   0    |    N     |  Y   |       |   |
|  2   | ORG_TYPE |   varchar   | 32 |   0    |    Y     |  N   |       |   |
|  3   | ORG_NAME |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  4   | ORG_TRUST_CODE |   varchar   | 18 |   0    |    Y     |  N   |       |   |
|  5   | OPERATE_ORG_ID |   varchar   | 32 |   0    |    Y     |  N   |       |   |
|  6   | OPERATE_NAME |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  7   | OPERATE_ID |   varchar   | 32 |   0    |    Y     |  N   |       |   |
|  8   | OPERATE_TIME |   varchar   | 14 |   0    |    Y     |  N   |       |   |
|  9   | DELETE_STATE |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  10   | XYPTGUID |   varchar   | 36 |   0    |    Y     |  N   |       |   |
|  11   | XYPTBATCHGUID |   varchar   | 36 |   0    |    Y     |  N   |       |   |
|  12   | XYPT_TIME_DXP |   timestamp   | 26 |   0    |    N     |  N   |   CURRENT_TIMESTAMP(6)    |   |
|  13   | XYPTBATCHNO |   decimal   | 15 |   0    |    Y     |  N   |       |   |
|  14   | JHSJ_ZF |   datetime   | 19 |   0    |    Y     |  N   |       |   |
|  15   | UAAC_CODE |   varchar   | 255 |   0    |    Y     |  N   |       |   |

**表名：** <a id="zzwk_style">zzwk_style</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | ID |   varchar   | 32 |   0    |    N     |  Y   |       |   |
|  2   | CODE |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  3   | NAME |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  4   | TYPE_CODE |   varchar   | 4 |   0    |    Y     |  N   |       |   |
|  5   | TYPE_NAME |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  6   | ORG_TRUST_CODE |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  7   | ORG_NAME |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  8   | ORG_LEVEL |   varchar   | 20 |   0    |    Y     |  N   |       |   |
|  9   | VERSION |   decimal   | 11 |   0    |    Y     |  N   |       |   |
|  10   | OPERATE_ORG_ID |   varchar   | 32 |   0    |    Y     |  N   |       |   |
|  11   | OPERATE_NAME |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  12   | OPERATE_ID |   varchar   | 32 |   0    |    Y     |  N   |       |   |
|  13   | OPERATE_TIME |   varchar   | 14 |   0    |    Y     |  N   |       |   |
|  14   | DELETE_STATE |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  15   | ISSUE_LEVEL |   varchar   | 20 |   0    |    Y     |  N   |       |   |
|  16   | MANY_TO_ONE |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  17   | CATEGORY |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  18   | TYPE |   varchar   | 20 |   0    |    Y     |  N   |       |   |
|  19   | REMARK |   varchar   | 500 |   0    |    Y     |  N   |       |   |
|  20   | XYPTGUID |   varchar   | 36 |   0    |    Y     |  N   |       |   |
|  21   | XYPTBATCHGUID |   varchar   | 36 |   0    |    Y     |  N   |       |   |
|  22   | XYPT_TIME_DXP |   timestamp   | 26 |   0    |    N     |  N   |   CURRENT_TIMESTAMP(6)    |   |
|  23   | XYPTBATCHNO |   decimal   | 15 |   0    |    Y     |  N   |       |   |
|  24   | JHSJ_ZF |   datetime   | 19 |   0    |    Y     |  N   |       |   |
|  25   | RULE_INFO_FIRST |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  26   | RULE_INFO_SECOND |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  27   | RULE_INFO_THIRD |   varchar   | 100 |   0    |    Y     |  N   |       |   |
