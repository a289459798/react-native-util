package com.ichong.zzy.util;

import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.ichong.zzy.util.receiver.NotifyReceiver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by zzy on 16/9/8.
 * Date : 16/9/8 13:21
 */
public class RNRemindModule extends ReactContextBaseJavaModule {

    private Context mContent;
    private LocalBroadcastManager lm;
    private NotifyReceiver notifyReceiver;

    public RNRemindModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContent = reactContext;

        lm = LocalBroadcastManager.getInstance(mContent);
        IntentFilter intentFilter = new IntentFilter("com.ichong.zzy.notify");
        //绑定
        notifyReceiver = new NotifyReceiver(reactContext.getCurrentActivity());
        lm.registerReceiver(notifyReceiver, intentFilter);
    }

    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        lm.unregisterReceiver(notifyReceiver);
    }

    @Override
    public String getName() {
        return "RNRemind";
    }

    @ReactMethod
    public void notify(final String title, final double time) {

        Intent intent = new Intent("com.ichong.zzy.notify");
        intent.putExtra("title", title);
        PendingIntent pi = PendingIntent.getBroadcast(mContent, 0, intent, 0);
        AlarmManager am = (AlarmManager) mContent.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, (long) time, pi);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @ReactMethod
    public void isEnabled(final Callback callback) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
        AppOpsManager mAppOps = (AppOpsManager) mContent.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = mContent.getApplicationInfo();
        String pkg = mContent.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        /* Context.APP_OPS_MANAGER */
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

                int value = (Integer) opPostNotificationValue.get(Integer.class);
                callback.invoke(((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED));
                return;
            }
            callback.invoke(true);
            return;

        } catch (Exception e) {
            e.printStackTrace();
        }
        callback.invoke(false);
    }

    @ReactMethod
    public void openSetting() {

        mContent.startActivity(new Intent(Settings.ACTION_SETTINGS));
    }


}
