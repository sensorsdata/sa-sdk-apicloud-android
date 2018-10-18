package com.sensorsdata.analytics;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.uzmap.pkg.uzcore.uzmodule.AppInfo;
import com.uzmap.pkg.uzcore.uzmodule.ApplicationDelegate;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzk on 2018/10/10
 */

public class SensorsAnalyticsDelegate extends ApplicationDelegate {

    private static final String TAG = "SA.ApplicationDelegate";
    private static final String SA_FEATRURE = "sensorsAnalyticsAPICloudSDK";
    private static final String VERSION = "1.1.0";

    public SensorsAnalyticsDelegate() {
    }

    @Override
    public void onApplicationCreate(Context context, AppInfo info) {
        super.onApplicationCreate(context, info);

        String sa_serverURL = null;
        String sa_token = null;
        String sa_debugMode = null;
        String sa_enableAutoTrack = null;
        String sa_enableLog = null;
        String sa_downloadChannel = null;

        try {
            //获取配置信息
            sa_serverURL = info.getFeatureValue(SA_FEATRURE, "serverURL");
            sa_token = info.getFeatureValue(SA_FEATRURE, "token");
            sa_debugMode = info.getFeatureValue(SA_FEATRURE, "debugMode");
            sa_enableAutoTrack = info.getFeatureValue(SA_FEATRURE, "enableAutoTrack");
            sa_enableLog = info.getFeatureValue(SA_FEATRURE, "enableLog");
            sa_downloadChannel = info.getFeatureValue(SA_FEATRURE, "downloadChannel");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG,"getFeatureValue error");
        }

        // 校验 debugMode，如果是老用户没有做 feature 配置，下边会 return
        SensorsDataAPI.DebugMode debugMode;
        if ("debugOff".equals(sa_debugMode)) {
            debugMode = SensorsDataAPI.DebugMode.DEBUG_OFF;
        } else if ("debugOnly".equals(sa_debugMode)) {
            debugMode = SensorsDataAPI.DebugMode.DEBUG_ONLY;
        } else if ("debugAndTrack".equals(sa_debugMode)) {
            debugMode = SensorsDataAPI.DebugMode.DEBUG_AND_TRACK;
        } else {
            Log.i(TAG, "debugMode 模式传入错误，取值只能为：debugOff、debugOnly、debugAndTrack，请检查传入的模式");
            return;
        }

        // 如果有 token 拼接给 serverURL
        String serverURL = "";
        if (!TextUtils.isEmpty(sa_token)) {
            serverURL = sa_serverURL + "&token=" + sa_token;
        } else {
            serverURL = sa_serverURL;
        }

        Log.i(TAG,"onApplicationCreate");
        // 初始化 SDK
        SensorsDataAPI.sharedInstance(context, // 传入 Context
                serverURL, // 数据接收的 URL
                debugMode); // Debug 模式选项

        // 激活事件
        try {
            if(!TextUtils.isEmpty(sa_downloadChannel)){
                SensorsDataAPI.sharedInstance().trackInstallation("AppInstall",new JSONObject().put("DownloadChannel",sa_downloadChannel));
            }else {
                Log.i(TAG,"downloadChannel 为空");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 采集启动、退出事件
        if (Boolean.parseBoolean(sa_enableAutoTrack)) {
            try {
                List<SensorsDataAPI.AutoTrackEventType> eventTypeList = new ArrayList<>();
                // $AppStart
                eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_START);
                // $AppEnd
                eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_END);
                SensorsDataAPI.sharedInstance().enableAutoTrack(eventTypeList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 日志
        SensorsDataAPI.sharedInstance().enableLog(Boolean.parseBoolean(sa_enableLog));

    }
}
