# 14-payment

## 获取商户列表



[TOC]
    
##### 简要描述

- 获取商户列表

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/MerchantInformation
  
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
    "uuid":"xxxxxxxxxxxx"
}
```

##### 返回示例 

``` 
{
    "data": [
        {
            "mch_type": 1002,
            "mch_name": "xxx照xxx",
            "company_name": "古田xxxx照明店",
            "mch_id": "1650xxxx"
        }
    ],
    "errcode": 0,
    "errorcode": 0,
    "errmsg": "ok"
}
```








---

## 获取收款二维码




[TOC]
    
##### 简要描述

- 获取收款二维码

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/GetUserMerchantQrCode
  
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
    "uuid": "xxxx",
    "name":"家曼xxx", //收款名称
    "amount":0.1,//收款金额 单位元 0.1元
    "mchid":"1650333xxx"//获取商户列表接口获取
}
```

##### 返回示例 

``` 
{
    "data": {
        "ret": 0,
        "qrcode_url": "http://127.0.0.1:8083/vdet6JKr2d00005c07ec97.png", //收款码
        "project_id": "vdet6JKr2d00005c07ec97",
        "open_payment_id": ""
    },
    "errcode": 0,
    "errorcode": 0,
    "errmsg": "ok"
}
```







---

## 发送收款小程序




[TOC]
    
##### 简要描述

- 发送收款小程序

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/sendPayment
  
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
    "uuid": "5894a5b28f5eaaf28db080d2094e98db",
    "name":"家曼照明店", //账户名称
    "amount":0.1, //收款金额
    "mchid":"1650333778",//商户号  从获取商户列表接口拿到
    "buyer_id":7881302555913738,//要发送收款的好友id
    "words":"给我转钱快点。"//收款说明
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

