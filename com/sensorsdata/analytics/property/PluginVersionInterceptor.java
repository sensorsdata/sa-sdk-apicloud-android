package com.sensorsdata.analytics.property;

import com.sensorsdata.analytics.SensorsAnalyticsAPICloudSDK;
import com.sensorsdata.analytics.property.SAPropertyManager.Interceptor;

import org.json.JSONArray;
import org.json.JSONObject;

public class PluginVersionInterceptor implements Interceptor {
    private static boolean isMergePluginVersion = false;

    public JSONObject proceed(JSONObject properties, boolean isAuto) {
        if (!isMergePluginVersion) {
            if (properties == null) {
                properties = new JSONObject();
            } else if (properties.has("$lib_plugin_version")) {
                return properties;
            }
            try {
                JSONArray array = new JSONArray();
                array.put("apicloud:" + SensorsAnalyticsAPICloudSDK.VERSION);
                properties.put("$lib_plugin_version", array);
            } catch (Exception ignored) {
                //ignore
            }
            isMergePluginVersion = true;
        }
        return properties;
    }
}