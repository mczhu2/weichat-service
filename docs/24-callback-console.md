# 24-callback-console

## 控制台下发群发客户群任务消息



    
**简要描述：** 

- 控制台下发群发客户群任务消息

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "flag": 0,
    "receiver": 0,
    "msgid": "112767170301410738",
    "sender_name": "",
    "is_room": 1,
    "server_id": 7122805,
    "title": "33通知你群发消息给客户群",
    "content": {
        "listdata": [
            {
                "now_cnt": 0,
                "total_cnt": 0,
                "time_stamp": 1720690464,
                "keywords": [

                ],
                "id": 112767170301410738,
                "seq": 12462094,
                "content": [
                    {
                        "atList": [

                        ],
                        "content": "sadfasd"
                    },
                    {
                        "file_id": "306d02010204663xxxx393962353366346336393335653239636566350203103800020300c6d004000201010201000400",
                        "aes_key": "716366626C6B7261746D6B6378796F6B",
                        "width": 358,
                        "is_hd": 1,
                        "file_size": 50884,
                        "md5": "bdc884a5c545699b53f4c6935e29cef5",
                        "height": 457
                    }
                ],
                "msg_send_vids": [

                ]
            }
        ],
        "is_end": false,
        "seq": 0
    },
    "issync": false,
    "send_time": 1720690467,
    "sender": 10167,
    "referid": 0,
    "app_info": "KHGSC_1688855587446404_112767170301410738_n",
    "readuinscount": 0,
    "msg_id": 1009446,
    "msgtype": 573
}

```

---

## 控制台群发客户消息任务下发



    
**简要描述：** 

- 控制台群发客户消息任务下发

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "flag": 0, 
    "receiver": 0, 
    "msgid": "109948395468847538", 
    "sender_name": "", 
    "is_room": 0, 
    "server_id": 12883270, 
    "title": "但是发射点通知你群发消息给客户", 
    "send_time": 1677679375, 
    "sender": 10067, 
    "referid": 0, 
    "app_info": "KHGSC_1688853790599424_109948395468847538", 
    "readuinscount": 0, 
    "msg_id": 1115206, 
    "msgtype": 573
}

```


---

## 朋友圈控制台下发



    
**简要描述：** 

- 朋友圈控制台下发

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "flag": 536870912, 
    "receiver": 0, 
    "sender_name": "", 
    "is_room": 0, 
    "server_id": 12884848, 
    "type": 2, 
    "title": "但是发射点通知你发表内容到客户的朋友圈", 
    "content": "朋友圈内容啊啊啊", 
    "sid": 7217451568562415000, 
    "send_time": 1680443894, 
    "sender": 10086, 
    "author_vid": 1688853790599424, 
    "referid": 0, 
    "app_info": "7217451568562414624_new", //要发送的id 传new_前的数字 自己分割一下
    "readuinscount": 0, 
    "msg_id": 1119290, 
    "msgtype": 529, 
    "seq": 1680443894083
}

```


---

