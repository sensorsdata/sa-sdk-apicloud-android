# 神策数据分析平台接入指南


## 概述

开发者在使用 sensorsAnalyticsAPICloudSDK 模块接入神策数据统计平台时，需要开发者到神策官网申请试用账号获取相应的数据接收地址的 URL 。并在初始化时填入对应的 URL 。

sensorsAnalyticsAPICloudSDK 模块的具体试用步骤如下：

### 初始化SDK

首先从 Sensors Analytics 系统中，获取数据接收URL。

![](https://www.sensorsdata.cn/manual/img/multi_project_data_api.png)


### 使用此模块需要在 config.xml 文件中配置相应的 feature

配置示例：

```xml
    <feature name="sensorsAnalyticsAPICloudSDK">
        <param name="serverURL" value="数据接收地址URL"/>
        <param name="debugMode" value="debugOff"/>
        <param name="enableAutoTrack" value="true"/>
        <param name="downloadChannel" value="应用宝" />
        <param name="enableLog" value="false"/>
    </feature>   
```
配置说明：

- feature 名称：`sensorsAnalyticsAPICloudSDK`

- param 参数
   - serverURL：(必填项，如果有特殊字符 `&` ，使用`&amp;`) 数据接收地址 URL。
   - debugMode：(必填项) Debug 模式，有三种模式：
      * `debugOff` - 关闭 Debug 模式，发版 App 时使用此模式
      * `debugAndTrack` - 打开 Debug 模式，校验数据，并将数据导入神策分析系统中
      * `debugOnly` - 打开 Debug 模式，校验数据，但不进行数据导入
   - enableAutoTrack：(可选项) 是否采集 App 启动、App 退出事件，传入字符串 true 表示采集启动、退出事件
   - downloadChannel：(可选项) App 的下载渠道，配置此参数时，会触发 App 安装激活事件（AppInstall），下载渠道会存储在 DownloadChannel 字段中
   - enableLog：(可选项) 是否开启调试日志，传入字符串 true 表示开启调试日志。

<font color=red>注意：正式发布 App 时请将 debugMode 指定成 `debugOff` 模式！</font>




### 识别用户

在集成了 sensorsAnalyticsAPICloudSDK 模块的 App 中，SDK 会为每个设备分配一个唯一 ID（DistinctId）作为 **匿名 ID**，用于标记产生事件的未登录用户，并以此进行用户相关分析，如留存率、事件漏斗等。默认情况下，对于 iOS sensorsAnalyticsAPICloudSDK 模块会优先使用 IDFV 作为 DistinctID，如果 IDFV 获取失败，则使用随机的 UUID。而 Android 默认获取 AndroidId 作为 DistinctID.

当一个用户 **注册成功** 或 **登录成功** 之后，可以通过 `login:` 方法设置用户的 **登录 ID** ，并将 **匿名 ID** 与 **登录 ID** 进行关联，以保证用户分析的准确性。

#### 示例代码

```js
document.getElementById('login').onclick = function() {
	var sa = api.require('sensorsAnalyticsAPICloudSDK');
	sa.login({
    	loginId: '123456'
	});
}
```

### 追踪事件
sensorsAnalyticsAPICloudSDK 模块成功初始化后，可以通过 `track:` 方法追踪用户行为事件，并为事件添加自定义属性。以电商产品为例，可以这样追踪一次购物行为：

```js
document.getElementById('track').onclick = function() {
	var sa = api.require('sensorsAnalyticsAPICloudSDK');
	sa.track({
    	event:'ViewProduct',
    	properties:{
       		ProductID:123456,
       		ProductCatalog:'Laptop Computer',
       		IsAddedToFav: false
    	}
	});
}
```

通过 Debug 模式，可以校验追踪的事件及属性是否正确。普通模式下，数据导入后，在 Sensors Analytics 中稍等片刻，便能看到追踪结果。请注意，**不要在正式发布的 App 中使用 Debug 模式**。

![](https://www.sensorsdata.cn/manual/img/ios_sdk_1.png)