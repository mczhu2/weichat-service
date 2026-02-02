# 03-message

## 撤回消息



[TOC]
    
##### 简要描述

- 撤回消息

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/RevokeMsg
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|msgid |是  |int |要撤回的消息id|
|roomid |是  |long |如果撤回的消息是群消息，则填写群id，联系人消息则填0|
##### 请求示例 

``` 
{
    "uuid":"a4ea6a39-4b3a-4098-a250-2a07bef57355",
    "msgid":1063645,
    "roomid":0
}
```

##### 返回示例 

``` 
{
    "data": null,
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 发送文本消息



[TOC]
    
##### 简要描述

- 发送文本消息

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SendTextMsg
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|send_userid |是  |long |要发送的人id|
|isRoom |是  |bool |是否是群消息|
|content |是  |String |发送文本内容|
##### 请求示例 

``` 
{
    "uuid":"1753cdff-0501-42fe-bb5a-2a4b9629f7fb",
	"kf_id":0,   //用来客服消息回复，正常发送忽略这个字段。
    "send_userid":7881302555913738,
    "isRoom":false,
    "content":"ddddddd"
}
```

##### 返回示例 

``` 
{
    "data": {
        "receiver": 7881302555913738,
        "sender": 1688853790599424,
        "at_list": [],
        "sender_name": "",
		"app_info":"from_msgid_697590829808721944",
        "is_room": 0,
        "sendtime": 1652169352,
        "msg_id": 1066230,
        "server_id": 12381203,
        "msgtype": 2,
        "content": "ddddddd"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 发送文本表情消息



[TOC]
    
##### 简要描述

- 发送文本表情消息

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SendTextAndExpMsg
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|send_userid |是  |long |要发送的人或群id|
|isRoom |是  |bool |是否是群消息|
##### 请求示例 

``` 
{
    "uuid":"1688853790xxxx",
    "send_userid":788130255xxxx,
    "isRoom":false,
    "content":[
        {
            "msgtype":0,
            "msg":"你好"
        },
        {
            "msgtype":3,
            "msg":"[微笑]"
        },
        {
            "msgtype":0,
            "msg":"哈哈"
        }
    ]
}
```

##### 返回示例 

``` 
{
    "data": {
        "receiver": 7881302555xxx,
        "sender": 1688853790xxx,
        "at_list": [],
        "sender_name": "",
        "is_room": 0,
        "sendtime": 1678197779,
        "msg_id": 1115653,
        "server_id": 12883461,
        "msgtype": 2,
        "content": "你好[微笑]]哈哈"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 发送CDN图片消息



[TOC]
    
##### 简要描述

- 发送CDN图片消息

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SendCDNImgMsg
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|send_userid |是  |long |要发送的人或群id|
|isRoom |是  |bool |是否是群消息|
|cdnkey |是  |String |cdnkey|
|aeskey |是  |String |aeskey|
|md5 |是  |String |md5|
|fileSize |是  |int |文件大小|
##### 请求示例 

``` 
{
    "uuid": "47b24a10e92f66b2fc440608a19c86c8",
    "send_userid": "7881302555913738",
    "kf_id": 0, //客服id 如果需要回复客服消息则填客服id 没有客服消息忽略他
    "isRoom": false, //是否发送群消息
    "cdnkey": "306b020102046430620201000204060b19e102034c4cd20204986b259902046807456b042466303163353336302d636437642d346335302d626238632d38356638333361613238376402031038000203317d8004107d8a8e79499210c6242c36afd10647c70201010201000400",
    "aeskey": "B26DB4824341608DB848A2DA563B7147",
    "md5": "7d8a8e79499210c6242c36afd10647c7",
    "fileSize": 3243381,
    "width": 1279, //宽度
    "height": 1706,//高度
    "thumb_image_height": 512, //缩略图高度
    "thumb_image_width": 384,//缩略图宽度
    "thumb_file_size": 15195,//缩略图大小
    "thumb_file_md5": "4331947dc5f67c0d8a2893653a3f0cee",//缩略图md5
    "is_hd": 1 //用来转发接收到的图片消息的， 如果接收的图片消息中的is_hd字段是1就填1 是2就填2，  正常上传发送忽略他
}
```

##### 返回示例 

``` 
{
    "data": {
        "receiver": 7881302555913738,
        "cdn_key": "308189020102048181307f0201000204ea44290002030f55cb0204a48e1e6f0204626c17c604444e45574944315f6561343432393030613438653165366636323663313833385f31313841414234302d453237422d346539312d383843442d33343735453032423531313202010002023c600410d9c8750bed0b3c7d089fa7d55720d6cf0201010201000400",
        "file_name": "",
        "sender_name": "",
        "is_room": 0,
        "server_id": 12381205,
        "size": 15444,
		"app_info":"from_msgid_697590829808721944",
        "sender": 1688853790599424,
        "aes_key": "66303662386666346430373037326666",
        "sendtime": 1652169457,
        "msg_id": 1066235,
        "msgtype": 14,
        "md5": "d9c8750bed0b3c7d089fa7d55720d6cf"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 发送CDN文件消息



[TOC]
    
##### 简要描述

- 发送CDN文件消息

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SendCDNFileMsg
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|send_userid |是  |long |要发送的人或群id|
|isRoom |是  |bool |是否是群消息|

##### 请求示例 

``` 
{
    "uuid":"1753cdff-0501-42fe-bb5a-2a4b9629f7fb",
    "send_userid":7881302555913738,
    "cdnkey":"30680201020461305f0201xxxx0501d03e3d1c0480fd7d1193205a080160201050201000400",
    "aeskey":"38633161326636666630396135353630",
    "md5":"501d03e3d1c0480fd7d1193205a08016",
    "file_name":"111.txt",
    "fileSize":2813,
    "isRoom":false
}
```

##### 返回示例 

``` 
{
    "data": {
        "receiver": 7881302555913738,
        "cdn_key": "30680201020461305f0201000204ea44290002030f55cb0204a48e1e6f0204626d8bb0042430453235394136372d443435322d343032302d393341372d38344445364532363030303902010002020b000410501d03e3d1c0480fd7d1193205a080160201050201000400",
        "file_name": "协议.txt",
        "sender_name": "",
        "is_room": 0,
        "server_id": 12381206,
        "size": 2813,
		"app_info":"from_msgid_697590829808721944",
        "sender": 1688853790599424,
        "aes_key": "38633161326636666630396135353630",
        "sendtime": 1652169489,
        "msg_id": 1066237,
        "msgtype": 15,
        "md5": "501d03e3d1c0480fd7d1193205a08016"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 发送CDN语音消息



[TOC]
    
##### 简要描述

- 发送CDN语音消息

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SendCDNVoiceMsg
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|send_userid |是  |long |要发送的人或群id|
|isRoom |是  |bool |是否是群消息|
##### 请求示例 

``` 
{
    "uuid":"1753cdff-0501-42fe-bb5a-2a4b9629f7fb",
    "send_userid":7881302555913738,
    "cdnkey":"308180020102047930770201000204bf366e0a02031e90380204afdf892b0204626d903d044c333230383031353337305f36353646373236463737363537303633363136313643363336373733373136325f34643061383739323537383238643361376636323565336234373835383961330201000202145004000201050201000400",
    "aeskey":"656F726F7765706361616C6367737162",
    "md5":"4d0a879257828d3a7f625e3b478589a3",
    "voice_time":4,
    "fileSize":5184,
    "isRoom":false
}
```

##### 返回示例 

``` 
{
    "data": {
        "receiver": 7881302555913738,
        "cdn_key": "308180020102047930770201000204bf366e0a02031e90380204afdf892b0204626d903d044c333230383031353337305f36353646373236463737363537303633363136313643363336373733373136325f34643061383739323537383238643361376636323565336234373835383961330201000202145004000201050201000400",
        "file_name": "",
        "sender_name": "",
        "is_room": 0,
		"app_info":"from_msgid_697590829808721944",
        "server_id": 12381207,
        "size": 5184,
        "sender": 1688853790599424,
        "aes_key": "656F726F7765706361616C6367737162",
        "sendtime": 1652169528,
        "msg_id": 1066239,
        "msgtype": 16,
        "md5": "4d0a879257828d3a7f625e3b478589a3"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 发送CDN视频消息



[TOC]
    
##### 简要描述

- 发送CDN视频消息

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SendCDNVideoMsg
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|send_userid |是  |long |要发送的人或群id|
|isRoom |是  |bool |是否是群消息|
##### 请求示例 

``` 
{
    "uuid":"505f61baa654b5b684a8c1917ad8bb00",
    "send_userid":7881302555913738,
    "cdnkey":"306b02010204643062020100020x04109c6131377dae038824b43f8e388efa1c0201040201000400",
    "aeskey":"BE02BA568E3B545F04009F5E81972C0E",
    "md5":"9c6131377dae038824b43f8e388efa1c",
    "video_duration":38,
    "fileSize":3561782,
    "video_img_size":65472, 
    "video_width":540, //视频宽度
    "video_height":1170,//视频高度
    "isRoom":false
}
```

##### 返回示例 

``` 
{
    "data": {
        "video_duration": 38,//视频时常
        "video_height": 1170,//视频高度
        "openim_cdn_ld_aeskey": "",
        "receiver": 78813025559xxx,  //接收人
        "openim_cdn_ld_size": 65472, //视频缩略图大小
        "file_name": "",
        "video_width": 540, //视频宽度
        "sender_name": "",
        "cdn_Key": "",
        "is_room": 0,
        "server_id": 7651694, //消息seq
        "file_size": 3561782,//视频大小
        "size": 3561782,//视频大小
        "VideoDuration": 38, //视频时常
        "sender": 1688854256622049,
        "file_id": "306b020102046430620201000204060b19ex131377dae038824b43f8e388efa1c0201040201000400", //视频id
        "openim_cdn_authkey": "",
        "aes_key": "BE02BA568E3B545F04009F5E81972C0E", //aeskey
        "app_info": "CIOACBDIjp7Q6jIY4bOssJCAgAMgEA==",
        "sendtime": 1746609341, //发送时间
        "msg_id": 1014578,
        "msgtype": 23,//消息类型
        "md5": "9c6131377dae038824b43f8e388efa1c", 
        "preview_img_url": ""
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 发送大视频文件




[TOC]
    
##### 简要描述

- 发送大视频文件 需要走大文件上传别走cdn上传！！！！！！！！！！！！！！！！！！！！

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SendCDNBigVideoMsg
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|send_userid |是  |long |要发送的人或群id|
|isRoom |是  |bool |是否是群消息|
##### 请求示例 

``` 
{
    "uuid":"1688853790xxx", //uuid 默认随机生成如果初始化传了id则用初始化的id作为uuid
    "send_userid":78813025xxx8,
    "cdnkey":"*1*u3W2k/2+7TigF3ieM2JYWY12fJ0m+w80zO84cIONH6YTO1P6L92K6u8yQCjGfAlJ8SxDBE/+9OD0s6neeXIErix3BGU8/AzaPPEEOPUkSQwO77/Yo1Gff2kkbclynhl7pnjN9Ureh7j5PptTxeuOjRmr7s=",
    "file_name":"d8f0a590e1x7e5d0265b5ae.MP4",
    "md5":"976A026AB9Bx75514A39E5C0",
    "video_duration":178,
    "fileSize":63647298,
    "imgurl":"https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png",//视频封面图
    "isRoom":false
}
```

##### 返回示例 

``` 
{
    "data": {
        "receiver": 7881302333338,
        "cdn_Key": "*1*u3W2k/2+7TigF3icccYV28gBh3eTguzdQNDSvTsX6weXFApoG+5jHWihl4Tq7kaiosiwiKRcWqOipf/77GX1EC1yqSKYkW1jQp2mVi3u8NfqEAyHheXZYwmvCoV85QMvmxIKd0XXTxIjAdQhZJ4lVVo5euOjRmr7s=",
        "sender_name": "",
        "file_name": "d8f0a59cccb0a7e5d0265b5ae.MP4",
        "is_room": 0,
        "server_id": 12781142,
        "size": 178,
		"app_info":"from_msgid_697590829808721944",
        "sender": 16888xxx,
        "aes_key": "",
        "sendtime": 1676876091,
        "msg_id": 1112326,
        "msgtype": 22,
        "md5": ""
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 发送大文件消息



[TOC]
    
##### 简要描述

- 发送大文件消息 需要走大文件上传别走cdn上传！！！！！！！！！！！！！！！！！！！！

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SendCDNBigFileMsg
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|send_userid |是  |long |要发送的人或群id|
|isRoom |是  |bool |是否是群消息|
##### 请求示例 

``` 
{
    "uuid":"1688d599424",
    "send_userid":78d5913738,
    "cdnkey":"*1*F34hSNMREYF+BNgTfNtYo7Lg5/va+EoTPkyESrZSpwtNs1IFEpFfXWbWXF+kvx+rrm7SUtsNBbdEUxBcMc5zyqXZ3Z1pT7CShVKq1LmjzbhtG3ZAK/NFdftW9WvvWpUoq2ynfnC765zusYDzYEiH5bbc4g=",
    "md5":"9c2e8389567e170012d3511166000151",
    "file_name":"2f0ecd499b93bd9cb0da022.zip",
    "fileSize":314065,
    "isRoom":false
}
```

##### 返回示例 

``` 
{
    "data": {
        "receiver": 788130x913738,
        "cdn_key": "*1*F34hSNMREYF+BNgTfNtYo7Lg5/va+EoTPkyESrZSpwtNs1IFEpFfXWbWXF+kvx+rrm7SUtsNBbdEUxBcMc5zyqXZ3Z1pT7CShVKq1LmjzbhtG3ZAK/NFx9WvvWpUoq2ynfnC765zusYDzYEiH5bbc4g=",
        "file_name": "2f0ec2d93x99b93bd9cb0da022.zip",
        "sender_name": "",
        "is_room": 0,
        "server_id": 12781146,
        "size": 314065,
        "sender": 16888x99424,
        "aes_key": "",
        "sendtime": 1676878382,
        "msg_id": 1112335,
        "msgtype": 20,
        "md5": "9c2e8389567e170012d3511166000151"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 发送连接卡片



[TOC]
    
##### 简要描述

- 发送连接卡片

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SendLinkMsg
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|send_userid |是  |long |要发送的人或群id|
|isRoom |是  |bool |是否是群消息|
##### 请求示例 

``` 
{
    "uuid":"543ed7f3-6ec1-4b1a-9e47-db8339a140f7",
    "send_userid":7881302555913738,
    "url":"https://www.baidu.com/",
    "title":"百度",
    "content":"百度内容",
    "imgurl":"https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png",
    "isRoom":false
}
```

##### 返回示例 

``` 
{
    "data": {
        "des": "百度内容",
        "receiver": 7881302555913738,
        "headImg": "https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png",
        "sender": 1688853790599424,
        "sender_name": "",
		"app_info":"from_msgid_697590829808721944",
        "is_room": 0,
        "sendtime": 1652169666,
        "msg_id": 1066243,
        "server_id": 12381209,
        "title": "百度",
        "msgtype": 13,
        "url": "https://www.baidu.com/"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 发送小程序



[TOC]
    
##### 简要描述

- 发送小程序

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SendAppMsg
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|send_userid |是  |long |要发送的人或群id|
|isRoom |是  |bool |是否是群消息|
##### 请求示例 

``` 
{
    "uuid":"543ed7f3-6ec1-4b1a-9e47-db8339a140f7",
    "send_userid":7881302555913738,
    "desc":"内容",
	"appName":"小程序名称头像右边的",
    "title":"小程序标题头像下边的",
    "weappIconUrl":"https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png",//小程序头像
    "pagepath":"pages/train/index/index.html",//小程序跳转地址
    "username":"gh_c4a2a98a7366@app", //账号原始id
    "appid":"wx45dff5234240ad90",//小程序appid
    "cdnkey":"30680201020461305f750bed0b3c7d089fa7d55720d6cf0201010201000400", //小程序封面图 cdn上传获取
    "md5":"d9c8750bed0b3c7d089fa7d55720d6cf",//封面图md5
    "aeskey":"34323235376238666264636630663166",//封面图 返回的aeskey
    "fileSize":15444,//封面图大小
    "isRoom":false
}
```

##### 返回示例 

``` 
{
    "uuid":"3240fde0-45e2-48c0-90e8-cb098d0ebe43",
    "send_userid":7881302555913738,
    "desc":"测试内容",
    "title":"小程序标题头像下边的",
	"appName":"小程序名称头像右边的",
    "weappIconUrl":"https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png",
    "pagepath":"pages/train/index/index.html",
    "username":"gh_c4a2a98a7366@app",
	"app_info":"from_msgid_697590829808721944",
    "appid":"wx45dff5234240ad90",
    "cdnkey":"30680201020461305f0201000204ea44290002030f55cb0204649f216f020462713262042444313436453338312d453244332d346438322d413443352d35453035453930384342463702010002023c600410d9c8750bed0b3c7d089fa7d55720d6cf0201010201000400",
    "md5":"d9c8750bed0b3c7d089fa7d55720d6cf",
    "aeskey":"34323235376238666264636630663166",
    "fileSize":15444,
    "isRoom":false
}
```








---

## 发送视频号消息



[TOC]
    
##### 简要描述

- 发送视频号消息

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SendVideoNumber
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|send_userid |是  |long |要发送的人或群id|
|isRoom |是  |bool |是否是群消息|
##### 请求示例 

``` 
{
    "uuid":"6d92d9e5-88fd-4d9f-8007-3e81f57a35a2",
    "send_userid":7881302555913738,
    "isRoom":false,
    "coverUrl":"http://wxapp.tc.qq.com/251/20304/stodownload?encfilekey=rjD5jyTuFrIpZ2ibE8T7YmwgiahniaXswqzREChAjGtdKh8GdzAL9lfgmmD9WIYiaF5OpAwBEjfBR8BecoibHmf3A2lfRJbj1zkUzCyPB2bAD5whFARU0fo1icUw&adaptivelytrans=0&bizid=1023&dotrans=0&hy=SH&idx=1&m=&scene=0&token=AxricY7RBHdVxH2gvQP2wl93KARNB9ELWFOYmcKON2EwsCqIofeHvdnibvw2icUJ30ODGQicZ03U3z8&finder_expire_time=1652448294&finder_eid=export%2FUzFfAgtgekIEAQAAAAAAOf827OoyYAAAAAstQy6ubaLX4KHWvLEZgBPElIMQQFweEf6DzNPgMIDXIE6IPCSmgIHh35qH2EMG",
    "thumbUrl":"http://wxapp.tc.qq.com/251/20304/stodownload?encfilekey=rjD5jyTuFrIpZ2ibE8T7YmwgiahniaXswqzREChAjGtdKh8GdzAL9lfgmmD9WIYiaF5OpAwBEjfBR8BecoibHmf3A2lfRJbj1zkUzCyPB2bAD5whFARU0fo1icUw&adaptivelytrans=0&bizid=1023&dotrans=0&hy=SH&idx=1&m=&scene=0&token=AxricY7RBHdVxH2gvQP2wl93KARNB9ELWFOYmcKON2EwsCqIofeHvdnibvw2icUJ30ODGQicZ03U3z8&finder_expire_time=1652448294&finder_eid=export%2FUzFfAgtgekIEAQAAAAAAOf827OoyYAAAAAstQy6ubaLX4KHWvLEZgBPElIMQQFweEf6DzNPgMIDXIE6IPCSmgIHh35qH2EMG",
    "avatar":"http://wx.qlogo.cn/finderhead/Y3WgNLFjO0cpO1hficKwicUcWiadyBmFf3Fk89eSBRI1xlQW1VebNKbeg/0",
    "nickname":"天下泉城",
    "desc":"#天下泉城 济南加油！",
    "url":"https://channels.weixin.qq.com/web/pages/feed?eid=export%2FUzFfAgtgekIEAQAAAAAAOf827OoyYAAAAAstQy6ubaLX4KHWvLEZgBPElIMQQFweEf6DzNPgMIDXIE6IPCSmgIHh35qH2EMG",
    "extras":"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",
	"objectId":"14479811159081949639",
	"objectNonceId":"1113158575529036062_0_0_0_0_0"
}
```

##### 返回示例 

``` 
{
    "data": {
        "thumb_url": "http://wxapp.tc.qq.com/251/20304/stodownload?encfilekey=rjD5jyTuFrIpZ2ibE8T7YmwgiahniaXswqzREChAjGtdKh8GdzAL9lfgmmD9WIYiaF5OpAwBEjfBR8BecoibHmf3A2lfRJbj1zkUzCyPB2bAD5whFARU0fo1icUw&adaptivelytrans=0&bizid=1023&dotrans=0&hy=SH&idx=1&m=&scene=0&token=AxricY7RBHdVxH2gvQP2wl93KARNB9ELWFOYmcKON2EwsCqIofeHvdnibvw2icUJ30ODGQicZ03U3z8&finder_expire_time=1652448294&finder_eid=export%2FUzFfAgtgekIEAQAAAAAAOf827OoyYAAAAAstQy6ubaLX4KHWvLEZgBPElIMQQFweEf6DzNPgMIDXIE6IPCSmgIHh35qH2EMG",
        "cover_url": "http://wxapp.tc.qq.com/251/20304/stodownload?encfilekey=rjD5jyTuFrIpZ2ibE8T7YmwgiahniaXswqzREChAjGtdKh8GdzAL9lfgmmD9WIYiaF5OpAwBEjfBR8BecoibHmf3A2lfRJbj1zkUzCyPB2bAD5whFARU0fo1icUw&adaptivelytrans=0&bizid=1023&dotrans=0&hy=SH&idx=1&m=&scene=0&token=AxricY7RBHdVxH2gvQP2wl93KARNB9ELWFOYmcKON2EwsCqIofeHvdnibvw2icUJ30ODGQicZ03U3z8&finder_expire_time=1652448294&finder_eid=export%2FUzFfAgtgekIEAQAAAAAAOf827OoyYAAAAAstQy6ubaLX4KHWvLEZgBPElIMQQFweEf6DzNPgMIDXIE6IPCSmgIHh35qH2EMG",
        "receiver": 7881302555913738,
        "extras": "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",
        "sender_name": "",
		"app_info":"from_msgid_697590829808721944",
        "is_room": 0,
        "avatar": "http://wx.qlogo.cn/finderhead/Y3WgNLFjO0cpO1hficKwicUcWiadyBmFf3Fk89eSBRI1xlQW1VebNKbeg/0",
        "server_id": 12381210,
        "url": "https://channels.weixin.qq.com/web/pages/feed?eid=export%2FUzFfAgtgekIEAQAAAAAAOf827OoyYAAAAAstQy6ubaLX4KHWvLEZgBPElIMQQFweEf6DzNPgMIDXIE6IPCSmgIHh35qH2EMG",
        "sender": 1688853790599424,
        "nickname": "天下泉城",
        "sendtime": 1652169903,
        "msg_id": 1066245,
        "msgtype": 141,
        "desc": "#天下泉城 济南加油！"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 发送视频号直播消息



[TOC]
    
##### 简要描述

- 发送视频号直播

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SendVideoNumberZhiBo
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|send_userid |是  |long |要发送的人或群id|
|isRoom |是  |bool |是否是群消息|
##### 请求示例 

``` 
{
        "uuid": "57224e08ace6632e6eff73b77320ceb9",
        "send_userid": 7881302555913738,
        "isRoom": false,
        "coverUrl": "https://finder.video.qq.com/261/20304/stodownload?encfilekey=oibeqyX228riaCwo9STVsGLIia39tYEhYEEzrbjBy8M5IziaWiabDVOeztVKTTLE6QQSL0IjE17CicJb5gibat8LWkgomsEdxSyZpOEjx18bqyPeMhmE5yn7AGUx1g3jxBXUZXMoqqf1yiblic2s&token=cztXnd9GyrHd4iajaaPHCJW4ZtfSic8HlHVaWZ1JLMFSbRsbPFnQPdNlhk11ASpWdg0Oj2CL5t1lR7Vd2yjl4Ut3lAjx5oORV6ymyfMdcpqeePq29EdWeKBSDO5MlLRZb1z48EmHEeJUJp3TiaHUq8jkAFYqvvfFuvvmia8x8PLqFR70Hjg0q21sSytBmFoT1HyW&bizid=1023&hy=SZ&idx=1&m=a5d642b207fa007e27cc99051888c93f&finder_expire_time=1767928339&wxampicformat=503&picformat=200&finder_eid=export%2FUzFfAgtgekIEAQAAAAAAKE8Gm6qxKAAAAAstQy6ubaLX4KHWvLEZgBPEpKEQGE5SObGOzNPgMIKM6o_uZoIKXQZfrl1JnToi",
        "thumbUrl": "https://finder.video.qq.com/261/20304/stodownload?encfilekey=oibeqyX228riaCwo9STVsGLIia39tYEhYEEzrbjBy8M5IziaWiabDVOeztVKTTLE6QQSL0IjE17CicJb5gibat8LWkgomsEdxSyZpOEjx18bqyPeMhmE5yn7AGUx1g3jxBXUZXMoqqf1yiblic2s&token=cztXnd9GyrHd4iajaaPHCJW4ZtfSic8HlHVaWZ1JLMFSbRsbPFnQPdNlhk11ASpWdg0Oj2CL5t1lR7Vd2yjl4Ut3lAjx5oORV6ymyfMdcpqeePq29EdWeKBSDO5MlLRZb1z48EmHEeJUJp3TiaHUq8jkAFYqvvfFuvvmia8x8PLqFR70Hjg0q21sSytBmFoT1HyW&bizid=1023&hy=SZ&idx=1&m=a5d642b207fa007e27cc99051888c93f&finder_expire_time=1767928339&wxampicformat=503&picformat=200&finder_eid=export%2FUzFfAgtgekIEAQAAAAAAKE8Gm6qxKAAAAAstQy6ubaLX4KHWvLEZgBPEpKEQGE5SObGOzNPgMIKM6o_uZoIKXQZfrl1JnToi",
        "avatar": "http://wx.qlogo.cn/finderhead/BKDgOfSUuOXibnVOZHgcSxVATOX91VYH2QqYzYuPRTnCKln6kLlre0HoUHyianicwYAsichHibtYD4F8/0",
        "nickname": "三角洲行动",
        "desc": "还不来我直播间带你百万撤离！",
        "url": "https://channels.weixin.qq.com/web/pages/live?eid=export%2FUzFfAgtgekIEAQAAAAAAKE8Gm6qxKAAAAAstQy6ubaLX4KHWvLEZgBPEpKEQGE5SObGOzNPgMIKM6o_uZoIKXQZfrl1JnToi",
        "extras": "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",
        "objectId":"14819532136085195120",
        "objectNonceId":"15766145125716611028_0_0_0_0_0_85eb5562-e13f-11f0-b082-e9c6925e17aa"
    }
```

##### 返回示例 

``` 
{
    "data": {
        "thumb_url": "https://finder.video.qq.com/261/20304/stodownload?encfilekey=oibeqyX228riaCwo9STVsGLIia39tYEhYEEzrbjBy8M5IziaWiabDVOeztVKTTLE6QQSL0IjE17CicJb5gibat8LWkgomsEdxSyZpOEjx18bqyPeMhmE5yn7AGUx1g3jxBXUZXMoqqf1yiblic2s&token=cztXnd9GyrHd4iajaaPHCJW4ZtfSic8HlHVaWZ1JLMFSbRsbPFnQPdNlhk11ASpWdg0Oj2CL5t1lR7Vd2yjl4Ut3lAjx5oORV6ymyfMdcpqeePq29EdWeKBSDO5MlLRZb1z48EmHEeJUJp3TiaHUq8jkAFYqvvfFuvvmia8x8PLqFR70Hjg0q21sSytBmFoT1HyW&bizid=1023&hy=SZ&idx=1&m=a5d642b207fa007e27cc99051888c93f&finder_expire_time=1767928339&wxampicformat=503&picformat=200&finder_eid=export%2FUzFfAgtgekIEAQAAAAAAKE8Gm6qxKAAAAAstQy6ubaLX4KHWvLEZgBPEpKEQGE5SObGOzNPgMIKM6o_uZoIKXQZfrl1JnToi",
        "cover_url": "https://finder.video.qq.com/261/20304/stodownload?encfilekey=oibeqyX228riaCwo9STVsGLIia39tYEhYEEzrbjBy8M5IziaWiabDVOeztVKTTLE6QQSL0IjE17CicJb5gibat8LWkgomsEdxSyZpOEjx18bqyPeMhmE5yn7AGUx1g3jxBXUZXMoqqf1yiblic2s&token=cztXnd9GyrHd4iajaaPHCJW4ZtfSic8HlHVaWZ1JLMFSbRsbPFnQPdNlhk11ASpWdg0Oj2CL5t1lR7Vd2yjl4Ut3lAjx5oORV6ymyfMdcpqeePq29EdWeKBSDO5MlLRZb1z48EmHEeJUJp3TiaHUq8jkAFYqvvfFuvvmia8x8PLqFR70Hjg0q21sSytBmFoT1HyW&bizid=1023&hy=SZ&idx=1&m=a5d642b207fa007e27cc99051888c93f&finder_expire_time=1767928339&wxampicformat=503&picformat=200&finder_eid=export%2FUzFfAgtgekIEAQAAAAAAKE8Gm6qxKAAAAAstQy6ubaLX4KHWvLEZgBPEpKEQGE5SObGOzNPgMIKM6o_uZoIKXQZfrl1JnToi",
        "objectNonceId": "15766145125716611028_0_0_0_0_0_85eb5562-e13f-11f0-b082-e9c6925e17aa",
        "receiver": 7881302555913738,
        "extras": "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",
        "sender_name": "6666",
        "is_room": 0,
        "avatar": "http://wx.qlogo.cn/finderhead/BKDgOfSUuOXibnVOZHgcSxVATOX91VYH2QqYzYuPRTnCKln6kLlre0HoUHyianicwYAsichHibtYD4F8/0",
        "server_id": 13274471,
        "url": "https://channels.weixin.qq.com/web/pages/live?eid=export%2FUzFfAgtgekIEAQAAAAAAKE8Gm6qxKAAAAAstQy6ubaLX4KHWvLEZgBPEpKEQGE5SObGOzNPgMIKM6o_uZoIKXQZfrl1JnToi",
        "coverUrl": "https://finder.video.qq.com/261/20304/stodownload?encfilekey=oibeqyX228riaCwo9STVsGLIia39tYEhYEEzrbjBy8M5IziaWiabDVOeztVKTTLE6QQSL0IjE17CicJb5gibat8LWkgomsEdxSyZpOEjx18bqyPeMhmE5yn7AGUx1g3jxBXUZXMoqqf1yiblic2s&token=cztXnd9GyrHd4iajaaPHCJW4ZtfSic8HlHVaWZ1JLMFSbRsbPFnQPdNlhk11ASpWdg0Oj2CL5t1lR7Vd2yjl4Ut3lAjx5oORV6ymyfMdcpqeePq29EdWeKBSDO5MlLRZb1z48EmHEeJUJp3TiaHUq8jkAFYqvvfFuvvmia8x8PLqFR70Hjg0q21sSytBmFoT1HyW&bizid=1023&hy=SZ&idx=1&m=a5d642b207fa007e27cc99051888c93f&finder_expire_time=1767928339&wxampicformat=503&picformat=200&finder_eid=export%2FUzFfAgtgekIEAQAAAAAAKE8Gm6qxKAAAAAstQy6ubaLX4KHWvLEZgBPEpKEQGE5SObGOzNPgMIKM6o_uZoIKXQZfrl1JnToi",
        "sender": 1688857368772759,
        "nickname": "三角洲行动",
        "app_info": "CIOACBCbup2ctTMYl4Gr/JuAgAMgKQ==",
        "room_conversation_id": "0",
        "sendtime": 1766632938,
        "msg_id": 1008780,
        "thumbUrl": "https://finder.video.qq.com/261/20304/stodownload?encfilekey=oibeqyX228riaCwo9STVsGLIia39tYEhYEEzrbjBy8M5IziaWiabDVOeztVKTTLE6QQSL0IjE17CicJb5gibat8LWkgomsEdxSyZpOEjx18bqyPeMhmE5yn7AGUx1g3jxBXUZXMoqqf1yiblic2s&token=cztXnd9GyrHd4iajaaPHCJW4ZtfSic8HlHVaWZ1JLMFSbRsbPFnQPdNlhk11ASpWdg0Oj2CL5t1lR7Vd2yjl4Ut3lAjx5oORV6ymyfMdcpqeePq29EdWeKBSDO5MlLRZb1z48EmHEeJUJp3TiaHUq8jkAFYqvvfFuvvmia8x8PLqFR70Hjg0q21sSytBmFoT1HyW&bizid=1023&hy=SZ&idx=1&m=a5d642b207fa007e27cc99051888c93f&finder_expire_time=1767928339&wxampicformat=503&picformat=200&finder_eid=export%2FUzFfAgtgekIEAQAAAAAAKE8Gm6qxKAAAAAstQy6ubaLX4KHWvLEZgBPEpKEQGE5SObGOzNPgMIKM6o_uZoIKXQZfrl1JnToi",
        "msgtype": 146,
        "feed_type": 9,
        "objectId": "14819532136085195120",
        "desc": "还不来我直播间带你百万撤离！"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 发送gif表情



[TOC]
    
##### 简要描述

- 发送gif表情

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SendEmotionMessage
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数


|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|send_userid |是  |long |要发送的人或群id|
|isRoom |是  |bool |是否是群消息|
##### 请求示例 

``` 
{
    "uuid":"41cba32733a3643f7bdc4cbdd360d797",
    "send_userid":7881302555913738,
    "isRoom":false,
    "imgurl":"https://wework.qpic.cn/wwpic3az/wwwx_954b864d1be1d3afbf6fd6581c1903c1/0",
    "width":240, //宽度
    "height":240,//高度
    "emotionType":0
}
```

##### 返回示例 

``` 
{
    "data": {
        "receiver": 7881302555913738,
        "sender": 1688853790599424,
        "EmotionType": "EMOTION_STATIC",
        "sender_name": "",
        "is_room": 0,
        "sendtime": 1652169938,
        "msg_id": 1066247,
		"app_info":"from_msgid_697590829808721944",
        "server_id": 12381211,
        "msgtype": 29,
        "url": "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimglf3.nosdn0.126.net%2Fimg%2FWEFHU2lQKzFVM1lGbWFTcG84YXdudFl2dnhPN3ZSY0g4UFRzSlpzMjZ1TGs5MEF4YTRYbjN3PT0.gif&refer=http%3A%2F%2Fimglf3.nosdn0.126.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654435655&t=4ef360eed30a307a951785967a097030"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 发送名片消息



[TOC]
    
##### 简要描述

- 发送名片消息

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SendBusinessCardMsg
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|send_userid |是  |long |要发送的人或群id|
|isRoom |是  |bool |是否是群消息|
##### 请求示例 

``` 
{
    "uuid":"6d92d9e5-88fd-4d9f-8007-3e81f57a35a2",
    "send_userid":7881302555913738, //要给谁发名片
	"headImg":"http://www.baidu.com/dxxxx.jpg",
    "isRoom":false,
    "username":7881302555913738, //被发的名片人id
    "nickname":"被发送人的昵称",
    "enterpriseName":"企业名称"
}
```

##### 返回示例 

``` 
{
    "data": {
        "receiver": 7881302555913738,
        "headImg": "",
        "sender_name": "",
        "is_room": 0,
        "server_id": 12381212,
        "sender": 1688853790599424,
        "nickname": "",
        "sendtime": 1652169970,
		"app_info":"from_msgid_697590829808721944",
        "msg_id": 1066249,
        "msgtype": 41,
        "enterpriseName": "",
        "corp_id": 0,
        "username": 7881302555913738
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 发送位置消息



[TOC]
    
##### 简要描述

- 发送位置消息

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SendLocationMsg
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|send_userid |是  |long |要发送的人或群id|
|isRoom |是  |bool |是否是群消息|
##### 请求示例 

``` 
{
    "uuid":"6d92d9e5-88fd-4d9f-8007-3e81f57a35a2",
    "send_userid":7881302555913738,
    "isRoom":false,
    "longitude":115.54214,
    "latitude":34.82553,
    "address":"山东菏泽曹县",
    "detailed_address":"山东曹县牛B666我的宝贝儿"
}
```

##### 返回示例 

``` 
{
    "data": {
        "detailed_address": "山东曹县牛B666我的宝贝儿",
        "address": "山东菏泽曹县",
        "receiver": 7881302555913738,
        "sender": 1688853790599424,
        "latitude": 34.82553,
        "sender_name": "",
		"app_info":"from_msgid_697590829808721944",
        "is_room": 0,
        "sendtime": 1652169999,
        "msg_id": 1066251,
        "server_id": 12381213,
        "msgtype": 6,
        "longitude": 115.54214
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 发送群@消息




[TOC]
    
##### 简要描述

- 发送群聊@消息

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SendTextAtMsg
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|send_userid |是  |long |要发送的群id|
##### 请求示例 

``` 
{
    "uuid":"603fc22b-2e0e-4faa-a03c-8880733d1228",
    "send_userid":10696052955013024,//要发送的id
    "atids":[
        7881302555913738  //@群成员id
    ],
    "content":"ddddddd", //内容 //发送内容
	"isRoom":true //群true  
}
```

##### 返回示例 

``` 
{
    "data": {
        "receiver": 0,
        "sender": 1688853790599424,
        "at_list": [
            {
                "user_id": 7881302555913738,
                "nikeName": ""
            }
        ],
        "sender_name": "",
		"app_info":"from_msgid_697590829808721944",
        "room_conversation_id": 10696052955013024,
        "is_room": 1,
        "sendtime": 1654572835,
        "msg_id": 1068996,
        "server_id": 12480017,
        "msgtype": 2,
        "content": "ddddddd"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 格式化发送@消息



[TOC]
    
##### 简要描述

- 发送CDN图片消息

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SendTextAtMsgTwo
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|send_userid |是  |long |要发送的人或群id|
|isRoom |是  |bool |是否是群消息|
##### 请求示例 

``` 
{
    "uuid":"1688853790599424",
    "send_userid":10696052955016139,
    "contentva":[
        {
            "msgtype":0,
            "content":"测试艾特消息"
        },
		{
            "msgtype":3,
            "content":"[微笑]"
        },
        {
            "msgtype":5, //就是@人的类型
            "vid":788130xx38 //@人id
        },
        {
            "msgtype":5,//@类型
            "vid":0 //填0就是所有人
        }
    ],
    "isRoom":true
}
```

##### 返回示例 

``` 
{
    "data": {
        "receiver": 0,
        "sender": 1688853790599424,
        "at_list": [
            {
                "user_id": 7881302555913738,
                "nikeName": ""
            }
        ],
        "sender_name": "",
        "room_conversation_id": 10696052955016139,
        "is_room": 1,
        "sendtime": 1680446975,
        "msg_id": 1119421,
        "server_id": 12884902,
        "msgtype": 2,
        "content": "测试艾特消息"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 同步消息记录




[TOC]
    
##### 简要描述

- 同步消息记录，用来同步消息记录，如果电脑微信在离线期间，手机端进行了消息操作，调用这个接口可以把离线期间的消息同步过来。
使用场景，登录成功的时候使用，然后就不需要用到它了，如果对消息没那么重要则不需要用它
##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SyncAllData
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|limit |是  |int |每次返回大小|
|seq |是  |int |查询下标|
``` 
建议seq传消息回调中的最后一次的server_id,下发中的消息回调，每个消息都会带
着server_id，这个id就是这里面的seq，建议每次接收那边的消息，就更新一下server_id，然后如果离线，过了几天，再次用
协议登录，就直接传递上次的server_id就会直接同步从上次最后一次保存server_id为开始，进行同步。
```
如下图的server_id，就是这里的seq
![](https://www.showdoc.com.cn/server/api/attachment/visitFile?sign=bf1f7393427b7313088dc6822af35733)
##### 请求示例 

``` 
{
    "uuid":"ecb033af-6fcd-4ec2-880e-41f070b65eaf",
    "limit":1000, 
    "seq":12481627
}
```

##### 返回示例 

``` 
{
    "data": {
        "is_select": 1, //这个参数是判断是否还需要进行消息遍历 1代表没有遍历完还需要进行遍历 0则是遍历完成不需要进行下一步的遍历
        "seq": 12481628,//下次调用传这个seq，和其他功能接口的seq同一个意思。
        "content": "消息记录查询完成，已经分发到消息回调中，请在回调中处理离线消息"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 群发消息




[TOC]
    
##### 简要描述

- 控制台群发消息

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SendGroupsMsg
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|vids |是  |long |要发送的人或者群idid|
|isroom |是  |bool |是否是群消息|
##### 请求示例 

``` 
{
	"uuid":"xxxxxxxx",
    "vids": [
        "1688851883777952"// 群id或者好友id 不能混填
    ], 
    "isroom": false, //是否发送给群 true是 false否
    "msg_list": [
        {
            "type": 0, //文字
            "content": "群发消息[微笑]"
        }, 
        {
            "type": 14, //图片
            "aeskey": "63636533323765343938636532633264", 
            "cdnkey": "30819202010204818a3081870201000204ea44290002030f55cb0204e2a91e6f0204643d4475044c4e45574944315f65613434323930306532613931653666363433xx981d4320d0c4af27774c2d6de31781e90201010201000400", 
            "fileSize": 24389, 
            "md5": "981d4320d0c4af27774c2d6de31781e9"
        }, 
        {
            "type": 13, //连接
            "url": "https://www.baidu.com/?tn=02003390_10_hao_pg", 
            "title": "百度一下，你就知道", 
            "content": "全球领先的中文搜索引擎、致力于让网民更便捷地获取信息，找到所求。百度超过千亿的中文网页数据库，可以瞬间找到相关的搜索结果。", 
            "headImg": "https://dss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/topnav/newfanyi-da0cea8f7e.png"
        }, 
        {
            "type": 78, //小程序
            "title": "测试标题", 
            "desc": "测试简介", 
            "weappIconUrl": "http://mmbiz.qpic.cn/mmbiz_png/BMAjdIR5qwChFiaRnxUTiadJicGwMpqchXVdfQQ67tHTzfgd0gkibupgeOKtHtyE9m9SZopHXxcpsSicWxzLKXwk0qA/640?wx_fmt=png&wxfrom=200", 
            "appid": "wx45dff5234240ad90", 
            "username": "gh_c4a2a98a7366@app", 
            "pagepath": "pages/train/index/index.html", 
            "cdnkey": "30819202010204818a30818702xx981d4320d0c4af27774c2d6de31781e90201010201000400", 
            "md5": "981d4320d0c4af27774c2d6de31781e9", 
            "aeskey": "63636533323765343938636532633264", 
            "fileSize": 24389
        }, 
        {
            "type": 15, //文件
            "fileName": "7014419642333305662.MP4", 
            "aeskey": "31343031353436666630396132346638", 
            "cdnkey": "30819302010204818b30818802010xx3b9399f4489989e2bb886fe35fce4df40201050201000400", 
            "fileSize": 753510, 
            "md5": "3b9399f4489989e2bb886fe35fce4df4"
        }, 
        {
            "type": 23, //视频
            "aeskey": "39616335386438356162623034623038", 
			"md5": "xxxxxxxxxxx", 
            "VideoDuration": 5, 
			"video_img_size"6000,// 视频封面图大小
            "cdnkey": "30690201020462306002010xx393746383402010002030b7f7004103b9399f4489989e2bb886fe35fce4df40201040201000400", 
            "fileSize": 753510
        }
    ]
}
```

##### 返回示例 

``` 
{
    "data": {
        "msg_id": 1066230
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 语音转文字



[TOC]
    
##### 简要描述

- 语音转文字

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SpeechToTextEntity
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|msgid |是  |int |要转文字的语音消息id|
##### 请求示例 

``` 
{
    "uuid":"a4ea6a39-4b3a-4098-a250-2a07bef57355",
    "msgid":1063645
}
```

##### 返回示例 

``` 
{
    "data": {
        "text": "牛牛牛牛。"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 已读消息



[TOC]
    
##### 简要描述

- 已读消息

##### 请求URL
- ` http://127.0.0.1:8083/wxwork/MarkAsRead
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|send_userid |是  |long |要发送的人id|
|isRoom |是  |bool |是否是群消息|
##### 请求示例 

``` 
{
    "uuid":"1753cdff-0501-42fe-bb5a-2a4b9629f7fb",
    "send_userid":7881302555913738, //好友或者群id
    "isRoom":false
}
```

##### 返回示例 

``` 
{
    "data": {
        "receiver": 0,
        "sender": 1688857826547606,
        "sender_name": "",
        "app_info": "CIOACBDHsODn4jEYlq/P1p2AgAMgAw==",
        "room_conversation_id": "10973555009565180",
        "is_room": 1,
        "sendtime": 1710151440,
        "msg_id": 1013548,
        "server_id": 6306123,
        "msgtype": 2001
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 发送引用消息



[TOC]
    
##### 简要描述

- 发送引用消息

##### 请求URL
- ` http://127.0.0.1:8083/wxwork/sendQuoteMsg
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

| 参数名       | 类型   | 必填 | 默认值 | 说明                     |
|--------------|--------|------|--------|--------------------------|
| uuid       | String | 是   | 无     | uuid用来操作账号           |
| kf_id     | Long | 否   | 无     | 客服id用来回复客服消息        |
| send_userid     | Long | 是   | 无     | 发送人或者群id        |
| isRoom     | Bool | 是   | 无     | 如果是给群发true 否则false        |
| content     | Object[] | 是   | 无     | 回复引用消息内容       |
| ├─ msgtype      | int | 是   | 无     | 消息类型0文本                 |
| ├─ content       | String| 是   | 无     | 引用消息内容                 |
| quoteMsg     | Object | 是   | 无     | 引用的那个消息对象       |
| ├─ appinfo   | String | 是   | 无     | 引用的那个消息appinfo                 |
| ├─ content_type   | int | 是   | 无     | 引用的那个消息,消息类型                     |
| ├─ sender    | Long | 是   | 无     | 引用的那个消息发送人id                 |
| ├─ quote_roomid    | Long | 否   | 无     | 如果是在群里回复引用消息这里填群id，如果是单聊则忽略不传                 |
| ├─ content    | Object | 是   | 无     | 引用的那条消息类型详细内容     |

 quoteMsg->content引用消息内容详细讲解
****msgtype字段详解
	
``` 
	TEXT=0 //文本消息
	IMAGE=1 //图片消息
	FILE=2 //文件消息
	VIDEO=3 //视频消息
	VOICE=4 //语音消息
	LINK=5 //连接消息
	CARD=6 //名片消息
	CARD=7 //小程序消息
```
引用的内容 一定要带着，不带着显示的不全
``` 
	[
		{ 
            "msgtype":0, //0 文本  1图片  2文件 
            "content":"4546545"
        },
		{
            "msgtype":1, //企微联系人发的图片消息
            "file_id":"306b020102046430620201000204060b1x38612d333231652d346432332d393338352d6166646562333666383866310203103800020300a2300410efaf887ca055806f139409b13f35b93c0201010201000400",
            "file_size":41505,
            "width":154,
            "height":154,
            "is_hd":1,
            "aes_key":"e2c3e663d321bbcd0051371f2d0ad4f0",
            "md5":"efaf887ca055806f139409b13f35b93c"
        },
		{  //个微联系人发的图片消息
            "msgtype":1,
            "file_id":"https://imunion.weixin.qq.com/cgi-bin/mmae-bin/tpdownloadmedia?param=v1_53a9369ab06bc67073755b0736a84150c98dd0e159f2093165d6e5e3bb0f3x16113bdb92cb2b84cce09a0627468599118336caab78810daa09cc6ec5c2336c5982debbf9808ae8b1676d06d9950a54d93b841e2546b4567987ee3d6d6e802f4756e080650343965ecaa9fec4778ab372c16f0287737b7dfe8a9a",
            "auth_key":"v1_53a9369ab06bc67073755b0736a84150c98dd0e159f2093165d6e5e3bb0f32a2b0a96db4cefa4484a82459ca4b6bed2acf3130c0c67f52054660d17a25bdc1ca0c91b41b1d0efa6a7066f9ef3a15def1",
            "file_size":41505,
            "width":150,
            "height":150,
            "aes_key":"aac34b4b27d8e1ff7f485786eae83bd6",
            "md5":"efaf887ca055806f139409b13f35b93c"
        },
		{ //个微联系人发的文件消息
            "msgtype":2,
            "file_id":"https://imunion.weixin.qq.com/cgi-bin/mmae-bin/tpdownloadmedia?param=v1_53a9369ab06bc67073755b0736a84150c98dd0e159x672912c67a7778ed7c7cc7a8bc1925f89718fe7509d6bf5009ee60cfc0f160ec8f3c6e14a73ba323154c5bfbd71a8c26cc64eaad99a9c349b9b4bfb5c14a79a83655f4927e913db4c8ea1ea6443d5f7233ffbb524f5cfab8582091c29ef86c57c",
            "file_name":"doc07125520240614090407.pdf", //必传 其他的都可以忽略
            "auth_key":"v1_53a9369ab06bc67073755b0736a84150c98dd0e159f2093165d6e5e3bb0f32a2b0a96db4cefa4484a82459ca4b6bed2ab340546848c899e0bb40cf6c24306de55950a3c87a792e967cacf446f5f841c8",
            "file_size":1385151,
            "aes_key":"9fad9e4315978d4b2cbb62eb39e04747",
            "md5":"01408d3f3647bfdbba8f741d09e36f3c"
        },
		{//企微微联系人发的文件消息
            "msgtype":2,
            "file_id":"306b02010204643062020193766372d663761626337316135353034020310000502031522c0041001408d3f3647bfdbba8f741d09e36f3c0201050201000400",
            "file_name":"doc07125520240614090407.pdf",//必传 其他的都可以忽略
            "file_size":1385151,
            "aes_key":"31363362373264336336646464356130",
            "md5":"01408d3f3647bfdbba8f741d09e36f3c"
        },
		{
            "msgtype":3, //外部联系人发送的视频，进行引用
            "file_id":"https://imunion.weixin.qq.com/cgi-bin/mmae-bin/tpdownloadmedia?param=v1_aa6db07f1804c84f7ff1f69f9641fa73b3385da824beda38b35f9c8ae1c7b841d2919270f0125d294cf4011fxb3bfed1b26c4941b34f10f1ceeff32e0b3579e5823ecbe7e609b0d39187bc817ddf8a8e8c831bae5f94207d6067f04c94f919f4a8198c1fc74973fecd85d3f5a9c873de", //视频fileid
            "preview_img_url":"https://imunion.weixin.qq.com/cgi-bin/mmae-bin/tpdownloadmedia?param=v1_aa6db07f1804c84f7ff1f69f9641fa73b3385da824beda38b35f9c8ae1c7b8413a4db420a8a4a9a60cb76b8d177xe8e7c99d1723d9008412bbcbaac26b0f0b6430309f8cd11475bf5b21d8c1da1aa4ec073a4a46c4687e06bd5af90ed868501be347db2521a80112a86d32fca9ca7d5b1ef54c4",//视频缩略图id
            "openim_cdn_ld_size":12747,//缩略图文件大小
            "auth_key":"v1_aa6db07f1804c84f7ff1f69f9641fa73b3385da824beda38b35f9c8ae1c7b8410fed71d07e5a79d9807ba79a82c54b357e66fa995047e43582225d28225633a7",
            "file_size":1736664,//视频大小
            "video_duration":15, //视频时长
            "aes_key":"bb5147116d57a321bde8fe15bcd81f24",
            "md5":"14f369b467e87ff77056152b55dc3fef"
        },
		{
            "msgtype":3, //内部联系人发送的视频，进行引用
            "file_id":"306b020102046430620201000204060b19e102030f55ca02042d4ba16f0204686f8d0d042464386563303632392d366333372d343563622d626563642d323434626130356237333335020310000402031a7fe0041014f369b467e87ff77056152b55dc3fef0201040201000400",
            "openim_cdn_ld_size":12747,
            "auth_key":"v1_aa6db07f1804c84f7ff1f69f9641fa73b3385da824beda38b35f9c8ae1c7b8410fed71d07e5a79d9807ba79a82c54b357e66fa995047e43582225d28225633a7",
            "file_size":1736664,
            "video_duration":15,
            "aes_key":"31643738303865333264393137613736",
            "md5":"14f369b467e87ff77056152b55dc3fef"
        },
		{//语音引用消息发送
            "msgtype":4,
            "file_id":"308183020102047c307a0201000204bf366e0a02031e90380204fdc1f46d0204686f82d5044c333230383031353337305f36333642363236423730363137303730364236423646373337343632364336425f3762646131626564366564656338613935636339666361316433383434306666020100020215400400020442350d710201000400",
            "voice_size":5425,
             "voice_time":3,
            "aes_key":"636B626B706170706B6B6F7374626C6B",
            "md5":"7bda1bed6edec8a95cc9fca1d38440ff"
        },
		{
            "msgtype":5,//连接消息
            "title":"天津一母亲临终将脑瘫儿子托付给21岁女儿，女儿含泪点头，谁料，她转身扔掉哥哥离开",
            "des":"",
            "headImg":"https://mmbiz.qpic.cn/sz_mmbiz_jpg/absA4Er6ibBRHWvJAURtLWBm5PsxF1HbqN8eRhIEbm1xv1gzTQMw44FPOTXibhM0BgdzmR1z8K6zK7znhkUhqoYA/300?wxtype=jpeg&wxfrom=0",
            "url":"http://mp.weixin.qq.com/s?__biz=MzkyOTQwNDE5MQ==&mid=2247491073&idx=1&sn=fb4e4ff33e881e6041ed9fc3e412ed48&chksm=c33e40a988b4e12f905bf34ada586b971ca1f569aa88113a6fbcddd87d6d0dc58d8e4f145402&scene=126&sessionid=1752141927#rd"
        },
		{
            "msgtype":6,//名片消息
            "username":78813020591,
            "nickname":"昵称",
            "headImg":"http://wx.qlogo.cn/mmhead/PiajxSqBRaEIoxYVIkdnwOMtXLKacdRUx1hibM275KmXrZeIia59coJibg/0",//头像
            "enterpriseName":"微信"//企业名字
        },
		{
            "msgtype":7,
            "desc":"简介",
            "title":"内容标题",
            "appName":"小程序名字",
            "pagepath":"pages/index/index.html",
            "username":"gh_e398fcb4745c@app",
            "appid":"",
            "cdnkey":"306d020102046630640201000204bf366e0a02031e90380204e7df892b02046926be1c0436333230383031353337305f313132393139393739355f63653230653265343135343061646135306564646665643535633566663733390203102800020303849004000201010201000400",
            "md5":"ce20e2e41540ada50eddfed55c5ff739",
            "aeskey":"71746670656C6A726A6D7A617A77797A",
            "fileSize":"230539",
            "weappIconUrl":"http://mmbiz.qpic.cn/mmbiz_png/TXFfqlFzBUiazSEKjaelenf67b50byezFaFf3Meh6t2evSrJbxda7Jaqiamgd6dOJibfmOe97JiaHJkS4Og81Gmwcw/640?wx_fmt=png&wxfrom=200"
        }
	]
```
	
##### 给文本消息，发送引用请求示例 

``` 
{
    "uuid":"7709ff0d22ab951c6c0770a676f0b44a",
    "kf_id":0,
    "send_userid":10817857838351957, 
    "isRoom":true,
    "content":[
        {
            "msgtype":0,
            "content":"\"十年一刻：\n4546545\"\n------\n"  //固定格式 如果是文本就是 引用的那个人的发送人名字  加发送内容 加----- 也可以写死随便一个东西，这个必须带着，但是内容可以随便或者固定，也可以按照我的方式填写。《》
        },
        {
            "msgtype":0,
            "content":"消息引用测试群聊" //这个就是你要回复的文本内容  
        }
    ],
    "quoteMsg":{
        "appinfo":"CIGABBCcw63DBhjhs6ywkICAAyAK", //引用的那个消息的appinfo
        "content_type":2, //引用的那个消息的类型
        "sender":1688854256622049, //引用的那个消息发送人id
        "quote_roomid":10817857838351957, //如果是群要填群id 如果不是群 忽略这个字段
        "content":{ //引用的内容 一定要带着，不带着显示的不全
            "msgtype":0, //0 文本  1图片  2文件 
            "content":"4546545"
        }
    }
}
```


##### 给企微用户发的图片消息，发送引用请求示例 

``` 
{
    "uuid":"7709ff0d22ab951c6c0770a676f0b44a",
    "kf_id":0,
    "send_userid":10817857838351957,
    "isRoom":true,
    "content":[
        {
            "msgtype":0,
            "content":"\"十年一刻：\n图片\"\n------\n"
        },
        {
            "msgtype":0,
            "content":"测试群引用消息，引用内容是图片"
        }
    ],
    "quoteMsg":{
        "appinfo":"CIGABBC79q3DBhjhs6ywkICAAyAT",
        "content_type":14,
        "sender":1688854256622049,
        "quote_roomid":10817857838351957,
        "content":{
            "msgtype":1,
            "file_id":"306b020102046430620201000204060b1x38612d333231652d346432332d393338352d6166646562333666383866310203103800020300a2300410efaf887ca055806f139409b13f35b93c0201010201000400",
            "file_size":41505,
            "width":154,
            "height":154,
            "is_hd":1,
            "aes_key":"e2c3e663d321bbcd0051371f2d0ad4f0",
            "md5":"efaf887ca055806f139409b13f35b93c"
        }
    }
}
```

##### 给个微用户发的图片消息，发送引用请求示例 

``` 
{
    "uuid":"7709ff0d22ab951c6c0770a676f0b44a",
    "kf_id":0,
    "send_userid":10817857838351957,
    "isRoom":true,
    "content":[
        {
            "msgtype":0,
            "content":"\"十年一刻：\n图片\"\n------\n"
        },
        {
            "msgtype":0,
            "content":"测试群引用消息，引用内容是外部联系人发的图片"
        }
    ],
    "quoteMsg":{
        "appinfo":"3826414583477651862",
        "content_type":101,
        "sender":7881302555913738,
        "quote_roomid":10817857838351957,
        "content":{
            "msgtype":1,
            "file_id":"https://imunion.weixin.qq.com/cgi-bin/mmae-bin/tpdownloadmedia?param=v1_53a9369ab06bc67073755b0736a84150c98dd0e159f2093165d6e5e3bb0f3x16113bdb92cb2b84cce09a0627468599118336caab78810daa09cc6ec5c2336c5982debbf9808ae8b1676d06d9950a54d93b841e2546b4567987ee3d6d6e802f4756e080650343965ecaa9fec4778ab372c16f0287737b7dfe8a9a",
            "auth_key":"v1_53a9369ab06bc67073755b0736a84150c98dd0e159f2093165d6e5e3bb0f32a2b0a96db4cefa4484a82459ca4b6bed2acf3130c0c67f52054660d17a25bdc1ca0c91b41b1d0efa6a7066f9ef3a15def1",
            "file_size":41505,
            "width":150,
            "height":150,
            "aes_key":"aac34b4b27d8e1ff7f485786eae83bd6",
            "md5":"efaf887ca055806f139409b13f35b93c"
        }
    }
}
```

##### 给个微用户发的文件消息，发送引用请求示例 

``` 
{
    "uuid":"7709ff0d22ab951c6c0770a676f0b44a",
    "kf_id":0,
    "send_userid":10817857838351957,
    "isRoom":true,
    "content":[
        {
            "msgtype":0,
            "content":"\"十年一刻：\n文件\"\n------\n"
        },
        {
            "msgtype":0,
            "content":"测试引用消息，引用外部联系人发的文件消息"
        }
    ],
    "quoteMsg":{
        "appinfo":"6697340850319438577",
        "content_type":102,
        "sender":7881302555913738,
        "quote_roomid":10817857838351957,
        "content":{
            "msgtype":2,
            "file_id":"https://imunion.weixin.qq.com/cgi-bin/mmae-bin/tpdownloadmedia?param=v1_53a9369ab06bc67073755b0736a84150c98dd0e159x672912c67a7778ed7c7cc7a8bc1925f89718fe7509d6bf5009ee60cfc0f160ec8f3c6e14a73ba323154c5bfbd71a8c26cc64eaad99a9c349b9b4bfb5c14a79a83655f4927e913db4c8ea1ea6443d5f7233ffbb524f5cfab8582091c29ef86c57c",
            "file_name":"doc07125520240614090407.pdf",
            "auth_key":"v1_53a9369ab06bc67073755b0736a84150c98dd0e159f2093165d6e5e3bb0f32a2b0a96db4cefa4484a82459ca4b6bed2ab340546848c899e0bb40cf6c24306de55950a3c87a792e967cacf446f5f841c8",
            "file_size":1385151,
            "aes_key":"9fad9e4315978d4b2cbb62eb39e04747",
            "md5":"01408d3f3647bfdbba8f741d09e36f3c"
        }
    }
}
```

##### 给企微用户发的文件消息，发送引用请求示例 

``` 
{
    "uuid":"7709ff0d22ab951c6c0770a676f0b44a",
    "kf_id":0,
    "send_userid":10817857838351957,
    "isRoom":true,
    "content":[
        {
            "msgtype":0,
            "content":"\"十年一刻：\n文件\"\n------\n"
        },
        {
            "msgtype":0,
            "content":"测试引用消息，引用内部联系人发的文件消息"
        }
    ],
    "quoteMsg":{
        "appinfo":"CIGABBD/va7DBhjhs6ywkICAAyAm",
        "content_type":15,
        "sender":1688854256622049,
        "quote_roomid":10817857838351957,
        "content":{
            "msgtype":2,
            "file_id":"306b02010204643062020193766372d663761626337316135353034020310000502031522c0041001408d3f3647bfdbba8f741d09e36f3c0201050201000400",
            "file_name":"doc07125520240614090407.pdf",
            "file_size":1385151,
            "aes_key":"31363362373264336336646464356130",
            "md5":"01408d3f3647bfdbba8f741d09e36f3c"
        }
    }
}
```

##### 返回示例 

``` 

```







---

