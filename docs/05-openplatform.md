# 05-openplatform

## 企业用户id转开放平台id



[TOC]
    
##### 简要描述

- 企业用户id转开放平台id

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/Vid2UserId
  
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
    "uuid": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
    "scorpid": "wpgizUDQAAGwYmiyjENKSxnuNdN-74oQ",  //第三方应用企业提供商的企业id。  不传默认转当前账号企业的开放平台id
    "corpid": "1970325068983426",//第三方应用企业提供商的内部企业id，需要用对应企业登录我的接口拿一下 不传默认转当前账号企业的开放平台id
    "user_id":[
        7881300143960291
    ]
}
```

##### 返回示例 

``` 
{
    "data": [
        {
            "user_id": 7881300143960291,
            "openid": "wmgizUDQAAwEpDeJjPdOnjKTzc3GsCAA"//对应开放平台 external_userid
        }
    ],
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 开放平台id转企业用户id



[TOC]
    
##### 简要描述

- 开放平台id转企业用户id

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/UserId2Vid
  
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
    "uuid": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
    "scorpid": "wpgizUDQAAGwYmiyjENKSxnuNdN-74oQ",  //第三方应用企业提供商的企业id。  不传默认转当前账号企业的开放平台id
    "corpid": "1970325068983426",//第三方应用企业提供商的内部企业id，需要用对应企业登录我的接口拿一下 不传默认转当前账号企业的开放平台id
    "openid":[
        "wmgizUDQAAwEpDeJjPdOnjKTzc3GsCAA"
    ]
}
```

##### 返回示例 

``` 
{
    "data": [
        {
            "user_id": 7881300143960291,
            "openid": "wmgizUDQAAwEpDeJjPdOnjKTzc3GsCAA"
        }
    ],
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 群id转chatid



[TOC]
    
##### 简要描述

- 群id转chatid

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/RoomIdToChatId
  
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
    "uuid":"3240fde0-45e2-48c0-90e8-cb098d0ebe43",
	"corpid":1970324938xxxxx,  //企业id 不传默认当前账号登录企业
    "room_id":10696052955016166
}
```

##### 返回示例 

``` 
{
    "data": {
        "room_id": 10696052955016166,
        "chatid": "wrO9o4EAAAeR_nSlmjeX1RWrKAKxN8jQ"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## chatid转群id



[TOC]
    
##### 简要描述

- chatid转群id

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/ChatIdToRoomId
  
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
    "uuid":"3240fde0-45e2-48c0-90e8-cb098d0ebe43",
	"corpid":197032493814xxx,  //不传默认当前账号登录企业
    "chatid":"wrO9o4EAAAeR_nSlmjeX1RWrKAKxN8jQ"
}
```

##### 返回示例 

``` 
{
    "data": {
        "room_id": 10696052955016166,
        "chatid": "wrO9o4EAAAeR_nSlmjeX1RWrKAKxN8jQ"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

