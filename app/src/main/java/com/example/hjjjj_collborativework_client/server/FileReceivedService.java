package com.example.hjjjj_collborativework_client.server;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.example.hjjjj_collborativework_client.FileReceivedActivity;
import com.example.hjjjj_collborativework_client.R;
import tcpClient.FileTransfer.ApplyForTransmissionRequest;

import static com.example.hjjjj_collborativework_client.server.Store.ClipboardText;

public class FileReceivedService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new FileReceivedServiceBinder();
    }


    //通知ID
    private final int NOTIFICATION_ID = 3333;
    //唯一的通知通道的ID
    private final String notificationChannelId = "notification_channel_id_03";
    private NotificationManager manager;
    private Notification note;


    @Override
    public void onCreate() {
        super.onCreate();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(notificationChannelId, "HJJJJ剪切板通知",
                    NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        startForeground(NOTIFICATION_ID, getNotification(this, "HJJJJ", "有新的文件需要处理"));//不创建通知

    }

    private Notification getNotification(Context context, String title, String text) {
        Intent intent = new Intent(this, FileReceivedActivity.class);
        PendingIntent pending = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        note = new NotificationCompat.Builder(this, notificationChannelId)
                .setContentTitle(title)
                .setContentText(text)
                .setOngoing(false)
                .setSmallIcon(R.drawable.list)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.list))
                .setColor(Color.parseColor("#ff0000"))//设置小图标颜色
                .setContentIntent(pending)//设置点击动作
                .setAutoCancel(true)
                .build();
        return note;
    }

    private void show(ApplyForTransmissionRequest request) {
        Store.ApplyForTransmissionRequest = request;
        manager.notify(NOTIFICATION_ID, note);
    }

    private void stopService(){
        stopSelf();
    }

    private void cancel() {
        stopForeground(true);
    }

    public class FileReceivedServiceBinder extends Binder {
        public void Show(ApplyForTransmissionRequest request) {
            show(request);
        }
        public void StopService(){stopService();}

        public void Cancel() {
            cancel();
        }
    }

}
