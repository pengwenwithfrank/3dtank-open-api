# Tank Open Api

##  1. 接入说明
>    - 基于 [OAuth2]: https://oauth.net/2 标准授权安全访问
>    - 请求类型 ContentType=application/x-www-form-urlencoded
>    - 方法签名MD5 (详情见第6条)
>    - 除创建token以外的其他请求不可以重复使用，ts和sign必须重新生成
>    - [示例代码] https://github.com/pengwenwithfrank/3dtank-open-api/blob/master/src/test/java/com/example/demo/DemoApplicationTests.java
   
## 2. 创建token (POST)
>   - https://open-api.3dtank.com/oauth/token
>   
  |  参数   | 类型  |   是否必填 | 描述 |
  |  ----  | ----  | ---- | ---- |
  | client_id  | string | 是 | 客户端id |
  | client_secret  | string | 是 | 密钥 |
  | scope  | string | 是 | 范围 |
  | grant_type  | string |  是 | 授权类型 |
>   - 返回值

  |  参数   | 类型  |   描述 |
  |  ----  | ----  |  ---- |
  | access_token  | string  | jwt token |
  | token_type  | string |  token 类型 |
  | expires_in  | int | token的有效期 |
  | scope  | string |  授权范围 |
  | jti  | string |  jwt唯一身份标识 |
    

## 3. 登录
>   - https://open-api.3dtank.com/tank-ymxk-web/api/login
>   
>
   |  header   | 类型  |   是否必填 | 描述 |
  |  ----  | ----  | ---- | ---- |
  | Authorization  | bearer | 是 | 授权token |
>
----------------------------
>
 |  参数   | 类型  |   是否必填 | 描述 |
  |  ----  | ----  | ---- | ---- |
  | user_id  | string | 是 | 用户id |
  | server_id  | string | 是 | 服务器id(默认1 ，只有一个服) |
  | ts  | long |  是 | 当前系统时间戳，单位毫秒 |
  | fcm  | string |  是 | 实名制（0 未验证 1 未成年 2 已成年） |
  | sign  | string |  是 | 请求签名 |
>   - 返回值

  |  参数   | 类型  |   描述 |
  |  ----  | ----  |  ---- |
  | errCode  | int  | 0 返回正确 -1 错误消息 |
  | message  | string |  错误消息 |
  | data  | string | 登录地址，直接跳转即可，(如果fcm 未验证，则会返回实名制地址) |
  | ticks  | int | 执行时间，暂无效 |
 

## 4. 获取用户信息
>   - https://open-api.3dtank.com/tank-ymxk-web/api/get-user-info
>   
>
   |  header   | 类型  |   是否必填 | 描述 |
  |  ----  | ----  | ---- | ---- |
  | Authorization  | bearer | 是 | 授权token |
>
----------------------------
>
 |  参数   | 类型  |   是否必填 | 描述 |
  |  ----  | ----  | ---- | ---- |
  | user_id  | string | 是 | 用户id |
  | ts  | long |  是 | 当前系统时间戳，单位毫秒 |
  | sign  | string |  是 | 请求签名 |
>   - 返回值

  |  参数   | 类型  |   描述 |
  |  ----  | ----  |  ---- |
  | errCode  | int  | 0 返回正确 -1 错误消息 |
  | message  | string |  错误消息 |
  | data  | Json | {"id":108005,"userId":"123","serverId":1,"addTime":"2021-06-23 08:38:23, channel=ymxk","data":"ymxk\|123\|1\|1624437501459\|2"} |
  | ticks  | int | 执行时间，暂无效 |



## 5. 充值
>   - https://open-api.3dtank.com/tank-ymxk-web/api/notification-of-top-up
>   
>
   |  header   | 类型  |   是否必填 | 描述 |
  |  ----  | ----  | ---- | ---- |
  | Authorization  | bearer | 是 | 授权token |
>
----------------------------
>
 |  参数   | 类型  |   是否必填 | 描述 |
  |  ----  | ----  | ---- | ---- |
  | user_id  | string | 是 | 用户id |
  | order_id  | string | 是 | 订单号20-32位，不重复的字符串 |
  | money  | string | 是 | RMB 单位元 |
  | coin  | int | 是 | 游戏币 |
  | ts  | long |  是 | 当前系统时间戳，单位毫秒 |
  | sign  | string |  是 | 请求签名 |
>   - 返回值

  |  参数   | 类型  |   描述 |
  |  ----  | ----  |  ---- |
  | errCode  | int  | 0 充值成功 -1 充值失败 |
  | message  | string |  错误消息 |
  | data  | string |  格式填充 |
  | ticks  | int | 执行时间，暂无效 |









    
