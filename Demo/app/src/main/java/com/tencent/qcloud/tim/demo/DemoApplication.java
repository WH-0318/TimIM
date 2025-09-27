package com.tencent.qcloud.tim.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.multidex.MultiDexApplication;

import com.hjq.http.EasyConfig;
import com.hjq.http.config.IRequestInterceptor;
import com.hjq.http.config.IRequestServer;
import com.hjq.http.model.HttpHeaders;
import com.hjq.http.model.HttpParams;
import com.hjq.http.request.HttpRequest;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.qcloud.tim.demo.config.AppConfig;
import com.tencent.qcloud.tim.demo.http.model.RequestHandler;
import com.tencent.qcloud.tim.demo.http.server.ReleaseServer;
import com.tencent.qcloud.tim.demo.http.server.TestServer;
import com.tencent.qcloud.tim.demo.utils.BrandUtil;
import com.tencent.qcloud.tim.demo.utils.Constants;
import com.tencent.qcloud.tim.demo.utils.PrivateConstants;
import com.tencent.qcloud.tuicore.TUIConfig;
import com.tencent.qcloud.tuicore.TUIConstants;
import com.tencent.qcloud.tuicore.TUICore;
import com.tencent.qcloud.tuicore.TUIThemeManager;
import com.tencent.qcloud.tuicore.interfaces.ITUINotification;
import com.tencent.qcloud.tuicore.interfaces.ITUIObjectFactory;
import com.tencent.qcloud.tuicore.util.SPUtils;
import com.tencent.qcloud.tuikit.timcommon.util.TUIUtil;
import java.lang.reflect.Field;
import java.util.Map;

import okhttp3.OkHttpClient;

public class DemoApplication extends MultiDexApplication {
    private static final String TAG = DemoApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();

        if (isMainProcess()) {
            initBugly();
            initHttp();
            initIMDemoAppInfo();
            setPermissionRequestContent();
            registerLanguageChangedReceiver();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // add language changed listener
        TUICore.registerEvent(
            TUIConstants.TUICore.LANGUAGE_EVENT, TUIConstants.TUICore.LANGUAGE_EVENT_SUB_KEY, new ITUINotification() {
                @Override
                public void onNotifyEvent(String key, String subKey, Map<String, Object> param) {
                    TUIThemeManager.setWebViewLanguage(base);
                }
            });
    }

    private void registerLanguageChangedReceiver() {
        BroadcastReceiver languageChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setPermissionRequestContent();
            }
        };

        IntentFilter languageFilter = new IntentFilter();
        languageFilter.addAction(Constants.DEMO_LANGUAGE_CHANGED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(languageChangedReceiver, languageFilter);
    }

    public void setPermissionRequestContent() {
        ApplicationInfo applicationInfo = getApplicationInfo();
        CharSequence labelCharSequence = applicationInfo.loadLabel(getPackageManager());
        String appName = "App";
        if (!TextUtils.isEmpty(labelCharSequence)) {
            appName = labelCharSequence.toString();
        }
        String micReason = getResources().getString(R.string.demo_permission_mic_reason);
        String micDeniedAlert = getResources().getString(R.string.demo_permission_mic_dialog_alert, appName);

        String cameraReason = getResources().getString(R.string.demo_permission_camera_reason);
        String cameraDeniedAlert = getResources().getString(R.string.demo_permission_camera_dialog_alert, appName);

        TUICore.unregisterObjectFactory(TUIConstants.Privacy.PermissionsFactory.FACTORY_NAME);
        TUICore.registerObjectFactory(TUIConstants.Privacy.PermissionsFactory.FACTORY_NAME, new ITUIObjectFactory() {
            @Override
            public Object onCreateObject(String objectName, Map<String, Object> param) {
                if (TextUtils.equals(
                        objectName, TUIConstants.Privacy.PermissionsFactory.PermissionsName.CAMERA_PERMISSIONS)) {
                    return cameraReason;
                } else if (TextUtils.equals(objectName,
                               TUIConstants.Privacy.PermissionsFactory.PermissionsName.MICROPHONE_PERMISSIONS)) {
                    return micReason;
                } else if (TextUtils.equals(objectName,
                               TUIConstants.Privacy.PermissionsFactory.PermissionsName.CAMERA_PERMISSIONS_TIP)) {
                    return cameraDeniedAlert;
                } else if (TextUtils.equals(objectName,
                               TUIConstants.Privacy.PermissionsFactory.PermissionsName.MICROPHONE_PERMISSIONS_TIP)) {
                    return micDeniedAlert;
                }
                return null;
            }
        });
    }

    private void initBugly() {
        TUICore.registerEvent(TUIConstants.TUILogin.EVENT_IMSDK_INIT_STATE_CHANGED,
            TUIConstants.TUILogin.EVENT_SUB_KEY_START_INIT, new ITUINotification() {
                @Override
                public void onNotifyEvent(String key, String subKey, Map<String, Object> param) {
                    CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
                    strategy.setAppVersion(BuildConfig.VERSION_NAME);
                    strategy.setDeviceModel(BrandUtil.getBuildModel());
                    CrashReport.initCrashReport(getApplicationContext(), PrivateConstants.BUGLY_APPID, true, strategy);
                }
            });
    }

    private void initIMDemoAppInfo() {
        TUIConfig.setTUIHostType(TUIConfig.TUI_HOST_TYPE_IMAPP);
        try {
            Field field = BuildConfig.class.getField("FLAVOR");
            AppConfig.DEMO_FLAVOR_VERSION = (String) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            // ignore
        }
    }

    private boolean isMainProcess() {
        String mainProcessName = this.getPackageName();
        String currentProcessName = TUIUtil.getProcessName();
        if (mainProcessName.equals(currentProcessName)) {
            return true;
        } else {
            return false;
        }
    }

    private void initHttp() {
        // 网络请求框架初始化
        IRequestServer server;
        if (BuildConfig.DEBUG) {
            server = new TestServer();
        } else {
            server = new ReleaseServer();
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        EasyConfig.with(okHttpClient)
                // 是否打印日志
                //.setLogEnabled(BuildConfig.DEBUG)
                // 设置服务器配置（必须设置）
                .setServer(server)
                // 设置请求处理策略（必须设置）
                .setHandler(new RequestHandler(this))
                // 设置请求参数拦截器
                .setInterceptor(new IRequestInterceptor() {
                    @Override
                    public void interceptArguments(@NonNull HttpRequest<?> httpRequest,
                                                   @NonNull HttpParams params,
                                                   @NonNull HttpHeaders headers) {
                        headers.put("timestamp", String.valueOf(System.currentTimeMillis()));
                    }
                })
                // 设置请求重试次数
                .setRetryCount(1)
                // 设置请求重试时间
                .setRetryTime(2000)
                // 添加全局请求参数
                //.addParam("xxx", "xxx")
                // 添加全局请求头
                .addHeader("Authorization", SPUtils.getInstance().getString("token"))
                .into();
    }
}