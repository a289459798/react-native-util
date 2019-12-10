package com.ichong.zzy.util.receiver;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

/**
 * Created by zzy on 2019/3/27.
 * Date : 2019/3/27 11:22
 */
public class NotifyReceiver extends BroadcastReceiver {

    private Activity activity;

    public NotifyReceiver(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String msg = intent.getStringExtra("title");

        Intent it = new Intent(context, activity.getClass());

        //创建一个启动其他Activity的Intent
        PendingIntent pi = PendingIntent.getActivity(context
            , 0, it, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify");
        int id = activity.getResources().getIdentifier("ic_launcher", "mipmap", activity.getPackageName());

        //设置标题
        mBuilder.setContentTitle("消息提醒")
            //设置内容
            .setContentText(msg)
            //设置小图标
            .setSmallIcon(id)
            //设置通知时间
            .setWhen(System.currentTimeMillis())
            //首次进入时显示效果
            .setTicker(msg)
            //设置通知方式，声音，震动，呼吸灯等效果，这里通知方式为声音
            .setDefaults(Notification.DEFAULT_SOUND)
            .setContentIntent(pi);

        //获取系统的NotificationManager服务
        NotificationManager notificationManager = (NotificationManager)
            context.getSystemService(Context.NOTIFICATION_SERVICE);
        //发送通知
        notificationManager.notify(1, mBuilder.build());

    }
}
