# 02-login

## 获取登录二维码



[TOC]
    
##### 简要描述

- 获取登录二维码

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/getQrCode
  
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
    "uuid":"f5a22e9b-9664-4250-b40a-08741dba549c"
}
```

##### 返回示例 

``` 
{
    "data": {
        "qrcode": "http://47.94.7.218:8083/980343ff-bf71-4789-8a0a-af3f7fd40bc0.png", //登陆二维码的直接访问连接
		"qrcode_data":"/9j/4AAQSkZJRgABAQEASABIAAD/2wBDABQODxIPDRQSEBIXFRQYHjIhHhwcHj0s",//登录二维码的base64位文件流
        "Ttl": 600,
        "Key": "5DBC31948BB057101F9C6B93569FB39B"
    },
    "errcode": 0,
    "errmsg": "获取二维码成功"
}
```






---

## 输入验证码设置



[TOC]
    
##### 简要描述

- 第一次登录的时候需要输入验证码，如果登陆过（然后初始化接口vid传值了，则在获取二维码就不需要输入验证码了）

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/CheckCode
  
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
    "qrcodeKey":"B547FC8D049DFA333D89075C04BA9178", //qr_codekey
    "code":"369130"//验证码
}
```

##### 返回示例 

``` 
{
    "data": null,
    "errcode": 0,
    "errorcode": 0,
    "errmsg": "ok"
}
```
##### 注意事项
如果调用这个接口出现了这个错误返回：qrcode_not need verify，这个就是输入验证码前把手机上的验证码界面x掉了，不要提前x掉，输入完验证码自己就会关掉。







---

## 自动登录



[TOC]
    
##### 简要描述

- 自动登录：自动登录条件，根据初始化企业微信接口，传递的vid，获取的uuid进行自动登录，如果初始化没有传递vid则自动登录失败，只要登陆过一次后期可以一直自动登录

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/automaticLogin
  
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
    "uuid": "8793dc28-a863-463b-a5d6-ccd369adb859"
}
```

##### 返回示例

``` 
{
    "data": null,
    "errcode": 0,
    "errmsg": "登陆成功"
}
```







---

## 退出登录



[TOC]
    
##### 简要描述

- 退出登录

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/LoginOut
  
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
    "uuid":"f5a22e9b-9664-4250-b40a-08741dba549c"
}
```

##### 返回示例 

``` 
{
    "data": {
        "code": 0,
        "errMsg": "ok",
        "obj": {},
        "pbObj": null
    },
    "errcode": 0,
    "errmsg": "退出成功"
}
```







---

## 获取二次验证二维码接口



[TOC]
    
##### 简要描述

- 获取二次验证二维码接口 用来二次验证

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SecondaryValidation
  
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
    "uuid":"f5a22e9b-9664-4250-b40a-08741dba549c"
}
```

##### 返回示例 

``` 
{
    "data": {
        "qrcode": "http://47.94.7.218:8083/980343ff-bf71-4789-8a0a-af3f7fd40bc0.png",
        "Ttl": 600,
        "Key": "5DBC31948BB057101F9C6B93569FB39B"
    },
    "errcode": 0,
    "errmsg": "获取二维码成功"
}
```






---

