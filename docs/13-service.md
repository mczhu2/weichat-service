# 13-service

## 获取客服列表



[TOC]
    
##### 简要描述

- 获取客服列表

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/getKFList
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
##### 请求示例 

``` 
{
    "uuid":"2b0863724106a1160212bd1ccf025295"
}
```

##### 返回示例 

``` 
{
    "data": {
        "kf_list": [
            {
                "work_week": {    //工作周数
                    "week": [
                        1,
                        2,
                        3,
                        4,
                        5,
                        6,
                        0
                    ]
                },
                "kfid": 25664978253479546, //客服id
                "corpid": 1970325631992544, //企业id
                "create_time": 1744273018, //创建时间
                "wx_reply_timeout": 172800, //超时时间 秒数
                "description": "",
                "kfcode": "kfc8cf0f339c71972c7", 
                "receive_priority": true,
                "type": 0,
                "update_time": 1744273018,
                "receive_limit": 20, //最大服务人数
                "open_satisfaction": false,
                "kf_finder_username": "",
                "name": "789797", //客服名称
                "receive_rule": 1,
                "receive_range": {  
                    "partyids": [],
                    "vids": [ //这个客服设置人员列表
                        1688855302603660,
                        1688854256622049
                    ]
                },
                "off_duty_reply_content": {
                    "content_type": 0,
                    "content": "当前客服人员不在线，你可以留言想要咨询的问题，客服上班后将第一时间回复。"
                },
                "onduty_range": { 
                    "start_time": 0,//工作每天的开始时间   0就是0点0分开始  单位秒
                    "end_time": 86400 //工作的结束时间    单位秒 86400/60 就是多少分钟/60 就是工作多少小时
                },
                "head_img_url": "https://wwcdn.weixin.qq.com/node/wework/images/avatar2.96df991a19.png", //客服头像
                "status": 0
            },
            {
                "work_week": {
                    "week": [
                        1,
                        2,
                        3,
                        4,
                        5,
                        6
                    ]
                },
                "kfid": 25686461678063782,
                "corpid": 1970325631992544,
                "create_time": 1742442662,
                "wx_reply_timeout": 172800,
                "description": "",
                "kfcode": "kfc1458167e7dd12e16",
                "receive_priority": true,
                "type": 0,
                "update_time": 1744360988,
                "receive_limit": 1,
                "open_satisfaction": false,
                "kf_finder_username": "",
                "name": "66666",
                "receive_rule": 1,
                "receive_range": {
                    "partyids": [],
                    "vids": [
                        1688854256622049,
                        1688855302603660
                    ]
                },
                "off_duty_reply_content": {
                    "content_type": 0,
                    "content": "当前客服人员不在线，你可以留言想要咨询的问题，客服上班后将第一时间回复。"
                },
                "onduty_range": {
                    "start_time": 11100,
                    "end_time": 29340
                },
                "head_img_url": "https://wwcdn.weixin.qq.com/node/wework/images/mini_customer_defualt_head_male.ffa7e6927a.png",
                "status": 0
            },
            {
                "work_week": {
                    "week": [
                        1,
                        2,
                        3,
                        4,
                        5,
                        6,
                        0
                    ]
                },
                "kfid": 25664265286998413,
                "corpid": 1970325631992544,
                "create_time": 1742363021,
                "wx_reply_timeout": 172800,
                "description": "",
                "kfcode": "kfc66919796419d49a5",
                "receive_priority": true,
                "type": 0,
                "update_time": 1742363044,
                "receive_limit": 20,
                "open_satisfaction": true,
                "kf_finder_username": "",
                "name": "是你那一刻",
                "receive_rule": 1,
                "receive_range": {
                    "partyids": [],
                    "vids": [
                        1688854256622049
                    ]
                },
                "off_duty_reply_content": {
                    "content_type": 0,
                    "content": "当前客服人员不在线，你可以留言想要咨询的问题，客服上班后将第一时间回复。"
                },
                "onduty_range": {
                    "start_time": 0,
                    "end_time": 86400
                },
                "head_img_url": "https://wwcdn.weixin.qq.com/node/wework/images/avatar4.c4b5b964d6.png",
                "status": 0
            },
            {
                "work_week": {
                    "week": [
                        1,
                        2,
                        3,
                        4,
                        5,
                        6,
                        0
                    ]
                },
                "kfid": 25651655265011840,
                "corpid": 1970325631992544,
                "create_time": 1744357505,
                "wx_reply_timeout": 172800,
                "description": "",
                "kfcode": "kfccd97d874ce19850a",
                "receive_priority": true,
                "type": 0,
                "update_time": 1744358241,
                "receive_limit": 1,
                "open_satisfaction": false,
                "kf_finder_username": "",
                "name": "234234",
                "receive_rule": 1,
                "receive_range": {
                    "partyids": [],
                    "vids": [
                        1688855302603660
                    ]
                },
                "off_duty_reply_content": {
                    "content_type": 0,
                    "content": "当前客服人员不在线，你可以留言想要咨询的问题，客服上班后将第一时间回复。"
                },
                "onduty_range": {
                    "start_time": 0,
                    "end_time": 86400
                },
                "head_img_url": "https://wwcdn.weixin.qq.com/node/wework/images/avatar2.96df991a19.png",
                "status": 0
            }
        ]
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 获取客服详情



[TOC]
    
##### 简要描述

- 获取客服详情

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/getKFInfo
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
##### 请求示例 

``` 
{
    "uuid":"9b6cc4ed68c0c292ab21d6adf71835b3",
    "kf_id":25664265286998413 //客服id
}
```

##### 返回示例 

``` 
{
    "data": {
        "profile": {
            "work_week": [
                1,
                2,
                3,
                4,
                5,
                6,
                0
            ],
            "kfid": 25664265286998413,
            "corpid": 1970325631992544,
            "create_time": 1742363021,
            "wx_reply_timeout": 172800,
            "description": "",
            "kfcode": "kfc66919796419d49a5",
            "receive_priority": true,
            "type": 0,
            "update_time": 1747648302,
            "receive_limit": 1,
            "open_satisfaction": true,
            "kf_finder_username": "",
            "name": "是你那一刻",
            "receive_rule": 1,
            "receive_range": {
                "partyids": [
                    1688854861677949
                ],
                "vids": [
                    1688854256622049
                ]
            },
            "off_duty_reply_content": {
                "content_type": 0,
                "content": "当前客服人员不在线，你可以留言想要咨询的问题，客服上班后将第一时间回复。"
            },
            "onduty_range": {
                "start_time": 0,
                "end_time": 86400
            },
            "head_img_url": "https://wwcdn.weixin.qq.com/node/wework/images/avatar4.c4b5b964d6.png",
            "status": 0
        },
        "name": "是你那一刻",
        "head_img_url": "https://wwcdn.weixin.qq.com/node/wework/images/avatar4.c4b5b964d6.png",
        "status": 0
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 获取客服当前状态



[TOC]
    
##### 简要描述

- 获取客服当前状态

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/getKFState
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
##### 请求示例 

``` 
{
    "uuid":"9b6cc4ed68c0c292ab21d6adf71835b3",
    "kf_id":25664265286998413 //客服id
}
```

##### 返回示例 

``` 
{
    "data": {
        "state": 1  //正常接待1  暂时挂起2 停止接待0
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 设置客服接待状态



[TOC]
    
##### 简要描述

- 设置客服接待状态

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/setKFState
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
##### 请求示例 

``` 
{
    "uuid":"9b6cc4ed68c0c292ab21d6adf71835b3",
    "kf_id":25664265286998413, //客服id
    "state":1//正常接待1  暂时挂起2 停止接待0
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

## 获取咨询排队列表



[TOC]
    
##### 简要描述

- 获取资讯排队列表

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/getWxKfWaitQueueItems
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
##### 请求示例 

``` 
{
    "uuid":"9b6cc4ed68c0c292ab21d6adf71835b3",
    "kf_id":25664265286998413, //客服id
    "startId":"" //首次空字符串或者不传  后续传返回的 类似分页
}
```

##### 返回示例 

``` 
{
    "data": {
        "total_num": 1, //排队等待总数
        "is_end": true, //是否查询完毕
        "lastid": "7506094773656687204", //startid 用来后续查询
        "items": [
            {
                "xid": 7881302814222765, //咨询的客户id
                "create_time": 1747648877//时间
            }
        ]
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 接入咨询排队列表中的客户



[TOC]
    
##### 简要描述

- 接入咨询排队列表中的客户

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/connectWithCustomers
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
##### 请求示例 

``` 
{
    "uuid":"029141ec9eb06b357e11ccf0566ca274",
    "kf_id":25664265286998413, //哪个客服id要接入
    "xids":[  //接入的客户id
        7881299726922560
    ]
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

## 获取转接的客服列表



[TOC]
    
##### 简要描述

- 获取转接的客服列表

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/getWxKfSingleCanTransferList
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
##### 请求示例 

``` 
{
    "uuid":"9b6cc4ed68c0c292ab21d6adf71835b3",
    "kf_id":25664265286998413
}
```

##### 返回示例 

``` 
{
    "data": [  //这个客服 所对接的同事id列表
        1688854256622049,
        1688855302603660
    ],
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 结束会话



[TOC]
    
##### 简要描述

- 结束会话

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/kfEndChat
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
##### 请求示例 

``` 
{
    "uuid":"9b6cc4ed68c0c292ab21d6adf71835b3",
    "kf_id":25664265286998413, //客服id
    "xid":7881302555913738 //当前聊天联系人的 id
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

## 会话转接给其他同事



[TOC]
    
##### 简要描述

- 会话转接给其他同事

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/kfSwitchChat
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
##### 请求示例 

``` 
{
    "uuid":"9b6cc4ed68c0c292ab21d6adf71835b3",
    "kf_id":25664265286998413, //客服id
    "transfer_vid":1688855302603660,//同事账号id
    "xid":7881300143960291, //当前聊天联系人的 id
    "text":"转接给你了，我不聊了"//转接说明
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

