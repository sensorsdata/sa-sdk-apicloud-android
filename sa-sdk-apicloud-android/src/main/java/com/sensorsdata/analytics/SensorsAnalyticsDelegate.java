package com.sensorsdata.analytics;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.sensorsdata.analytics.android.sdk.SAConfigOptions;
import com.sensorsdata.analytics.android.sdk.SensorsAnalyticsAutoTrackEventType;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.property.PluginVersionInterceptor;
import com.sensorsdata.analytics.property.SAPropertyManager;
import com.uzmap.pkg.uzcore.uzmodule.AppInfo;
import com.uzmap.pkg.uzcore.uzmodule.ApplicationDelegate;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yzk on 2018/10/10
 * Update  by chenru on 2021/04/22
 */

public class SensorsAnalyticsDelegate extends ApplicationDelegate {

    private static final String TAG = "SA.ApplicationDelegate";
    private static final String SA_FEATRURE = "sensorsAnalyticsAPICloudSDK";

    public SensorsAnalyticsDelegate() {
    }

    @Override
    public void onApplicationCreate(Context context, AppInfo info) {
        super.onApplicationCreate(context, info);

        String sa_serverURL = null;
        String sa_enableAutoTrack = null;
        String sa_enableLog = null;
        String sa_downloadChannel = null;
        String sa_flushInterval = null;
        String sa_flushBulkSize = null;
        String sa_maxCacheSize = null;
        String sa_minRequestInterval = null;
        String sa_maxRequestInterval = null;
        String sa_disableRandomTimeRequestRemoteConfig = null;
        String sa_encrypt = null;
        String sa_disableDataCollect = null;
        String sa_disableSDK = null;

        try {
            //获取配置信息
            sa_serverURL = info.getFeatureValue(SA_FEATRURE, "serverURL");
            sa_enableAutoTrack = info.getFeatureValue(SA_FEATRURE, "enableAutoTrack");
            sa_enableLog = info.getFeatureValue(SA_FEATRURE, "enableLog");
            sa_downloadChannel = info.getFeatureValue(SA_FEATRURE, "downloadChannel");
            sa_flushInterval = info.getFeatureValue(SA_FEATRURE, "flushInterval");
            sa_flushBulkSize = info.getFeatureValue(SA_FEATRURE, "flushBulkSize");
            sa_maxCacheSize = info.getFeatureValue(SA_FEATRURE, "maxCacheSizeAndroid");
            sa_minRequestInterval = info.getFeatureValue(SA_FEATRURE, "minRequestInterval");
            sa_maxRequestInterval = info.getFeatureValue(SA_FEATRURE, "maxRequestInterval");
            sa_disableRandomTimeRequestRemoteConfig = info.getFeatureValue(SA_FEATRURE, "disableRandomTimeRequestRemoteConfig");
            sa_encrypt = info.getFeatureValue(SA_FEATRURE, "encrypt");
            sa_disableDataCollect = info.getFeatureValue(SA_FEATRURE, "disableDataCollect");
            sa_disableSDK = info.getFeatureValue(SA_FEATRURE, "disableSDK");

        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "getFeatureValue error");
        }


        Log.i(TAG, "serverURL:" + (sa_serverURL == null ? "null" : sa_serverURL) + "\n"
                + "enableAutoTrack:" + (sa_enableAutoTrack == null ? "false" : sa_enableAutoTrack) + "\n"
                + "enableLog:" + (sa_enableLog == null ? "false" : sa_enableLog) + "\n"
                + "encrypt:" + (sa_encrypt == null ? "false" : sa_encrypt) + "\n"
                + "disableDataCollect:" + (sa_disableDataCollect == null ? "false" : sa_disableDataCollect) + "\n"
                + "downloadChannel:" + (sa_downloadChannel == null ? "null" : sa_downloadChannel) + "\n"
                + "flushInterval:" + (sa_flushInterval == null ? "null" : sa_flushInterval) + "\n"
                + "flushBulkSize:" + (sa_flushBulkSize == null ? "null" : sa_flushBulkSize) + "\n"
                + "maxCacheSize:" + (sa_maxCacheSize == null ? "null" : sa_maxCacheSize) + "\n"
                + "minRequestInterval:" + (sa_minRequestInterval == null ? "null" : sa_minRequestInterval) + "\n"
                + "maxRequestInterval:" + (sa_maxRequestInterval == null ? "null" : sa_maxRequestInterval) + "\n"
                + "disableRandomTimeRequestRemoteConfig:" + (sa_disableRandomTimeRequestRemoteConfig == null ? "false" : sa_disableRandomTimeRequestRemoteConfig));

        SAConfigOptions configOptions = new SAConfigOptions(sa_serverURL);

        // 设置两次数据发送的最小时间间隔，最小值 5 秒
        if (sa_flushInterval != null) {
            try {
                configOptions.setFlushInterval(Integer.parseInt(sa_flushInterval));
            } catch (Exception e) {
                //ignored
            }
        }

        // 设置本地缓存日志的最大条目数
        if (sa_flushBulkSize != null) {
            try {
                configOptions.setFlushBulkSize(Integer.parseInt(sa_flushBulkSize));
            } catch (Exception e) {
                //ignored
            }
        }

        // 设置本地缓存上限值，单位 MB，默认为 32MB，最小 16MB，若小于 16MB，则按 16MB 处理。
        if (sa_maxCacheSize != null) {
            try {
                configOptions.setMaxCacheSize(Integer.parseInt(sa_maxCacheSize) * 1024 * 1024);
            } catch (Exception e) {
                //ignored
            }
        }

        // 设置远程配置请求最小间隔时长
        if (sa_minRequestInterval != null) {
            try {
                configOptions.setMinRequestInterval(Integer.parseInt(sa_minRequestInterval));
            } catch (Exception e) {
                //ignored
            }
        }

        // 设置远程配置请求最大间隔时长
        if (sa_maxRequestInterval != null) {
            try {
                configOptions.setMaxRequestInterval(Integer.parseInt(sa_maxRequestInterval));
            } catch (Exception e) {
                //ignored
            }
        }

        // 采集启动、退出事件
        if (Boolean.parseBoolean(sa_enableAutoTrack)) {
            configOptions.setAutoTrackEventType(SensorsAnalyticsAutoTrackEventType.APP_START |
                    SensorsAnalyticsAutoTrackEventType.APP_END);
        }

        // 禁用分散请求远程配置
        if (Boolean.parseBoolean(sa_disableRandomTimeRequestRemoteConfig)) {
            configOptions.disableRandomTimeRequestRemoteConfig();
        }

        // 开启加密
        if (Boolean.parseBoolean(sa_encrypt)) {
            configOptions.enableEncrypt(true);
        }

        // 开启合规
        if (Boolean.parseBoolean(sa_disableDataCollect)) {
//            configOptions.disableDataCollect();
        }

        // 开启日志
        if (Boolean.parseBoolean(sa_enableLog)) {
            configOptions.enableLog(true);
        }
        // 关闭 SDK
        if (Boolean.parseBoolean(sa_disableSDK)) {
            configOptions.disableSDK(true);
        }

        Log.i(TAG, "onApplicationCreate");
        // 初始化 SDK
        SensorsDataAPI.startWithConfigOptions(context, configOptions);
        // 注册插件版本号信息拦截器
        SAPropertyManager.addInterceptor(new PluginVersionInterceptor());
        // 激活事件
        try {
            if (!TextUtils.isEmpty(sa_downloadChannel)) {
                SensorsDataAPI.sharedInstance().trackInstallation("AppInstall", new JSONObject().put("DownloadChannel", sa_downloadChannel));
            } else {
                Log.i(TAG, "downloadChannel 为空");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
