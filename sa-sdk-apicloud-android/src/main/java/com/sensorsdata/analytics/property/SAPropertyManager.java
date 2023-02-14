package com.sensorsdata.analytics.property;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SAPropertyManager {

    public static List<Interceptor> interceptors = new ArrayList<>();

    public static void addInterceptor(Interceptor interceptor){
        interceptors.add(interceptor);
    }

    public static JSONObject mergeProperty(JSONObject properties){
        return mergeProperty(properties, false);
    }

    public static JSONObject mergeProperty(JSONObject properties, boolean isAuto){
        for(Interceptor interceptor:interceptors){
            properties = interceptor.proceed(properties, isAuto);
        }
        return properties;
    }

    interface Interceptor{
        JSONObject proceed(JSONObject properties, boolean isAuto);
    }
}