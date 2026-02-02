# 21-callback-message

## 文本消息接收



    
**简要描述：** 

- 文本消息接收

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "flag": 16777216,
	"kf_id":111111111, //客服id 只有客服消息接收才会有值否则就是0
    "receiver": 1688855587446404, //接收人
    "sender_name": "",
    "is_room": 0,
    "server_id": 7130717,
    "content": "32423423",
    "issync": false,
    "send_time": 1724034152,
    "sender": 7881302555913738,
    "referid": 0,
    "app_info": "3304183318011621608",
    "readuinscount": 0,
    "msg_id": 1011720,
    "msgtype": 2, //消息类型 0 和 2文本消息
    "atList": [

    ]
}

```


---

## GIF表情消息接收



    
**简要描述：** 

- gif 表情消息接收

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "flag": 16777216,
    "receiver": 1688855587446404,
    "sender_name": "",
    "is_room": 0,
    "server_id": 7130718,
    "url": "https://wework.qpic.cn/wwpic3az/wwwx_c1f24912f3d283214fd7bf5da3c3b9cd/0",
    "issync": false,
    "send_time": 1724034243,
	"width": 100,
	"height": 100,
    "sender": 7881302555913738,
    "referid": 0,
    "EmotionType": "EMOTION_DYNAMIC",
    "app_info": "7678571470421336167",
    "readuinscount": 0,
    "msg_id": 1011722,
    "msgtype": 104
}

```


---

## 小程序消息



    
**简要描述：** 

- 小程序消息

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
	"thumbFileId": "306b020102046430620201000204bf366e0a02031e90380204dfc6f46d0204639acc410436333230383031353337305f313531323736303031375f38326163316362353632346136313035393265643566363861353963326666650201000203009c8004000201010201000400",
	"flag": 16777216,
	"receiver": 1688853790599424,
	"sender_name": "",
	"is_room": 0,
	"server_id": 12779516,
	"title": "铁友火车票",
	"appName": "小程序名称",
	"thumbMD5": "82ac1cb5624a610592ed5f68a59c2ffe",
	"pagepath": "pages/home/index.html",
	"send_time": 1671089217,
	"size": 40058,
	"sender": 7881302555913738,
	"referid": 0,
	"appid": "wx45dff5234240ad90",
	"thumbAESKey": "7063756277636571667576656873616A",
	"app_info": "from_msgid_2000643230453161151",
	"readuinscount": 0,
	"msg_id": 1108389,
	"msgtype": 78,
	"username": "gh_c4a2a98a7366@app",
	"weappIconUrl": "http://mmbiz.qpic.cn/mmbiz_png/BMAjdIR5qwChFiaRnxUTiadJicGwMpqchXVdfQQ67tHTzfgd0gkibupgeOKtHtyE9m9SZopHXxcpsSicWxzLKXwk0qA/640?wx_fmt=png&wxfrom=200",
	"desc": ""
}

```


---

## 连接消息



    
**简要描述：** 

- 连接消息

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
	"authkey": "",
	"flag": 16777216,
	"receiver": 1688853790599424,
	"headImg": "https://mmbiz.qpic.cn/mmbiz_jpg/0oBxv6CfzTpWI5gwRZg3PTSmHNkCI6sfuqge5woWBCEMZKwWiaOyvBseEvq3hPNkc8h5Xe20nAnDwv0ictew7VWQ/300?wxtype=jpeg&wxfrom=0",
	"sender_name": "",
	"is_room": 0,
	"server_id": 12779517,
	"title": "不限户籍！济南新一轮300元现金补贴来了！",
	"url": "http://mp.weixin.qq.com/s?__biz=Mzk0ODE5OTg3OA==&mid=2247486862&idx=1&sn=341e039070001e40b35e3c3a221ec4b1&chksm=c36a0527f41d8c317008aa6ef7699ee501d8c139a02234c85e2bfe3345b9501f4780dd4d0852#rd",
	"send_time": 1671089268,
	"des": "美味君的福利  又双叒叕来啦！",
	"size": 0,
	"sender": 7881302555913738,
	"referid": 0,
	"app_info": "from_msgid_1491271774350930187",
	"readuinscount": 0,
	"state": "1",
	"aeskey": "",
	"msg_id": 1108392,
	"msgtype": 13
}

```


---

## 视频号消息



    
**简要描述：** 

- 视频号消息

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
	"thumb_url": "http://wxapp.tc.qq.com/251/20350/stodownload?encfilekey=Cvvj5Ix3eexKX1zo1IZZBrQomawdVfSQH1uu2U31EqHhx1QOrTTdw1RUwEmSiaZCAFnM77JFJoYuQpiahtJehYibzBtzKlcpKIsrkmQU4Nlg8BaiaJyqM7yK72HRXGxfY8UfKg0vaEbTk8PNrDRGP4ibwUWOK8gDwn7BXkAjAknahGY0&adaptivelytrans=0&bizid=1023&dotrans=0&hy=SH&idx=1&m=598b0abead239f31dc34b9b24a7698a1&token=x5Y29zUxcibC9gkDvObknpjEKmTiahrwGfE61uZibzqBZTaibGrWlIj3gSoVWHBlK0T0x982aQDck4I&finder_expire_time=1671694153&finder_eid=export%2FUzFfAgtgekIEAQAAAAAAhKkzEi37bwAAAAstQy6ubaLX4KHWvLEZgBPEgKM8Q3FrDPuBzNPgMIKn8ubxw9bwD9zYyTKt3gKI",
	"cover_url": "http://wxapp.tc.qq.com/251/20350/stodownload?encfilekey=Cvvj5Ix3eexKX1zo1IZZBrQomawdVfSQH1uu2U31EqHhx1QOrTTdw1RUwEmSiaZCAFnM77JFJoYuQpiahtJehYibzBtzKlcpKIsrkmQU4Nlg8BaiaJyqM7yK72HRXGxfY8UfKg0vaEbTk8PNrDRGP4ibwUWOK8gDwn7BXkAjAknahGY0&adaptivelytrans=0&bizid=1023&dotrans=0&hy=SH&idx=1&m=598b0abead239f31dc34b9b24a7698a1&token=x5Y29zUxcibC9gkDvObknpjEKmTiahrwGfE61uZibzqBZTaibGrWlIj3gSoVWHBlK0T0x982aQDck4I&finder_expire_time=1671694153&finder_eid=export%2FUzFfAgtgekIEAQAAAAAAhKkzEi37bwAAAAstQy6ubaLX4KHWvLEZgBPEgKM8Q3FrDPuBzNPgMIKn8ubxw9bwD9zYyTKt3gKI",
	"flag": 16777216,
	"receiver": 1688853790599424,
	"extras": "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",
	"sender_name": "",
	"is_room": 0,
	"avatar": "http://wx.qlogo.cn/finderhead/ver_1/y4ZA3geMVqNNoaVOYynb1ib0JFcAqdjZ2y4QgwpVW0Lb5mrq3c2qibBA9SUaw2rB9j9fFTx6ae4p6Wnl4hSmUn0ica9RsQe29yzzmftjNGpHzs/0",
	"server_id": 12779518,
	"url": "https://channels.weixin.qq.com/web/pages/feed?eid=export%2FUzFfAgtgekIEAQAAAAAAhKkzEi37bwAAAAstQy6ubaLX4KHWvLEZgBPEgKM8Q3FrDPuBzNPgMIKn8ubxw9bwD9zYyTKt3gKI",
	"send_time": 1671089353,
	"sender": 7881302555913738,
	"referid": 0,
	"nickname": "云剪辑.",
	"objectId":"14479811159081949639",
	"objectNonceId":"1113158575529036062_0_0_0_0_0",
	"app_info": "from_msgid_721532404981185807",
	"readuinscount": 0,
	"msg_id": 1108395,
	"msgtype": 141,
	"desc": "#余罪2 余儿康复训练"
}

```


---

## 名片消息



    
**简要描述：** 

- 名片消息

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
	"flag": 16777216,
	"receiver": 1688853790599424,
	"headImg": "http://wx.qlogo.cn/mmhead/X4XEGYefSBTTPqheGS9crfw2OeJW9Yb4IDfWYod02btPnsJtIXrGEQ/0",
	"sender_name": "",
	"is_room": 0,
	"server_id": 12779520,
	"send_time": 1671089416,
	"sender": 7881302555913738,
	"referid": 0,
	"nickname": "huyang",
	"app_info": "from_msgid_997448547889589003",
	"readuinscount": 0,
	"msg_id": 1108401,
	"msgtype": 41,
	"enterpriseName": "微信",
	"corp_id": 0,
	"username": 7881299726922560
}

```


---

## 位置消息



    
**简要描述：** 

- 位置消息

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
	"detailed_address": "xxx广场1号楼(xxxxxx路东50米)",
	"flag": 16777216,
	"address": "xxxxxxxxxx东路",
	"receiver": 1688853790599424,
	"latitude": 363.664135,
	"sender_name": "",
	"is_room": 0,
	"server_id": 12779521,
	"send_time": 1671089496,
	"sender": 7881302555913738,
	"referid": 0,
	"app_info": "from_msgid_4430967334562546003",
	"readuinscount": 0,
	"msg_id": 1108405,
	"msgtype": 6,
	"longitude": 11.148247
}

```


---

## 外部联系人图片消息



    
**简要描述：** 

- 外部联系人图片消息（个微发的）

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
	"flag": 16777216,
	"receiver": 1688853790599424,
	"sender_name": "",
	"is_room": 0,
	"server_id": 12779523,
	"file_size": 7982507,
	"send_time": 1671089597,
	"sender": 7881302555913738,
	"referid": 0,
	"file_id": "https://imunion.weixin.qq.com/cgi-bin/mmae-bin/tpdownloadmedia?param=v1_9402dda5b09845e753c80d85b8fdbbd9fec7a526c9da0b0d88ac1b7a74a4c4c1dd9b982527532a112af12c7a8a624b8117d171397ec6d0b94ca02c80bc5633357326cebc5d348c35625b2e6980fe6c9f3ecea79c3f22b5c4ab0c2d6c59d86e403f95bfcb1ffe7311b67cc0d7b31d4dd349ce38628e7c95561b036c6b787d6a04460b7146c54651e4ce73c5c31d5519936614efc9fb510d13f03229fac07d847626ed6dcf4673cd7312763fd73f763f208b2fa9e388e90fba6fc8dbb777bba4b94143d39f09a4e97a807a10195c4e871f8065321f8e9355304a669ac1deafc333399b2d06d072390ac980fcc80c4fa764ae59dd01b16dee0384df3073cdf28744",
	"openim_cdn_authkey": "v1_9402dda5b09845e753c80d85b8fdbbd9fec7a526c9da0b0d88ac1b7a74a4c4c1e5230c9b1626f74f55ffe8c3660d9d7aa62fe7376a0dddbb8f6575d040e9f369",
	"width": 0,
	"app_info": "from_msgid_9211250126199164434",
	"readuinscount": 0,
	"aes_key": "a2078ba39c47bfcfdbd9ec2574c2e134",
	"msg_id": 1108410,
	"msgtype": 101,
	"md5": "714d6b8ee6ca20fa6143338ba9c1c69a",
	"height": 0
}

```


---

## 外部联系人文件消息



    
**简要描述：** 

- 外部联系人文件消息

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
	"flag": 16777216,
	"receiver": 1688853790599424,
	"file_name": "dump.rdb",
	"sender_name": "",
	"is_room": 0,
	"server_id": 12779529,
	"file_size": 113564,
	"send_time": 1671089980,
	"sender": 7881302555913738,
	"referid": 0,
	"file_id": "https://imunion.weixin.qq.com/cgi-bin/mmae-bin/tpdownloadmedia?param=v1_9402dda5b09845e753c80d85b8fdbbd9fec7a526c9da0b0d88ac1b7a74a4c4c1e8b38d076dc0683fbb01af67a9ebcbeab65ecdf9aa68764d4683bec9cd8e9ffe7453d9de6b915504638ab0dda6c0c10c2ba4b38a1c7366faf736656344e31c47532c5a174bc5b4c249318253a995fa8462b2868f4b63b63fa16eef6120f377252c12817e0a30db83157402b395ff793c679d7d5f1dc3dff24f147d9f3381a3170b4ec59a28235105d1eeb7f57173afffb8aecf0e69c3620e291a6a7e78e6e188709fb6550891770dbc899598f19bd2e688204a46b30e812443fb5deafbab5bf30593f3a0b01dfb920ba86be52370103d29813dbcd0d1aa170efbbc237acbe40f",
	"openim_cdn_authkey": "v1_9402dda5b09845e753c80d85b8fdbbd9fec7a526c9da0b0d88ac1b7a74a4c4c1bf9e0299514bfddca8e6db1d3f11977073255bc80fb5ec7a0512fff9548d9783",
	"app_info": "from_msgid_3239399033279654384",
	"readuinscount": 0,
	"aeskey": "b28c380313f3e8827a2740da0a1decff",
	"msg_id": 1108425,
	"msgtype": 102,
	"md5": "f506a445ee47e2232a087d628043935c"
}
```


---

## 外部联系人视频消息



    
**简要描述：** 

- 外部视频消息

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "video_duration": 4, 
    "video_height": 0, 
    "openim_cdn_ld_aeskey": "fae8cf6f653b9c92adc6faec93a6e57a", //缩略图aes
    "flag": 16777216, 
    "receiver": 1688853790599424, 
    "openim_cdn_ld_size": 4997, //缩略图大小
    "video_width": 0, 
    "sender_name": "", 
    "is_room": 0, 
    "server_id": 12884979, 
    "file_size": 311641, 
	"openim_cdn_ld_size":2546,
    "send_time": 1680526456, 
    "sender": 7881302555913738, 
    "referid": 0, 
	//fileid是视频下载地址
    "file_id": "https://imunion.weixin.qq.com/cgi-bin/mmae-bin/tpdownloadmedia?param=v1_9402dda5b09845e753c80d8x06f87792ff1795fe3b00fdafcb1f52df", 
    "openim_cdn_authkey": "v1_9402dda5b09845e75xc5785886e719d334044e189991e6b36c850334edf2427756af9b",  //视频和缩略图authkey
    "aes_key": "fae8cf6f653b9c92adc6faec93a6e57a", 
    "app_info": "from_msgid_64299209193941486", 
    "readuinscount": 0, 
    "msg_id": 1119632, 
    "msgtype": 103, 
    "md5": "18826ff24ce328cd4d850edfa3f8c60c", 
    "preview_img_url": "https://imunion.weixin.qq.com/cgi-bin/mmae-bin/tpdownloadmedia?param=v1_9402dda5b09845e753c80dx2809bfcf49fa67e02354a4741a5b1e" //缩略图地址
}

```


---

## 内部联系人图片消息



    
**简要描述：** 

- 内部联系人图片消息（企业微信用户）

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "innerkf_vid": 0,
    "flag": 83886080,
    "extraContent": {

    },
    "mid_img_size": 25297,
    "openim_cdn_ld_height": 290,
    "devInfo": 65537,
    "sender_name": "",
    "is_room": 1,
    "is_hd": 1,
    "openim_cdn_ld_md5": "58e3b6f7ec1aa792f1b273dd821e91d9",
    "app_info": "CIGABBCK263DBhjhs6ywkICAAyAP",
    "room_conversation_id": "10817857838351957",
    "msg_id": 1026382,
    "msgtype": 14,
    "height": 154,
    "receiver": 0,
    "thumb_img_size": 6849,
    "file_name": "1722476983013.png",
    "server_id": 7752688,
    "kf_id": 0,
    "file_size": 41505,
    "issync": false,
    "send_time": 1751870858,
    "sender": 1688854256622049,
    "openim_cdn_ld_width": 290,
    "referid": 0,
    "file_id": "306b020102046430620201000204060b19e102030f55ca020498ec1d3c0204686b6d8a042430316539333437612d376664372d346238332d386362352d3530306564363038343432380203103800020300a2300410efaf887ca055806f139409b13f35b93c0201010201000400",
    "aes_key": "5349db00f66c5000d85aa9ff3a9898ca",
    "width": 154,
    "readuinscount": 0,
    "md5": "efaf887ca055806f139409b13f35b93c"
}

```


---

## 内部联系人文件消息



    
**简要描述：** 

- 文件消息内部

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
	"flag": 16777216,
	"receiver": 7881302555913738,
	"file_name": "dump.rdb",
	"sender_name": "",
	"is_room": 0,
	"server_id": 12779532,
	"file_size": 113564,
	"send_time": 1671094399,
	"sender": 1688853790599424,
	"referid": 0,
	"file_id": "3069020102046230600201000204ea44290002030f55cb0204789f216f0204639ae07e042437336331303039352d343030342d343732342d393062622d303432326361653230363763020100020301bba00410f506a445ee47e2232a087d628043935c0201050201000400",
	"aes_key": "7e22920298d94b038fac516bcf97d3ce",
	"app_info": "CAQQ/cDrnAYYgNKQ0o6AgAMg0+HxqwE=",
	"readuinscount": 0,
	"msg_id": 1108433,
	"msgtype": 15,
	"md5": "f506a445ee47e2232a087d628043935c"
}

```


---

## 内部联系人视频消息



    
**简要描述：** 

- 内部视频消息

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "video_duration": 23, 
    "video_height": 0, 
    "openim_cdn_ld_aeskey": "", 
    "flag": 16777216, 
    "receiver": 7881302555913738, 
    "openim_cdn_ld_size": 24389, 
    "video_width": 0, 
    "sender_name": "", 
    "is_room": 0, 
    "server_id": 15319792, 
    "file_size": 1592432, 
    "send_time": 1689910819, 
    "sender": 1688855749266556, 
    "referid": 0, 
    "file_id": "30819302010204818b30818802010002045f030c7c02030f55cb0204179f216f020464b9fd4f044c4e45574944315f35663033306337633137396632313666363462396665323330303034336133325f39356636613365342d346332352d346361652d626361332d3861656630383461333539630201000203184c7004107dc7b1a27f9aa62c26b0790cc9cd859b0201040201000400", 
    "openim_cdn_authkey": "", 
    "aes_key": "64393035623864326166303932653431", 
    "app_info": "CAQQovznpQYY/JiM+JWAgAMgutTfsQ4=", 
    "readuinscount": 0, 
    "msg_id": 1018391, 
    "msgtype": 23, 
    "md5": "7dc7b1a27f9aa62c26b0790cc9cd859b", 
    "preview_img_url": ""
}

```


---

## 红包消息



    
**简要描述：** 

- 红包消息

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
	"icon_url": "http://dldir1.qq.com/qqcontacts/hongbao1x_20160413.png",
	"flag": 553648128,
	"receiver": 1688853790599424,
	"sender_name": "",
	"remark": "恭喜发财，大吉大利",
	"is_room": 0,
	"server_id": 12779524,
	"send_time": 1671089820,
	"sender": 7881302555913738,
	"packet_id": "1800008896202212157398737500018",
	"referid": 0,
	"app_info": "hongbao_7881302555913738_1800008896202212157398737500018",
	"readuinscount": 0,
	"msg_id": 1108413,
	"msgtype": 26,
	"desc": "来自十年一刻的红包，请进入手机版企业微信领取"
}

```

---

## 语音消息



    
**简要描述：** 

- 语音消息

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
	"flag": 16777216,
	"receiver": 1688853790599424,
	"voice_id": "308180020102047930770201000204bf366e0a02031e90380204a4df892b0204639acf92044c333230383031353337305f36323734363337303637373336383735373736453634363536323645363736465f6438333562623966636330656336366337363162666331363564643961666236020100020219a004000201050201000400",
	"sender_name": "",
	"is_room": 0,
	"server_id": 12779530,
	"voice_time": 4,
	"send_time": 1671090066,
	"sender": 7881302555913738,
	"referid": 0,
	"aes_key": "6274637067736875776E6465626E676F",
	"app_info": "from_msgid_6602574656373321919",
	"voice_size": 6544,
	"readuinscount": 0,
	"msg_id": 1108428,
	"msgtype": 16,
	"md5": "d835bb9fcc0ec66c761bfc165dd9afb6"
}

```


---

## 语音或者视频通话消息



    
**简要描述：** 

- 语音或者视频通话消息

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "flag": 536870912, 
    "receiver": 0, 
    "inviteId": 7881302555913738, 
    "msgid": 7227528031095598000, 
    "sender_name": "", 
    "call_user": [ //发起人
        {
            "xid": 7881302555913738, 
            "openid": "o9cq804RcItRx7Uxv4njTZf2-mnM@im.wechat", 
            "name": "十年一刻", 
            "headUrl": "http://wx.qlogo.cn/mmhead/Q3auHgzwzM7BQVtj83EnfQcDMjz2hhrIv6vzOPdXX4mLpIEJUbE2ew/0"
        }
    ], 
    "inviteType": 0, 
    "is_room": 0, 
    "server_id": 8079815, 
    "type": 1, 
    "roomid": 1235025531, 
    "send_time": 1682790003, 
    "sender": 10024, 
    "referid": 0, 
    "app_info": "7227528031095598083", 
    "actType": 1, 
    "readuinscount": 0, 
    "roomkey": 0, 
    "msg_id": 1007924, 
    "msgtype": 503, 
    "call_type": 2,  // 2语音消息 1视频消息
    "timestamp": 1682790003, 
    "content_masg": "十年一刻 邀请你进行语音通话"
}

```

---

## 语音或者视频通话未接听消息



    
**简要描述：** 

- 语音或者视频通话，未接听消息

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "flag": 536870912, 
    "receiver": 0, 
    "msgid": 7227528061160334000, 
    "sender_name": "", 
    "inviteType": 2,  //2语音 1视频
    "is_room": 0, 
    "server_id": 8079818, 
    "type": 3, //3是未接听  1是正在打电话 
    "roomid": 1235025531, 
    "senderid": 7881302555913738, 
    "send_time": 1682790010, 
    "sender": 10024, 
    "referid": 0, 
    "app_info": "wxvoipmiss_1688858100400545_12350255311682790010", 
    "readuinscount": 0, 
    "msg_id": 1007930, 
    "wording": "来自十年一刻6666649765的未接语音通话", 
    "msgtype": 503, //正在通话 挂断 未接都是503 根据里面的字段状态区分具体是哪一个
    "timestamp": 1682790010
}

```


---

## 语音或者视频通话在其他端接听消息



    
**简要描述：** 

- 语音或者视频通话，在其他端接听消息

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "flag": 536870912, 
    "receiver": 0, 
    "msgid": 7227530934493491000, //当前语音消息的id 根据这个来匹配这个语音的生命周期每个语音消息都有这个id 通话 接听 或者挂断 接听时常
    "sender_name": "", 
    "is_room": 0, 
    "server_id": 8079825, 
    "type": 2, // 1是正在打电话 2是其他端接收 3是挂断
    "send_time": 1682790679, 
    "sender": 10024, 
    "referid": 0, 
    "app_info": "7227530934493491289", 
    "readuinscount": 0, 
    "msg_id": 1007946, 
    "msgtype": 503, 
    "timestamp": 1682790679, 
    "content_masg": "已在其他终端接听/拒接"
}

```


---

## 语音或者视频通话消息对方取消通话



    
**简要描述：** 

- 语音或者视频通话消息，对方取消通话

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "flag": 553648128, 
    "receiver": 1688858100400545, 
    "sender_name": "", 
    "is_room": 0, 
    "recordwording": "对方已取消", 
    "server_id": 8079817, 
    "send_time": 1682790010, 
    "sender": 7881302555913738, //语音发起人
    "acceptOrRejectPlatform": 131074, 
    "referid": 0, 
    "recordtype": 1, //1代表对方取消 5代表语音结束
    "app_info": "wxvoipconv_1688858100400545_12350255311682790010", 
    "readuinscount": 0, 
    "invitetype": 2, //2语音  1视频
    "msg_id": 1007928, 
    "msgtype": 40, 
    "acceptOrRejectImei": "A9C97D19-77AA-4CCD-A88B-B789F5D8CACE"
}




```


---

## 语音视频通话时常消息通知




    
**简要描述：** 

- 语音视频通话时常消息通知

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "flag": 553648128, 
    "receiver": 1688858100400545, 
    "sender_name": "", 
    "is_room": 0, 
    "recordwording": "通话时长00:03", 
    "server_id": 8079827, 
    "send_time": 1682790685, 
    "sender": 7881302555913738, 
    "acceptOrRejectPlatform": 131074, 
    "referid": 0, 
    "recordtype": 5, 
    "app_info": "wxvoipconv_1688858100400545_12349965841682790685", 
    "readuinscount": 0, 
    "invitetype": 2, 
    "msg_id": 1007950, 
    "msgtype": 40, 
    "acceptOrRejectImei": "A9C97D19-77AA-4CCD-A88B-B789F5D8CACE"
}

```

---

## 撤回消息回调



    
**简要描述：** 

- 撤回消息

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "revoke_time": 1720166719,//撤回的消息的发送时间
    "flag": 0,
    "receiver": 1688855587446404, //接收人
    "sender_name": "",
    "is_room": 0,
    "revoke_msgtype": 2, //撤回消息的消息类型
    "server_id": 7122663,
    "revoke_ref_appinfo": "2068197696680161941", //撤回消息的appinfo
    "issync": false,
    "send_time": 1720166720,//撤回时间
    "sender": 7881302555913738,//发送人 也是撤回人
    "referid": 0,
    "app_info": "UbOr114nTxOjSjb",
    "readuinscount": 0,
    "msg_id": 1009088,
    "msgtype": 2063
}

```


---

## 合并消息




    
**简要描述：** 

- 合并消息

**返回类型：** 
- ` 100001 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "flag": 16777216, 
    "receiver": 1688853790599424, 
    "sender_name": "", 
    "is_room": 0, 
    "server_id": 12885464, 
    "send_time": 1681057244, 
    "sender": 7881302555913738, 
    "referid": 0, 
    "app_info": "from_msgid_4323568062863172402", 
    "readuinscount": 0, 
    "msg_id": 1120902, 
    "msgtype": 4, 
    "msglist": [
        {
            "send_time": 1680871380, 
            "sender": 0, 
            "send_name": "sfdsf", 
            "msgtype": 2, 
            "atList": [ ], 
            "content": "456"
        }, 
        {
            "send_time": 1680871380, 
            "sender": 0, 
            "send_name": "sfdsf", 
            "msgtype": 2, 
            "atList": [ ], 
            "content": "45"
        }, 
        {
            "send_time": 1680871380, 
            "sender": 0, 
            "send_name": "sfdsf", 
            "msgtype": 2, 
            "atList": [ ], 
            "content": "54"
        }, 
        {
            "video_duration": 4, 
            "video_height": 0, 
            "openim_cdn_ld_aeskey": "588c27ec325bca3f906e8b545f281dc8", 
            "openim_cdn_ld_size": 19207, 
            "video_width": 0, 
            "file_size": 753510, 
            "send_time": 1680981000, 
            "sender": 0, 
            "send_name": "sfdsf", 
            "file_id": "https://imunion.weixin.qq.com/cgi-bin/mmae-bin/tpdownloadmedia?param=v1_9402dda5b09845e753c80d85b8fdbbd9fec7a526c9da0b0d88ac1b7a74a4c4c19759fb25e6358f94c27c21c56da8848244d5d7f92a277a2c4b3a3c05719b5c1c789ddeb5bca81b08b7e158ba9ca786499a4bd003f4e8f68e8d59cacbe6a2fdabd8f9338c0fb8f2d4784e4b9ec7f85b6ab984a2ecdf7c678cb1d71065d5e4cf03112bd9b9cf1a382e12f6f067e335dd4f3001780aacf1cac1102515952eddc6a5869b9cb91b2b34c810c700e1783e9e2f69dc35b72af085998e5ba760c848d7bf2f784d5a0d0", 
            "openim_cdn_authkey": "v1_9402dda5b09845e753c80d85b8fdbbd9fec7a526c9da0b0d88ac1b7a74a4c4c1b3af2ae4a2d511d840070be7565291c2a9447818d75fbffd07e052b4ef0a7d73", 
            "aes_key": "2a2e1f60c749c6ad925e1e59aa7945aa", 
            "msgtype": 103, 
            "md5": "3b9399f4489989e2bb886fe35fce4df4", 
            "preview_img_url": "https://imunion.weixin.qq.com/cgi-bin/mmae-bin/tpdownloadmedia?param=v1_9402dda5b09845e753c80d85b8fdbbd9fec7a526c9da0b0d88ac1b7a74a4c4c17f12c4a584c08854f3380188dcaf6be657e9411151907717abe99cdf99facee539dd2aae29c125946ae0574a9d7db878cd560c4f61ec9290aa389a3c17584bc78379d4eaf85445291826662e6e13a3960fa73b5fe3a4f28fd6047651227244a6687b6a2deb205193710fc58ba8e81"
        }, 
        {
            "authkey": "v1_9402dda5b09845e753c80d85b8fdbbd9fec7a526c9da0b0d88ac1b7a74a4c4c1b3af2ae4a2d511d840070be7565291c2a9447818d75fbffd07e052b4ef0a7d73", 
            "send_time": 1681038120, 
            "des": "百度内容", 
            "size": 15444, 
            "headImg": "https://imunion.weixin.qq.com/cgi-bin/mmae-bin/tpdownloadmedia?param=v1_9402dda5ddfdaa9592b7db664ebbecc90e639e9374ccd1fcc5e8aeda95eff1c20d07577787f5b50923c9dace523dc5e72e25a2e6ad6c112d9a1bfed1e9bedc3232344a69f8cbeb1a32c47d20c17c0b7d55e0f5766ee90", 
            "sender": 0, 
            "send_name": "十年一刻", 
            "state": "1", 
            "aeskey": "80f537cf7f8e63dca87e08a5a02d444d", 
            "title": "百度", 
            "msgtype": 13, 
            "url": "https://www.baidu.com/"
        }
    ]
}

```

---

## 系统消息



    
**简要描述：** 

- 系统消息返回

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "msg": "因账号违规，当前功能已被限制使用。 查看详情",
    "flag": 16777216,
    "receiver": 1688856286xxxxx,
    "extraContent": {
        "extraMessageLink": {
            "jumpUrl": "https://work.weixin.qq.com/webapp/spam/info?key=%2FBe%2BZ9UmUqQFKjmhn6LLrFnZtUWQ0ZPud1Or1NgA0MySuAHkVQX7b6Cn64b2jnI18CbSyYv8Hu%2FwKgKiyJiRLDvDgvlck8sNOPhF86cUWi3RtRyVlesQ2hhy0CBLNaTqxYB%2B2txNf9Z3oOA3igKPFl%2FabvJGCem6lzlLJ5JrqMu7TiRBL17J6CWrOpuuNLml5d1C9vDUZRNtskQuh3IEyg%3D%3D&type=3&need_login=true", //详情连接，根据此连接去调用封禁查询接口。
            "linkType": 1
        }
    },
    "sender_name": "",
    "is_room": 0,
    "server_id": 15380771,
    "issync": false,
    "send_time": 1745750312,
    "sender": 788130229xxxx,
    "referid": 0,
    "app_info": "spam_block_CIOACBClj8+25zIYyuir+JeAgAMgDw==",
    "readuinscount": 0,
    "msg_id": 1000796,
    "msgtype": 1011
}

```


---

## 接龙消息接收



    
**简要描述：** 

- 接龙消息接收

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "innerkf_vid": 0,
    "flag": 83886080,
    "receiver": 0,
    "extraContent": {

    },
    "content": {
        "solitaireInfo": {
            "author_id": 7881302555913738,//接龙发起人id
            "default_punctuation": ".",
            "example_range": "9-6", //接龙示例内容，从title下标9开始数6个数 代表接龙示例：《 格式示例\n》
            "has_header": true,
            "items": [
                {
                    "content": "",
                    "header": false,
                    "punctuation": ".",
                    "range": "20-11", //接龙发送内容 20-11 表示从下标20开始数11个数 代表这个人的接龙内容 ：十年一刻 666666
                    "timestamp": 1750605934, //接龙发送时间
                    "user_id": 7881302555913738  //接龙发送人
                },
                {
                    "content": "",
                    "header": false,
                    "punctuation": ".",
                    "range": "35-17",//接龙发送内容 35-17 表示从title下标35开始数17个数 代表这个人的接龙内容 ：77777 23423423423
                    "timestamp": 1750606059, //接龙发送时间
                    "user_id": 1688854256622049 //接龙发送人
                }
            ],
            "loss": false,
            "tail_range": "",
            "title_range": "0-8" //标题 0-8 表示从0开始数8个数 是标题内容
        },
        "title": "#接龙\n测试接龙\n例 格式示例\n\n1. 十年一刻 666666\n2. 77777 23423423423" //接龙内容
    },
    "devInfo": 65537,
    "sender_name": "77777",
    "is_room": 1,
    "server_id": 7752377,
    "kf_id": 0,
    "issync": false,
    "send_time": 1750606064,
    "sender": 1688854256622049,
    "referid": 0,
    "app_info": "CIGABBDwweDCBhjhs6ywkICAAyAD",
    "room_conversation_id": "10831012479402677",
    "readuinscount": 0,
    "msg_id": 1025642,
    "msgtype": 213
}

```


---

## 视频号直播消息回调



    
**简要描述：** 

- 视频号直播消息

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "thumb_url": "https://finder.video.qq.com/261/20304/stodownload?encfilekey=oibeqyX228riaCwo9STVsGLIia39tYEhYEEKGI4Gox5zM20A8jGDaUDxBy3SUwgTq5Da9HWpFDSIgRicZfqXq5j79iaWAtMsX847x56t6Lfqe0l1Dnf89WuOibY6tUB9WVIkRlbUCaqes4TI8&token=x5Y29zUxcibDMyGL6Na7xYEOQicWBULHl5tre0jffh0lQ1AtIL9Ore7V26I833jia7hy8pMrxY6cRU0bicyiaWQxE4icjusOOcHCnib4Lfy3TqNsgvQpjSnkHIMibR7iapWA53XXF8j1nGqVwWCscRw4ibJ2wUv36gN5wHVdvoJ9ibuS4G7NV2Z1NMF2BQp1UjNWx0ornDj&bizid=1023&hy=SZ&idx=1&m=8fee888ecafc02e8c31c3dbdaa5dce9a&finder_expire_time=1767685885&wxampicformat=503&picformat=200&finder_eid=export%2FUzFfAgtgekIEAQAAAAAAwO0AoQR3AgAAAAstQy6ubaLX4KHWvLEZgBPEyoNceT8YK7WOzNPgMIIz8OIpYmWXb2aByCDBEEbP",
    "innerkf_vid": 0,
    "flag": 16777216,
    "extraContent": {

    },
    "devInfo": 0,
    "extras": "CAEQACLuFAAE9OmXBAAAAQAAAAAAuVcjx+TNrK/hvX34SGkgAAAAaeq5SzX7s7sPwaz04zCEwYwyALHFYGIb/l1etP1AtP2rte45PWnOZDz47woFHnbMCGriUicXi4T4a+7brqoCpmiXEj8MhAaOP4UvXZ82mpD2QZdh3e7PnyERRC218+aU8FlcATWnp0PYaNhn92X6EdWYwypMtXrzCVuq9LDLLWaVX+Li+Cefe/NZDWczsTfo7hu5ZhHL9jkI2+OSkB7HaGRvzBCe6tjx8t1yD83GaCIFfv0lLKMOjUzsNhEWdBvmFIOV2pzUT1IS56OtJv05hGGwYJCycrDer1QMTZ7Got80ygWfTPAMYcGgA3naboe9OdO//ewBx9mi7Knf+FnUg5ffe8k+P8x1aqhGBIB5pL1PItgGKXnXcXE/Os4YelWjWIJAQQH4kLVFMmEx2HKdCd3sIORFffdL82LHEyL3AjP9/wNL+5W4PppeZlXsd7kFlqZ+MTk4FFbDhSq4T3v97ew1/Mvqm3VTCvNWkEWH4kdM4rmLwPNwjSfgAxBxHZ1/p6E+iBT75QX27ekc96fDD+vRIHUDWlkUQgfup/vImHLdX8tS4qfTaY7a052ZCRvgnhSMBD8+2QSg9OPpvQSONeK+aWBvHZi5GvwhbQQhbYJt/UDQ6bjmqwFCkUDKGWLj5Hx2Hq62u/fBFjN4tcNuwAQD4QOuhgBB6XY4lgyUTscCoF5L67TagWyQsShPug1DyU9yM9Lm/nALYRhw7N6PenZipoqkwdOQbnbaJhOyR+MV7q9v6yIArfno2bvWvHH5QJ+IPR9o56Yh0Q7iGnyl+mKzgO//hx7uwM/QLilC6sp3byCoI934Z4JAJhz+uFZqeTNCGjF3TYIOzqtxBW2IPuTiFTRYVb4KcCiALbE+eMF+wTY6zWFODrVUEQktcqApH5JzonF1I+Dk/9LLhy4vQBH7LWP6+DzW+iat4xU7Dfm8pS5MO+w12Y6LYqxTNXqG+cUx1hIO9UN1+stReuK0GqsWtdafgBSRcP4n/JPZ4rJxm6e0ZflhhCHb/u/HwuHrq2D6/cIeCJ0l70bKLhGykGLPtR5KbIIlE/eAS+3tKY8Fha7G3SNc4RMNHK67zZa4PrfQwikHwiuD2yiRxWWl6sFZ7KFfk7EX8lNPGeJpTxVBdNmuxz9d8YcAD6bFOXXFdEM53+XGoJBMQTPEfpJCjalXSjAuSUU8IUzUR1wSMMMs4TtByycYX95VpiuPIu0J54cNILf2xTaV4m0oYkWQo4iHWUuwLjk7/fIIhq1SQjZwvpQEXitEcdpqn4+KkhyAByQBn8C6zsJscCcuPv9dShNVHnIjSuNRMHN4SAa4xZdMvuGDfvN35O4eAjGSZe3KbxvBuxNS3lSggsYb4EjJ0hl60lB9/U2l3TkN6P6w+n8Ihb/gB3BCTD/YR72QsNUUvyCAsNdMu9F6UDNUZI692UegIWNVLU/LxEPuW+hhOCkrKQ/wjHIeScz9Uw/J0UR3VFFH1V4IcqcXNjAUV1i+oJCzmYJmJNiKtwdV+Xu2Lo9yHoiHbr+p9wzTSdmZDxpjWCNH3ZYdyohbwWvYuDmkWJMSX+EIxN2XynQmR1r2qYUbhsxc6B5+/GhGeCAM1XPkH9McJ28yZvOXVYGrMamqyb7CFi/brGEn9SPTvL5O25j1mtiZ1WpZybm5T+mcb8OvE81n6GmakicKWDXKD6aLDVbGAc91UnYpCdDT7bkO1J4Vr3Kg/BwufxqbS5ss4WG2zIqenf6n9xmPhGYrSquiPbPhDTWjgYEoegXq5Q0ZAQx5xEulbDFiBta9mJfGOFsSoxmNHCypw3bTnyP/8Bw4bHyCOCr2e2Wr8qkAFeEiJJgZo0/qlqUmtVzlQapv50GeG/5yZ0mpN1/llA9ILDe2+xrpBC8BrsS9U5smSxdOe/43aCZzaK6MsJwssXVeU0JG01TFjsDbt41odz4XQjS2sMcKAlfj0RPRg4EGYV/WkzkGUOHDxTV37chrY+3ZPokaWjBszxCFX+7uLfD1/RxVSdT5cHbIFA1lBblBVBDn0o76otbUFqdW+nzM4sEis5dbUs2D3J+vfm08nLSSe70ffH+hx1bJglOic3EeaSh2n0LM/vhPEw6qf0/Txq5yMrGIB2xd8ibgXKqfqueCak5/2U+N6LUxIzXLS60qxsxsNRJaNeifGSkQKZ5Ku2NcEkqIyLAlWBRHk0OtM5LmmphJHlGzZ+pSrBH9VsdvligpxCTEdy2jkYmt8v/E/onKnUFGOf6Hdl34UM6dg1k9OkDxF4ETep9agf1/bIdjha23eSVWtgweFf1DjdA00U5/IJX7+f9rdLuzbqFXYOSvgr1BpAZmWzG+9gNDZfB542lyAQn8f4939ejZ2y4fIV6+ql/g8I33nvPt/Vhwi6nfWarkk8ePPdc0QmqeQOtfDHcMF7/w5I9BKD2Wauf5MBy6udiKcPMnOX7rob9Wn32Yt+oCYYX7wET7gI51LXoy8+brDJ3ZrcDv24eKIwMVgYnhkg23JPqLTDT+4UtiUQdPIcrAuo7WzJfOipdUz6PUKSTG1csRQaJnz+lbA4uIo4D5qaJZz7nLGKg/lc+fEAZtsYlXtFsl+ShmBLVE6Pf4qKWbxzRWZ5vL7PrKUiVZ7EigOR20yWusQmTS9Nj98Yd+VXgJTOVw7+Msi0Bh5vJANC7n5gRiYkK+mjNsnb9M/DVjNe+0K3PvOBeqARBqEpB9E0x386SbTCelE/wyyXnQPBz7Mcc0dGPC+wfcuZ2OS0VD3Q3FfhCnYGOo6RaGKh6+72+IiF587HUbgohS0THmpjWXq3rlhmXnVAj9++VyQx9Zf1pQLuIQXTmbdtQWbAs3wp1M1cbuaD5Dy0LdaSt0svN2iK6kEJ5M854slAJzPUFhb0M9E+EDB2EVg4AIXv2mIuBjYP08VUuDL/bTj620VnF1Nd/7Ifka8M4WZZ9yN6u8zBvE9c7HiKhA47yotM+1RmG3ntqQ+BuNiVaoDj7IropQsj2zKHH5Ok+y3bqQdltusvsfUXEB7st5X/NbFuz1aBlObk7/ez8VjeTde2ZXEmw8drlPF+wCaVyoNyns3AhhPRY5BwH1MaIw75VNSll02BNdkcvIczz11mJTztxRcmY6EeuQvOaUszPPt7gwclWZFOXIHvtTdk+eWRb43QQU/kxbo7PY1tr6e6uNQ36I7nNGyXn8oENaYzFraPOwriSEMtH1KUZ9zWtuarBQ++9hdDt/tWmjW144tMMqt+7D3Q1QP3Q30znglEe14XIcLH75IrjFpQIW8Y4YcNfFcCfsNCppDpY5fRElp4mQKhYqvVhitV+2/sbwceSlVb7/zAfVo7izaBxnV0m/N43nO5wTV7Q2485nqvtTkZQRGxVh8iFm9XgV8FhkImFWwfjrueraIca/ZUAAuI/x14rO7rdfyC9N6wDWgzVaQS2X+fRooGc5909RaduL7C2nQ8rHTC0IiK3UgtfNFAFjmvhjhGRZ4cvYK/e/QTHKxAZDutCLD7L+P75IhSlZusFmZQfNzykgPigA",
    "sender_name": "",
    "is_room": 0,
    "nickname": "三角洲行动丶海浪呀",
    "app_info": "3826338618172894593",
    "msg_id": 1008137,
    "msgtype": 146,
    "objectId": "14817343751800428574",
    "cover_url": "https://finder.video.qq.com/261/20304/stodownload?encfilekey=oibeqyX228riaCwo9STVsGLIia39tYEhYEEKGI4Gox5zM20A8jGDaUDxBy3SUwgTq5Da9HWpFDSIgRicZfqXq5j79iaWAtMsX847x56t6Lfqe0l1Dnf89WuOibY6tUB9WVIkRlbUCaqes4TI8&token=x5Y29zUxcibDMyGL6Na7xYEOQicWBULHl5tre0jffh0lQ1AtIL9Ore7V26I833jia7hy8pMrxY6cRU0bicyiaWQxE4icjusOOcHCnib4Lfy3TqNsgvQpjSnkHIMibR7iapWA53XXF8j1nGqVwWCscRw4ibJ2wUv36gN5wHVdvoJ9ibuS4G7NV2Z1NMF2BQp1UjNWx0ornDj&bizid=1023&hy=SZ&idx=1&m=8fee888ecafc02e8c31c3dbdaa5dce9a&finder_expire_time=1767685885&wxampicformat=503&picformat=200&finder_eid=export%2FUzFfAgtgekIEAQAAAAAAwO0AoQR3AgAAAAstQy6ubaLX4KHWvLEZgBPEyoNceT8YK7WOzNPgMIIz8OIpYmWXb2aByCDBEEbP",
    "objectNonceId": "10043444170675585450_0_0_0_0_0_03fd0748-df0b-11f0-86dc-212ae746b05c",
    "receiver": 1688857368772759,
    "avatar": "http://wx.qlogo.cn/finderhead/c1Ohodl25RZ9EV2LicMM88ceSOqeNfFm36BUgjsFQjHcHsEg8zicYnMHcaxeXZ25wAficQTUhQQHUU/0",
    "server_id": 13274178,
    "kf_id": 0,
    "url": "https://channels.weixin.qq.com/web/pages/live?eid=export%2FUzFfAgtgekIEAQAAAAAAwO0AoQR3AgAAAAstQy6ubaLX4KHWvLEZgBPEyoNceT8YK7WOzNPgMIIz8OIpYmWXb2aByCDBEEbP",
    "issync": false,
    "send_time": 1766389884,
    "sender": 7881302555913738,
    "referid": 0,
    "readuinscount": 0,
    "desc": "绝密航天典狱长"
}

```


---

## 内部联系人大文件消息



    
**简要描述：** 

- 内部联系人大文件消息

**返回类型：** 
- ` 102000 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "innerkf_vid": 0,
    "flag": 16777216,
    "receiver": 1688857368772759,
    "extraContent": {

    },
    "file_name": "postman_v7.6.0_downyi.com.zip",
    "devInfo": 65537,
    "sender_name": "xxx",
    "is_room": 0,
    "server_id": 13274472,
    "kf_id": 0,
    "file_size": 72127884,
    "issync": false,
    "send_time": 1766651990,
    "sender": 1688856274767936,
    "referid": 0,
    "file_id": "*1*u3W2k/2+7TigF3ieM2JYWY12fJ0m+w80zO84cIONH6ap/yvmyj0JtKad3fMPQ3X7GvDGg2jv4q6CzqKlbkJVHcg6Q0BzWKuCZzNdwsCXYXNDs54NPJ579w7JqJsaRQ0RN8RSOpRUvS0jYWuRYCWToWn5rXx1OAJnNRHrwZsPOWWRkXBENPjqwRfLI8dbJ09aP2H31RfIliESX9PQuwmA2UecUBxCEMq4C7SzgMBhKFojPM4qniFtRbI/RfClC5cN2/eLtyxNaSoPZY/vLVKYeDKOXrOg6jE7X7++kHSJFIxVTo6uxmEqM+1ePWR4vSNRJy1GAKFl0r42hLoOMqN3MRNJu7yY8oLDNw9O5KmCP4y0wg5D0sXhNtaNaiVDlNLHMzcmnF7ScaumVLydgn6owW8driac/MXyqPiI3u6lJpQCGu3fW2L++DcOYSdCeESqPnyHk4OKObizEMgY5A/EKcvUeCQ7hyIz3Y4/0ZEWuKY=",
    "aes_key": "",
    "app_info": "CAEQ1vCzygYYwKDW8peAgAMgBw==",
    "readuinscount": 0,
    "msg_id": 1008782,
    "msgtype": 20,
    "md5": "02FD6642C4333E1E8104EB3F0A61212F"
}

```


---

## 新增客服聊天排队通知



    
**简要描述：** 

- 新增客服聊天排队通知

**返回类型：** 
- ` 102000 `

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
    "server_id": 7752275,
    "kf_id": 25664265286998413, //这个客服id收到的排队通知
    "issync": false,
    "send_time": 1749800760,
    "sender": 10151,
    "referid": 0,
    "app_info": "Dhodmav-QbmseNc",
    "readuinscount": 0,
    "msg_id": 1025395,
    "msgtype": 2314 //排队通知类型 收到这个类型查一下排队列表
}

```


---

