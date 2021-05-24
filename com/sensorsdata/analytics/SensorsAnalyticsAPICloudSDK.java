package com.sensorsdata.analytics;

import android.text.TextUtils;
import android.util.Log;

import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.SensorsNetworkType;
import com.sensorsdata.analytics.property.SAPropertyManager;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.ModuleResult;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by yzk on 2018/10/10
 * Update  by chenru on 2021/04/22
 */

public class SensorsAnalyticsAPICloudSDK extends UZModule {

    private static final String TAG = "SA.APICloudSDK";
    public static final String VERSION = "2.0.0";

    public SensorsAnalyticsAPICloudSDK(UZWebView webView) {
        super(webView);
    }

    @Override
    protected void onClean() {
        super.onClean();
    }

    /**
     * Sensors Analytics SDK 初始化。
     * <p>
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 sharedInstance 函数，完成 Sensors Analytics SDK 初始化<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * sa = api.require('sensorsAnalyticsAPICloudSDK');
     * <p>
     * var argument = { sa_server_url: "【数据接收地址】", debugMode: "【Debug模式】" };
     * sa.sharedInstance(argument);
     *
     * @param sensorsAnalyticsAPICloudSDK (Required) serverURL 用于收集事件的服务地址（类型为String） debugMode
     * Debug模式（类型为String,有3种模式，分别为"debugOff"或"debugOnly"或"debugAndTrack"）
     * "debugOff" 关闭 Debug 模式。 "debugOnly" 打开 Debug 模式，校验数据，但不进行数据入库。
     * "debugAndTrack" 打开 Debug 模式，校验数据，并将数据导入库到 Sensors Analytics 中。
     * 注意！请不要在正式发布的 App 中使用 "debugOnly" 或 "debugAndTrack" 模式！ 要使用
     * "debugOff" 这个模式。
     */

    public void jsmethod_sharedInstance(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String sa_server_url = sensorsAnalyticsAPICloudSDK.optString("serverURL");
            //String sa_configure_url = sensorsAnalyticsAPICloudSDK.optString("configureURL");
            String sa_debug_mode = sensorsAnalyticsAPICloudSDK.optString("debugMode");

            SensorsDataAPI.DebugMode debugMode = null;
            // 校验debugMode
            if ("debugOff".equals(sa_debug_mode)) {
                debugMode = SensorsDataAPI.DebugMode.DEBUG_OFF;
            } else if ("debugOnly".equals(sa_debug_mode)) {
                debugMode = SensorsDataAPI.DebugMode.DEBUG_ONLY;
            } else if ("debugAndTrack".equals(sa_debug_mode)) {
                debugMode = SensorsDataAPI.DebugMode.DEBUG_AND_TRACK;
            } else {
                Log.i(TAG, "debugMode模式传入错误，取值只能为：debugOff、debugOnly、debugAndTrack，请检查传入的模式");
                return;
            }

            SensorsDataAPI.sharedInstance(mContext, // 传入 Context
                    sa_server_url, // 数据接收的 URL
                    debugMode); // Debug 模式选项
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 login 函数，设置当前用户的 loginId <br>
     * <br>
     * <strong>JS Example：</strong><br>
     * var argument={loginId:"你们服务端分配的用户ID",properties:{}}; sa.login(argument);
     *
     * @param sensorsAnalyticsAPICloudSDK (Required) loginId 前用户的loginId , properties 事件属性
     */
    public void jsmethod_login(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String loginId = sensorsAnalyticsAPICloudSDK.optString("loginId");
            JSONObject properties = sensorsAnalyticsAPICloudSDK.optJSONObject("properties");
            if (!TextUtils.isEmpty(loginId)) {
                SensorsDataAPI.sharedInstance().login(loginId, SAPropertyManager.mergeProperty(properties));
            } else {
                Log.i(TAG, "loginId（登录ID)为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 track 函数，追踪一个带有属性的事件<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * var argument={event:"your event name",properties:{ ProductID:123456,
     * ProductCatalog:'Laptop Computer', IsAddedToFav: false }};
     * sa.track(argument);
     *
     * @param sensorsAnalyticsAPICloudSDK (Required) event 事件的名称,事件名的命名要符合变量命名规范（即由 字母，数字，_,$
     * 组成，且不能以数字开头） properties 事件的属性
     */
    public void jsmethod_track(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String eventName = sensorsAnalyticsAPICloudSDK.optString("event");
            JSONObject properties = sensorsAnalyticsAPICloudSDK.optJSONObject("properties");
            if (TextUtils.isEmpty(eventName)) {
                Log.i(TAG, "event(事件名)为空，请检查传入值");
                return;
            }
            SensorsDataAPI.sharedInstance().track(eventName, SAPropertyManager.mergeProperty(properties));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 trackViewScreen 函数，触发 $AppViewScreen 事件<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * var argument={url:"https://www.sensorsdata.cn/manual/apicloud_sdk.html",properties:{"$title":"主页","$screen_name":"cn.sensorsdata.demo.HomePage"}};
     * sa.trackViewScreen(argument);
     *
     * @param sensorsAnalyticsAPICloudSDK (Required) 页面的 url  记录到 $url 字段中(如果不需要此属性，可以传 null )
     * properties 页面的属性
     */
    public void jsmethod_trackViewScreen(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String url = sensorsAnalyticsAPICloudSDK.optString("url");
            if (TextUtils.isEmpty(url)) {
                url = null;
            }
            JSONObject properties = sensorsAnalyticsAPICloudSDK.optJSONObject("properties");
            SensorsDataAPI.sharedInstance().trackViewScreen(url, properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 trackTimerStart 函数，计时开始<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * var argument={event:"your event name"};
     * sa.trackTimerStart(argument);
     *
     * @param sensorsAnalyticsAPICloudSDK (Required) event 事件的名称
     * @return 交叉计时的事件名称
     */
    public ModuleResult jsmethod_trackTimerStart_sync(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String eventName = sensorsAnalyticsAPICloudSDK.optString("event");
            if (TextUtils.isEmpty(eventName)) {
                Log.i(TAG, "event(事件名)为空，请检查传入值");
            } else {
                return new ModuleResult(SensorsDataAPI.sharedInstance().trackTimerStart(eventName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModuleResult("");
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 trackTimerEnd 函数，计时结束<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * var argument={event:"your event name",properties:{}};
     * sa.trackTimerEnd(argument);
     *
     * @param sensorsAnalyticsAPICloudSDK (Required) event 事件的名称,事件名的命名要符合变量命名规范（即由 字母，数字，_,$
     * 组成，且不能以数字开头） properties 事件的属性
     */
    public void jsmethod_trackTimerEnd(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String eventName = sensorsAnalyticsAPICloudSDK.optString("event");
            JSONObject properties = sensorsAnalyticsAPICloudSDK.optJSONObject("properties");
            if (TextUtils.isEmpty(eventName)) {
                Log.i(TAG, "event(事件名)为空，请检查传入值");
                return;
            }
            SensorsDataAPI.sharedInstance().trackTimerEnd(eventName, SAPropertyManager.mergeProperty(properties));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 clearTrackTimer 函数，清除所有计时器<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * <p>
     * sa.clearTrackTimer();
     */
    public void jsmethod_clearTrackTimer(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            SensorsDataAPI.sharedInstance().clearTrackTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 logout 函数，清除登录ID<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * <p>
     * sa.logout();
     */
    public void jsmethod_logout(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            SensorsDataAPI.sharedInstance().logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 profileDelete 函数，删除此用户所有用户属性<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * <p>
     * sa.profileDelete();
     */
    public void jsmethod_profileDelete(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            SensorsDataAPI.sharedInstance().profileDelete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript中 sa 对象的 trackInstallation 函数，记录激活事件(AppInstall)、渠道追踪<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * var argument={properties:{ DownloadChannel:"具体的下载渠道"}};
     * sa.trackInstallation(argument);
     *
     * @param sensorsAnalyticsAPICloudSDK (Required) 事件名的命名要符合变量命名规范（即由 字母，数字，_,$
     * 组成，且不能以数字开头） properties 事件的属性
     */
    public void jsmethod_trackInstallation(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            JSONObject properties = sensorsAnalyticsAPICloudSDK.optJSONObject("properties");
            SensorsDataAPI.sharedInstance().trackInstallation("AppInstall", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 profileSet 函数，设置用户属性<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * var argument={properties:{ sex:"男"}};
     * sa.profileSet(argument);
     *
     * @param sensorsAnalyticsAPICloudSDK properties 用户的属性
     */
    public void jsmethod_profileSet(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            JSONObject properties = sensorsAnalyticsAPICloudSDK.optJSONObject("properties");
            SensorsDataAPI.sharedInstance().profileSet(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 profileSetOnce 函数，设置用户首次属性<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * var argument={properties:{ firstCharge:100}};
     * sa.profileSetOnce(argument);
     *
     * @param sensorsAnalyticsAPICloudSDK properties 用户的属性
     */
    public void jsmethod_profileSetOnce(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            JSONObject properties = sensorsAnalyticsAPICloudSDK.optJSONObject("properties");
            SensorsDataAPI.sharedInstance().profileSetOnce(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript中 sa 对象的 profileIncrement 函数，给一个数值类型的 Profile 增加一个数值. 只能对数值型属性进行操作，若该属性
     * 未设置，则添加属性并设置默认值为0<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * var argument={properties:{money:10,phone:50}};
     * sa.profileIncrement(argument);
     *
     * @param sensorsAnalyticsAPICloudSDK property 属性名，value 属性的值，值的类型只允许为 Number .
     */
    public void jsmethod_profileIncrement(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            JSONObject properties = sensorsAnalyticsAPICloudSDK.optJSONObject("properties");
            if (null == properties) {
                Log.i(TAG, "profileIncrement properties 为空，请检查传入值");
                return;
            }
            HashMap<String, Number> hashMap = new HashMap<String, Number>();
            Iterator iterable = properties.keys();
            while (iterable.hasNext()) {
                String key = iterable.next().toString();
                Double value = properties.optDouble(key);
                hashMap.put(key, value);
            }
            SensorsDataAPI.sharedInstance().profileIncrement(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript中 sa 对象的 profileAppend 函数，给 Profile 增加一个元素
     * <br>
     * <strong>JS Example：</strong><br>
     * var argument={property:"VIP",value:["GOD","DDD"]};
     * sa.profileAppend(argument);
     *
     * @param sensorsAnalyticsAPICloudSDK property 属性名，value 属性的值，值的类型只允许为 String .
     */
    public void jsmethod_profileAppend(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String property = sensorsAnalyticsAPICloudSDK.optString("property");
            JSONArray value = sensorsAnalyticsAPICloudSDK.optJSONArray("value");
            if (TextUtils.isEmpty(property) || null == value) {
                Log.i(TAG, "profileAppend  property/value 为空，请检查传入值");
                return;
            }
            HashSet<String> hashSet = new HashSet<String>();
            for (int i = 0; i < value.length(); i++) {
                String str = value.optString(i);
                if (!TextUtils.isEmpty(str)) {
                    hashSet.add(str);
                }
            }
            SensorsDataAPI.sharedInstance().profileAppend(property, hashSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 profileUnset 函数，删除用户属性<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * var argument={property:"sex"};
     * sa.profileUnset(argument);
     *
     * @param sensorsAnalyticsAPICloudSDK (Required)
     */
    public void jsmethod_profileUnset(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String property = sensorsAnalyticsAPICloudSDK.optString("property");
            if (TextUtils.isEmpty(property)) {
                Log.i(TAG, "profileUnset property 为空，请检查传入值");
                return;
            }
            SensorsDataAPI.sharedInstance().profileUnset(property);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 identify 函数，设置当前用户的 anonymousId <br>
     * <br>
     * <strong>JS Example：</strong><br>
     * var argument={anonymousId:"你们自己的匿名 ID"}; sa.identify(argument);
     *
     * @param sensorsAnalyticsAPICloudSDK (Required) loginId 前用户的loginId
     */
    public void jsmethod_identify(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String anonymousId = sensorsAnalyticsAPICloudSDK.optString("anonymousId");
            if (!TextUtils.isEmpty(anonymousId)) {
                SensorsDataAPI.sharedInstance().identify(anonymousId);
            } else {
                Log.i(TAG, "identify anonymousId（匿名ID)为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 registerSuperProperties 函数，设置公共属性<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * var argument={properties:{ PlatformType:"Android"}};
     * sa.registerSuperProperties(argument);
     *
     * @param sensorsAnalyticsAPICloudSDK properties 事件的属性
     */
    public void jsmethod_registerSuperProperties(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            JSONObject properties = sensorsAnalyticsAPICloudSDK.optJSONObject("properties");
            SensorsDataAPI.sharedInstance().registerSuperProperties(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 unregisterSuperProperty 函数，删除事件公共属性<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * var argument={property:"PlatformType"};
     * sa.unregisterSuperProperty(argument);
     *
     * @param sensorsAnalyticsAPICloudSDK property 事件的属性
     */
    public void jsmethod_unregisterSuperProperty(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String property = sensorsAnalyticsAPICloudSDK.optString("property");
            if (TextUtils.isEmpty(property)) {
                Log.i(TAG, "unregisterSuperProperty property 为空，请检查传入值");
                return;
            }
            SensorsDataAPI.sharedInstance().unregisterSuperProperty(property);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 enableLog 函数，删除事件公共属性<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * var argument={enableLog:true};
     * sa.enableLog(argument);
     *
     * @param sensorsAnalyticsAPICloudSDK property 事件的属性
     */
    public void jsmethod_enableLog(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            boolean b = sensorsAnalyticsAPICloudSDK.optBoolean("enableLog");
            SensorsDataAPI.sharedInstance().enableLog(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 getDistinctId 函数，获取 当前的 DistinctId<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * <p>
     * var distinctId = sa.getDistinctId();
     */
    public ModuleResult jsmethod_getDistinctId_sync(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String mLoginId = SensorsDataAPI.sharedInstance().getLoginId();
            if (!TextUtils.isEmpty(mLoginId)) {
                return new ModuleResult(mLoginId);
            } else {
                return new ModuleResult(SensorsDataAPI.sharedInstance().getAnonymousId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModuleResult("");
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 flush 函数，将所有本地缓存的日志发送到 Sensors Analytics<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * sa.flush();
     */
    public void jsmethod_flush(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            SensorsDataAPI.sharedInstance().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 deleteAll 函数，删除本地所有缓存数据<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * sa.deleteAll();
     */
    public void jsmethod_deleteAll(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            SensorsDataAPI.sharedInstance().deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 setServerUrl 函数，设置数据接收地址<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * sa.setServerUrl({serverUrl:"数据接收地址"});
     */
    public void jsmethod_setServerUrl(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String serverUrl = sensorsAnalyticsAPICloudSDK.optString("serverUrl");
            if (TextUtils.isEmpty(serverUrl)) {
                Log.i(TAG, "setServerUrl serverUrl 为空，请检查传入值");
                return;
            }
            SensorsDataAPI.sharedInstance().setServerUrl(serverUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 trackTimerPause 函数，暂停事件计时器，计时单位为秒。<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * sa.trackTimerPause({eventName:"需要暂停计时的事件名称"});
     */
    public void jsmethod_trackTimerPause(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String event = sensorsAnalyticsAPICloudSDK.optString("event");
            if (TextUtils.isEmpty(event)) {
                Log.i(TAG, "event 为空，请检查传入值");
                return;
            }
            SensorsDataAPI.sharedInstance().trackTimerPause(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 trackTimerResume 函数，恢复事件计时器，计时单位为秒。<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * sa.trackTimerResume({eventName:"需要恢复计时的事件名称"});
     */
    public void jsmethod_trackTimerResume(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String event = sensorsAnalyticsAPICloudSDK.optString("event");
            if (TextUtils.isEmpty(event)) {
                Log.i(TAG, "event 为空，请检查传入值");
                return;
            }
            SensorsDataAPI.sharedInstance().trackTimerResume(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 profilePushId 函数，保存用户推送 ID 到用户表。<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * sa.profilePushId({pushTypeKey:"key",pushId:"pushId"});
     */
    public void jsmethod_profilePushId(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String pushTypeKey = sensorsAnalyticsAPICloudSDK.optString("pushTypeKey");
            String pushId = sensorsAnalyticsAPICloudSDK.optString("pushId");
            SensorsDataAPI.sharedInstance().profilePushId(pushTypeKey, pushId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 profileUnsetPushId 函数，删除用户设置的 pushId。<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * sa.profileUnsetPushId({pushTypeKey:"key"});
     */
    public void jsmethod_profileUnsetPushId(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String pushTypeKey = sensorsAnalyticsAPICloudSDK.optString("pushTypeKey");
            SensorsDataAPI.sharedInstance().profileUnsetPushId(pushTypeKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 clearSuperProperties 函数，删除所有事件公共属性。<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * sa.clearSuperProperties();
     */
    public void jsmethod_clearSuperProperties(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            SensorsDataAPI.sharedInstance().clearSuperProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 getSuperProperties 函数，获取事件公共属性。<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * sa.getSuperProperties();
     */
    public ModuleResult jsmethod_getSuperProperties_sync(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            return new ModuleResult(SensorsDataAPI.sharedInstance().getSuperProperties());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModuleResult();
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 itemSet 函数，设置 item。<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * var argument={itemType:"type",itemId:"itemId",properties:{ itemName:"苹果"}};
     * sa.itemSet(argument);
     */
    public void jsmethod_itemSet(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String itemType = sensorsAnalyticsAPICloudSDK.optString("itemType");
            String itemId = sensorsAnalyticsAPICloudSDK.optString("itemId");
            JSONObject properties = sensorsAnalyticsAPICloudSDK.optJSONObject("properties");
            SensorsDataAPI.sharedInstance().itemSet(itemType, itemId, properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 itemDelete 函数，删除 item 信息。<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * var argument={itemType:"type",itemId:"itemId"};
     * sa.itemDelete(argument);
     */
    public void jsmethod_itemDelete(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String itemType = sensorsAnalyticsAPICloudSDK.optString("itemType");
            String itemId = sensorsAnalyticsAPICloudSDK.optString("itemId");
            SensorsDataAPI.sharedInstance().itemDelete(itemType, itemId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 getPresetProperties 函数，获取预置属性。<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * sa.getPresetProperties();
     */
    public ModuleResult jsmethod_getPresetProperties_sync(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            return new ModuleResult(SensorsDataAPI.sharedInstance().getPresetProperties());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModuleResult();
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 getLoginId 函数，获取 LoginId。<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * sa.getLoginId();
     */
    public ModuleResult jsmethod_getLoginId_sync(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            return new ModuleResult(SensorsDataAPI.sharedInstance().getLoginId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModuleResult();
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 isAutoTrackEnabled 函数，获取全埋点开启状态。<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * sa.isAutoTrackEnabled();
     */
    public ModuleResult jsmethod_isAutoTrackEnabled_sync(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            return new ModuleResult(SensorsDataAPI.sharedInstance().isAutoTrackEnabled());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModuleResult();
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 setFlushNetworkPolicy 函数<br>
     * 设置 flush 时网络发送策略，默认 3G、4G、WI-FI 环境下都会尝试 flush
     * TYPE_NONE = 0;//NULL
     * TYPE_2G = 1;//2G
     * TYPE_3G = 1 << 1;//3G 2
     * TYPE_4G = 1 << 2;//4G 4
     * TYPE_WIFI = 1 << 3;//WIFI 8
     * TYPE_5G = 1 << 4;//5G 16
     * TYPE_ALL = 0xFF;//ALL 255
     * 例：若需要开启 4G 5G 发送数据，则需要设置 4 + 16 = 20
     * <br>
     * <strong>JS Example：</strong><br>
     * sa.setFlushNetworkPolicy({networkPolicy:20});
     */
    public void jsmethod_setFlushNetworkPolicy(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            SensorsDataAPI.sharedInstance().setFlushNetworkPolicy(sensorsAnalyticsAPICloudSDK.optInt("networkPolicy",SensorsNetworkType.TYPE_3G | SensorsNetworkType.TYPE_4G | SensorsNetworkType.TYPE_WIFI | SensorsNetworkType.TYPE_5G));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript中 sa 对象的 trackInstallation 函数，记录激活事件(AppInstall)、渠道追踪<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * var argument={properties:{ DownloadChannel:"具体的下载渠道"}};
     * sa.trackInstallation(argument);
     *
     * @param sensorsAnalyticsAPICloudSDK (Required) 事件名的命名要符合变量命名规范（即由 字母，数字，_,$
     * 组成，且不能以数字开头） properties 事件的属性
     */
    public void jsmethod_trackAppInstall(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            JSONObject properties = sensorsAnalyticsAPICloudSDK.optJSONObject("properties");
            SensorsDataAPI.sharedInstance().trackInstallation("$AppInstall", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 enableNetWorkRequest 函数，设置是否允许请求网络，默认是 true。<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * sa.enableNetWorkRequest({isRequest: false });
     */
    public void jsmethod_enableNetWorkRequest(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            SensorsDataAPI.sharedInstance().enableNetworkRequest(sensorsAnalyticsAPICloudSDK.optBoolean("isRequest", true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 isNetworkRequestEnable 函数，是否允许请求网络。<br>
     * <br>
     * <strong>JS Example：</strong><br>
     * sa.isNetworkRequestEnable();
     */
    public ModuleResult jsmethod_isNetworkRequestEnable_sync(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            return new ModuleResult(SensorsDataAPI.sharedInstance().isNetworkRequestEnable());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModuleResult();
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 setSessionIntervalTime 函数。<br>
     * 设置 App 切换到后台与下次事件的事件间隔
     * 默认值为 30*1000 毫秒
     * 若 App 在后台超过设定事件，则认为当前 Session 结束，发送 $AppEnd 事件
     * <br>
     * <strong>JS Example：</strong><br>
     * sa.setSessionIntervalTime(30000);
     */
    public void jsmethod_setSessionIntervalTime(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            SensorsDataAPI.sharedInstance().setSessionIntervalTime(sensorsAnalyticsAPICloudSDK.optInt("sessionIntervalTime"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 enableDataCollect 函数。<br>
     * 开启数据采集
     * <br>
     * <strong>JS Example：</strong><br>
     * sa.enableDataCollect();
     */
    public void jsmethod_enableDataCollect(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            SensorsDataAPI.sharedInstance().enableDataCollect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>函数</strong><br>
     * <br>
     * 该函数映射至 Javascript 中 sa 对象的 getSessionIntervalTime 函数。<br>
     * 获取 App 切换到后台与下次事件的事件间隔
     * 默认值为 30*1000 毫秒
     * 若 App 在后台超过设定事件，则认为当前 Session 结束，发送 $AppEnd 事件
     * <br>
     * <strong>JS Example：</strong><br>
     * sa.getSessionIntervalTime();
     */
    public ModuleResult jsmethod_getSessionIntervalTime_sync(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            return new ModuleResult(SensorsDataAPI.sharedInstance().getSessionIntervalTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModuleResult();
    }
}