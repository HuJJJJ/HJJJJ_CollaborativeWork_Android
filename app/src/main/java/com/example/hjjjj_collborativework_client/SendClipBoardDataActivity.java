package com.example.hjjjj_collborativework_client;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import tcpClient.TCPClient;
import tcpClient.handle.ExchangeClipboardHandle;

import java.util.Timer;
import java.util.TimerTask;

public class SendClipBoardDataActivity extends AppCompatActivity {
    private TCPClient client = TCPClient.getInstance(this);
    private static int time;
    //时间
    private static TextView tv_time;
    private static TextView tv_tips;
    private Timer timer;

    protected static final int MSG_WHAT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_clip_board_data);
        tv_time = findViewById(R.id.tv_time);
        tv_tips = findViewById(R.id.tv_tips);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> {
            //传送剪切板
            deliverClipboard();
            time = 3;
            tv_time.setText("3");
            countdown();
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
                            intent.setClass(SendClipBoardDataActivity.this, MainActivity.class);
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


    ///传送剪切板
    private void deliverClipboard() {
        try {
            if (client.ConnectClientID.equals(null)||client.ConnectClientID.equals("")) {
                Toast.makeText(this, "请连接设备后再传送剪切板", Toast.LENGTH_LONG).show();
            } else {
                ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                // 返回数据
                ClipData clipData = clipboard.getPrimaryClip();
                String str = clipData.getItemAt(0).getText().toString();
                String pack = ExchangeClipboardHandle.Pack(str);
                client.Send(pack);
                tv_tips.setText("传送完成");
            }
        }catch (Exception ex){

        }

    }
}
