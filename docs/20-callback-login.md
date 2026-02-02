# 20-callback-login

## 扫码返回数据



    
**简要描述：** 

- 扫码返回数据

**返回类型：** 
- ` 100001 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "Vid":1688853790599424,
    "Qrcode_key":"D368DC88254A3E79FF72942B588D7FBE",
    "Corpid":1970325109135931,
    "Nick_name":"姓名",
    "Icon_url":"http://wework.qpic.cn/bizmail/yfG7qDz7ianwM1s5JXtgBLsaZKQfQ5YxZIiaQpUxAkEu9icIMaKxF3C4Q/0",
    "Logo":"https://p.qlogo.cn/bizmail/haQ4Dk0CFb61TIkB1JVibTUPkRynsZfK1hG9X0gDCv4cicaCObHtUCsA/0"
}

```

---

## 取消扫码返回



    
**简要描述：** 

- 取消扫码返回

**返回类型：** 
- ` 100003 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "Vid":1688853790599424,
    "Qrcode_key":"D368DC88254A3E79FF72942B588D7FBE",
    "Corpid":1970325109135931,
    "Nick_name":"姓名",
    "Icon_url":"http://wework.qpic.cn/bizmail/yfG7qDz7ianwM1s5JXtgBLsaZKQfQ5YxZIiaQpUxAkEu9icIMaKxF3C4Q/0",
    "Logo":"https://p.qlogo.cn/bizmail/haQ4Dk0CFb61TIkB1JVibTUPkRynsZfK1hG9X0gDCv4cicaCObHtUCsA/0"
}

```


---

## 确认扫码返回数据



    
**简要描述：** 

- 确认扫码返回数据

**返回类型：** 
- ` 100002 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "Vid":1688853790599424,
    "Qrcode_key":"D368DC88254A3E79FF72942B588D7FBE",
    "Corpid":1970325109135931,
    "Nick_name":"姓名",
    "Icon_url":"http://wework.qpic.cn/bizmail/yfG7qDz7ianwM1s5JXtgBLsaZKQfQ5YxZIiaQpUxAkEu9icIMaKxF3C4Q/0",
    "Logo":"https://p.qlogo.cn/bizmail/haQ4Dk0CFb61TIkB1JVibTUPkRynsZfK1hG9X0gDCv4cicaCObHtUCsA/0"
}

```


---

## 登录成功返回数据



    
**简要描述：** 

- 登录成功返回数据

**返回类型：** 
- ` 104001 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "Corpid":0,
    "unionid":"ozynqskXWQK3wAWWKT_izAOfcIlY",
    "create_time":0,
    "Admin_vid":0,
    "sex":1,
    "mobile":"13573788293",
    "Corp_full_name":"十年一刻",
    "acctid":"xxxxpeng",
    "avatar":"http://wework.qpic.cn/bizmail/yfG7qDz7ianwM1s5JXtgBLsaZKQfQ5YxZIiaQpUxAkEu9icIMaKxF3C4Q/0",
    "corp_name":"十年一刻",
    "Create_time":0,
    "english_name":"别名",
    "realname":"xxxxxx",
    "Vid":0,
    "Mail":"",
    "Ownername":"",
    "user_id":1688853790599424,
    "nickname":"姓名",
    "position":"111",
    "Corp_desc":"",
    "corp_id":1970325109135931,
    "Corp_name":"",
    "Corp_logo":""
}

```


---

## 企业切换回调




    
##### 简要描述

- 企业切换

##### 操作码

- 100006




##### 返回示例 

``` 
{
	"this_vid":1688854256622049//切换前当前登陆的账号vid
    "vid": 1688857338601153, //切换企业后的账号vid
    "corp_full_name": "",
    "corpid": 1970326674001513, //切换后的企业id
    "create_time": 1691574887,
    "mail": "",
    "ownername": "刘xx", //企业创建人昵称
    "card_url": "https://work.weixin.qq.com/wework_admin/user/h5/corp?",//企业头像
    "nickname": "奥利给干就完了", //切换后的账号昵称
    "avatar": "https://wework.qpic.cn/wwpic3az/259628_Ehj-EqAlSROfFMB_1713965307/0", //切换后的账号头像
    "corp_name": "哼哈二将"//切换后的企业名称
}
```

##### 备注 

- 更多返回错误代码请看首页的错误代码描述







---

## 需要输入验证码消息回调



    
**简要描述：** 

- 需要验证二维码消息

**返回类型：** 
- ` 100004 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "Vid": 1688857826547606, 
    "Qrcode_key": "E58DBF1516D268A519CC0C714FA0B50D", 
    "Corpid": 1970326674001513, 
    "Nick_name": "xxx", 
    "Icon_url": "https://wework.qpic.cn/wwpic/28825_3HEipRLPR4-SUsQ_1691575135/0", 
    "Logo": "https://wework.qpic.cn/wwpic/447429_AZoXgU5fQ5efSYi_1691574887/0"
}

```


---

