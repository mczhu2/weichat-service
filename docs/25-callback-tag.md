# 25-callback-tag

## 个人标签新增回调



    
**简要描述：** 

- 标签新增回调

**返回类型：** 
- ` 130001 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "labellist": [
        {
            "op": 3, 
            "bDeleted": 0, //0代表新增
            "create_time": 1678114162, 
            "label_groupid": 14073749131792038, 
            "label_type": 2, 
            "source_appid": 0, 
            "business_type": 0, 
            "name": "测试回调标签", 
            "data_type": 1, 
            "id": 14073750799769410, 
            "service_groupid": 0, 
            "new_order": 0, 
            "order": 0
        }
    ], 
    "index": 12882694
}

```


---

## 个人标签删除回调通知




    
**简要描述：** 

- 个人标签删除回调通知

**返回类型：** 
- ` 130001 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "labellist": [
        {
            "op": 2, 
            "bDeleted": 1, //1代表删除
            "create_time": 1678114162, 
            "label_groupid": 14073749131792038, 
            "label_type": 2, 
            "source_appid": 0, 
            "business_type": 0, 
            "name": "测试回调标签", 
            "data_type": 1, 
            "id": 14073750799769410, 
            "service_groupid": 0, 
            "new_order": 0, 
            "order": 0
        }
    ], 
    "index": 12882695
}

```

---

## 企业标签删除回调



    
**简要描述：** 

- 企业标签删除回调

**返回类型：** 
- ` 130002 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "labellist": [
        {
            "op": 2, 
            "bDeleted": 1, //1代表删除
            "create_time": 1678112965, 
            "label_groupid": 14073749131791700, 
            "label_type": 1, 
            "source_appid": 0, 
            "business_type": 0, 
            "name": "新增测试回调", 
            "data_type": 1, 
            "id": 14073748971730364, 
            "service_groupid": 0, 
            "new_order": 0, 
            "order": 0
        }
    ], 
    "index": 9684989
}

```


---

## 企业标签新增回调




    
**简要描述：** 

- 企业标签新增回调

**返回类型：** 
- ` 130002 `

**返回uuid：** 
- ` f7503bb5-7d27-408f-ab24-8c4ace7f068e `

**返回示例**
``` 
{
    "labellist": [
        {
            "op": 2, 
            "bDeleted": 0, //0代表新增
            "create_time": 1678112965, 
            "label_groupid": 14073749131791700, 
            "label_type": 1, 
            "source_appid": 0, 
            "business_type": 0, 
            "name": "新增测试回调", 
            "data_type": 1, 
            "id": 14073748971730364, 
            "service_groupid": 0, 
            "new_order": 0, 
            "order": 0
        }
    ], 
    "index": 9684989
}

```

---

