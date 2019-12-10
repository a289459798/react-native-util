package com.ichong.zzy.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationManagerCompat;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.ichong.zzy.util.receiver.NotifyReceiver;

/**
 * Created by zzy on 16/9/8.
 * Date : 16/9/8 13:21
 */
public class RNRemindModule extends ReactContextBaseJavaModule {

    private ReactApplicationContext mContent;
    private NotifyReceiver notifyReceiver;

    public RNRemindModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContent = reactContext;

    }

    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        mContent.unregisterReceiver(notifyReceiver);
    }

    @Override
    public String getName() {
        return "RNRemind";
    }

    @ReactMethod
    public void notify(final String title, final double time) {

        String receiverName = mContent.getPackageName() + "_com.ichong.zzy.notify";
        if (notifyReceiver == null) {
            notifyReceiver = new NotifyReceiver(mContent.getCurrentActivity());
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(receiverName);
            mContent.registerReceiver(notifyReceiver, intentFilter);
        }
        Intent intent = new Intent(receiverName);
        intent.putExtra("title", title);
        PendingIntent pi = PendingIntent.getBroadcast(mContent, 0, intent, 0);
        AlarmManager am = (AlarmManager) mContent.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, (long) time, pi);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @ReactMethod
    public void notifyEnabled(final Callback callback) {

        NotificationManagerCompat notification = NotificationManagerCompat.from(mContent);
        boolean isEnabled = notification.areNotificationsEnabled();
        callback.invoke(isEnabled);
    }

    @ReactMethod
    public void openSetting() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            Intent intent = new Intent()
                .setAction("android.settings.APPLICATION_DETAILS_SETTINGS")
                .setData(Uri.fromParts("package",
                    mContent.getPackageName(), null));
            mContent.getCurrentActivity().startActivity(intent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent()
                .setAction("android.settings.APPLICATION_DETAILS_SETTINGS")
                .setData(Uri.fromParts("package",
                    mContent.getApplicationContext().getPackageName(), null));
            mContent.getCurrentActivity().startActivity(intent);
        }
    }


}
