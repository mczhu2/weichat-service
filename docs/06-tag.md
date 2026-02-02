# 06-tag

## 获取标签列表



[TOC]
    
##### 简要描述

- 发送CDN图片消息

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/GetLabelListReq
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|index |是  |int |下标第一次传0|
|sync_type |是  |int |1或者2 1企业标签2是个人标签|
##### 请求示例 

``` 
{
    "uuid": "1688xxxx99424",
    "index":0,
    "sync_type":2
}
```

##### 返回示例 

``` 
{
    "data": {
        "labellist": [
            {
                "op": 3,
                "bDeleted": 0,//是否删除
                "create_time": 1677119449, //创建时间
                "label_groupid": 14073751173597643, //标签组id
                "label_type": 2, //标签类型2 个人标签
                "source_appid": 0,
                "business_type": 0,
                "name": "接口添加测试",
                "data_type": 1, 
                "id": 14073750275804621,//标签id
                "service_groupid": 0,
                "new_order": 0,
                "order": 0
            }
        ],
        "index": 12779388
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 添加标签



[TOC]
    
##### 简要描述

- 添加标签

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/AddLabelReq
  
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
    "uuid": "ac19c4447bbc5f2cd86f782ae11697b6",
    "labelName":"测试标签新增22222", //标签名
    "data_type":1,//默认1
    "label_type":2,//2是个人标签  填2固定
    "groupid":14073749513871998 //组id  填个人标签的那个id  
}
```

##### 返回示例 

``` 
{
    "data": {
        "op": 1,
        "bDeleted": 0,
        "create_time": 1677120051,
        "label_groupid": 14073751173597643,
        "label_type": 2,
        "source_appid": 0,
        "business_type": 0,
        "name": "接口添加测试啊",
        "data_type": 1,
        "id": 14073752190771826,
        "service_groupid": 0,
        "seq": 12779390,
        "new_order": 0,
        "order": 0
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 修改标签



[TOC]
    
##### 简要描述

- 修改标签

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/EditLabelReq
  
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
    "uuid": "xxx",
    "labelName":"接口修改测试啊",
    "labelid":1407375xx5804621,
    "groupid":14073xxx597643
}
```

##### 返回示例 

``` 
{
    "data": {
        "op": 3,
        "bDeleted": 0,
        "create_time": 1677119449,
        "label_groupid": 14073751173597643,
        "label_type": 2,
        "source_appid": 0,
        "business_type": 0,
        "name": "接口修改测试啊",
        "data_type": 1,
        "id": 14073750275804621,
        "service_groupid": 0,
        "seq": 12779389,
        "new_order": 0,
        "order": 0
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 删除标签



[TOC]
    
##### 简要描述

- 删除标签

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/DelLabelReq
  
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
    "uuid": "1688xxx90599424",
    "labelid":1407xxx0771826,
    "groupid":14xxx173597643
}
```

##### 返回示例 

``` 
{
    "data": {
        "op": 2, //类型 1添加 2删除 3修改
        "bDeleted": 1,
        "create_time": 1677120051,
        "label_groupid": 14073751173597643,
        "label_type": 2,
        "source_appid": 0,
        "business_type": 0,
        "name": "接口添加测试啊",
        "data_type": 1,
        "id": 14073752190771826,
        "service_groupid": 0,
        "seq": 12779391,
        "new_order": 0,
        "order": 0
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 一个标签多个用户



[TOC]
    
##### 简要描述

- 一个标签多个用户

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/LabelAddUserReq
  
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
    "uuid": "168885xxx24",
    "labelid":14073750275804621,
    "userid_list":[//要添加的用户id可以空数组
        7881xxxx3
    ],
    "del_userid":[ //要移除的用户id可以空数组
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

## 一个用户多个标签




[TOC]
    
##### 简要描述

- 一个用户多个标签

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/UserAddLabelsReq
  
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
    "uuid": "168885xxx599424",
    "userid":0,//用户id
    "labelid_list":[ //标签id数组
        0
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

