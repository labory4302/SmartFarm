package com.smartfarm.www;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class appInfo extends Application {


    public static final String SMARTFARM_CHANNEL_ID = "69981";
    NotificationChannel channel; // 푸쉬 알림 채널 객체

    public String test="dfdsf";

    @Override
    public void onCreate() {

        CharSequence channelName  = "smartfarm channel";
        String description = "camera detection";
        int importance = NotificationManager.IMPORTANCE_HIGH; // 긴급: 알림음이 울리며 헤드업 알림으로 표시됩니다.

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(SMARTFARM_CHANNEL_ID, channelName, importance);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);



            // 노티피케이션 채널을 시스템에 등록
            assert notificationManager != null;

            notificationManager.createNotificationChannel(channel);

        }

        super.onCreate();
    }
}
