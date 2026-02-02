# 22-callback-group

## 群新增通知






    
**简要描述：** 

- 群新增通知

**返回类型：** 
- ` 115001 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "room_id": "10927640775842980", 
    "create_user_id": 7881302555913738, 
    "total": 4, 
    "notice_sendervid": 10927640775842980, 
    "flag": 268435585, 
    "create_time": 1687687448, 
    "notice_content": "", 
    "nickname": "", 
    "member_list": [
        {
            "unionid": "", 
            "create_time": 0, 
            "sex": 1, 
            "mobile": "", 
            "acctid": "", 
            "join_scene": 1, 
            "avatar": "https://wework.qpic.cn/wwpic/510408_Qat5EH4YQaidquh_1678715517/0", 
            "english_name": "", 
            "realname": "xxx", 
            "room_notes": "", 
            "jointime": 1687687448, 
            "nickname": "xxxx", 
            "room_nickname": "", 
            "position": "", 
            "uin": 168xxx199881, 
            "invite_user_id": 7881302555913738, 
            "corp_id": 1970324968093920
        }, 
        {
            "unionid": "ozynqskXWQK3wAWWKT_izAOfcIlY", 
            "create_time": 0, 
            "sex": 1, 
            "mobile": "13573788293", 
            "acctid": "ShiNianYiKe", 
            "join_scene": 1, 
            "avatar": "https://wework.qpic.cn/wwpic/247382__uUOzm-wQSmgT5w_1687280998/0", 
            "english_name": "", 
            "realname": "xxx", 
            "room_notes": "", 
            "jointime": 1687687448, 
            "nickname": "xx", 
            "room_nickname": "", 
            "position": "", 
            "uin": 1688xxx0400545, 
            "invite_user_id": 7881302555913738, 
            "corp_id": 1970325085040417
        }, 
        {
            "unionid": "", 
            "create_time": 0, 
            "sex": 1, 
            "mobile": "", 
            "acctid": "", 
            "join_scene": 1, 
            "avatar": "https://wework.qpic.cn/wwpic/80928_SFNfb-ufTySFa6W_1652593247/0", 
            "english_name": "", 
            "realname": "xxx", 
            "room_notes": "", 
            "jointime": 1687687448, 
            "nickname": "十年一刻", 
            "room_nickname": "", 
            "position": "", 
            "uin": 16888581xxx422, 
            "invite_user_id": 7881302555913738, 
            "corp_id": 1970325105140387
        }, 
        {
            "unionid": "", 
            "create_time": 0, 
            "sex": 1, 
            "mobile": "", 
            "acctid": "", 
            "join_scene": 1, 
            "avatar": "http://wx.qlogo.cn/mmhead/Q3auHgzwzM7BQVtj83EnfQcDMjz2hhrIvssZl9fv1H6rYUaN8VRibjg/0", 
            "english_name": "", 
            "realname": "", 
            "room_notes": "", 
            "jointime": 1687687448, 
            "nickname": "十年一刻", 
            "room_nickname": "", 
            "position": "", 
            "uin": 788130xx13738, 
            "invite_user_id": 7881302555913738, 
            "corp_id": 1970325134026788
        }
    ], 
    "new_flag": 4, 
    "notice_time": 10927640775842980, 
    "managers": [ ]
}

```


---

## 移除群成员通知





    
**简要描述：** 

- 移除群成员通知

**返回类型：** 
- ` 115002 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 

{
    "flag": 0, 
    "receiver": 0, 
    "sender_name": "", 
    "is_room": 1, 
    "server_id": 15318083, 
    "send_time": 1687688952, 
    "sender": 1688855749266556, 
    "referid": 0, 
    "member_list": [
        1688856554448765, 
        1688854853482590, 
        1688855749266675
    ], 
    "app_info": "CAQQ+K3gpAYYACD3+fXFAQ==", 
    "room_conversation_id": "266246527627906", 
    "readuinscount": 0, 
    "msg_id": 1014584, 
    "msgtype": 1003
}

```



---

## 邀请进群通知




    
**简要描述：** 

- 邀请进群通知

**返回类型：** 
- ` 115003 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 

{
    "flag": 0, 
    "receiver": 1688858100400545, 
    "sender_name": "", 
    "is_room": 1, 
    "server_id": 8082424, 
    "invitee": 7881302555913738, 
    "send_time": 1687687454, 
    "sender": 7881302555913738, 
    "referid": 0, 
    "member_list": [ ], 
    "app_info": "hAfWo6o-TieYSeYynewroom", 
    "room_conversation_id": "10927640775842980", 
    "readuinscount": 0, 
    "msg_id": 1013451, 
    "msgtype": 1002
}

```



---

## 自己主动退出群聊通知





    
**简要描述：** 

- 自己主动推出群聊通知（比如手机端退出或者其他端退出群聊）

**返回类型：** 
- ` 115004 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "op_user_id": 1688855749266556, 
    "flag": 16777216, 
    "receiver": 0, 
    "sender_name": "", 
    "is_room": 1, 
    "server_id": 15318052, 
    "send_time": 1687688387, 
    "sender": 1688855749266556, 
    "referid": 0, 
    "app_info": "QUIT_CC_CAQQwqngpAYYACCNytanDg==", 
    "room_conversation_id": "10927640775842980", 
    "readuinscount": 0, 
    "msg_id": 1014522, 
    "msgtype": 2055
}

```


---

## 群名变更通知




    
**简要描述：** 

- 群名变更通知

**返回类型：** 
- ` 115005 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "flag": 83886080, 
    "receiver": 0, 
    "sender_name": "", 
    "is_room": 1, 
    "server_id": 8082398, 
    "room_name": "666366123456789123456",  //变动群名
    "send_time": 1687684854, 
    "operation_user_name": "", 
    "sender": 1688858100400545, 
    "referid": 0, 
    "app_info": "42C0EC26619C41B7A9AFBA2DE2EBCCC7", 
    "room_conversation_id": "10884332696128689", 
    "readuinscount": 0, 
    "msg_id": 1013395, 
    "msgtype": 1001, 
    "operation_user_id": 1688858100400545
}

```


---

## 群主转让通知等其他设置通知





    
**简要描述：** 

- 群主转让通知

**返回类型：** 
- ` 115006 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "op_user_id": 1688855302603660,
    "flag": 16777216,
    "receiver": 0,
    "extraContent": {

    },
    "sender_name": "",
    "is_room": 1,
    "server_id": 7651920,
    "content": "已经成为新的群主",
    "issync": false,
    "send_time": 1747119529,
    "sender": 1688854256622049,
    "referid": 0,
    "app_info": "1CC983A50ABF48D1BCF7273B1939EE94",
    "room_conversation_id": "10882195777349793",
    "readuinscount": 0,
    "msg_id": 1015064,
    "msgtype": 1022
}
```


---

## 群管理员变动通知






    
**简要描述：** 

- 群管理员变动通知

**返回类型：** 
- ` 115007 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "flag": 83886080, 
    "receiver": 0, 
    "sender_name": "", 
    "is_room": 1, 
    "server_id": 12984113, 
    "send_time": 1687685690, 
    "admin_vid": [
        1688853788065293, 
        1688853788065294
    ], 
    "sender": 1688853790599424, 
    "referid": 0, 
    "app_info": "CAQQupTgpAYYgNKQ0o6AgAMg1ZPK/gI=", 
    "room_conversation_id": "10696052955012996", 
    "readuinscount": 0, 
    "msg_id": 1129753, 
    "msgtype": 1043
}

```


---

## 群解散通知





    
**简要描述：** 

- 群解散通知

**返回类型：** 
- ` 115008 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
	"flag": 16777216,
	"receiver": 0,
	"sender_name": "",
	"is_room": 1,
	"server_id": 15318223,
	"send_time": 1687707487,
	"sender": 1688855749266556,
	"referid": 0,
	"app_info": "1C55A1242928401A8C28EF3E455EB0F3@foreign.dismiss",
	"room_conversation_id": "10838966791521282",
	"readuinscount": 0,
	"msg_id": 1014863,
	"msgtype": 1023
}

```


---

## 群变动通知




    
**简要描述：** 

- 群变动通知 群成员新增删除

**返回类型：** 
- ` 115009 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "flag": 83886080,
    "receiver": 0,
    "sender_name": "",
    "is_room": 1,
    "server_id": 7129903,
    "issync": false,
    "send_time": 1721210833,
    "sender": 1688855587446404,
    "referid": 0,
    "app_info": "yT9Gw89sQPursHO",
    "room_conversation_id": "10767918571905215", //变动的群id 根据这个id去拿群成员列表 去对比
    "readuinscount": 0,
    "msg_id": 1009812,
    "msgtype": 2118
}

```


---

