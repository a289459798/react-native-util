package com.ichong.zzy.util.receiver;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.ichong.zzy.util.R;

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

        int id = activity.getResources().getIdentifier("ic_launcher", "mipmap", activity.getPackageName());

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
            PendingIntent.FLAG_ONE_SHOT);
        final NotificationManager notificationManager = (NotificationManager) this.activity.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel("ID", "NAME", importance);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "ID");
        mBuilder.setSmallIcon(id);
        mBuilder.setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentTitle("预约提醒");
        mBuilder.setContentText(msg);
        mBuilder.setContentIntent(pendingIntent);
        final Notification notify = mBuilder.build();

        //随机id 1000-2000
        final int notifyId = (int) (Math.random() * 1000 + 1000);
        if (notificationManager != null) {
            notificationManager.notify(notifyId, notify);
        }

    }
}
