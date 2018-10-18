package com.sensorsdata.analytics;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.ModuleResult;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;
import android.text.TextUtils;
import android.util.Log;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by yzk on 2018/10/10
 */

public class SensorsAnalyticsAPICloudSDK extends UZModule {

    private static final String TAG = "SA.APICloudSDK";
    private static final String VERSION = "1.1.0";

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
     *                                    Debug模式（类型为String,有3种模式，分别为"debugOff"或"debugOnly"或"debugAndTrack"）
     *                                    "debugOff" 关闭 Debug 模式。 "debugOnly" 打开 Debug 模式，校验数据，但不进行数据入库。
     *                                    "debugAndTrack" 打开 Debug 模式，校验数据，并将数据导入库到 Sensors Analytics 中。
     *                                    注意！请不要在正式发布的 App 中使用 "debugOnly" 或 "debugAndTrack" 模式！ 要使用
     *                                    "debugOff" 这个模式。
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
                SensorsDataAPI.sharedInstance().login(loginId,properties);
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
     *                                    组成，且不能以数字开头） properties 事件的属性
     */
    public void jsmethod_track(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String eventName = sensorsAnalyticsAPICloudSDK.optString("event");
            JSONObject properties = sensorsAnalyticsAPICloudSDK.optJSONObject("properties");
            if (TextUtils.isEmpty(eventName)) {
                Log.i(TAG, "event(事件名)为空，请检查传入值");
                return;
            }
            SensorsDataAPI.sharedInstance().track(eventName, properties);
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
     *                                    properties 页面的属性
     *
     */
    public void jsmethod_trackViewScreen(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String url = sensorsAnalyticsAPICloudSDK.optString("url");
            if(TextUtils.isEmpty(url)){
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
     */
    public void jsmethod_trackTimerStart(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String eventName = sensorsAnalyticsAPICloudSDK.optString("event");
            if (TextUtils.isEmpty(eventName)) {
                Log.i(TAG, "event(事件名)为空，请检查传入值");
                return;
            }
            SensorsDataAPI.sharedInstance().trackTimerStart(eventName);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     *                                    组成，且不能以数字开头） properties 事件的属性
     */
    public void jsmethod_trackTimerEnd(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
        try {
            String eventName = sensorsAnalyticsAPICloudSDK.optString("event");
            JSONObject properties = sensorsAnalyticsAPICloudSDK.optJSONObject("properties");
            if (TextUtils.isEmpty(eventName)) {
                Log.i(TAG, "event(事件名)为空，请检查传入值");
                return;
            }
            SensorsDataAPI.sharedInstance().trackTimerEnd(eventName, properties);
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
     *                                    组成，且不能以数字开头） properties 事件的属性
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
     * @param sensorsAnalyticsAPICloudSDK  properties 用户的属性
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
     * @param sensorsAnalyticsAPICloudSDK  properties 用户的属性
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
            if(null==properties){
                Log.i(TAG, "profileIncrement properties 为空，请检查传入值");
                return;
            }
            HashMap<String, Number> hashMap = new HashMap<String, Number>();
            Iterator iterable = properties.keys();
            while (iterable.hasNext()){
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
            if (TextUtils.isEmpty(property)||null==value) {
                Log.i(TAG, "profileAppend  property/value 为空，请检查传入值");
                return;
            }
            HashSet<String> hashSet = new HashSet<String>();
            for(int i=0;i<value.length();i++){
                String str = value.optString(i);
                if(!TextUtils.isEmpty(str)){
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
     *
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
        SensorsDataAPI.sharedInstance().flush();
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
        SensorsDataAPI.sharedInstance().deleteAll();
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
        String serverUrl = sensorsAnalyticsAPICloudSDK.optString("serverUrl");
        if (TextUtils.isEmpty(serverUrl)) {
            Log.i(TAG, "setServerUrl serverUrl 为空，请检查传入值");
            return;
        }
        SensorsDataAPI.sharedInstance().setServerUrl(serverUrl);
    }

}
