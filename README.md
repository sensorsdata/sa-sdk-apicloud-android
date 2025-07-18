APICloud 文档
===========

1\. 概述
======

SensorsAnalyticsAPICloudSDK 封装了 Sensors Analytics 数据统计 Android & iOS SDK，使用此模块可进行用户行为数据采集。

神策分析，是针对企业级客户推出的深度用户行为分析产品，支持私有化部署，客户端、服务器、业务数据、第三方数据的全端采集和建模，驱动营销渠道效果评估、用户精细化运营改进、产品功能及用户体验优化、老板看板辅助管理决策、产品个性化推荐改造、用户标签体系构建等应用场景。作为 PaaS 平台支持二次开发，可通过 BI、大数据平台、CRM、ERP 等内部 IT 系统，构建用户数据体系，让用户行为数据发挥深远的价值。

## 神策埋点 SDK 官网
如需了解神策埋点 SDK 的更多商业授权信息，请访问[神策埋点 SDK 官网](https://jssdk.debugbox.sensorsdata.cn/)获取更多详细信息。

## 联系我们
若您有商业合作或产品集成需求，请通过下面的渠道联系我们获取专业服务与支持。

| 加微信号：skycode008，或扫码添加联系人 <img src="https://github.com/sensorsdata/sa-sdk-android/blob/master/WechatIMG180.jpg" width="300" height="450" /> | 扫码关注「神策埋点 SDK」公众号 ![gzh](https://github.com/sensorsdata/sa-sdk-android/blob/master/gzh.jpeg) |
| ------ | ------ |

2\. 模块添加
========

在模块库中搜索 SensorsAnalyticsAPICloudSDK 然后点击添加按钮。

![](https://doc.sensorsdata.cn/download/attachments/115667450/image2021-4-27_17-46-58.png?version=1&modificationDate=1619516819000&api=v2)


3\. 功能接口
========

initSDK
----------

初始化 SDK。

_**initSDK({params})**_

params：

*   类型：json 格式
*   描述：SDK 初始化配置属性
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk. initSDK({
        server_url:'数据接收地址',
        enable_log:false,//是否开启日志，默认 false
        auto_track:false,//只支持 $AppStart $AppEnd 事件，默认 false
        flush_interval:15000,//两次上报数据间隔，单位：毫秒，默认 15000 毫秒
        flush_bulkSize:100,设置本地缓存日志的最大条目数，最小 50 条， 默认 100 条
        encrypt:false,//是否开启加密，需后端支持，加密仅支持 RSA+AES，默认 false
    });
```


track
----------

track 事件。

_**track({params})**_

event：

*   类型：字符串
*   描述：(必填项)事件名称，名称需要满足一般变量的命名规则，即不能以数字开头，且只包含：大小写字母、数字、下划线和$

properties：

*   类型：json 格式
*   描述：(选填项)事件属性，名称需要满足一般变量的命名规则，即不能以数字开头，且只包含：大小写字母、数字、下划线和$
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.track({
        event:'ViewProduct',
        properties:{
            ProductID:123456,
            ProductCatalog:'Laptop Computer',
            IsAddedToFav: false
        }
    });
```

setServerUrl
-----------------

设置数据接收地址。

***setServerUrl({params})***

serverUrl:

*   类型：字符串
*   描述：数据接收地址

requestRemoteConfig：

*   类型：boolean
*   描述：是否请求远程配置

```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.setServerUrl({serverUrl:'testURL', requestRemoteConfig:false});
```

getServerUrl
-----------------

获取数据接收地址。

***getServerUrl({params})***

*   返回值类型：字符串
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    var serverUrl = sensorsApiCloudSdk.getServerUrl();
```


login
----------

设置当前用户的 loginId

_**login({params})**_

loginId：

*   类型：字符串
*   描述：(必填项)用户的登录id，不能为空，且长度不能大于255

properties：

*   类型：json 格式
*   描述：(选填项)用户属性，名称需要满足一般变量的命名规则，即不能以数字开头，且只包含：大小写字母、数字、下划线和$
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.login({
        loginId: '123456',
        properties:{
            age:18,
            name:'sensors'
        }
    });
```


loginWithKey
----------

设置当前用户的登录 IDKey 和 loginId

_**loginWithKey({params})**_

key：

*   类型：字符串
*   描述：(必填项)登录 IDKey

id：

*   类型：字符串
*   描述：(必填项)登录 loginId

```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.loginWith({
         key: 'mobile',
         id:  '133XXXXXXXX'
    });
```

getLoginId
----------------

获取当前用户的 loginId

***getLoginId()***

*   返回值类型：字符串
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    var loginId = sensorsApiCloudSdk.getLoginId();
```

logout
------------

登出

***logout()***
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.logout();
```

registerSuperProperties
----------------------------

设置公共属性，设置之后，之后触发的事件会带上设置的公共属性

_**registerSuperProperties({params})**_

properties：

*   类型：json 格式
*   描述：(必填项)公共属性，属性名需要满足一般变量的命名规则
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.registerSuperProperties({properties:{ PlatformType:"Android"}});
```

getDistinctId
------------------

getDistinctId 获取当前用户的 distinctId ，如果用户未登录时，返回值为 匿名 ID ；登录（调用login）后，返回值为 登录 ID 。

_**getDistinctId()**_

*   返回值类型：String 字符串
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    var distinctId = sensorsApiCloudSdk.getDistinctId();
```

identify
------------------

* 设置当前用户的 distinctId。一般情况下，如果是一个注册用户，则应该使用注册系统内
  的 user_id，如果是个未注册用户，则可以选择一个不会重复的匿名 ID，如设备 ID 等，如果
  客户没有调用 identify，则使用SDK自动生成的匿名 ID

_**identify({params})**_

anonymousId

*   类型：String
*   描述：(必填项) 匿名 ID
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    var distinctId = sensorsApiCloudSdk.identify({anonymousId:"anonymousId"});
```

getAnonymousId
------------------

* 获取匿名 ID

_**getAnonymousId()**_

*   返回值类型：String 字符串
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    var anonymousId = sensorsApiCloudSdk.getAnonymousId();
```

resetAnonymousId
------------------

* 重置默认匿名 ID

_**resetAnonymousId()**_

```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.resetAnonymousId();
```


trackTimerStart
--------------------

初始化事件的计时器，计时单位为秒。

***trackTimerStart({params})***

event:
*   类型：字符串
*   描述：事件名称

*   返回值类型：交叉计时事件Id

```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.trackTimerStart({event:'ApiCloudTrackTimer'});
```

trackTimerPause
--------------------

暂停事件计时

***trackTimerPause({params})***

event:

*   类型：字符串
*   描述：事件名称或事件的 eventId
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.trackTimerPause({event:'ApiCloudTrackTimer'});
```

trackTimerResume
---------------------

恢复事件计时

***trackTimerResume({params})***

event:

*   类型：字符串
*   描述：事件名称或事件的 eventId
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.trackTimerResume({event:'ApiCloudTrackTimer'});
```

trackTimerEnd
---------------------

停止事件计时器，并触发事件

***trackTimerEnd({params})***

event:

*   类型：字符串
*   描述：事件名称或事件的 eventId

properties：

*   类型：json 格式
*   描述：(选填项)事件属性，名称需要满足一般变量的命名规则，即不能以数字开头，且只包含：大小写字母、数字、下划线和$
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.trackTimerEnd({
        event:'ApiCloudTrackTimer'
        properties:{
            ProductID:123456,
            ProductCatalog:'Laptop Computer',
            IsAddedToFav: false
        }
    });
```

removeTimer
---------------------

删除事件的计时器

***removeTimer({params})***

event:

*   类型：字符串
*   描述：事件名称或事件的 eventId
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.removeTimer({event:'ApiCloudTrackTimer'});
```


clearTrackTimer
--------------------

删除所有事件计时

***clearTrackTimer()***
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.clearTrackTimer();
```

getSuperProperties
------------------------

获取公共属性

***getSuperProperties()***

*   返回值类型：json 字符串
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    var superProperties = sensorsApiCloudSdk.getSuperProperties();
```

trackAppInstall
---------------------

记录 $AppInstall 事件，用于在 App 首次启动时追踪渠道来源，并设置追踪渠道事件的属性

***trackAppInstall({params})***

properties:

*   类型：json 格式
*   描述：追踪渠道事件的属性
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.trackAppInstall({
                                properties:{
                                    testAppInstall:"testAppInstallValue"
                                    }
                                });
```

clearSuperProperties
--------------------------

删除当前所有的 superProperty

_**clearSuperProperties()**_
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.clearSuperProperties();
```

setFlushNetworkPolicy
---------------------------

设置 flush 时网络发送策略，默认 3G、4G、WI-FI 环境下都会尝试 flush

_**setFlushNetworkPolicy({params})**_

networkPolicy：

*   类型：int 类型
*   描述：
    *   TYPE\_NONE = 0;//NULL

    *   TYPE\_2G = 1;//2G

    *   TYPE\_3G = 1 << 1;//3G 2

    *   TYPE\_4G = 1 << 2;//4G 4

    *   TYPE\_WIFI = 1 << 3;//WIFI 8

    *   TYPE\_5G = 1 << 4;//5G 16

    *   TYPE\_ALL = 0xFF;//ALL 255

    *   例：若需要开启 4G 5G 发送数据，则需要设置 4 + 16 = 20

    *   iOS 不支持 5G。
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.setFlushNetworkPolicy({networkPolicy:31});
```

getPresetProperties
-------------------------

返回预置属性

_**getPresetProperties()**_

*   返回值类型：json 字符串
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    var result = sensorsApiCloudSdk.getPresetProperties();
```
itemSet
-------------

设置 item

***itemSet({params})***

itemType：

*   类型：字符串
*   描述：item 类型

itemId：

*   类型：字符串
*   描述：item Id

properties：

*   类型：json 格式
*   描述：item 相关属性
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.itemSet({itemType: 'itemType', itemId: 'itemId', properties: {itemProperty: 'itemPropertyValue'}});
```

itemDelete
----------------

删除 item

***itemDelete({params})***

itemType：

*   类型：字符串
*   描述：item 类型

itemId：

*   类型：字符串
*   描述：item Id
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.itemDelete({itemType: 'itemType', itemId: 'itemId'});
```

isAutoTrackEnabled
------------------------

是否开启 AutoTrack

***isAutoTrackEnabled()***

*   返回值类型：布尔值
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    result = sensorsApiCloudSdk.isAutoTrackEnabled();
```

profileSet
----------------

profileSet 设置用户属性。

***profileSet({params})***

properties：

*   类型：json 格式
*   描述：(选填项)用户属性，名称需要满足一般变量的命名规则，即不能以数字开头，且只包含：大小写字母、数字、下划线和$
```js
    var sensorsApiCloudSdk =     api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.profileSet({
      properties:{
        sex:"男"
      }
    });
```

profileSetOnce
--------------------

profileSetOnce 设置用户首次属性。

_**profileSetOnce({params})**_

properties：

*   类型：json 格式
*   描述：(选填项)用户属性，名称需要满足一般变量的命名规则，即不能以数字开头，且只包含：大小写字母、数字、下划线和$
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.profileSet({
        properties:{
          firstCharge:100
        }
    });
```

profileIncrement
----------------------

给一个数值类型的 Profile 增加一个数值

_**profileIncrement({params}})**_

properties

*   类型：Map
*   描述：增加一组用户行，用户属性 value 只能为数值。
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.profileIncrement({properties:{chinese:60,math:70}});
```

profileAppend
-------------------

给 Profile 增加一个元素

_**profileAppend({params})**_

property

*   类型：String
*   描述：用户属性名

value

*   类型： 数组
*   描述：用户属性值
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.profileAppend({property:'ArrayDesc',value:['SDK','后端','算法']});
```

profileUnset
------------------

函数，删除用户属性

_**profileUnset({params})**_

property

*   类型：String
*   描述：需删除的用户属性名
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.profileUnset({property:'ApiCloudAge'});
```

profileDelete
-------------------

删除此用户所有用户属性

_**profileDelete**_
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.profileDelete();
```

profilePushId
-------------------

设置用户的 pushId

_**profilePushId({params})**_

pushTypeKey:

*   类型：字符串
*   描述：pushId 的 key

pushId：

*   类型：字符串
*   描述：pushId 的值
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.profilePushId({pushTypeKey: 'pushTypeKey', pushId: 'pushId_'});
```

profileUnsetPushId
------------------------

删除用户设置的 pushId

_**profileUnsetPushId({params})**_

pushTypeKey：

*   类型：字符串
*   描述：pushId 的 key
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.profileUnsetPushId({pushTypeKey: 'pushTypeKey'});
```

enableNetWorkRequest
--------------------------

**(Android)** 设置是否允许请求网络，默认是 true。

_**enableNetWorkRequest({params })**_

isRequest：

*   类型：字符串
*   描述：是否允许请求网络，默认是 true。
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.enableNetWorkRequest({isRequest: false });
```

isNetworkRequestEnabled
----------------------------

**(Android)** 获取是否允许请求网络。

_**isNetworkRequestEnabled()**_

*   返回值类型: boolean
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    var isEnable = sensorsApiCloudSdk.isNetworkRequestEnabled();
```

setSessionIntervalTime
----------------------------

**(Android)** 设置 App 切换到后台与下次事件的事件间隔

***setSessionIntervalTime({params})***

sessionIntervlTime：

*   类型：int
*   描述：App 切换到后台与下次事件的事件间隔，单位：毫秒
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.setSessionIntervalTime({sessionIntervalTime:15000});
```

getSessionIntervalTime
----------------------------

**(Android)** 获取 App 切换到后台与下次事件的事件间隔

_**getSessionIntervalTime()**_

*   返回值类型：int
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    var session = sensorsApiCloudSdk.getSessionIntervalTime();
```


trackViewScreen
---------------------

触发 $AppViewScreen 事件

***trackViewScreen({params})***

url：

*   类型：String
*   描述：页面的 url 记录到 $url 字段中(如果不需要此属性，可以传 null

properties

*   类型：json 格式
*   描述：页面的属性
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.trackViewScreen({url:'url',properties:{$title:'title'}});
```

unregisterSuperProperty
-----------------------------

删除特定静态公共属性

***unregisterSuperProperty({params})***

property

*   类型：String
*   描述：需删除的静态公共属性 key
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.unregisterSuperProperty({property:'ApiCloudSuperKey'});
```

clearSuperProperties
--------------------------

清除静态公共属性

***clearSuperProperties()***
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.clearSuperProperties();
```

flush
-----------

强制发送数据到服务端

***flush()***
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.flush();
```

deleteAll
---------------

删除本地数据库的所有数据！！！请谨慎使用

***deleteAll()***
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.deleteAll();
```

enableLog
---------------

打印日志控制

***enableLog({params})***

enableLog

*   类型: boolean
*   描述: 是否打印日志
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.enableLog({enableLog:true}});
```

getIdentities
---------------

获取当前的 identities

***getIdentities()***
* 返回值类型：json
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    var identities = sensorsApiCloudSdk.getIdentities();
```

bind
---------------

绑定业务

***bind({params})***
key
*   类型: String
*   描述: 绑定的 key

value
*   类型: String
*   描述: 绑定的 value

```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.bind({key:'mobile',value:'133XXXXXXXX');
```

unbind
---------------

解绑业务
***unbind({params})***
key
*   类型: String
*   描述: 解绑的 key

value
*   类型: String
*   描述: 解绑的 value

```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.unbind({key:'mobile',value:'133XXXXXXXX');
```

unbind
---------------

解绑业务
***unbind({params})***
key
*   类型: String
*   描述: 解绑的 key

value
*   类型: String
*   描述: 解绑的 value

```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.unbind({key:'mobile',value:'133XXXXXXXX');
```

setFlushBulkSize
---------------

设置本地缓存日志的最大条目数，最小 50 条

***setFlushBulkSize({params})***
flushBulkSize
*   类型: int
*   描述: 缓存日志的最大条目数
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.setFlushBulkSize({flushBulkSize:最大缓存条数});
```

getFlushBulkSize
----------------

获取本地缓存日志的最大条目数

***getFlushBulkSize()***

*   返回值类型：int
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    var flushBulkSize = sensorsApiCloudSdk.getFlushBulkSize();
```

setFlushInterval
---------------

设置两次数据发送的最小时间间隔

***setFlushInterval({params})***
flushInterval
*   类型: int
*   描述: 设置两次数据发送的最小时间间隔，单位毫秒
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.setFlushInterval({flushInterval:数据发送间隔});
```

getFlushInterval
----------------

获取两次数据发送的最小时间间隔

***getFlushInterval()***

*   返回值类型：int
```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    var flushInterval = sensorsApiCloudSdk.getFlushInterval();
```

enableSDK
---------------

开启 SDK

***enableSDK()***

```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.enableSDK();
```

disableSDK
---------------

关闭 SDK

***disableSDK()***

```js
    var sensorsApiCloudSdk = api.require('sensorsAnalyticsAPICloudSDK');
    sensorsApiCloudSdk.disableSDK();
```
