# 01-init

## 初始化企业微信(第一步必须)

##### 简要描述

- 初始化企业微信

##### 请求URL

- ` http://47.94.7.218:8083/wxwork/init

##### 请求方式

- POST
- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----|:---|:-----|-----|
|vid |否  |long |第一次登录可以传空字符串,登陆成功后下次初始化填登录账号的id，用来自动登录操作，这个值代表了这个号的设备绑定，首次不传值，会默认生成新设备信息，然后扫码登录了，登陆了A账号成功后，后续在重新登录的时候，vid参数就传A账号的账号id 16888开头的就是账号id。   |
|proxySituation |否  |int |是否全局代理(必须设置代理的时候他才生效，不设置代理默认就是0，设置了代理不传这个字段默认就是全局代理)，1是全局代理代表设置上就是长效的代理不能取消，0代表不是全局代理，可以设置上登录成功后用代理设置接口去取消代理。  |

##### 请求示例

```
{
    "vid":"", 
    "ip":"", //代理ip
    "port":"",//代理端口
    "proxyType":"", //http 代理类型
    "userName":"", //代理账号 没有不传
	"passward":"", //代理密码 没有不传
	"proxySituation":0,  
	"deverType":"ipad"
}
```

##### 返回示例

```
{
    "data": {
        "uuid": "427d7ee5-3a1c-4183-a83b-532ba1e71a1e", //uuid是这个账号生命周期内一直用到的，用来操作哪个账号执行接口功能的。
		"is_login": "false"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```



---

## 设置消息回调地址



[TOC]
    
##### 简要描述

- 设置消息回调地址

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/SetCallbackUrl
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|url |是  |String |消息回调地址|

##### 请求示例(HTTP回调设置)

``` 
//消息回调地址设置规则 
//1. 搭建一个web服务 
//2. 设定post请求接口，ContentType:"application/json" json请求方式
//3. 接收参数设置成对象，对象属性为以下属性
		{
			"uuid":"", //uuid 运行实例id
			"json":"", //回调消息内容为json格式
			"type": //消息类型
		}
//具体回调内容及类型请查看下发文档
{
    "uuid":"f5a22e9b-9664-4250-b40a-08741dba549c",
    "url":"http://127.0.0.1:8084/wxwork/callback"
}
```
回调示例java版本
![](https://www.showdoc.com.cn/server/api/attachment/visitFile?sign=9f20ec3f3a49733f458577c273d8bca0)
##### 请求示例(RABBITMQ)

``` 
{
    "uuid": "5a5e6ebc176da6ea0d8cb4ffd09f1956",
    "callbackType":"RABBITMQ",
    "mqExchange":"message.exchange.ultraMsg", //交换机 direct类型
    "mqRoutingKey":"ssss",//路由键
	"extraContent":"66666" //自定义回调额外内容可以为null 不传就行
}
```
##### 返回示例

``` 
{
    "data": null,
    "errcode": 0,
    "errmsg": "设置成功"
}
```






---

## 获取运行中的实例



[TOC]
    
##### 简要描述

- 获取运行中的实例：直接post请求地址就可以了

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/GetRunClient
  
##### 请求方式
- POST 

##### 参数
无
##### 请求示例 

``` 
无参数
```

##### 返回示例 

``` 
{
    "data": [
        {
            "1688857362464764": {
                "isLogin": true,
                "clientId": "8314b481e596fc85d850487e35bbe88b",
                "updateTime": 1715042291,
                "userid": 1688xxxx464764,
                "uuid": "8314b481e596fc85d850487e35bbe88b",
                "requrl": "http://47.94.7.218:",
                "object": {
                    "unionid": "ozynqstMEN_38dTCLJIlwe73ZhB0",
                    "create_time": 0,
                    "sex": 1,
                    "mobile": "135xxxxx01",
                    "Corp_full_name": "",
                    "acctid": "Baxxxang",
                    "scorp_id": "ww75xxxxccc9e",
                    "avatar": "https://wework.qpic.cn/wwpic/533154_tevrSjnsQVKBlHH_1678777868/0",
                    "corp_name": "明月科技",
                    "english_name": "",
                    "realname": "刘xxx",
                    "user_id": 16888xxxxx,
                    "nickname": "班xxx",
                    "position": "",
                    "Corp_desc": "",
                    "corp_id": 1970325xxxx87
                }
            }
        }
    ],
    "errcode": 0,
    "errmsg": "获取成功"
}
```





---

## 根据uuid查看实例详情



[TOC]
    
##### 简要描述

- 根据uuid查看实例详情

##### 请求URL
- ` http://127.0.0.1:8083/wxwork/GetRunClientByUuid
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |要查询的账号状态|
##### 请求示例 

``` 
{
    "uuid":"xxxxxx" //uuid
}
```

##### 返回示例 

``` 
{
    "data": {
        "logintime": 1744427201,
        "loginType": 2, //0 未初始化  1初始化了未登录  2已登录
        "user_info": {
            "object": {
                "unionid": "ozynqsgN5h3Vuulx4E07Oy17j-7s",
                "create_time": 0,
                "sex": 1,
                "mobile": "xxxx",
                "acctid": "xxxx",
                "scorp_id": "xxxxx",
                "avatar": "https://wework.qpic.cn/wwpic3az/319926_srytF-ZZSX2lOL7_1743776394/0",
                "corp_name": "xxx",
                "english_name": "xx",
                "realname": "xxx",
                "user_id": xxxx,
                "nickname": "xxx",
                "position": "",
                "corp_id": xxxx,
                "corp_full_name": "xxxxxx",
                "corp_desc": ""
            },
            "userid": xxxxxx,
            "clientId": "e1d672c613717ca41fcbd6823da9015a",
            "updateTime": 1745125943,
            "uuid": "e1d672c613717ca41fcbd6823da9015a",
            "requrl": "http://47.94.7.218:8083",
            "isLogin": true
        },
        "uuid": "e1d672c613717ca41fcbd6823da9015a"
    },
    "errcode": 0,
    "errmsg": "获取成功"
}
```







---

## 关闭链接



[TOC]
    
##### 简要描述

- 关闭链接

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/CloseConnent
  
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
    "data": null,
    "errcode": 0,
    "errmsg": "关闭成功"
}
```







---

## 设置或者取消代理接口



[TOC]
    
##### 简要描述

- 设置取消代理接口

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/setProxy
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |对应账号操作id   |
##### 请求示例 

``` 
{
    "uuid": "f866182dfc70b000bcdfcb2013fddcab",
    "ip": "127.0.0.1", //代理ip   如果用127.0.0.1代表用本机ip  设置成其他代理ip就用对应的代理 127.0.0.1也可以理解成移除代理
    "port": 1081, //代理端口
    "proxyType": "http", //代理类型
    "passward": "",//代理密码
    "userName": ""//代理账号
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

