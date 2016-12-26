# 神策数据分析平台接入指南


## 概述

开发者在使用sensorsAnalyticsAPICloudSDK模块接入神策数据统计平台时，需要开发者到神策官网申请试用账号获取相应的数据接收地址的URL和配置分发地址的URL。并在初始化时填入对应的URL。

sensorsAnalyticsAPICloudSDK模块的具体试用步骤如下：

### 初始化SDK

首先从 Sensors Analytics 系统中，获取数据接收和配置分发的 URL。

![](https://www.sensorsdata.cn/manual/img/multi_project_data_api.png)

如果使用 Sensors Analytics Cloud 服务，需获取的配置信息为:

 * 数据接收地址: http://{$service_name}.cloud.sensorsdata.cn:8006/sa?project={$project_name}&token={$project_token}
 * 配置分发地址: http://{$service_name}.cloud.sensorsdata.cn:8006/config?project={$project_name}

如果用户使用单机版私有部署的 Sensors Analytics，默认的配置信息为：

 * 数据接收地址: http://{host_name}:8006/sa?project={$project_name}
 * 配置分发地址: http://{host_name}:8006/config?project={$project_name}

如果用户使用集群版私有部署的 Sensors Analytics，默认的配置信息为：

 * 数据接收地址: http://{host_name}:8106/sa?project={$project_name}
 * 配置分发地址: http://{host_name}:8106/config?project={$project_name}

如果私有部署的过程中修改了 Nginx 的默认配置，或通过 CDN 等访问 Sensors Analytics，则请咨询相关人员获得配置信息，特别地，除上述数据接收的 URL 和配置分发的 URL 外，还需要获取可视化埋点服务的 URL。


首先必须在index.html或应用入口文件里调用 `sharedInstance` 接口初始化模块（模块使用前必须初始化，在下面几项功能介绍时不再重复描述了）

#### 示例代码

```js
apiready = function() {
	var saAPICloud = api.require('sensorsAnalyticsAPICloudSDK');
	saAPICloud.sharedInstance({
    	serverURL: 'your server url', 
   		configureURL: 'your configure url', 
    	debugMode: 'your debug mode'
	});
}
```

其中：

serverURL：数据接收地址的URL

configureURL：配置分发地址的URL

debugMode 有三种形式

  * `debugOff` - 关闭debug模式
  * `debugOnly` - 打开 Debug 模式，校验数据，但不进行数据导入
  * `debugAndTrack` - 打开 Debug 模式，校验数据，并将数据导入到 Sensors Analytics 中
 
<font color=red>注意：正式发布app时请将 `debugMode` 指定成 `debugOff` 模式！</font>

### 识别用户

在集成了 sensorsAnalyticsAPICloudSDK 模块的 App 中，SDK 会为每个设备随机分配一个唯一 ID（DistinctId）作为 **匿名 ID**，用于标记产生事件的未登录用户，并以此进行用户相关分析，如留存率、事件漏斗等。默认情况下，对于iOS sensorsAnalyticsAPICloudSDK 模块会优先使用 IDFV 作为 DistinctID，如果 IDFV 获取失败，则使用随机的 UUID。而Android默认获取 UUID 作为 DistinctID.

当一个用户 **注册成功** 或 **登录成功** 之后，可以通过 `login:` 方法设置用户的 **登录 ID** ，并将 **匿名 ID** 与 **登录 ID** 进行关联，以保证用户分析的准确性。

#### 示例代码

```js
document.getElementById('login').onclick = function() {
	var saAPICloud = api.require('sensorsAnalyticsAPICloudSDK');
	saAPICloud.login({
    	loginId: '123456'
	});
}
```

### 追踪事件
sensorsAnalyticsAPICloudSDK 模块成功初始化后，可以通过 `track:` 方法追踪用户行为事件，并为事件添加自定义属性。以电商产品为例，可以这样追踪一次购物行为：

```js
document.getElementById('track').onclick = function() {
	var saAPICloud = api.require('sensorsAnalyticsAPICloudSDK');
	saAPICloud.track({
    	event:'ViewProduct',
    	properties:{
       		ProductID:123456,
       		ProductCatalog:'Laptop Computer',
       		IsAddedToFav: false
    	}
	});
}
```

通过Debug模式，可以校验追踪的事件及属性是否正确。普通模式下，数据导入后，在 Sensors Analytics 中稍等片刻，便能看到追踪结果。请注意，*不要在正式发布的 App 中使用 Debug 模式*。

![](https://www.sensorsdata.cn/manual/img/ios_sdk_1.png)

### 手动同步数据

在`debugOff` 模式下每次调用 `track` 方法的时，Sensors Analytics SDK 会将事件与属性保存在 App 的存储空间中，并会检查如下条件，以判断是否向服务器上传数据:

 *  当前是否是 WIFI / 3G / 4G 网络
 *  是否满足发送事件之一:
   1. 与上次发送的时间间隔是否大于 15秒
   2. 本地缓存的事件条目数是否大于 100条
 
如果追求数据采集的时效性，可以调用 `flush` 方法，强制将数据发送到 Sensors Analytics，例如：

###示例代码

```js
document.getElementById('flush').onclick = function() {
	var saAPICloud = api.require('sensorsAnalyticsAPICloudSDK');
	saAPICloud.flush();
}
```

