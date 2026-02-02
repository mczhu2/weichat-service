# 12-chattag

## 增删改聊天标签



[TOC]
    
##### 简要描述

- 增删改聊天标签

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/ModSessionTag
  
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
    "uuid":"2b0863724106a1160212bd1ccf025295",
    "cmd":1, // 1新增 2修改 3删除
    "tagName":"测试标签啊啊啊啊", //标签名字
    "tagid":1111111111111,//标签id 修改和删除需要用 添加不需要
    "globalver": 24// 这个必填 从获取聊天标签列表中拿到，给的啥填啥
}
```

##### 返回示例 

``` 
{
    "data": {
        "global_ver": 0,
        "synckey": 0,
        "taginfo": [
            {
                "flag": 0,
                "prev_tagid": 0,
                "after_tagid": 0,
                "tagid": 10133099634635291,
                "name": "测试标签啊啊啊啊",
                "cmd": 0,
                "order": 1999495
            }
        ]
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 获取聊天标签列表



[TOC]
    
##### 简要描述

- 获取聊天标签列表

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/GetSessionTagList
  
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
    "uuid":"2b0863724106a1160212bd1ccf025295",
    "synckey":0
}
```

##### 返回示例 

``` 
{
    "data": {
        "global_ver": 24,
        "synckey": 8085912,
        "taginfo": [
            {
                "flag": 0,
                "prev_tagid": 0,
                "after_tagid": 0,
                "tagid": 10133099634634240,
                "name": "4444",
                "cmd": 0,
                "order": 127967728
            },
            {
                "flag": 0,
                "prev_tagid": 0,
                "after_tagid": 0,
                "tagid": 10133099634634245,
                "name": "55555",
                "cmd": 0,
                "order": 63983864
            },
            {
                "flag": 0,
                "prev_tagid": 0,
                "after_tagid": 0,
                "tagid": 10133099634634257,
                "name": "66666",
                "cmd": 0,
                "order": 31991932
            },
            {
                "flag": 0,
                "prev_tagid": 0,
                "after_tagid": 0,
                "tagid": 10133099634634262,
                "name": "77777",
                "cmd": 0,
                "order": 15995966
            },
            {
                "flag": 0,
                "prev_tagid": 0,
                "after_tagid": 0,
                "tagid": 10133099634634265,
                "name": "88888",
                "cmd": 0,
                "order": 7997983
            },
            {
                "flag": 0,
                "prev_tagid": 0,
                "after_tagid": 0,
                "tagid": 10133099634634271,
                "name": "0000",
                "cmd": 0,
                "order": 3998991
            },
            {
                "flag": 0,
                "prev_tagid": 0,
                "after_tagid": 0,
                "tagid": 10133099634635291,
                "name": "测试标签啊啊啊啊",
                "cmd": 0,
                "order": 1999495
            },
            {
                "flag": 0,
                "prev_tagid": 0,
                "after_tagid": 0,
                "tagid": 10133100967624013,
                "name": "555555555",
                "cmd": 0,
                "order": 1023741824
            },
            {
                "flag": 0,
                "prev_tagid": 0,
                "after_tagid": 0,
                "tagid": 10133102590631983,
                "name": "11111111111",
                "cmd": 0,
                "order": 511870912
            },
            {
                "flag": 0,
                "prev_tagid": 0,
                "after_tagid": 0,
                "tagid": 10133102590632040,
                "name": "444444444",
                "cmd": 0,
                "order": 255935456
            }
        ]
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 获取标签下的联系人



[TOC]
    
##### 简要描述

- 获取标签下的联系人

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/GetSessionInTagReq
  
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
    "uuid":"2b0863724106a1160212bd1ccf025295",
    "synckey":0 //第一次0  第二次传第一次返回的
}
```

##### 返回示例 

``` 
{
    "data": {
        "global_ver": 15, //用来给联系人添加删除聊天标签用
        "synckey": 5401188,
        "taginfo": [
            {
                "tagids": [],
                "sessionid": 7881302555913610,
                "msgtype": 0
            },
            {
                "tagids": [
                    10133102389612482,
                    10133101616638504
                ],
                "sessionid": 1688856591445501,
                "msgtype": 0
            },
            {
                "tagids": [
                    10133101638621810
                ],
                "sessionid": 10944457190247899,
                "msgtype": 1
            },
            {
                "tagids": [],
                "sessionid": 7881302555913738,
                "msgtype": 0
            },
            {
                "tagids": [
                    10133102389612482
                ],
                "sessionid": 10909734557224664,
                "msgtype": 1
            },
            {
                "tagids": [
                    10133102389612482,
                    10133101616638504
                ],
                "sessionid": 7881302059975581,
                "msgtype": 0
            }
        ]
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 新增联系人移除联系人到聊天标签



[TOC]
    
##### 简要描述

- 新增联系人移除联系人到聊天标签

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SessionInTagReq
  
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
    "uuid":"2b0863724106a1160212bd1ccf025295",
    "tagids":[
        10133101616638504 //标签id数组可以打多个空数组就是全部移除标签，如果想打两个就填俩标签id，追加标签就要带着原本的标签id否则会覆盖
    ], 
    "vid":7881302555913738,//要打的联系人id或者群id
    "globalver":15, //获取聊天标签下的联系人列表中的这个字段拿过来用每次新增删除都需要拿递增的
    "type": 0 //0联系人 1群
}
```

##### 返回示例 

``` 
{
    "data": {
        "global_ver": 0,
        "taginfo": [
            {
                "tagids": [
                    10133101616638504
                ],
                "sessionid": 7881302555913738,
                "msgtype": 0
            }
        ]
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

