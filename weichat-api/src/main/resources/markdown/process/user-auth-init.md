# 用户授权初始化流程

## 1. 适用范围

本文档用于说明企微账号的授权初始化流程，覆盖以下两种场景：

- 新用户首次初始化设备并完成授权登录
- 老用户掉线后基于历史 `vid` 重新发起授权登录

初始化流程成功后，系统会自动拉取好友列表、群列表、群成员并入库。

## 2. 流程总览

### 2.1 新用户首次授权

1. 调用初始化接口创建设备实例，获取 `uuid`
2. 绑定消息回调地址
3. 获取登录二维码
4. 企业微信扫码登录
5. 如果出现设备码校验，调用验证码接口完成二次授权

### 2.2 老用户掉线后重新授权

1. 根据历史账号 `vid` 调用初始化接口，重新获取本次登录用的 `uuid`
2. 绑定消息回调地址
3. 获取登录二维码
4. 企业微信扫码登录
5. 如果扫码后出现设备码授权页，再调用验证码接口完成授权

## 3. 关键参数说明

| 参数 | 来源 | 用途 |
|:---|:---|:---|
| `uuid` | 初始化接口返回 | 后续所有登录、发消息、拉取数据操作都需要携带 |
| `vid` | 已授权账号在库中的历史账号标识 | 老用户重新授权时用于复用历史设备信息 |
| `qrcodeKey` | 获取登录二维码接口返回的 `key` | 设备码二次授权时必填 |

## 4. 新用户首次授权流程

### 4.1 初始化设备

首次初始化时不传 `vid`，服务端会创建一个新的设备实例，并返回后续流程要使用的 `uuid`。

#### 请求示例

```bash
curl 'http://47.115.229.106:8066/api/v1/init/init' \
  -X POST \
  -H 'Content-Type: application/json' \
  --data-raw '{}'
```

#### 返回示例

```json
{
  "code": 0,
  "msg": "ok",
  "data": {
    "uuid": "57a1da3c19ca240e0075ed5b71ef1f39",
    "vid": null,
    "loginState": null,
    "qrCode": null
  }
}
```

说明：

- 后续所有操作都需要携带该 `uuid`
- 首次登录场景下，`vid` 可以为空

### 4.2 绑定消息回调

设备初始化完成后，先绑定回调地址，用于接收好友消息、群消息及登录相关通知。

#### 请求示例

```bash
curl 'http://47.115.229.106:8066/api/v1/init/setCallbackUrl' \
  -X POST \
  -H 'Content-Type: application/json' \
  --data-raw '{
    "url": "http://47.115.229.106:8066/wxwork/SetCallbackUrl",
    "uuid": "57a1da3c19ca240e0075ed5b71ef1f39"
  }'
```

说明：

- `uuid` 使用步骤 4.1 返回值
- `url` 为业务系统实际接收回调的地址

### 4.3 获取登录二维码

#### 请求示例

```bash
curl 'http://47.115.229.106:8066/api/v1/init/getQrCode' \
  -X POST \
  -H 'Content-Type: application/json' \
  --data-raw '{
    "uuid": "57a1da3c19ca240e0075ed5b71ef1f39"
  }'
```

#### 返回示例

```json
{
  "code": 0,
  "msg": "ok",
  "data": {
    "qrCode": "http://47.94.7.218:9952/CE374BDF-4D4D-42A9-90EB-6974B9D5D111.png?time=17751340330007325",
    "status": null,
    "expireTime": null,
    "key": "A034EBEF06A72789F1DE3E5367E32BBC"
  }
}
```

说明：

- `qrCode` 用于前端展示二维码图片
- `key` 即后续设备码授权所需的 `qrcodeKey`

### 4.4 企业微信扫码登录

用户使用企业微信客户端扫码登录。

- 如果扫码后直接登录成功，则授权流程结束
- 如果出现设备码验证，则继续执行步骤 4.5

### 4.5 设备码二次授权

当扫码后出现设备码验证页时，调用验证码校验接口完成授权。

#### 请求示例

```bash
curl 'http://47.115.229.106:8066/api/v1/init/checkCode' \
  -X POST \
  -H 'Content-Type: application/json' \
  --data-raw '{
    "code": "269157",
    "qrcodeKey": "79BB4B495AD88E769E33DED069C3A2AD",
    "uuid": "dede93adc5dbb87bcda5e0cc87aaa98b"
  }'
```

说明：

- 返回 HTTP 200 且业务结果成功，即表示授权完成
- `qrcodeKey` 取自获取二维码接口的返回值 `key`

## 5. 老用户掉线后重新授权

### 5.1 根据历史 vid 初始化设备

老用户重新授权时，需要先根据历史账号的 `vid` 初始化设备实例。

`vid` 可根据微信昵称到 `wx_user_info` 表中查询对应历史账号记录。

#### 请求示例

```bash
curl 'http://47.115.229.106:8066/api/v1/init/init' \
  -X POST \
  -H 'Content-Type: application/json' \
  --data-raw '{
    "vid": "1688853293330182"
  }'
```

#### 返回示例

```json
{
  "code": 0,
  "msg": "ok",
  "data": {
    "uuid": "413844c00e3c336e93ff6756be5cf7b3",
    "vid": null,
    "loginState": null,
    "qrCode": null
  }
}
```

说明：

- 本次返回的 `uuid` 是当前重连流程的设备实例标识
- 后续回调绑定、二维码获取、发消息等操作都使用这个 `uuid`

### 5.2 绑定消息回调

```bash
curl 'http://47.115.229.106:8066/api/v1/init/setCallbackUrl' \
  -X POST \
  -H 'Content-Type: application/json' \
  --data-raw '{
    "url": "http://47.115.229.106:8066/wxwork/SetCallbackUrl",
    "uuid": "413844c00e3c336e93ff6756be5cf7b3"
  }'
```

### 5.3 获取登录二维码

```bash
curl 'http://47.115.229.106:8066/api/v1/init/getQrCode' \
  -X POST \
  -H 'Content-Type: application/json' \
  --data-raw '{
    "uuid": "413844c00e3c336e93ff6756be5cf7b3"
  }'
```

#### 返回示例

```json
{
  "code": 0,
  "msg": "ok",
  "data": {
    "qrCode": "http://47.94.7.218:9952/CA9A068C-BF49-4247-AB86-ECA87CD17381.png?time=17751337110009642",
    "status": null,
    "expireTime": null,
    "key": "84D64C006A377BF36E075A9F7D94DFE6"
  }
}
```

### 5.4 企业微信扫码登录

- 如果扫码后直接成功，则本次重连授权完成
- 如果弹出设备码授权页，则继续调用步骤 4.5 的验证码校验接口

## 6. 授权成功后的结果

用户授权成功后，系统会自动执行以下数据同步动作：

- 拉取好友列表
- 拉取群列表
- 拉取群成员列表
- 将同步结果写入本地业务库

## 7. 发送文本消息示例

授权完成后，可使用授权设备的 `uuid` 发送消息。

#### 请求示例

```bash
curl 'http://47.115.229.106:8066/api/v1/message/sendText' \
  -X POST \
  -H 'Content-Type: application/json' \
  --data-raw '{
    "content": "测试小程序消息接口",
    "isRoom": false,
    "send_userid": 7881303467912712,
    "uuid": "413844c00e3c336e93ff6756be5cf7b3"
  }'
```

参数说明：

- `uuid`：发送人的授权设备号
- `send_userid`：`wx_friend_info.user_id` 字段值
- `content`：发送的文本消息内容

关系说明：

- 发送人的账号信息可在 `wx_user_info` 表中查询
- 当前发送人的 `user_id` 对应好友表中的 `owner_user_id`

## 8. 相关表说明

### 8.1 wx_user_info

用于保存已授权用户账号信息，可通过昵称等字段定位历史账号，并获取 `vid`、`uuid`、`user_id` 等关键字段。

关键字段：

- `vid`：历史账号设备标识，重新授权时使用
- `uuid`：当前账号关联的设备实例标识
- `user_id`：当前登录用户ID
- `nickname`：微信昵称

### 8.2 wx_friend_info

用于保存好友信息，发送消息时需要从该表中取目标好友的 `user_id`。

关键字段：

- `owner_user_id`：该好友所属的登录用户
- `user_id`：好友ID，发消息时作为 `send_userid`
- `nickname`：好友昵称
- `is_external`：是否外部联系人

## 9. 排查建议

- 初始化后务必保存 `uuid`，否则后续流程无法继续
- 二次授权时务必使用获取二维码接口返回的 `key`
- 老用户重连时优先使用历史 `vid` 初始化，避免重新生成新设备
- 如果消息发送失败，先核对 `uuid` 是否为当前在线设备，`send_userid` 是否来自正确的 `wx_friend_info` 记录
