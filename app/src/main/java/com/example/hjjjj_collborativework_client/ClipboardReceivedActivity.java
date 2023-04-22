package com.example.hjjjj_collborativework_client;

import android.content.*;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.hjjjj_collborativework_client.server.ClipboardReceivedService;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.hjjjj_collborativework_client.server.Store.ClipboardText;

public class ClipboardReceivedActivity extends AppCompatActivity {

    private ClipboardReceivedService.ClipboardReceivedServiceBinder ClipboardReceviedServiceBinder;
    private static int time;
    //时间
    private static TextView tv_time;
    private static TextView tv_tips;
    private Timer timer;
    protected static final int MSG_WHAT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipboard_recevied);
        tv_time = findViewById(R.id.tv_time);
        tv_tips = findViewById(R.id.tv_tips);
        Intent ClipboardReceviedService = new Intent(this, ClipboardReceivedService.class);
        bindService(ClipboardReceviedService, new Connect(), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> {
            //传送剪切板
            WriteClipborad();
            time = 3;
            tv_time.setText("3");
            countdown();
            ClipboardReceviedServiceBinder.Cancel();
        }, 1000);

    }

    //跳回主界面倒计时
    private void countdown() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    mHandler.sendEmptyMessage(MSG_WHAT);
                }
            }, 0, 1000);
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            tv_time.setText(time + "秒后跳回主界面");
            switch (msg.what) {
                case MSG_WHAT:
                    if (time > 0) {
                        time--;
                    } else {
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                            Intent intent = new Intent();
                            intent.setClass(ClipboardReceivedActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    };


    private void WriteClipborad() {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", ClipboardText);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }

    class Connect implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ClipboardReceviedServiceBinder = (ClipboardReceivedService.ClipboardReceivedServiceBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }




}