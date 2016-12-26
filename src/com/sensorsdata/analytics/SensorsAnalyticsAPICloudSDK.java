package com.sensorsdata.analytics;

import org.json.JSONObject;

import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.exceptions.InvalidDataException;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class SensorsAnalyticsAPICloudSDK extends UZModule {
	private static final String TAG = "SensorsAnalyticsAPICloudSDK";

	public SensorsAnalyticsAPICloudSDK(UZWebView webView) {
		super(webView);
	}

	@Override
	protected void onClean() {
		super.onClean();
	}

	/**
	 * Sensors Analytics SDK 初始化。
	 * 
	 * <strong>函数</strong><br>
	 * <br>
	 * 该函数映射至Javascript中sa对象的sharedInstance函数，完成Sensors Analytics SDK 初始化<br>
	 * <br>
	 * <strong>JS Example：</strong><br>
	 * sa = api.require('sensorsAnalyticsAPICloudSDK');
	 * 
	 * var argument = { sa_server_url: "your server url", sa_configure_url:
	 * "your configure url", debugMode: "your debug mode" };
	 * sa.sharedInstance(argument);
	 * 
	 * @param sensorsAnalyticsAPICloudSDK
	 *            (Required) serverURL 用于收集事件的服务地址（类型为String） configureURL
	 *            用于获取SDK配置的服务地址（类型为String） debugMode
	 *            Debug模式（类型为String,有3种模式，分别为"debugOff"或"debugOnly"或"debugAndTrack"）
	 *            "debugOff" 关闭 Debug 模式。 "debugOnly" 打开 Debug 模式，校验数据，但不进行数据入库。
	 *            "debugAndTrack" 打开 Debug 模式，校验数据，并将数据导入库到 Sensors Analytics 中。
	 *            注意！请不要在正式发布的 App 中使用 "debugOnly" 或 "debugAndTrack" 模式！ 要使用
	 *            "debugOff" 这个模式。
	 */

	public void jsmethod_sharedInstance(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
		try {
			String sa_server_url = sensorsAnalyticsAPICloudSDK.optString("serverURL");
			String sa_configure_url = sensorsAnalyticsAPICloudSDK.optString("configureURL");
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

			// 校验serverURL
			if (TextUtils.isEmpty(sa_server_url)) {
				if (isDebuggable(debugMode)) {
					Toast.makeText(mContext, "serverURL(收集事件的服务地址)为空,请检查传入值", Toast.LENGTH_SHORT).show();
				} else {
					Log.i(TAG, "serverURL(收集事件的服务地址)为空,请检查传入值");
				}
				return;
			}
			// 校验configureURl
			if (TextUtils.isEmpty(sa_configure_url)) {
				if (isDebuggable(debugMode)) {
					Toast.makeText(mContext, "configureURl(用于获取SDK配置的服务地址)为空，请检查传入值", Toast.LENGTH_SHORT).show();
				} else {
					Log.i(TAG, "configureURl(用于获取SDK配置的服务地址)为空，请检查传入值");
				}
				return;
			}

			SensorsDataAPI.sharedInstance(mContext, // 传入 Context
					sa_server_url, // 数据接收的 URL
					sa_configure_url, // 配置分发的 URL
					debugMode); // Debug 模式选项
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <strong>函数</strong><br>
	 * <br>
	 * 该函数映射至Javascript中sa对象的login函数，设置当前用户的loginId<br>
	 * <br>
	 * <strong>JS Example：</strong><br>
	 * var argument={loginId:"your login id"}; sa.login(argument);
	 * 
	 * @param sensorsAnalyticsAPICloudSDK
	 *            (Required) loginId 前用户的loginId
	 */
	public void jsmethod_login(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
		try {
			String loginId = sensorsAnalyticsAPICloudSDK.optString("loginId");
			if (!TextUtils.isEmpty(loginId)) {
				SensorsDataAPI.sharedInstance(mContext).login(loginId);
			} else {
				Log.i(TAG, "loginId（登录ID)为空");
			}
		} catch (InvalidDataException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <strong>函数</strong><br>
	 * <br>
	 * 该函数映射至Javascript中sa对象的track函数，追踪一个带有属性的事件<br>
	 * <br>
	 * <strong>JS Example：</strong><br>
	 * var argument={event:"your event name",properties:{ ProductID:123456,
	 * ProductCatalog:'Laptop Computer', IsAddedToFav: false }};
	 * sa.track(argument);
	 * 
	 * @param sensorsAnalyticsAPICloudSDK
	 *            (Required) event 事件的名称,事件名的命名要符合变量命名规范（即由 字母，数字，_,$
	 *            组成，且不能以数字开头） properties 事件的属性
	 */
	public void jsmethod_track(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
		try {
			String eventName = sensorsAnalyticsAPICloudSDK.optString("event");
			JSONObject properties = sensorsAnalyticsAPICloudSDK.optJSONObject("properties");

			if (TextUtils.isEmpty(eventName)) {
				Log.i(TAG, "event(事件名)为空，请检查传入值");
				return;
			}
			SensorsDataAPI.sharedInstance(mContext).track(eventName, properties);
		} catch (InvalidDataException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <strong>函数</strong><br>
	 * <br>
	 * 该函数映射至Javascript中sa对象的flush函数，将所有本地缓存的日志发送到 Sensors Analytics<br>
	 * <br>
	 * <strong>JS Example：</strong><br>
	 * sa.flush();
	 * 
	 * 
	 */
	public void jsmethod_flush(final UZModuleContext sensorsAnalyticsAPICloudSDK) {
		SensorsDataAPI.sharedInstance(mContext).flush();
	}

	// 当前的debugMode模式
	private boolean isDebuggable(SensorsDataAPI.DebugMode debugMode) {
		return debugMode != SensorsDataAPI.DebugMode.DEBUG_OFF;
	}

}
