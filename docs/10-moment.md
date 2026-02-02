# 10-moment

## 获取朋友圈列表



[TOC]
    
##### 简要描述

- 发送CDN图片消息

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/GetSnsList
  
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
    "uuid":"168885379xxxx",
    "seq":-1,
    "limit":10
}
```

##### 返回示例 

``` 
{
    "data": [
        {
            "flag": 0,//0是未删除  1是被删除
            "task_sid": 0,
            "postid": "CIOACBDL4reugTIYhL33qpWAgAMgDg==",
            "media": [ //图片视频
                {
                    "size": 90092,
                    "width": 600,
                    "video_len": 0,
                    "store_type": 0,
                    "type": 1, //文件类型 1 图片 2视频 3视频封面
                    "fileid": "*1*viLte4iMW9/VC5OZ/ZZ7FuE9VD+AhmsNxWKoL+WZI6sIZZBgOJs90NPaP6WefVRdiM+JCAfcHuTXGiawQgAn3rGdHaXkWIJpytUgbBR4zT1vxhvnwdgxHfmhPtzN1zYe+9+aYNHW6c5EAS1WWHb5XWTO33m2zgrLlR17Abv1fGgg546umsOe1EpNNYUFYf4V/0F50lLI3KrfzRlzup9V7KDKN2JsX2Hfm/18caLvy03+EpcXCI+jTUIVoZCZ5jdJSzzW2QhsC/NPE/QVOkE0XkhsI0+5P/nsHgqwjAEUzY89hDyxQO8BCvlgiYmsXRWlJ0x/HNBbeOsUdwq8R3vsCBGffWiJDsMZ5WYfgvb0DL7oMuUvzVxYfUoxtq3J66lmBiUZTFenlJYAmzaHqJlHWnFEMSnK/DfWOr2jiZvFKV+6z9JXn7O1yrY725OF13asLOu9pJfKQDfNbLDICpL4M7TUOguTXfyGTqo7dOR2H60=",
                    "md5": "29a647af6dc7ec08653aa37db2c5c273",
                    "height": 411
                }
            ],
            "type": 0,
            "content": "9999999",//文字朋友圈内容
            "sid": 7380268805390937952, //朋友圈唯一id
            "update_time": 1718358258161,//更新时间
            "xid": [],//是否可见列表
            "author_vid": 1688855587446404,
            "delete_comment_list": [//删除的评论列表
                {
                    "from_id": 1688855587446404,
                    "flag": 3,
                    "commentid": 99,
                    "comment": "99999",
                    "time": 1718358136,
                    "postid": "CIOACBCDrYGxgTIYhL33qpWAgAMgGw==",
                    "ref_commentid": 33,
                    "sid": 7380268805390937952
                }
            ],
            "comment_list": [//评论列表
                {
                    "from_id": 1688855587446404,
                    "flag": 2,
                    "commentid": 3,
                    "comment": "11111111",
                    "time": 1718357924,
                    "postid": "CIOACBDBufSwgTIYhL33qpWAgAMgDg==",
                    "ref_commentid": 0,
                    "sid": 7380268805390937952
                },
                {
                    "from_id": 7881302555913738,
                    "flag": 2,
                    "commentid": 33,
                    "comment": "66666",
                    "time": 1718358066,
                    "postid": "",
                    "ref_commentid": 3,
                    "sid": 7380268805390937952
                },
                {
                    "from_id": 1688855587446404,
                    "flag": 2,
                    "commentid": 67,
                    "comment": "99999",
                    "time": 1718358082,
                    "postid": "CIOACBD/iv6wgTIYhL33qpWAgAMgGA==",
                    "ref_commentid": 33,
                    "sid": 7380268805390937952
                }
            ],
			"link_info": { //朋友圈连接消息内容
                "content_url": "",//连接地址
                "title": ""//链接标题
            },
            "visible_type": 1,
            "time": 1718352736,
            "seq": 1718352736332,
            "SnsLikeInfo": []//朋友圈点赞列表
        },
        {
            "flag": 0,
            "task_sid": 0,
            "postid": "CAQQ2YOmsAYYhL33qpWAgAMg47LoAw==",
            "media": [],
            "type": 0,
            "content": "6666666",
            "sid": 7352550640457469644,
            "update_time": 1711899098735,
            "xid": [],
            "author_vid": 1688855587446404,
            "delete_comment_list": [],
            "comment_list": [],
            "visible_type": 0,
            "time": 1711899098,
            "seq": 1711899098735,
            "SnsLikeInfo": []
        }
    ],
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 获取朋友圈详情



[TOC]
    
##### 简要描述

- 获取朋友圈详情

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/GetSnsInfo
  
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
    "uuid":"xxxxxxxxxxxx",
    "sid":7380268805390937952 //朋友圈id
}
```

##### 返回示例 

``` 
{
    "data": {
        "flag": 0,
        "task_sid": 0,
        "postid": "CIOACBDL4reugTIYhL33qpWAgAMgDg==",
        "media": [ //图片视频信息
            {
                "size": 90092,
                "width": 600,
                "video_len": 0,
                "store_type": 0,
                "type": 1,//类型 图片 2视频 3视频缩略图
                "fileid": "*1*viLte4iMW9/VC5OZ/ZZ7FuE9VD+AhmsNxWKoL+WZI6sIZZBgOJs90NPaP6WefVRdiM+JCAfcHuTXGiawQgAn3rGdHaXkWIJpytUgbBR4zT1vxhvnwdgxHfmhPtzN1zYe+9+aYNHW6c5EAS1WxWOr2jiZvFKV+6z9JXn7O1yrY725OF13asLOu9pJfKQDfNbLDICpL4M7TUOguTXfyGTqo7dOR2H60=",
                "md5": "29a647af6dc7ec08653aa37db2c5c273",
                "height": 411
            }
        ],
        "type": 0,
        "content": "9999999",
        "sid": 7380268805390937952,//朋友圈id
        "update_time": 1718358258161,
        "xid": [ //可见人列表
            7881300143960291,
            7881302059975581,
            7881300621116017,
            7881299726922560,
            7881302555913738
        ],
        "author_vid": 1688855587446404,
        "delete_comment_list": [//删除的评论列表
            {
                "from_id": 1688855587446404,
                "flag": 3,
                "commentid": 99,
                "comment": "99999",
                "time": 1718358136,
                "postid": "CIOACBCDrYGxgTIYhL33qpWAgAMgGw==",
                "ref_commentid": 33,
                "sid": 7380268805390937952
            }
        ],
        "comment_list": [//当前评论列表
            {
                "from_id": 1688855587446404,
                "flag": 2,
                "commentid": 3,
                "comment": "11111111",
                "time": 1718357924,
                "postid": "CIOACBDBufSwgTIYhL33qpWAgAMgDg==",
                "ref_commentid": 0,
                "sid": 7380268805390937952
            },
            {
                "from_id": 7881302555913738,
                "flag": 2,
                "commentid": 33,
                "comment": "66666",
                "time": 1718358066,
                "postid": "",
                "ref_commentid": 3,
                "sid": 7380268805390937952
            },
            {
                "from_id": 1688855587446404,
                "flag": 2,
                "commentid": 67,
                "comment": "99999",
                "time": 1718358082,
                "postid": "CIOACBD/iv6wgTIYhL33qpWAgAMgGA==",
                "ref_commentid": 33,
                "sid": 7380268805390937952
            }
        ],
        "link_info": {
            "content_url": "",
            "title": ""
        },
        "visible_type": 1,
        "time": 1718352736,
        "seq": 1718352736332,
        "SnsLikeInfo": [ //点赞列表
              {
               "xid": 1688855587446404, //点赞人
               "time": 1718359990,//时间
               "postid": "",
               "sid": 7380268805390937952
           }
        ]
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 发送朋友圈



[TOC]
    
##### 简要描述

- 发送朋友圈

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/PostPyq
  
##### 请求方式
- POST 

- ContentType:"application/json"

##### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uuid |是  |String |每个实例的唯一标识，根据uuid操作具体企业微信|
|vid |否  |数组长整型 |可见人数组空或者[] 则全部可见|

- 所有的图片 视频 都走大文件上传 别走cdn上传 要走大文件上传！！！！！！！！！！！！！！！！！！！！！！！！！！！！
##### 请求示例 发送视频朋友圈

``` 
{
    "uuid":"2b0863724106a1160212bd1ccf025295",
    "content":"测试朋友圈发送",
    "file_content":
    [
        {
            "type":3, //视频图片类型 ，发送视频朋友圈这个type3是必带的 3 2 3 2这种不能只单发一个2 type1 是图片 视频只能发送一个一次请求里面只能一个3 2  多了无效
            "fileid":"*1*FdYBcjYlkXxMbjhLq4gaskUndl0tSn3NM6NuCii4NJEfibEUg0Lm7Jwk7L1JkWasl7MM/IeH5D0dbmPKXsLKO+dtUbrvw0Zjq/G6f2wXZf4LQQBRUkyH2ZnUAMzbSxEQ0QlQB58503KT0OETQadW/NxMm8=",
            "md5":"981d4320d0c4af27774c2d6de31781e9",
            "size":24389,
            "width":200,
            "height":200
        },
        {
            "type":2,//视频类型
            "fileid":"*1*FdYBcjYlkXiMuyxMWndBkjKgrHOcp641+X4L/APYAtqtu+tSub3EHKqQtivTVbf/qq/fr9Rotm9xfMID0c//EXclKcthkIwDJgSSlzmp1m9kfA8TyA14zfw+JZ+cFTobDmXwbhZGIHxS6H5zVCHz7703epfi0D2Nm1o119dfpa3gT5oC4xwfPbdDLdnwvqlDVIxSqDVCZfc+A+EQCr4YzavVPtzoKeS6ZDkFIJNN52G1Q3DNt/Q/ATaCNoOXiUyT8EIF2vHQ=",
            "md5":"7dc7b1a27f9aa62c26b0790cc9cd859b",
            "videoLen":4, //视频时常
            "size":1592424
        }
    ],
    "vids":[
        7881301488924502
    ]
}
```

##### 请求示例  发送图片朋友圈

``` 
{
    "uuid":"2b0863724106a1160212bd1ccf025295",
    "content":"测试朋友圈发送",
    "file_content":
    [
		{
            "type":1, //图片类型可以多个 但是不能和2 3 共存
            "fileid":"*1*FdYBcjYlkXxMbjhLq4gaskUndl0tSn3NM6NuCii4NJEfibEUg0Lm7Jwk7L1JkWasl7MM/IeH5D0dbmPKXsLKO+dtUbrvw0Zjq/G6f2wXZf4LQQBRUkyH2ZnUAMzbSxEQ0QlQB58503KT0OETQadW/NxMm8=",
            "md5":"981d4320d0c4af27774c2d6de31781e9",
            "size":24389,
            "width":200,
            "height":200
        }
    ],
    "vids":[
        7881301488924502
    ]
}
```
##### 请求示例  发送连接朋友圈

``` 
{
    "uuid":"2b0863724106a1160212bd1ccf025295",
    "content":"测试朋友圈发送",
    "file_content":
    [
		{
            "type":1, //连接封面图
            "fileid":"*1*FdYBcjYlkXxMbjhLq4gaskUndl0tSn3NM6NuCii4NJEfibEUg0Lm7Jwk7L1JkWasl7MM/IeH5D0dbmPKXsLKO+dtUbrvw0Zjq/G6f2wXZf4LQQBRUkyH2ZnUAMzbSxEQ0QlQB58503KT0OETQadW/NxMm8=",
            "md5":"981d4320d0c4af27774c2d6de31781e9",
            "size":24389,
            "width":200,
            "height":200
        }
    ],
    "vids":[
        7881301488924502
    ],
    "link_info":{ //链接朋友圈
        "title":"标题测试啊啊啊啊啊啊啊啊",
        "content_url":"https://www.baidu.com/?tn=02003390_10_hao_pg"
    }


}
```
##### 请求示例  发送文字朋友圈

``` 
{
    "uuid":"2b0863724106a1160212bd1ccf025295",
    "content":"测试朋友圈发送",
    "file_content":[],
    "vids":[]
}
```

##### 返回示例 

``` 
{
    "data": {
        "sid": 7553604806482115006,
        "author_vid": 1688857368772759,
        "seq": 1758710669314,
        "time": 1758710669,
        "content": "测试123123",
        "media": [
            {
                "fileid": "*1*F34hSNMREYF+BNgTfNtYo7Lg5/va+EoTPkyESrZSpwvPAoJpAras/Zrv9SF1o4pu4bOgJMG3D+2VaBOiBAtl38Ydm6+Z9kpKGVJm7e6IoGsZMUsu/SYMOsIowWjlehHbHXex+9V3yBMD+LgM9XD9j/FVhrrAOkGGa3d3hv10YohZqKtksQaJnE/EcATcv9yUKukgaOM//VtrPLdjS8uTf4NlblRUYLA/WWbgm6xhH0JpBbb2FaF33nQl0viUJFqaK2OJy5Lh3FhI4mOtecXgMFVfcJ+wL3ecfE1D9Duz0XwbPANmeAuaGugKM7VAE6RJ5Win18tN11ScE4zbrKEYwJcbXCw8qV1zk4wqlfTufEimE1cGNNlUu95FKjnfrEml702uNTaCtoY9gdDSMd/JAd0VZ8P3j8dRV1r6l1MRfdu60Yzf34LY2vz7S9THWY2cwczOelofeEM43yudTarHtJUIM7j1pM50R6ELInOogSw=",
                "md5": "626f56cc7d16f5a94017442c67975f93",
                "width": 0,
                "height": 0,
                "store_type": 0,
                "type": 2,
                "video_len": 3,
                "size": 611560
            },
            {
                "fileid": ":*1*F34hSNMREYF+BNgTfNtYo7Lg5/va+EoTPkyESrZSpwvPAoJpAras/Zrv9SF1o4pu4bOgJMG3D+2VaBOiBAtl38d4/yawYresryehdtMQYWmfeWvA3EiEE8E2bRGyR9hjiosKaQg+1Ib5Ql6X/3PrSz0ccjFMAgpyIOm2cPTYpmX8dD2+iD1ktWWRM2VSuxaLS1pJ1MIkedavlCGhtODA4QLKoSs8glwWM7F4jk+VDwdKdaLaQihkTGin285EzmS25M4haTo/Cm9Nur37WMQsV7TkrnZVmMfD9AvGmoRmXFpAs70oTypq4FU83UGFuKvExV4judYel3r9Sr4nAIONZ5QsqvV6cwplC8rmO2oz5Mqu8muPXnWYX7jEe9f7QP10DakupWUuWGAR2BJHzn/n266QL/mrrj3xt3ycrBWXoW0fsf34HGqsYMWPmKDnMjlfT9s+u83oCIJYS9SEU/fbSPTrwlR0lGzKCQLUqwFJw+8=",
                "md5": "9c333d9b82227489ad4de467d4f21307",
                "width": 0,
                "height": 0,
                "store_type": 0,
                "type": 3,
                "video_len": 0,
                "size": 24989
            }
        ],
        "comment_list": [],
        "postid": "CIOACBCzvszalzMYl4Gr/JuAgAMgEA==",
        "flag": 0,
        "update_time": 1758710669314,
        "delete_comment_list": [],
        "xid": [
            7881302555913738
        ],
        "type": 0,
        "task_sid": 0,
        "link_info": {
            "title": "",
            "content_url": ""
        },
        "poiInfo": {
            "city": "",
            "latitude": "",
            "longitude": "",
            "poiAddress": "",
            "poiClassifyId": "",
            "poiClassifyType": "",
            "poiClickableStatus": "",
            "poiName": "",
            "poiScale": ""
        },
        "visible_type": 0,
        "SnsLikeInfo": []
    },
    "errcode": 0,
    "errmsg": ""
}
```








---

## 删除朋友圈



[TOC]
    
##### 简要描述

- 删除朋友圈

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/DeleteSns
  
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
    "uuid":"1688855587446404",
    "sid":7380268805390937952 //朋友圈id
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

## 发送朋友圈评论



[TOC]
    
##### 简要描述

- 发送朋友圈评论

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/CommentSnsReq
  
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
    "uuid":"1688857826547606",
    "ref_commentid":0, //评论id 默认传0  如果需要回复别人的评论则传别人评论的id 从获取朋友圈列表中获取
    "sid":7297137800987783140, //朋友圈消息对应的id
    "comment":"哈个头" //评论内容
}
```

##### 返回示例 

``` 
{
    "data": {
        "commentid": 0
    },
    "errcode": 0,
    "errmsg": "ok"
}
```








---

## 删除朋友圈评论



[TOC]
    
##### 简要描述

- 删除朋友圈评论

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/DeleteSnsComment
  
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
    "uuid":"22222swwwwwwwwwwwwwww",
    "sid":7380268805390937952,//朋友圈id
    "commentid":99//评论id
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

## 朋友圈点赞或者取消点赞



[TOC]
    
##### 简要描述

- 朋友圈点赞或者取消点赞

##### 请求URL
- ` http://47.94.7.218:8083/wxwork/LikeOrUnLikeSns
  
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
    "uuid":"1688855587446404",
    "operation":1,//0点赞  1取消点赞
    "sid":7380268805390937952 //朋友圈id
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

