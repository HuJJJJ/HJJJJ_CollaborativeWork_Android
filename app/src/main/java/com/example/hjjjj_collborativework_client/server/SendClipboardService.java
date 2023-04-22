package com.example.hjjjj_collborativework_client.server;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.example.hjjjj_collborativework_client.R;
import com.example.hjjjj_collborativework_client.SendClipBoardDataActivity;

public class SendClipboardService extends Service {

    //通知ID
    private final int NOTIFICATION_ID = 1111;
    //唯一的通知通道的ID
    private final String notificationChannelId = "notification_channel_id_01";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new SendClipboardServiceBinder();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //调用startForeground() 方法
        startForeground(NOTIFICATION_ID, getNotification(this, "HJJJJ", "点击传送剪切板内容"));//创建一个通知，创建通知前记得获取开启通知权限
        //startForeground(1, null);//不创建通知
    }



    private Notification getNotification(Context context, String title, String text) {
        boolean isSilent = true;//是否静音
        boolean isOngoing = true;//是否持续(为不消失的常驻通知)
        String channelName = "服务常驻通知";

        String category = Notification.CATEGORY_SERVICE;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent nfIntent = new Intent(context, SendClipBoardDataActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, nfIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notificationChannelId)
                .setContentIntent(pendingIntent) //设置PendingIntent
                .setSmallIcon(R.mipmap.ic_launcher) //设置状态栏内的小图标
                .setContentTitle(title) //设置标题
                .setContentText(text) //设置内容
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)//设置通知公开可见
                .setOngoing(isOngoing)//设置持续(不消失的常驻通知)
                .setCategory(category)//设置类别
                .setPriority(NotificationCompat.PRIORITY_MAX);//优先级为：重要通知
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//安卓8.0以上系统要求通知设置Channel,否则会报错
            NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);//锁屏显示通知
            notificationManager.createNotificationChannel(notificationChannel);
            builder.setChannelId(notificationChannelId);
        }
        return builder.build();
    }


    private void stopService(){
        stopSelf();
    }

    public class SendClipboardServiceBinder extends Binder {

        public void StopService() {
            stopService();
        }

    }
}
