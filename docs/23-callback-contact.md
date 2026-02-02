# 23-callback-contact

## 联系人添加申请通知



    
**简要描述：** 

- 联系人添加申请通知

**返回类型：** 
- ` 116001 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
 	"issync": false, //是否是因为调用消息同步接口返回的
    "list":[
        {
            "unionid":"ozynqskXWQK3wAWWKT_izAOfcIlY",
            "create_time":0,
            "sex":1,
            "mobile":"",
            "company_remark":"",
            "acctid":"",
            "avatar":"http://wx.qlogo.cn/mmhead/Q3auHgzwzM7BQVtj83EnfQcDMjz2hhrIvssZl9fv1H6rYUaN8VRibjg/0",
            "english_name":"",
            "remark_phone":[

            ],
            "realname":"",
            "real_remarks":"",
            "user_id":7881302555913738,
            "nickname":"十年一刻222222",
            "position":"",
            "corp_id":1970325134026788,
            "remarks":"",
            "seq":8085933,
            "status":0
        }
    ],
    "seq":8085933
}

```


---

## 联系人变动通知




    
**简要描述：** 

- 联系人变动通知

**返回类型：** 
- ` 116002 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 

{
 	"issync": false,//是否是因为调用消息同步接口返回
    "list": [
        {
            "unionid": "ozynqskXWQK3wAWWKT_izAOfcIlY", 
            "create_time": 0, 
            "add_customer_time": 1678428081, 
            "sex": 1, 
            "mobile": "", 
            "company_remark": "测试企业55555", 
            "acctid": "", 
            "avatar": "http://wx.qlogo.cn/mmhead/Q3auHgzwzM7BQVtj83EnfQcDMjz2hhrIv6vzOPdXX4mLpIEJUbE2ew/0", 
            "source": 104,
			"ItemFlag": 1,
            "english_name": "", 
            "remark_phone": [
                "13573788293"
            ], 
            "realname": "", 
            "real_remarks": "十年一刻66666497657", 
            "labelid": [
                "14073749556770626", 
                "14073751625749478"
            ], 
            "user_id": 7881302555913738, 
            "nickname": "十年一刻", 
            "position": "", 
            "ItemFlag": 0, 
            "corp_id": 1970325134026788, 
            "source_info": {
                "vid": 0, 
                "wx_friend_name": "", 
                "apply_mode": 1, 
                "mobile": "", 
                "source_roomid": 10889703532203248, 
                "source_type": 104
            }, 
            "remarks": "是你身边", 
            "seq": 8077754, 
            "status": 2057 //2049 代表被删除 8也代表被删除  9代表新增通知 2057就代表 备注 标签 电话等信息变动
        }
    ], 
    "seq": 8077754
}

```

---

## 内部联系人标签描述变动通知



    
**简要描述：** 

- 内部联系人标签描述变动通知

**返回类型：** 
- ` 116003 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "list": [
        {
            "vid": 1688856554448765, 
            "description": "描述修改", 
            "remark": "备注修改", 
            "timestamp": 1683273947
        }
    ], 
    "seq": 15312890
}

```

---

## 当前联系人已读消息通知回调



    
**简要描述：** 

- 当前联系人已读消息通知回调，在手机上已读了消息的回调，用来去除自己开发的应用小红点。

**返回类型：** 
- ` 102006 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "flag": 0,
    "receiver": 7881302059975581, //已读的好友id
    "sender_name": "",
    "is_room": 0,
    "server_id": 7549931,
    "issync": false,
    "send_time": 1733673509,
    "sender": 1688854256622049,
    "referid": 0,
    "app_info": "CAEQpYTXugYY4bOssJCAgAMgEg==",
    "readuinscount": 0,
    "msg_id": 1007061,
    "msgtype": 2001
}

```


---

## 联系人发消息提示删除通知



    
**简要描述：** 

- 联系人删除通知

**返回类型：** 
- ` 102001 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "msg": "十年一刻开启了联系人验证，你还不是他（她）的联系人。请先发送联系人验证请求，对方验证通过后，才能聊天。 发送联系人验证",
    "flag": 16777216,
    "receiver": 788130255591xxxx,  //好友id
    "sender_name": "",
    "is_room": 0,
    "server_id": 7649730,
    "issync": false,
    "send_time": 1741528470,
    "sender": 16888542566xxxxx, //发送人id
    "referid": 0,
    "app_info": "CIOACBDynL7Z1zIY4bOssJCAgAMgNA==_need_verify",
    "readuinscount": 0,
    "msg_id": 1009857,
    "msgtype": 1011
}

```


---

## 联系人发消息提示拉黑通知



    
**简要描述：** 

- 联系人拉黑通知

**返回类型：** 
- ` 102002 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "msg": "消息已发出，但被对方拒收了",
    "flag": 16777216,
    "receiver": 788130255591xxxx,
    "sender_name": "",
    "is_room": 0,
    "server_id": 7649739,
    "issync": false,
    "send_time": 1741528681,
    "sender": 168885425662xxxx,
    "referid": 0,
    "app_info": "CIOACBDUkMvZ1zIY4bOssJCAgAMgOg==_blacklist",
    "readuinscount": 0,
    "msg_id": 1009877,
    "msgtype": 1011
}

```


---

## 置顶免打扰标记操作通知



    
**简要描述：** 

- 置顶免打扰标记操作通知
出现这个通知代表其他端操作了置顶 免打扰 或者标记操作，
调用一下[获取置顶免打扰标记列表接口](https://www.showdoc.com.cn/1889765213630885/11558938230701720 "获取置顶免打扰标记列表接口")

**返回类型：** 
- ` 116005 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "innerkf_vid": 0,
    "flag": 0,
    "receiver": 0,
    "extraContent": {

    },
    "devInfo": 0,
    "sender_name": "",
    "is_room": 0,
    "server_id": 12574586,
    "kf_id": 0,
    "issync": false,
    "send_time": 1758881937,
    "sender": 10008,
    "referid": 0,
    "app_info": "HP1K_0QkQSulhN1",
    "readuinscount": 0,
    "msg_id": 1005422,
    "msgtype": 2115
}
```


---

