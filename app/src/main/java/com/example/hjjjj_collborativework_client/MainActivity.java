package com.example.hjjjj_collborativework_client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.hjjjj_collborativework_client.dialog.ConfirmDialog;
import com.example.hjjjj_collborativework_client.server.ClipboardReceivedService;
import com.example.hjjjj_collborativework_client.server.FileReceivedService;
import com.example.hjjjj_collborativework_client.server.SendClipboardService;
import com.example.hjjjj_collborativework_client.utils.Utils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import tcpClient.FileTransfer.ApplyForTransmissionRequest;
import tcpClient.TCPClient;
import tcpClient.handle.*;
import tcpClient.pojo.ClientDisConnectedNotification;
import tcpClient.pojo.ReplyConnectResponseCode;
import tcpClient.pojo.SocketClient;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TCPClient client = TCPClient.getInstance(this);
    private Button ConnBtn;
    private View deviceList_title;
    private TextView deviceList_content;
    private SocketClient SelectDevice;
    private TextView clientConnectState;
    private TextView serverConnectState;

    private ClipboardReceivedService.ClipboardReceivedServiceBinder ClipboardReceivedServiceBinder;
    private FileReceivedService.FileReceivedServiceBinder FileReceivedServiceBinder;
    private SendClipboardService.SendClipboardServiceBinder SendClipboardServiceBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnBtn = findViewById(R.id.btn_Conn);
        deviceList_title = findViewById(R.id.deviceList_title);
        deviceList_content = findViewById(R.id.deviceList_content);
        clientConnectState = findViewById(R.id.clientConnectState);
        serverConnectState = findViewById(R.id.serverConnectState);
        deviceList_title.setOnClickListener(this);
        deviceList_content.setOnClickListener(this);
        ConnBtn.setOnClickListener(this);
        //获取设备id并展示
        setDeviceNo();
        //获取手机型号并作为客户端id展示
        setClientUserName();

        //设置服务端连接状态改变回调函数
        client.setConnectServerStateChangeCall((res) -> {
            if (res) {
                serverConnectState.setText("已连接");
                serverConnectState.setTextColor(getResources().getColor(R.color.green));
            } else {
                serverConnectState.setText("未连接");
                serverConnectState.setTextColor(getResources().getColor(R.color.color_red));
            }
        });

        //设置客户端连接状态改变回调函数
        client.setConnectClientStateChangeCall((res) -> {
            if (res) {
                clientConnectState.setText("已连接");
                clientConnectState.setTextColor(getResources().getColor(R.color.green));
                setConnectBtnStyle(1);
            } else {
                clientConnectState.setText("未连接");
                clientConnectState.setTextColor(getResources().getColor(R.color.color_red));
                setConnectBtnStyle(0);
            }
        });


    }

    ///接收OnlineDeviceListActivity的返回数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            SocketClient device = (SocketClient) data.getSerializableExtra("device");
            deviceList_content.setText(device.getUserName());
            SelectDevice = device;
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            //注册事件总线
            registerEventBus();
            //判断client是否为空，为空则获取
            if (client == null) {
                client = TCPClient.getInstance(MainActivity.this);
            }
            if (!client.isConnectServer()) {
                client.Connect();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    ///回复连接请求
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getOnlineDevices(ApplyConnectClientHandle.ApplyConnectClientResponse response) {
        try {
            String message = "";
            boolean isOk = ConfirmDialog.showConfirmDialog(MainActivity.this, "提示", response.getSourceClientID() + "客户端发来连接请求,是否接受");
            ReplyConnectResponseCode code;
            if (isOk) {
                code = ReplyConnectResponseCode.Accept;
                message = "连接成功";
                client.ConnectClientID = response.getTargetClientID();
                client.setConnectClient(true);
                //设置连接按钮样式
                startService();
            } else {
                code = ReplyConnectResponseCode.Refuse;
                message = "已拒绝连接";
                client.setConnectClient(false);
            }
            String payload = ReplyConnectClientHandle.Pack(code, response.getTargetClientID(), response.getSourceClientID());
            client.Send(payload);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {

        }
    }

    ///客户端离线通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getOnlineDevices(ClientDisConnectedNotification response) {
        client.setConnectClient(false);
    }

    ///有新的剪切板内容，弹出通知提醒用户
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getOnlineDevices(ExchangeClipboardHandle.ExchangeClipboardResponse response) {
        if (response.getData().trim().equals("")) return;
        ClipboardReceivedServiceBinder.Show(response.getData());
    }

    ///注册服务器回调函数
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getOnlineDevices(ConnectServerHandle.ConnectServerResponse response) {
        client.setConnectServer(response.isResult());
        Toast.makeText(this, "注册服务器成功", Toast.LENGTH_LONG).show();
    }

    ///收到连接回复
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getOnlineDevices(ReplyConnectClientHandle.ReplyConnectClientResponse response) {
        String message = "";
        switch (response.ResponseCode) {
            case Accept:
                message = "连接成功";
                client.ConnectClientID = response.TargetClientID;
                client.setConnectClient(true);
                //设置连接按钮样式
                startService();
                break;
            case Busy:
                message = "客户端繁忙";
                client.setConnectClient(false);
                break;
            case Refuse:
                message = "客户端拒绝连接";
                client.setConnectClient(false);
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    //设置连接按钮颜色
    private void setConnectBtnStyle(int type) {
        if (type == 1) {
            ConnBtn.setText("已连接");
            ConnBtn.setTextColor(getResources().getColor(R.color.light_blue));
            ConnBtn.setBackground(getResources().getDrawable(R.drawable.animation));
        } else {
            ConnBtn.setText("未连接");
            ConnBtn.setTextColor(getResources().getColor(R.color.grey));
            ConnBtn.setBackground(getResources().getDrawable(R.drawable.btn_circular));
            deviceList_content.setText("");
            FileReceivedServiceBinder.StopService();
            SendClipboardServiceBinder.StopService();
            ClipboardReceivedServiceBinder.StopService();
        }
    }

    ///客户端发来文件传输申请
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getOnlineDevices(ApplyForTransmissionRequest request) {
        FileReceivedServiceBinder.Show(request);
    }


    ///请求在线设备回调函数
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getOnlineDevices(GetOnlineDevicesHandle.GetOnlineDevicesResponse response) {
        Intent queryIntent = new Intent(this, OnlineDeviceListActivity.class);
        queryIntent.putExtra("devices", (Serializable) response.getClients());
        startActivityForResult(queryIntent, 100);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.btn_Conn:
                    //连接客户端
                    if (!client.isConnectClient()) ApplyConnectClient();
                    else {
                        //已连接则断开连接
                        client.setConnectClient(false);
                        client.Send(ClientDisConnectedNotificationHandle.Pack(
                                new ClientDisConnectedNotificationHandle
                                        .ClientDisConnectedRequest()));
                    }
                    break;
                case R.id.deviceList_content:
                case R.id.deviceList_title:
                    //获取在线设备
                    getOnlineDevices();
                    break;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    ///请求连接客户端
    public void ApplyConnectClient() throws Exception {
        String request = ApplyConnectClientHandle.Pack(SelectDevice.getClientID(), Utils.getDeviceNo(this));
        client.Send(request);
    }

    ///获取在线设备
    public void getOnlineDevices() throws Exception {
        String request = GetOnlineDevicesHandle.Pack();
        client.Send(request);
    }

    private void startService() {
        //接受剪切板服务
        Intent clipboardReceivedService = new Intent(this, ClipboardReceivedService.class);
        //发送剪切板服务
        Intent sendClipboardService = new Intent(this, SendClipboardService.class);
        //接收文件服务
        Intent fileReceivedService = new Intent(this, FileReceivedService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0后才支持
            startForegroundService(clipboardReceivedService);
            startForegroundService(sendClipboardService);
            startForegroundService(fileReceivedService);
        } else {
            startService(clipboardReceivedService);
            startService(sendClipboardService);
            startService(fileReceivedService);
        }
        bindService(clipboardReceivedService, new ClipboardReceivedServiceConnect(), 0);
        bindService(fileReceivedService, new FileReceivedServiceConnect(), 0);
        bindService(sendClipboardService, new SendClipboardServiceConnect(), 0);
    }

    //获取ClientID并展示在界面上
    private void setClientUserName() {
        TextView clientNoTxt = findViewById(R.id.clientNo_content);
        clientNoTxt.setText(Build.BRAND + "-" + Build.MODEL);
    }

    //获取设备ID并展示在界面上
    public void setDeviceNo() {
        String deviceNo = Utils.getDeviceNo(this);
        TextView deviceNoTxt = findViewById(R.id.deviceNo_content);
        deviceNoTxt.setText(deviceNo);
    }

    //注册事件总线
    private void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    ///剪切板服务
    class ClipboardReceivedServiceConnect implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ClipboardReceivedServiceBinder = (ClipboardReceivedService.ClipboardReceivedServiceBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }

    ///接收文件服务
    class FileReceivedServiceConnect implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            FileReceivedServiceBinder = (FileReceivedService.FileReceivedServiceBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }

    class SendClipboardServiceConnect implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SendClipboardServiceBinder = (SendClipboardService.SendClipboardServiceBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }
}