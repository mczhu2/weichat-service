# 09-account

## 获取当前账号详情



[TOC]
    
##### 简要描述

- 获取当前账号详情

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/GetProfile
  
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
        "unionid": "ozynqskEsd4gEtIJpxXB_YPu1T4o",
        "create_time": 0,
        "sex": 1,
        "mobile": "13573753761",
        "corp_full_name": "",
        "acctid": "LiuXianKun",
		"scorp_id": "ww6509c50a6839c841",
        "avatar": "https://wework.qpic.cn/wwpic/839179_8l0dm09iQ0qV76v_1652237791/0",
        "corp_name": "明月科技",
        "english_name": "",
        "realname": "刘xxx",
        "user_id": 1688853132666001,
        "nickname": "刘xxx",
        "position": "",
        "corp_desc": "",
        "corp_id": 1970325105140387
    },
    "errcode": 0,
    "errmsg": "ok"
}
```






---

## 获取当前账号二维码



[TOC]
    
##### 简要描述

- 获取当前账号二维码

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/GetThisQrCode
  
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
        "QrCodeUrl": "https://wework.qpic.cn/wwpic/614600_ky2A2t1FRfKmue5_1652238985/0"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 获取当前账号对外名片二维码



[TOC]
    
##### 简要描述

- 获取当前账号对外名片二维码

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/ExternalBusinessCard
  
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
	//base64二维码图片
        "qr_image": "iVBORw0KGgoAAAANSUhEUgAAAxgAAAMYCAYAAABIfUFkAAAgAElEQVR4Xu3d53U1VZYwYF0t/aeZBOj+EsAkgAmgMQlgAsAF0JgAMBMApgPABQD9JoBJAIYJYGgSuPfTFi0Qkq5UZlfVMc9di0XPULVrn2cfSbXvKbM7nH9OfAgQIECAAAECBAgQIDBf4HA6P4YIBAgQIECAAAECBAgQ+E1Ag2EmECBAgAABAgQIECCQJqDBSKMUiAABAgQIECBAgAABDYY5QIAAAQIECBAgQIBAmoAGI41SIAIECBAgQIAAAQIENBjmAAECBAgQIECAAAECaQIajDRKgQgQIECAAAECBAgQ0GCYAwQIECBAgAABAgQIpAloMNIoBSJAgAABAgQIECWlkzgBAgQIECBAgACB8gQ0GOXVREYECBAgQIAAAQIEqhXQYFRbOokTIECAAAECBAgQKE9Ag1FeTWREgAABAgQIECBAoFoBDUa1pZM4AQIECBAgQIAAgfIENBjl1URGBAgQIECAAAECBKoV0GBUWzqJEyBAgAABAgQIEChPQINRXk1kRIAAAQIECBAgQKBagd3h/FNt9hInQIAAAQIECBAgQGCIQJzz74ZsOHObgxWMmYJ2J0CAAAECBAgQIEDgDwENhtlAgAABAgQIECBAgECagAYjjVIgAgQIECBAgAABAgQ0GOYAAQIECBAgQIAAAQJpAhqMNEqBCBAgQIAAAQIECBA4a4Fgt5t2Q/yxB2hlxyvduPTxZueXHS+7vtn5lR4v26/0eNn1yB5vb/llj7e3eNnzr/R4pdc322/KeGOf/X6fnUr38eKc9fR0/LrAVvUYn2n3JQZAgAABAgQIECBAgMAxAQ2GuUGAAAECBAgQIECAQJqABiONUiACBAgQIECAAAECBDQY5gABAgQIECBAgAABAmkCGow0SoEIECBAgAABAgQIENBgmAMECBAgQIAAAQIECKQJaDDSKAUiQIAAAQIECBAgQECDYQ4QIECAAAECBAgQIJAmoMFIoxSIAAECBAgQIECAAAENhjlAgAABAgQIECBAgECagAYjjVIgAgQIECBAgAABAgQ0GOYAAQIECBAgQIAAAQJpAhqMNEqBCBAgQIAAAQIECBDYHc4/GAgQIECAAAECBAgQaFogzvl3K4zwYAVjBWWHIECAAAECBAgQINCLgAajl0obJwECBAgQIECAAIEVBDQYKyA7BAECBAgQIECAAIFeBDQYvVTaOAkQIECAAAECBAisIKDBWAHZIQgQIECAAAECBAj0InC2xUB3u2k3sB974FXp8bYwHnPM3vyyxzvGuoVts/1Kj1d6zbL9jLd0gXn5Zc+X0uPN01p+79L9puQX++z3+xt4cQ53ejr+e23xfqPM9lt6do+v9NIZiU+AAAECBAgQIECAQLUCGoxqSydxAgQIECBAgAABAuUJaDDKq4mMCBAgQIAAAQIECFQroMGotnQSJ0CAAAECBAgQIFCegAajvJrIiAABAgQIECBAgEQIajPJqIiMCBAgQIECAAAEC1QpoMKotncQJECBAgAABAgQIlCegwSivJjIiQIAAAQIECBAgUK2ABqPa0kmcAAECBAgQIECAQHkCGozyaiIjAgQIECBAgAABAtUKaDCqLZ3ECRAgQIAAAQIECJQnoMEoryYyIkCAAAECBAgQIFCtgAaj2tJJnAABAgQIECBAgEB5AhqM8moiIwIECBAgQIAAAQLVCuwO559qs5c4AQIECBAgQIAAAQJDBOKcfzdkw5nbHKxgzBS0OwECBAgQIECAAAECfwhoMMwGAgQIECBAgAABAgTSBDQYaZQCESBAgAABAgQIECCgwTAHCBAgQIAAAQIECBBIEzhLiyQQAQIECGwu8D//8z8n3"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 设置头像



[TOC]
    
##### 简要描述

- 设置头像

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/AddImage
  
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
    "uuid":"1c57e661-74bf-4eaf-bbf3-9a49114f27b9",
    "imgurl":"https://www.baidu.com/img/flexible/logo/pc/result.png"
}
```

##### 返回示例 

``` 
{
    "data": {
        "image_url": "https://wework.qpic.cn/wwpic/827947_xnqwcg3ATxmoUf-_1656095832/0",
        "image_id": ""
    },
    "errcode": 0,
    "errmsg": "ok"
}
```






---

## 设置用户信息



[TOC]
    
##### 简要描述

- 设置用户信息

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/UpdateUserInfo
  
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
    "uuid":"6d25981b-071c-4e1e-aa01-5770d7a6559d",
    "emile":"1693590929@qq.com", //邮箱
    "name":"name嗷嗷",//名字
    "english_name":"yingwenmingcheng",//英文名
    "phone":"222",//座机
    "position":"232",//职位
    "mobile":"12222222",//手机号
    "gender":1//性别 1男2女
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

## 设置是否自动同意



[TOC]
    
##### 简要描述

- 设置是否自动同意

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/AutomaticConsent
  
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
    "uuid":"bc48e30d0c00492083fdec4c1a7e5c94",
    "state":1 //1 是需要验证同意（需要手动点击同意） 0关闭验证 自动同意好友 
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

## 根据企业id获取企业详情



[TOC]
    
##### 简要描述

- 根据i企业d获取企业详情

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/GetCorpInfo
  
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
    "uuid":"e1d672c613717ca41fcbd6823da9015a",
    "corpid": 1970324993045436
}
```

##### 返回示例 

``` 
{
    "data": {
        "list": [
            {
                "Staffnum": 0,
                "Auth_time": 1743673059,
                "Card_url": "https://work.weixin.qq.com/apph5/user/h5/corp?",
                "Corpid": 197032499x45xxx,
                "Admin_vid": 16888522833xx8,
                "V_superadmin_vid": 16888522xx208,
                "Corp_stat": "AUTH_CORP",
                "Corp_full_name": "xxxx有限公司",
                "IsAccepted": false,
                "Create_time": 1534914566,
                "Vid": 1688857345791146,
                "Scorp_id": "ww84bf86xxxxx",
                "Mail": "",
                "Ownername": "佘xxx",
                "Corp_desc": "",
                "Corp_name": "xxxxx",
                "Corp_logo": "https://p.qlogo.cn/bizmail/vhpBKorAa29UibBkcysHzF1M93Yq1iaEuMJf9tHk1DNCoH7gjC2aLOUg/0"
            }
        ]
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 获取当前账号封禁原因



[TOC]
    
##### 简要描述

- 获取当前账号封禁原因以及解封时间
![](https://www.showdoc.com.cn/server/api/attachment/visitFile?sign=3b19dbf5f8f84ffde264ce6c623ec0a0&file=file.png)
![](https://www.showdoc.com.cn/server/api/attachment/visitFile?sign=0662b87ce22afdd8f4eabbaa032946c6&file=file.png)

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/getBanDetile
  
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
    "uuid": "d3062b1658696330c06de618b52e8f95",
    "url":"https://work.weixin.qq.com/webapp/spam/info?key=5lnY61CGjPFUvYNwLanEklEzH2H2kv9ndXtnqnOlgfMNWq4tPE8qRYS5nGoew4WXio%2FAQMui9qhbDFDApfo61XnSbW9S8OwA0K2CZb290XCD%2Fj%2FI0jI4GkF5fomdypWnTXgiw4jzjqGZaeCvNK6KlwAGVzR1C9sP7ITa36Nt4O7zj3598FQBlbkRXFOMbDESnkpqi1%2F4NSjSxaO4NmuJxQ%3D%3D&type=3&need_login=true" //从系统消息回调中获取
}
```

##### 返回示例 

``` 
{
    "data": {
        "vid": "168885628xxxx2",
        "gid": "225180304xxxx116",
        "mainData": {
            "ban_start_time": "1745647588", //开始封禁时间
            "appeal_id": "R9Eb5Sui7oDmQvbI79j1xNlXe9DBMRfy1i5Oee6lKTBX1wM2zbsM7iPUrYHDTSXS3GJd1Ohw0AUGqFbpzkCL/+VX1uz2GeR8S3hhFqY3qEFBnEx7iUTPQ5SQ4m7uh0Th",
            "ban_source": 0,
            "is_show_title": true,
            "err_code": 0,
            "ban_title": "你的部分功能已被限制使用", //封禁标题
            "type": "3",
            "is_show_reason": true,
            "is_show_func": true,
            "ban_finish_time": "1746252388", //结束封禁时间
            "ban_category": 2,
            "appeal_status": 0,
            "can_appeal": true,
            "ban_id_type": 1,
            "need_login": "true",
            "ban_reason": "涉嫌存在骚扰/恶意引流/欺诈等违规行为", //封禁原因
            "template_type": 0,
            "exposed_id": "v008b6bbbbd9",
            "ban_func": "新建客户群；邀请客户加入群聊；添加客户；客户聊天；客户朋友圈；群发助手", //封禁功能
            "is_show_time": true,
            "can_urge": false,
            "key": "5lnY61CGjPFUvYNwLanEklEzH2H2kv9ndXtnqnOlgfMNWq4tPE8qRYS5nGoew4WXio/AQMui9qhbDFDApfo61XnSbW9S8OwA0K2CZb290XCD/j/I0jI4GkF5fomdypWnTXgiw4jzjqGZaeCvNK6KlwAGVzR1C9sP7ITa36Nt4O7zj3598FQBlbkRXFOMbDESnkpqi1/4NSjSxaO4NmuJxQ=="
        },
        "isWechat": false,
        "isWxwork": true,
        "is_superadmin": 0,
        "order_id": "R9Eb5Sui7oDmQvbI79j1xNlXe9DBMRfy1i5Oee6lKTBX1wM2zbsM7iPUrYHDTSXS3GJd1Ohw0AUGqFbpzkCL/+VX1uz2GeR8S3hhFqY3qEFBnEx7iUTPQ5SQ4m7uh0Th",
        "corp_id": "197032xxxx045"
    },
    "errcode": 0,
    "errmsg": "ok"
}
```






---

## 上传图片到企微



[TOC]
    
##### 简要描述

- 上传图片到企微

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/add_image
  
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
    "uuid":"0444e61126a73521ee38e5674937ceff",
    "url":"http://47.94.7.218:8060/download/Img/20250516/c310ad688eddb3ed252a5d8271256a90.jpg"
}
```

##### 返回示例 

``` 
{
    "data": {
        "image_height": 961,
        "image_url": "https://wework.qpic.cn/wwpic3az/219139_sCKF32atQZqiyeA_1747382239/0",
        "image_width": 877,
        "image_id": "cos*wwpic3az*219139_sCKF32atQZqiyeA_1747382239",
        "file_size": 104540
    },
    "errcode": 0,
    "errmsg": "ok"
}
```






---

