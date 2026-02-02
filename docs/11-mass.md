# 11-mass

## 确认群发消息



[TOC]
    
##### 简要描述

- 确认群发消息

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/oksendgroup
  
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
    "uuid":   "d85c7605-ae91-41db-aad5-888762493bac",
    "vids":[],//如果是客户群群发需要填群id
    "msgid":109778712984080799,
    "isroom":0//0 联系人群发 1客户群群发
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

## 获取未发送群发列表




[TOC]
    
##### 简要描述

- 获取未发送群发列表

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/GetGroupMsgList
  
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
    "uuid":"1753cdff-0501-42fe-bb5a-2a4b9629f7fb"
}
```

##### 返回示例 

``` 
{
    "data": {
        "listdata": [
            {
                "now_cnt": 109948045799913290,
                "total_cnt": 109948045799913290,
                "time_stamp": 1677674038,
                "id": 109948045799913290,
                "seq": 9684990,
                "content": [
                    {
                        "atList": [],
                        "content": "dfgdsfgdsfdfgdf"
                    }
                ]
            },
            {
                "now_cnt": 109941534663399259,
                "total_cnt": 109941534663399259,
                "time_stamp": 1677574686,
                "id": 109941534663399259,
                "seq": 9684986,
                "content": [
                    {
                        "atList": [],
                        "content": "测试服答复"
                    }
                ]
            },
            {
                "now_cnt": 109773094713924640,
                "total_cnt": 109773094713924640,
                "time_stamp": 1675004497,
                "id": 109773094713924640,
                "seq": 9632238,
                "content": [
                    {
                        "atList": [],
                        "content": "wsdf"
                    }
                ]
            },
            {
                "now_cnt": 109773089454890363,
                "total_cnt": 109773089454890363,
                "time_stamp": 1675004416,
                "id": 109773089454890363,
                "seq": 9632235,
                "content": [
                    {
                        "atList": [],
                        "content": "sdfasfdsa"
                    },
                    {
                        "file_id": "3069020102046230600201000204ea44290002031e90380204bcdf892b020463d689fe0434333933303333353438385f31303936333031325f633936326633663132633063623033623038303637386431646330303665333702010002030484e004000201010201000400",
                        "aes_key": "76656B64737069716477627074706D63",
                        "width": 1080,
                        "is_hd": 1,
                        "file_size": 296144,
                        "md5": "c962f3f12c0cb03b080678d1dc006e37",
                        "height": 1920
                    }
                ]
            },
            {
                "now_cnt": 109773020986125565,
                "total_cnt": 109773020986125565,
                "time_stamp": 1675003372,
                "id": 109773020986125565,
                "seq": 9632232,
                "content": [
                    {
                        "atList": [],
                        "content": "dfgdsfsd"
                    },
                    {
                        "file_id": "306b020102046430620201000204ea44290002031e9038020411c6f46d020463d685dd0436333933303333353438385f313236343931323537335f633936326633663132633063623033623038303637386431646330303665333702010002030484e004000201010201000400",
                        "aes_key": "6A6B7869747A6B7A786D687074766C6F",
                        "width": 1080,
                        "is_hd": 1,
                        "file_size": 296144,
                        "md5": "c962f3f12c0cb03b080678d1dc006e37",
                        "height": 1920
                    }
                ]
            },
            {
                "now_cnt": 109766362330255595,
                "total_cnt": 109766362330255595,
                "time_stamp": 1674901768,
                "id": 109766362330255595,
                "seq": 9632229,
                "content": [
                    {
                        "atList": [],
                        "content": "dssssssssssssssssssssssssssssssssssssss"
                    }
                ]
            },
            {
                "now_cnt": 109766043162686149,
                "total_cnt": 109766043162686149,
                "time_stamp": 1674896899,
                "id": 109766043162686149,
                "seq": 9632094,
                "content": [
                    {
                        "atList": [],
                        "content": "66666666"
                    },
                    {
                        "file_id": "306b02010xxx53033383366303837383061646664616463303463633064653730383430330201000203009c5004000201010201000400",
                        "aes_key": "636D6469766474616B7069726A75646B",
                        "width": 430,
                        "is_hd": 1,
                        "file_size": 40015,
                        "md5": "4e0383f08780adfdadc04cc0de708403",
                        "height": 430
                    },
                    {
                        "video_duration": 4,
                        "url": "xxxx.MP4",
                        "file_size": 753510,
                        "video_id": "*1*FdYBcjYlkXiMuyOJFNhjuPoL+N0IQ71yJLzTlAdCe6nbfvJLebsgj/ytbO6TqsY1RkecRfuAMmqgOc6eH6J7ZOgfIuVFCSGU5/ijLQqW+8aJhdbF7ZlxxxxQryZq86Jb0oOFkNcXBJAPRTr85+ajErVTWglflaQHyldt2+mJaw3SScghJfttaQwXFo04995XK48sQGCyt3V4Q3EPBbUMl4r+0p/eCg5FMci6VrLV7ty6NUn3r59YENvwMJZtBA==",
                        "md5": "3b9399f4489989e2bb886fe35fce4df4",
                        "preview_img_url": {
                            "validUtf8": true,
                            "empty": false
                        }
                    },
                    {
                        "file_name": "淘米帐号-944398623.txt",
                        "file_id": "*1*h1tVdJzfxrcV0qtOQr+bXTtdd+Jo52scfl9qb4tRUkt6VjU7ffDjqIScr3eDrrQCLI//R3/kbqF5buGuXgeLiK2ZtzFWaxjLqJZdNAQCSqRZgi0Q+FZU3kM7YIvreZGnXc/xo8onDkSoZTklUMPbrthD//GD2nJqjYrhshpS9KQrhjWrFzRckkiXYZmL7mtoIXovH1WaUCuo9WNLsCPcaCzHsgTjDcmNLybb5iwlc3pmW2wdC6PSyqiRUsSyDnzCkoZgruGhjmrz6xrHix8exfisj4GUm6tYX5f+ULmuwjGC3h+M/xjqBxKKlbeYNPK0yQ2xE+47+2cII5vRcnU38HH+iBjz4gJ1YcpME7CORht+Um98ot+1aYCahRlmzNktMBZFxoP954sNRCzP750eYwfQFG9qi73jZCL771aJ1ivwHE4zwk572aDcJjpN2gXL2NIdqW4WL5bK5kdvwns0Qg==",
                        "file_size": 36,
                        "md5": "d6ae2c30a68512931b3add44249276d6"
                    },
                    {
                        "des": "全球领先的中文搜索引擎、致力于让网民更便捷地获取信息，找到所求。百度超过千亿的中文网页数据库，可以瞬间找到相关的搜索结果。",
                        "headImg": "https://dss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/topnav/newfanyi-da0cea8f7e.png",
                        "state": "0",
                        "title": "百度一下，你就知道",
                        "url": "https://www.baidu.com/?tn=62095104_17_oem_dg"
                    },
                    {
                        "thumbFileId": "3063020102045c305a0201000202271002031e90380204f0c4f46d020463d4e602043131303030305f313034303034373031375f3836643532623436613035323431376566653330353334623966663936356465020100020221a004000201010201000400",
                        "pagepath": "https://www.baidu.com/?tn=62095104_17_oem_dg",
                        "size": 8603,
                        "appid": "wx82bb8b42c0abe9dc",
                        "thumbAESKey": "796B6F626978726F66726D657A706D62",
                        "title": "法大大丨微合同",
                        "thumbMD5": "86d52b46a052417efe30534b9ff965de",
                        "username": "gh_f7039110b029@app",
                        "weappIconUrl": "http://wx.qlogo.cn/mmhead/Q3auHgzwzM68v7IicvvtXPvKT8Na7Rdj7mMVWU4FQe8Agt4LkENK8eA/0",
                        "desc": "疫情当前，法大大微合同助力企业远程办公。无需见面，用户即可高效签署合法合规的电子合同并实现电子数据文件区块链存证。"
                    }
                ]
            },
            {
                "now_cnt": 108341119486439178,
                "total_cnt": 108341119486439178,
                "time_stamp": 1653154289,
                "id": 108341119486439178,
                "seq": 9632091,
                "content": [
                    {
                        "atList": [],
                        "content": "3 "
                    }
                ]
            },
            {
                "now_cnt": 108341021843586549,
                "total_cnt": 108341021843586549,
                "time_stamp": 1653152799,
                "id": 108341021843586549,
                "seq": 9632002,
                "content": [
                    {
                        "atList": [],
                        "content": "23423423423"
                    }
                ]
            },
            {
                "now_cnt": 108340943561622329,
                "total_cnt": 108340943561622329,
                "time_stamp": 1653151604,
                "id": 108340943561622329,
                "seq": 9631999,
                "content": [
                    {
                        "atList": [],
                        "content": "dsfdsfdsfdsfds"
                    },
                    {
                        "file_id": "306b020102046430620201000204ea44290002031e9038020420c6f46d02046289176c0436333933303333353438385f313639393833383931335f3931333836366565313562313665666331313561333666343735383766306463020100020300e6f004000201010201000400",
                        "aes_key": "696C737A6A64686C6D6E676D76737A70",
                        "width": 510,
                        "is_hd": 1,
                        "file_size": 59117,
                        "md5": "913866ee15b16efc115a36f47587f0dc",
                        "height": 492
                    }
                ]
            },
            {
                "now_cnt": 108340932637657792,
                "total_cnt": 108340932637657792,
                "time_stamp": 1653151437,
                "id": 108340932637657792,
                "seq": 9631996,
                "content": [
                    {
                        "atList": [],
                        "content": "sdfdssdfsdfdsfdsf4444444444444444"
                    }
                ]
            },
            {
                "now_cnt": 108340929682243773,
                "total_cnt": 108340929682243773,
                "time_stamp": 1653151393,
                "id": 108340929682243773,
                "seq": 9631992,
                "content": [
                    {
                        "atList": [],
                        "content": "33333333333333"
                    }
                ]
            },
            {
                "now_cnt": 108340921018870081,
                "total_cnt": 108340921018870081,
                "time_stamp": 1653151260,
                "id": 108340921018870081,
                "seq": 9631989,
                "content": [
                    {
                        "atList": [],
                        "content": "电风扇犯得上"
                    }
                ]
            },
            {
                "now_cnt": 108329293169968332,
                "total_cnt": 108329293169968332,
                "time_stamp": 1652973834,
                "id": 108329293169968332,
                "seq": 9631890,
                "content": [
                    {
                        "atList": [],
                        "content": "66666666666"
                    },
                    {
                        "file_id": "306b020102046430620201000204ea44290002031e9038020479c6f46d0204628661060436333933303333353438385f313138343737353732385f6337616433613064346564626166376337653039323565646130653165393432020100020301388004000201010201000400",
                        "aes_key": "6C7079707A6F77767A746D7568766162",
                        "width": 396,
                        "is_hd": 1,
                        "file_size": 79992,
                        "md5": "c7ad3a0d4edbaf7c7e0925eda0e1e942",
                        "height": 396
                    }
                ]
            }
        ],
        "is_end": true,
        "seq": 0
    },
    "errcode": 0,
    "errmsg": "ok"
}
```







---

## 客户群发送任务获取要发送的客户群列表



[TOC]
    
##### 简要描述

- 客户群发送任务，获取要发送的客户群列表

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/QueryGroupSendReq
  
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
    "uuid":  "1688853790599424",
    "id":110129274704417637, //群发任务id
    "keywords":[//关键词筛选 没有填空数组
        "555"
    ]
}
```

##### 返回示例 

``` 
{
    "data": [
        10696052955013038
    ],
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 获取群发任务详细信息



[TOC]
    
##### 简要描述

- 获取群发任务详细信息

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/GetGroupMsgInfo
  
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
    "uuid": "1688853790599424",
    "id":109948045799913290,//消息id
    "seq":0,//默认
    "sendtype":0//默认
}
```

##### 返回示例 

``` 
{
    "data": {
        "listdata": [
            {
                "now_cnt": 110129274704417637,
                "total_cnt": 110129274704417637,
                "time_stamp": 1680439372,
                "keywords": [ 
                    "555"//如果是群消息则 这里是名字关键词筛选
                ],
                "id": 110129274704417637,
                "seq": 9704108,
                "content": [
                    {
                        "atList": [],
                        "content": "sadfdsafsad"
                    }
                ],
                "msg_send_vids": [
                    1688853790599424
                ]
            }
        ],
        "is_end": false,
        "seq": 0
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 朋友圈控制台任务执行



[TOC]
    
##### 简要描述

- 朋友圈控制台任务执行

##### 请求URL
- ` http://127.0.0.1:8085/wxwork/oksendsnsmsg
  
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
    "uuid":  "xxxx",
    "msgid":7217451568562414624 //朋友圈任务id
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

## 管理员获取企业成员客户列表



[TOC]
    
##### 简要描述

- 管理员获取企业成员客户列表

##### 请求URL
- ` http://127.0.0.01:8083/wxwork/MemberCustomerStat
  
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
    "uuid":"xxxxxxxxx",
    "vid":1688855xxxxx,
    "limit":500, //每次查询数量
    "start_index":0 //每次查询的下标 比如是有10个好友 这里填5就是从第五个好友开始查500个 首次填0 建议就是第一次0 第二次就是0+limit 以此类推
}
```

##### 返回示例 

``` 
{
    "data": {
        "total": 10,
        "next_start": -1,
        "userinfo_list": [
            {
                "relation_flag": 1,
                "follow_vid": 1688855587446404,
                "predecessor_vid": 0,
				"sex": 1,
                "update_time": 1688458415,
                "create_time": 1688458415,
                "nickname": "郑海xxxx-xxxxx",
                "avatar": "https://wework.qpic.cn/wwpic/389095_y_VHiyFwTgGKg6W_1673502830/0",
                "customer_id": 1688851218830253,
                "seq": 12358696,
                "shift_time": 1688458415
            },
            {
                "relation_flag": 1,
                "follow_vid": 1688855587446404, //公司内部人员id
                "predecessor_vid": 0,
				"sex": 1,
                "update_time": 1716276825,
                "create_time": 1716276825,
                "nickname": "微xxxxx",
                "avatar": "https://wework.qpic.cn/wwpic3az/89433_hjSyi_cSQO6LWwY_1702881235/0",
                "customer_id": 1688854336550791, //客户id
                "seq": 12437114,
                "shift_time": 1716276825
            }
        ],
        "last_page_max_id": 0
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

