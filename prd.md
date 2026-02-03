# 微信用户信息表设计文档

## 1. 需求背景
基于提供的JSON示例，设计微信用户信息表`wx_user_info`，用于存储企业微信用户的基本信息。

## 2. 设计原则
- 字段尽量设置为非必填
- 数据类型选择合适
- 添加必要的索引以提高查询性能
- 字段命名规范，使用下划线命名法

## 3. 建表语句

```sql
CREATE TABLE `wx_user_info` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `corpid` BIGINT(20) DEFAULT NULL COMMENT '企业微信企业ID',
  `unionid` VARCHAR(100) DEFAULT NULL COMMENT '用户唯一标识',
  `create_time` BIGINT(20) DEFAULT NULL COMMENT '创建时间戳',
  `admin_vid` BIGINT(20) DEFAULT NULL COMMENT '管理员ID',
  `sex` TINYINT(1) DEFAULT NULL COMMENT '性别：1-男，2-女，0-未知',
  `mobile` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `corp_full_name` VARCHAR(200) DEFAULT NULL COMMENT '企业全称',
  `acctid` VARCHAR(100) DEFAULT NULL COMMENT '账号ID',
  `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
  `corp_name` VARCHAR(200) DEFAULT NULL COMMENT '企业名称',
  `english_name` VARCHAR(100) DEFAULT NULL COMMENT '英文名',
  `realname` VARCHAR(100) DEFAULT NULL COMMENT '真实姓名',
  `vid` BIGINT(20) DEFAULT NULL COMMENT '视频号ID',
  `mail` VARCHAR(200) DEFAULT NULL COMMENT '邮箱',
  `ownername` VARCHAR(100) DEFAULT NULL COMMENT '所有者名称',
  `user_id` BIGINT(20) DEFAULT NULL COMMENT '用户ID',
  `nickname` VARCHAR(100) DEFAULT NULL COMMENT '昵称',
  `position` VARCHAR(100) DEFAULT NULL COMMENT '职位',
  `corp_desc` TEXT DEFAULT NULL COMMENT '企业描述',
  `corp_logo` VARCHAR(500) DEFAULT NULL COMMENT '企业LOGO',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_unionid` (`unionid`),
  KEY `idx_corp_id` (`corpid`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信用户信息表';
```

## 4. 字段说明

| 字段名 | 数据类型 | 长度 | 默认值 | 是否必填 | 注释 |
|-------|---------|------|-------|----------|------|
| id | BIGINT | 20 | 自增 | 是 | 主键ID |
| corpid | BIGINT | 20 | NULL | 否 | 企业微信企业ID |
| unionid | VARCHAR | 100 | NULL | 否 | 用户唯一标识 |
| create_time | BIGINT | 20 | NULL | 否 | 创建时间戳 |
| admin_vid | BIGINT | 20 | NULL | 否 | 管理员ID |
| sex | TINYINT | 1 | NULL | 否 | 性别：1-男，2-女，0-未知 |
| mobile | VARCHAR | 20 | NULL | 否 | 手机号 |
| corp_full_name | VARCHAR | 200 | NULL | 否 | 企业全称 |
| acctid | VARCHAR | 100 | NULL | 否 | 账号ID |
| avatar | VARCHAR | 500 | NULL | 否 | 头像URL |
| corp_name | VARCHAR | 200 | NULL | 否 | 企业名称 |
| english_name | VARCHAR | 100 | NULL | 否 | 英文名 |
| realname | VARCHAR | 100 | NULL | 否 | 真实姓名 |
| vid | BIGINT | 20 | NULL | 否 | 视频号ID |
| mail | VARCHAR | 200 | NULL | 否 | 邮箱 |
| ownername | VARCHAR | 100 | NULL | 否 | 所有者名称 |
| user_id | BIGINT | 20 | NULL | 否 | 用户ID |
| nickname | VARCHAR | 100 | NULL | 否 | 昵称 |
| position | VARCHAR | 100 | NULL | 否 | 职位 |
| corp_desc | TEXT | - | NULL | 否 | 企业描述 |
| corp_logo | VARCHAR | 500 | NULL | 否 | 企业LOGO |
| update_time | TIMESTAMP | - | CURRENT_TIMESTAMP | 否 | 更新时间 |

## 5. 索引设计

| 索引名称 | 索引类型 | 索引字段 | 作用 |
|---------|---------|---------|------|
| PRIMARY | 主键索引 | id | 唯一标识记录 |
| uk_unionid | 唯一索引 | unionid | 确保用户唯一标识不重复 |
| idx_corp_id | 普通索引 | corpid | 加速企业维度的查询 |
| idx_user_id | 普通索引 | user_id | 加速用户ID维度的查询 |

## 6. 数据示例

```json
{
  "corpid": 1970325109135931,
  "unionid": "ozynqskXWQK3wAWWKT_izAOfcIlY",
  "create_time": 0,
  "admin_vid": 0,
  "sex": 1,
  "mobile": "13573788293",
  "corp_full_name": "十年一刻",
  "acctid": "xxxxpeng",
  "avatar": "http://wework.qpic.cn/bizmail/yfG7qDz7ianwM1s5JXtgBLsaZKQfQ5YxZIiaQpUxAkEu9icIMaKxF3C4Q/0",
  "corp_name": "十年一刻",
  "english_name": "别名",
  "realname": "xxxxxx",
  "vid": 0,
  "mail": "",
  "ownername": "",
  "user_id": 1688853790599424,
  "nickname": "姓名",
  "position": "111",
  "corp_desc": "",
  "corp_logo": ""
}
```

## 7. 设计说明

1. **字段去重**：移除了重复字段，如`Create_time`（与`create_time`重复）、重复的`Corp_name`
2. **数据类型选择**：
   - 大整数使用BIGINT(20)
   - 字符串根据长度选择合适的VARCHAR
   - 文本内容使用TEXT
   - 时间戳使用BIGINT存储原始值，同时添加TIMESTAMP类型的update_time字段便于管理
3. **索引设计**：
   - 主键索引确保唯一性
   - 唯一索引确保unionid不重复
   - 普通索引加速常用查询维度
4. **非必填设计**：除主键外，所有字段均设置为非必填，符合用户要求

## 8. 后续扩展建议

- 根据实际业务需求，可以添加更多字段
- 考虑分库分表策略，当数据量大时提高性能
- 添加数据同步机制，确保数据实时更新
- 定期清理无效数据，优化存储空间

---

# 微信消息信息表设计文档

## 1. 需求背景
基于提供的多种消息格式JSON示例（纯文本、表情、小程序、图片），设计微信消息信息表`wx_message_info`，用于存储企业微信好友消息的各种格式数据。

## 2. 设计原则
- 支持多种消息类型，包括文本、表情、小程序、图片等
- 公共字段合并存储，定制化字段拆分保留
- 字段尽量设置为非必填
- 数据类型选择合适
- 添加必要的索引以提高查询性能
- 字段命名规范，使用下划线命名法

## 3. 建表语句

```sql
CREATE TABLE `wx_message_info` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `msg_id` BIGINT(20) DEFAULT NULL COMMENT '消息ID',
  `msgtype` TINYINT(4) DEFAULT NULL COMMENT '消息类型：0-文本，2-文本，78-小程序，101-图片，104-表情',
  `flag` BIGINT(20) DEFAULT NULL COMMENT '消息标记',
  `receiver` BIGINT(20) DEFAULT NULL COMMENT '接收人ID',
  `sender` BIGINT(20) DEFAULT NULL COMMENT '发送人ID',
  `sender_name` VARCHAR(255) DEFAULT NULL COMMENT '发送人名称',
  `is_room` TINYINT(1) DEFAULT NULL COMMENT '是否为群聊：0-否，1-是',
  `server_id` BIGINT(20) DEFAULT NULL COMMENT '服务器ID',
  `send_time` BIGINT(20) DEFAULT NULL COMMENT '发送时间戳',
  `referid` BIGINT(20) DEFAULT NULL COMMENT '参考ID',
  `app_info` VARCHAR(255) DEFAULT NULL COMMENT '应用信息',
  `readuinscount` INT(11) DEFAULT NULL COMMENT '已读人数',
  
  /* 文本消息字段 */
  `kf_id` BIGINT(20) DEFAULT NULL COMMENT '客服ID，只有客服消息接收才会有值否则为0',
  `content` TEXT DEFAULT NULL COMMENT '文本消息内容',
  `issync` TINYINT(1) DEFAULT NULL COMMENT '是否同步',
  `at_list` JSON DEFAULT NULL COMMENT '@列表，JSON格式',
  
  /* 表情消息字段 */
  `url` VARCHAR(500) DEFAULT NULL COMMENT '表情URL',
  `width` INT(11) DEFAULT NULL COMMENT '宽度，表情/图片/视频',
  `height` INT(11) DEFAULT NULL COMMENT '高度，表情/图片/视频',
  `emotion_type` VARCHAR(50) DEFAULT NULL COMMENT '表情类型',
  
  /* 小程序消息字段 */
  `thumb_file_id` VARCHAR(500) DEFAULT NULL COMMENT '小程序缩略图文件ID',
  `title` VARCHAR(255) DEFAULT NULL COMMENT '小程序标题',
  `app_name` VARCHAR(255) DEFAULT NULL COMMENT '小程序名称',
  `thumb_md5` VARCHAR(50) DEFAULT NULL COMMENT '小程序缩略图MD5',
  `pagepath` VARCHAR(500) DEFAULT NULL COMMENT '小程序页面路径',
  `size` INT(11) DEFAULT NULL COMMENT '小程序大小',
  `appid` VARCHAR(100) DEFAULT NULL COMMENT '小程序APPID',
  `thumb_aes_key` VARCHAR(100) DEFAULT NULL COMMENT '小程序缩略图AES密钥',
  `username` VARCHAR(255) DEFAULT NULL COMMENT '小程序用户名',
  `weapp_icon_url` VARCHAR(500) DEFAULT NULL COMMENT '小程序图标URL',
  `desc` TEXT DEFAULT NULL COMMENT '小程序描述',
  
  /* 图片消息字段 */
  `file_size` BIGINT(20) DEFAULT NULL COMMENT '图片大小',
  `file_id` TEXT DEFAULT NULL COMMENT '图片文件ID',
  `openim_cdn_authkey` VARCHAR(500) DEFAULT NULL COMMENT '图片CDN授权密钥',
  `aes_key` VARCHAR(100) DEFAULT NULL COMMENT '图片AES密钥',
  `md5` VARCHAR(50) DEFAULT NULL COMMENT '图片MD5值',
  
  /* 公共字段 */
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_msg_id` (`msg_id`),
  KEY `idx_receiver` (`receiver`),
  KEY `idx_sender` (`sender`),
  KEY `idx_send_time` (`send_time`),
  KEY `idx_msgtype` (`msgtype`),
  KEY `idx_server_id` (`server_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信消息信息表';
```

## 4. 字段说明

| 字段名 | 数据类型 | 长度 | 默认值 | 是否必填 | 注释 |
|-------|---------|------|-------|----------|------|
| id | BIGINT | 20 | 自增 | 是 | 主键ID |
| msg_id | BIGINT | 20 | NULL | 否 | 消息ID |
| msgtype | TINYINT | 4 | NULL | 否 | 消息类型：0-文本，2-文本，78-小程序，101-图片，104-表情 |
| flag | BIGINT | 20 | NULL | 否 | 消息标记 |
| receiver | BIGINT | 20 | NULL | 否 | 接收人ID |
| sender | BIGINT | 20 | NULL | 否 | 发送人ID |
| sender_name | VARCHAR | 255 | NULL | 否 | 发送人名称 |
| is_room | TINYINT | 1 | NULL | 否 | 是否为群聊：0-否，1-是 |
| server_id | BIGINT | 20 | NULL | 否 | 服务器ID |
| send_time | BIGINT | 20 | NULL | 否 | 发送时间戳 |
| referid | BIGINT | 20 | NULL | 否 | 参考ID |
| app_info | VARCHAR | 255 | NULL | 否 | 应用信息 |
| readuinscount | INT | 11 | NULL | 否 | 已读人数 |
| kf_id | BIGINT | 20 | NULL | 否 | 客服ID，只有客服消息接收才会有值否则为0 |
| content | TEXT | - | NULL | 否 | 文本消息内容 |
| issync | TINYINT | 1 | NULL | 否 | 是否同步 |
| at_list | JSON | - | NULL | 否 | @列表，JSON格式 |
| url | VARCHAR | 500 | NULL | 否 | 表情URL |
| width | INT | 11 | NULL | 否 | 宽度，表情/图片/视频 |
| height | INT | 11 | NULL | 否 | 高度，表情/图片/视频 |
| emotion_type | VARCHAR | 50 | NULL | 否 | 表情类型 |
| thumb_file_id | VARCHAR | 500 | NULL | 否 | 小程序缩略图文件ID |
| title | VARCHAR | 255 | NULL | 否 | 小程序标题 |
| app_name | VARCHAR | 255 | NULL | 否 | 小程序名称 |
| thumb_md5 | VARCHAR | 50 | NULL | 否 | 小程序缩略图MD5 |
| pagepath | VARCHAR | 500 | NULL | 否 | 小程序页面路径 |
| size | INT | 11 | NULL | 否 | 小程序大小 |
| appid | VARCHAR | 100 | NULL | 否 | 小程序APPID |
| thumb_aes_key | VARCHAR | 100 | NULL | 否 | 小程序缩略图AES密钥 |
| username | VARCHAR | 255 | NULL | 否 | 小程序用户名 |
| weapp_icon_url | VARCHAR | 500 | NULL | 否 | 小程序图标URL |
| desc | TEXT | - | NULL | 否 | 小程序描述 |
| file_size | BIGINT | 20 | NULL | 否 | 图片大小 |
| file_id | TEXT | - | NULL | 否 | 图片文件ID |
| openim_cdn_authkey | VARCHAR | 500 | NULL | 否 | 图片CDN授权密钥 |
| aes_key | VARCHAR | 100 | NULL | 否 | 图片AES密钥 |
| md5 | VARCHAR | 50 | NULL | 否 | 图片MD5值 |
| create_time | TIMESTAMP | - | CURRENT_TIMESTAMP | 否 | 创建时间 |
| update_time | TIMESTAMP | - | CURRENT_TIMESTAMP | 否 | 更新时间 |

## 5. 索引设计

| 索引名称 | 索引类型 | 索引字段 | 作用 |
|---------|---------|---------|------|
| PRIMARY | 主键索引 | id | 唯一标识记录 |
| uk_msg_id | 唯一索引 | msg_id | 确保消息ID不重复 |
| idx_receiver | 普通索引 | receiver | 加速接收人维度的查询 |
| idx_sender | 普通索引 | sender | 加速发送人维度的查询 |
| idx_send_time | 普通索引 | send_time | 加速时间维度的查询 |
| idx_msgtype | 普通索引 | msgtype | 加速消息类型维度的查询 |
| idx_server_id | 普通索引 | server_id | 加速服务器ID维度的查询 |

## 6. 数据示例

### 6.1 文本消息
```json
{
  "flag": 16777216,
  "kf_id": 111111111,
  "receiver": 1688855587446404,
  "sender_name": "",
  "is_room": 0,
  "server_id": 7130717,
  "content": "32423423",
  "issync": false,
  "send_time": 1724034152,
  "sender": 7881302555913738,
  "referid": 0,
  "app_info": "3304183318011621608",
  "readuinscount": 0,
  "msg_id": 1011720,
  "msgtype": 2,
  "atList": []
}
```

### 6.2 表情消息
```json
{
  "flag": 16777216,
  "receiver": 1688855587446404,
  "sender_name": "",
  "is_room": 0,
  "server_id": 7130718,
  "url": "https://wework.qpic.cn/wwpic3az/wwwx_c1f24912f3d283214fd7bf5da3c3b9cd/0",
  "issync": false,
  "send_time": 1724034243,
  "sender": 7881302555913738,
  "referid": 0,
  "emotion_type": "EMOTION_DYNAMIC",
  "app_info": "7678571470421336167",
  "readuinscount": 0,
  "msg_id": 1011722,
  "msgtype": 104,
  "width": 100,
  "height": 100
}
```

### 6.3 小程序消息
```json
{
  "thumbFileId": "306b020102046430620201000204bf366e0a02031e90380204dfc6f46d0204639acc410436333230383031353337305f313531323736303031375f38326163316362353632346136313035393265643566363861353963326666650201000203009c8004000201010201000400",
  "flag": 16777216,
  "receiver": 1688853790599424,
  "sender_name": "",
  "is_room": 0,
  "server_id": 12779516,
  "title": "铁友火车票",
  "appName": "小程序名称",
  "thumbMD5": "82ac1cb5624a610592ed5f68a59c2ffe",
  "pagepath": "pages/home/index.html",
  "send_time": 1671089217,
  "size": 40058,
  "sender": 7881302555913738,
  "referid": 0,
  "appid": "wx45dff5234240ad90",
  "thumbAESKey": "7063756277636571667576656873616A",
  "app_info": "from_msgid_2000643230453161151",
  "readuinscount": 0,
  "msg_id": 1108389,
  "msgtype": 78,
  "username": "gh_c4a2a98a7366@app",
  "weappIconUrl": "http://mmbiz.qpic.cn/mmbiz_png/BMAjdIR5qwChFiaRnxUTiadJicGwMpqchXVdfQQ67tHTzfgd0gkibupgeOKtHtyE9m9SZopHXxcpsSicWxzLKXwk0qA/640?wx_fmt=png&wxfrom=200",
  "desc": ""
}
```

### 6.4 图片消息
```json
{
  "flag": 16777216,
  "receiver": 1688853790599424,
  "sender_name": "",
  "is_room": 0,
  "server_id": 12779523,
  "file_size": 7982507,
  "send_time": 1671089597,
  "sender": 7881302555913738,
  "referid": 0,
  "file_id": "https://imunion.weixin.qq.com/cgi-bin/mmae-bin/tpdownloadmedia?param=v1_9402dda5b09845e753c80d85b8fdbbd9fec7a526c9da0b0d88ac1b7a74a4c4c1dd9b982527532a112af12c7a8a624b8117d171397ec6d0b94ca02c80bc5633357326cebc5d348c35625b2e6980fe6c9f3ecea79c3f22b5c4ab0c2d6c59d86e403f95bfcb1ffe7311b67cc0d7b31d4dd349ce38628e7c95561b036c6b787d6a04460b7146c54651e4ce73c5c31d5519936614efc9fb510d13f03229fac07d847626ed6dcf4673cd7312763fd73f763f208b2fa9e388e90fba6fc8dbb777bba4b94143d39f09a4e97a807a10195c4e871f8065321f8e9355304a669ac1deafc333399b2d06d072390ac980fcc80c4fa764ae59dd01b16dee0384df3073cdf28744",
  "openim_cdn_authkey": "v1_9402dda5b09845e753c80d85b8fdbbd9fec7a526c9da0b0d88ac1b7a74a4c4c1e5230c9b1626f74f55ffe8c3660d9d7aa62fe7376a0dddbb8f6575d040e9f369",
  "width": 0,
  "app_info": "from_msgid_9211250126199164434",
  "readuinscount": 0,
  "aes_key": "a2078ba39c47bfcfdbd9ec2574c2e134",
  "msg_id": 1108410,
  "msgtype": 101,
  "md5": "714d6b8ee6ca20fa6143338ba9c1c69a",
  "height": 0
}
```

## 7. 设计说明

1. **消息类型支持**：
   - 文本消息（msgtype: 0, 2）
   - 表情消息（msgtype: 104）
   - 小程序消息（msgtype: 78）
   - 图片消息（msgtype: 101）

2. **字段设计策略**：
   - 公共字段合并存储，如msg_id、msgtype、receiver、sender等
   - 定制化字段按消息类型拆分保留，如文本消息的content字段，表情消息的url字段，小程序消息的title、appid字段等
   - 使用JSON类型存储动态数据，如at_list字段

3. **数据类型选择**：
   - 大整数使用BIGINT(20)
   - 小整数使用TINYINT或INT
   - 字符串根据长度选择合适的VARCHAR
   - 文本内容使用TEXT
   - 结构化数据使用JSON
   - 时间使用BIGINT存储原始时间戳，同时添加TIMESTAMP类型的update_time字段便于管理

4. **索引设计**：
   - 主键索引确保唯一性
   - 唯一索引确保msg_id不重复
   - 普通索引加速常用查询维度：receiver、sender、send_time、msgtype、server_id

5. **非必填设计**：
   - 除主键外，所有字段均设置为非必填
   - 不同消息类型只填充对应类型的字段，其他字段可留空

## 8. 后续扩展建议

- 根据实际业务需求，支持更多消息类型，如语音、视频、文件等
- 考虑分库分表策略，按时间或用户维度分表，提高查询性能
- 添加消息状态字段，如已读/未读状态
- 添加消息扩展字段，支持更灵活的消息扩展
- 定期清理过期消息，优化存储空间
- 考虑添加全文检索功能，方便搜索消息内容
- 实现消息同步机制，确保数据实时更新

---

# 微信群信息表设计文档

## 1. 需求背景
基于提供的群聊JSON示例，设计微信群信息表`wx_group_info`和微信群成员表`wx_group_member`，用于存储微信群聊基本信息和群成员信息。

## 2. 设计原则
- 支持微信群聊和群成员的完整信息存储
- 字段尽量设置为非必填
- 数据类型选择合适
- 添加必要的索引以提高查询性能
- 字段命名规范，使用下划线命名法
- 表关系清晰，一对多关系设计

## 3. 建表语句

### 3.1 群表 `wx_group_info`

```sql
CREATE TABLE `wx_group_info` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `room_id` VARCHAR(100) DEFAULT NULL COMMENT '群ID',
  `create_user_id` BIGINT(20) DEFAULT NULL COMMENT '创建者ID',
  `total` INT(11) DEFAULT NULL COMMENT '群成员总数',
  `notice_sendervid` BIGINT(20) DEFAULT NULL COMMENT '通知发送者ID',
  `flag` BIGINT(20) DEFAULT NULL COMMENT '标记',
  `create_time` BIGINT(20) DEFAULT NULL COMMENT '创建时间戳',
  `notice_content` TEXT DEFAULT NULL COMMENT '通知内容',
  `nickname` VARCHAR(255) DEFAULT NULL COMMENT '群昵称',
  `new_flag` INT(11) DEFAULT NULL COMMENT '新标记',
  `notice_time` BIGINT(20) DEFAULT NULL COMMENT '通知时间',
  `managers` JSON DEFAULT NULL COMMENT '管理员列表，JSON格式',
  
  /* 公共字段 */
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_room_id` (`room_id`),
  KEY `idx_create_user_id` (`create_user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信群信息表';
```

### 3.2 群成员表 `wx_group_member`

```sql
CREATE TABLE `wx_group_member` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `room_id` VARCHAR(100) DEFAULT NULL COMMENT '群ID，关联wx_group_info表',
  `unionid` VARCHAR(100) DEFAULT NULL COMMENT '唯一标识',
  `create_time` BIGINT(20) DEFAULT NULL COMMENT '创建时间戳',
  `sex` TINYINT(1) DEFAULT NULL COMMENT '性别：1-男，2-女，0-未知',
  `mobile` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `acctid` VARCHAR(100) DEFAULT NULL COMMENT '账号ID',
  `join_scene` INT(11) DEFAULT NULL COMMENT '加入场景',
  `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
  `english_name` VARCHAR(100) DEFAULT NULL COMMENT '英文名',
  `realname` VARCHAR(100) DEFAULT NULL COMMENT '真实姓名',
  `room_notes` TEXT DEFAULT NULL COMMENT '群备注',
  `jointime` BIGINT(20) DEFAULT NULL COMMENT '加入时间戳',
  `nickname` VARCHAR(255) DEFAULT NULL COMMENT '昵称',
  `room_nickname` VARCHAR(255) DEFAULT NULL COMMENT '群昵称',
  `position` VARCHAR(100) DEFAULT NULL COMMENT '职位',
  `uin` BIGINT(20) DEFAULT NULL COMMENT '用户ID',
  `invite_user_id` BIGINT(20) DEFAULT NULL COMMENT '邀请者ID',
  `corp_id` BIGINT(20) DEFAULT NULL COMMENT '企业ID',
  
  /* 公共字段 */
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`),
  KEY `idx_room_id` (`room_id`),
  KEY `idx_uin` (`uin`),
  KEY `idx_join_time` (`jointime`),
  KEY `idx_corp_id` (`corp_id`),
  KEY `idx_invite_user_id` (`invite_user_id`),
  
  /* 外键约束，可根据实际情况决定是否启用 */
  CONSTRAINT `fk_group_member_room_id` FOREIGN KEY (`room_id`) REFERENCES `wx_group_info` (`room_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信群成员表';
```

## 4. 字段说明

### 4.1 群表 `wx_group_info`

| 字段名 | 数据类型 | 长度 | 默认值 | 是否必填 | 注释 |
|-------|---------|------|-------|----------|------|
| id | BIGINT | 20 | 自增 | 是 | 主键ID |
| room_id | VARCHAR | 100 | NULL | 否 | 群ID |
| create_user_id | BIGINT | 20 | NULL | 否 | 创建者ID |
| total | INT | 11 | NULL | 否 | 群成员总数 |
| notice_sendervid | BIGINT | 20 | NULL | 否 | 通知发送者ID |
| flag | BIGINT | 20 | NULL | 否 | 标记 |
| create_time | BIGINT | 20 | NULL | 否 | 创建时间戳 |
| notice_content | TEXT | - | NULL | 否 | 通知内容 |
| nickname | VARCHAR | 255 | NULL | 否 | 群昵称 |
| new_flag | INT | 11 | NULL | 否 | 新标记 |
| notice_time | BIGINT | 20 | NULL | 否 | 通知时间 |
| managers | JSON | - | NULL | 否 | 管理员列表，JSON格式 |
| update_time | TIMESTAMP | - | CURRENT_TIMESTAMP | 否 | 更新时间 |

### 4.2 群成员表 `wx_group_member`

| 字段名 | 数据类型 | 长度 | 默认值 | 是否必填 | 注释 |
|-------|---------|------|-------|----------|------|
| id | BIGINT | 20 | 自增 | 是 | 主键ID |
| room_id | VARCHAR | 100 | NULL | 否 | 群ID，关联wx_group_info表 |
| unionid | VARCHAR | 100 | NULL | 否 | 唯一标识 |
| create_time | BIGINT | 20 | NULL | 否 | 创建时间戳 |
| sex | TINYINT | 1 | NULL | 否 | 性别：1-男，2-女，0-未知 |
| mobile | VARCHAR | 20 | NULL | 否 | 手机号 |
| acctid | VARCHAR | 100 | NULL | 否 | 账号ID |
| join_scene | INT | 11 | NULL | 否 | 加入场景 |
| avatar | VARCHAR | 500 | NULL | 否 | 头像URL |
| english_name | VARCHAR | 100 | NULL | 否 | 英文名 |
| realname | VARCHAR | 100 | NULL | 否 | 真实姓名 |
| room_notes | TEXT | - | NULL | 否 | 群备注 |
| jointime | BIGINT | 20 | NULL | 否 | 加入时间戳 |
| nickname | VARCHAR | 255 | NULL | 否 | 昵称 |
| room_nickname | VARCHAR | 255 | NULL | 否 | 群昵称 |
| position | VARCHAR | 100 | NULL | 否 | 职位 |
| uin | BIGINT | 20 | NULL | 否 | 用户ID |
| invite_user_id | BIGINT | 20 | NULL | 否 | 邀请者ID |
| corp_id | BIGINT | 20 | NULL | 否 | 企业ID |
| update_time | TIMESTAMP | - | CURRENT_TIMESTAMP | 否 | 更新时间 |

## 5. 索引设计

### 5.1 群表 `wx_group_info`

| 索引名称 | 索引类型 | 索引字段 | 作用 |
|---------|---------|---------|------|
| PRIMARY | 主键索引 | id | 唯一标识记录 |
| uk_room_id | 唯一索引 | room_id | 确保群ID不重复 |
| idx_create_user_id | 普通索引 | create_user_id | 加速创建者维度的查询 |
| idx_create_time | 普通索引 | create_time | 加速时间维度的查询 |

### 5.2 群成员表 `wx_group_member`

| 索引名称 | 索引类型 | 索引字段 | 作用 |
|---------|---------|---------|------|
| PRIMARY | 主键索引 | id | 唯一标识记录 |
| idx_room_id | 普通索引 | room_id | 加速群维度的查询 |
| idx_uin | 普通索引 | uin | 加速用户维度的查询 |
| idx_join_time | 普通索引 | jointime | 加速加入时间维度的查询 |
| idx_corp_id | 普通索引 | corp_id | 加速企业ID维度的查询 |
| idx_invite_user_id | 普通索引 | invite_user_id | 加速邀请者维度的查询 |

## 6. 数据示例

### 6.1 群表数据示例

```json
{
  "room_id": "10927640775842980",
  "create_user_id": 7881302555913738,
  "total": 4,
  "notice_sendervid": 10927640775842980,
  "flag": 268435585,
  "create_time": 1687687448,
  "notice_content": "",
  "nickname": "",
  "new_flag": 4,
  "notice_time": 10927640775842980,
  "managers": []
}
```

### 6.2 群成员表数据示例

```json
{
  "room_id": "10927640775842980",
  "unionid": "",
  "create_time": 0,
  "sex": 1,
  "mobile": "",
  "acctid": "",
  "join_scene": 1,
  "avatar": "https://wework.qpic.cn/wwpic/510408_Qat5EH4YQaidquh_1678715517/0",
  "english_name": "",
  "realname": "xxx",
  "room_notes": "",
  "jointime": 1687687448,
  "nickname": "xxxx",
  "room_nickname": "",
  "position": "",
  "uin": "168xxx199881",
  "invite_user_id": 7881302555913738,
  "corp_id": 1970324968093920
}
```

## 7. 设计说明

1. **表关系设计**：
   - 一对多关系：一个群可以有多个成员
   - 通过room_id字段关联两张表
   - 外键约束可选，根据实际业务需求决定是否启用

2. **字段设计策略**：
   - 群表存储群的基本信息和统计信息
   - 群成员表存储每个成员的详细信息
   - 结构化数据使用JSON类型存储，如管理员列表

3. **数据类型选择**：
   - 大整数使用BIGINT(20)
   - 字符串根据长度选择合适的VARCHAR
   - 文本内容使用TEXT
   - 结构化数据使用JSON
   - 时间戳使用BIGINT存储原始值，同时添加TIMESTAMP类型的update_time字段

4. **索引设计**：
   - 群表：room_id唯一索引，create_user_id、create_time普通索引
   - 群成员表：room_id、uin、jointime、corp_id、invite_user_id普通索引

5. **非必填设计**：
   - 除主键外，所有字段均设置为非必填
   - 适应不同群聊场景的差异

## 8. 后续扩展建议

- 根据实际业务需求，添加更多群相关字段
- 考虑分库分表策略，按群ID或时间维度分表
- 添加群状态字段，如群是否活跃
- 添加群标签字段，支持群分类
- 实现群消息同步机制
- 定期清理不活跃的群和成员数据
- 考虑添加群公告、群文件等相关功能的表设计
- 实现群成员权限管理功能

---

# 微信好友信息表设计文档

## 1. 需求背景
基于提供的好友列表JSON示例，设计微信好友信息表`wx_friend_info`，用于存储微信好友基本信息。

## 2. 设计原则
- 支持微信好友完整信息存储
- 字段尽量设置为非必填
- 数据类型选择合适
- 添加必要的索引以提高查询性能
- 字段命名规范，使用下划线命名法
- 支持好友列表同步功能

## 3. 建表语句

```sql
CREATE TABLE `wx_friend_info` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `unionid` VARCHAR(100) DEFAULT NULL COMMENT '用户唯一标识',
  `create_time` BIGINT(20) DEFAULT NULL COMMENT '创建时间戳',
  `sex` TINYINT(1) DEFAULT NULL COMMENT '性别：1-男，2-女，0-未知',
  `mobile` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `company_remark` VARCHAR(255) DEFAULT NULL COMMENT '公司备注',
  `acctid` VARCHAR(100) DEFAULT NULL COMMENT '账号ID',
  `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
  `english_name` VARCHAR(100) DEFAULT NULL COMMENT '英文名',
  `remark_phone` JSON DEFAULT NULL COMMENT '备注手机号，JSON数组格式',
  `realname` VARCHAR(100) DEFAULT NULL COMMENT '真实姓名',
  `real_remarks` VARCHAR(255) DEFAULT NULL COMMENT '真实备注',
  `user_id` BIGINT(20) DEFAULT NULL COMMENT '好友ID',
  `nickname` VARCHAR(255) DEFAULT NULL COMMENT '昵称',
  `position` VARCHAR(100) DEFAULT NULL COMMENT '职位',
  `corp_id` BIGINT(20) DEFAULT NULL COMMENT '企业ID',
  `remarks` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `seq` BIGINT(20) DEFAULT NULL COMMENT '序列，用于同步',
  `status` TINYINT(1) DEFAULT NULL COMMENT '状态：0-正常，1-已删除，2-黑名单等',
  
  /* 公共字段 */
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_unionid` (`unionid`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_seq` (`seq`),
  KEY `idx_corp_id` (`corp_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信好友信息表';
```

## 4. 字段说明

| 字段名 | 数据类型 | 长度 | 默认值 | 是否必填 | 注释 |
|-------|---------|------|-------|----------|------|
| id | BIGINT | 20 | 自增 | 是 | 主键ID |
| unionid | VARCHAR | 100 | NULL | 否 | 用户唯一标识 |
| create_time | BIGINT | 20 | NULL | 否 | 创建时间戳 |
| sex | TINYINT | 1 | NULL | 否 | 性别：1-男，2-女，0-未知 |
| mobile | VARCHAR | 20 | NULL | 否 | 手机号 |
| company_remark | VARCHAR | 255 | NULL | 否 | 公司备注 |
| acctid | VARCHAR | 100 | NULL | 否 | 账号ID |
| avatar | VARCHAR | 500 | NULL | 否 | 头像URL |
| english_name | VARCHAR | 100 | NULL | 否 | 英文名 |
| remark_phone | JSON | - | NULL | 否 | 备注手机号，JSON数组格式 |
| realname | VARCHAR | 100 | NULL | 否 | 真实姓名 |
| real_remarks | VARCHAR | 255 | NULL | 否 | 真实备注 |
| user_id | BIGINT | 20 | NULL | 否 | 好友ID |
| nickname | VARCHAR | 255 | NULL | 否 | 昵称 |
| position | VARCHAR | 100 | NULL | 否 | 职位 |
| corp_id | BIGINT | 20 | NULL | 否 | 企业ID |
| remarks | VARCHAR | 255 | NULL | 否 | 备注 |
| seq | BIGINT | 20 | NULL | 否 | 序列，用于同步 |
| status | TINYINT | 1 | NULL | 否 | 状态：0-正常，1-已删除，2-黑名单等 |
| update_time | TIMESTAMP | - | CURRENT_TIMESTAMP | 否 | 更新时间 |

## 5. 索引设计

| 索引名称 | 索引类型 | 索引字段 | 作用 |
|---------|---------|---------|------|
| PRIMARY | 主键索引 | id | 唯一标识记录 |
| uk_unionid | 唯一索引 | unionid | 确保用户唯一标识不重复 |
| idx_user_id | 普通索引 | user_id | 加速按好友ID查询 |
| idx_seq | 普通索引 | seq | 加速按序列同步查询 |
| idx_corp_id | 普通索引 | corp_id | 加速按企业ID查询 |
| idx_status | 普通索引 | status | 加速按状态查询 |

## 6. 数据示例

```json
{
  "unionid": "ozynqskXWQK3wAWWKT_izAOfcIlY",
  "create_time": 0,
  "sex": 1,
  "mobile": "",
  "company_remark": "",
  "acctid": "",
  "avatar": "http://wx.qlogo.cn/mmhead/Q3auHgzwzM7BQVtj83EnfQcDMjz2hhrIvssZl9fv1H6rYUaN8VRibjg/0",
  "english_name": "",
  "remark_phone": [],
  "realname": "",
  "real_remarks": "",
  "user_id": 7881302555913738,
  "nickname": "十年一刻222222",
  "position": "",
  "corp_id": 1970325134026788,
  "remarks": "",
  "seq": 8085933,
  "status": 0
}
```

## 7. 设计说明

1. **字段映射**：根据JSON示例中的字段，将每个好友属性映射为表字段，数组类型使用JSON存储
2. **数据类型选择**：
   - 大整数使用BIGINT(20)
   - 字符串根据长度选择合适的VARCHAR
   - 数组使用JSON类型
   - 时间使用BIGINT存储原始时间戳，同时添加TIMESTAMP类型的update_time字段
3. **索引设计**：
   - 主键索引确保唯一性
   - 唯一索引确保unionid不重复
   - 普通索引加速常用查询维度：user_id、seq、corp_id、status
4. **非必填设计**：
   - 除主键外，所有字段均设置为非必填
   - 适应不同好友信息的差异
5. **同步支持**：
   - 保留seq字段，用于好友列表的同步更新
6. **状态管理**：
   - 添加status字段，支持好友状态管理（正常、删除、黑名单等）

## 8. 后续扩展建议

- 添加好友分组功能，支持好友分类
- 添加好友标签字段，支持多标签管理
- 添加好友来源字段，记录好友添加方式
- 添加好友关系字段，区分单向/双向好友
- 添加好友活跃度字段，记录好友活跃情况
- 考虑分库分表策略，按user_id或时间维度分表
- 实现好友同步机制，确保数据实时更新
- 定期清理无效好友数据，优化存储空间
- 添加好友备注历史记录功能
- 支持好友消息免打扰设置

---

# 回调任务表设计文档

## 1. 需求背景
设计回调任务表`wx_callback_task`，用于存储微信回调请求参数和处理状态，支持异步处理和重试机制。

## 2. 设计原则
- 支持回调请求的持久化存储
- 支持任务状态管理和重试机制
- 添加必要的索引以提高查询性能
- 字段命名规范，使用下划线命名法

## 3. 建表语句

```sql
CREATE TABLE `wx_callback_task` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `uuid` VARCHAR(64) NOT NULL COMMENT '运行实例ID',
  `json_content` TEXT NOT NULL COMMENT '回调消息内容JSON',
  `type` VARCHAR(64) NOT NULL COMMENT '消息类型',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态:0-待处理,1-处理中,2-成功,3-失败',
  `retry_count` INT NOT NULL DEFAULT 0 COMMENT '重试次数',
  `max_retry_count` INT NOT NULL DEFAULT 3 COMMENT '最大重试次数',
  `error_message` VARCHAR(1024) DEFAULT NULL COMMENT '错误信息',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `process_time` DATETIME DEFAULT NULL COMMENT '处理时间',
  PRIMARY KEY (`id`),
  INDEX `idx_status_retry` (`status`, `retry_count`),
  INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='回调任务表';
```

## 4. 字段说明

| 字段名 | 数据类型 | 默认值 | 是否必填 | 注释 |
|-------|---------|-------|----------|------|
| id | BIGINT | 自增 | 是 | 主键ID |
| uuid | VARCHAR(64) | - | 是 | 运行实例ID |
| json_content | TEXT | - | 是 | 回调消息内容JSON |
| type | VARCHAR(64) | - | 是 | 消息类型 |
| status | TINYINT | 0 | 是 | 状态:0-待处理,1-处理中,2-成功,3-失败 |
| retry_count | INT | 0 | 是 | 重试次数 |
| max_retry_count | INT | 3 | 是 | 最大重试次数 |
| error_message | VARCHAR(1024) | NULL | 否 | 错误信息 |
| create_time | DATETIME | CURRENT_TIMESTAMP | 是 | 创建时间 |
| update_time | DATETIME | NULL | 否 | 更新时间 |
| process_time | DATETIME | NULL | 否 | 处理时间 |

## 5. 索引设计

| 索引名称 | 索引类型 | 索引字段 | 作用 |
|---------|---------|---------|------|
| PRIMARY | 主键索引 | id | 唯一标识记录 |
| idx_status_retry | 联合索引 | status, retry_count | 加速待处理任务查询 |
| idx_create_time | 普通索引 | create_time | 加速按时间查询 |

## 6. 状态流转

```
待处理(0) -> 处理中(1) -> 成功(2)
                      -> 失败(3) -> 待处理(0) [重试]
```

## 7. 设计说明

1. **异步处理**：Controller接收请求后立即入库返回，由定时任务异步处理
2. **重试机制**：失败后自动重试，达到最大重试次数后标记为失败
3. **乐观锁**：通过status字段实现任务锁定，防止重复处理

---
