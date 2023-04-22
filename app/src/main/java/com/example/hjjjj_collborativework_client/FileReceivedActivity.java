package com.example.hjjjj_collborativework_client;

import android.content.Intent;
import android.os.Environment;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PascalNameFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.hjjjj_collborativework_client.Runnable.ReceivedFileRunnable;
import com.example.hjjjj_collborativework_client.dialog.ConfirmDialog;
import com.example.hjjjj_collborativework_client.server.Store;
import com.example.hjjjj_collborativework_client.utils.Utils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import tcpClient.FileTransfer.ApplyForTransmissionRequest;
import tcpClient.FileTransfer.FileTransferResponse;
import tcpClient.FileTransfer.TransferringRequest;
import tcpClient.TCPClient;
import tcpClient.handle.BuildOverallMessageHandle;
import tcpClient.handle.GetOnlineDevicesHandle;
import tcpClient.pojo.MessageRequest;
import tcpClient.pojo.SendFileResponseCode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;


public class FileReceivedActivity extends AppCompatActivity implements View.OnClickListener {

    private Button returnBtn;
    private ProgressBar progressBar;
    private TextView tv;
    private TCPClient client = TCPClient.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviced_file);
        progressBar = findViewById(R.id.progressBar1);
        tv = findViewById(R.id.tv_received);
        returnBtn = findViewById(R.id.return_btn);
        returnBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册事件总线
        registerEventBus();
        try {
            ApplyForTransmissionRequest request = Store.ApplyForTransmissionRequest;
            if (request != null) {
                boolean isOk = ConfirmDialog.showConfirmDialog(this, "提示", "客户端发来文件:" + request.getFileName() + "是否接受");
                FileTransferResponse response = new FileTransferResponse();
                if (isOk) {
                    response.Code = SendFileResponseCode.AcceptTransmission;
                } else {
                    response.Code = SendFileResponseCode.RejectTransmission;
                }
                String jsonString = JSONObject.toJSONString(response, new PascalNameFilter(), SerializerFeature.WriteMapNullValue);
                checkConnectState();
                client.Send(jsonString);
            } else {
                Toast.makeText(this, "没有文件需要处理", Toast.LENGTH_SHORT).show();
                // backMainActivity();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void checkConnectState() throws Exception {
        if (client == null) {
            client = TCPClient.getInstance(this);
        }
        if (!client.isConnectServer()) {
            client.Connect();
        }
    }


    ///保存临时文件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceivedDataHandle(MessageRequest message) {
        TransferringRequest request = JSONObject.toJavaObject(message.getJsonObject(), TransferringRequest.class);
        // 获取外部存储器上的公共文档目录
        File publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        // 在公共文档目录中为应用程序创建一个新文件夹
        File tempFolder = new File(publicDir, Store.ApplicationFolder + "/temp");
        File hjjjjFolder = new File(publicDir, Store.ApplicationFolder);
        boolean mkResult = tempFolder.mkdirs();
        // 在新文件夹中创建一个新文件并写入数据
        File myFile = new File(tempFolder, String.valueOf(request.getCurrentBlock()));
        try {
            FileOutputStream outputStream = new FileOutputStream(myFile);
            outputStream.write(message.getPayload());
            outputStream.close();
            //更新进度条
            System.out.println("第" + request.getCurrentBlock() + "片");
            int progress = (int) Utils.ExecPercent(request.getCurrentBlock(), Store.ApplyForTransmissionRequest.getTotalBlocks());
            progressBar.setProgress(progress);

            //检查传输是否完成
            if (request.getCurrentBlock() == Store.ApplyForTransmissionRequest.getTotalBlocks()) {
                System.out.println("开始合并文件");
                //合并文件
                Utils.MergeFile(tempFolder.getAbsolutePath(), new File(publicDir, Store.ApplicationFolder + "/" + Store.ApplyForTransmissionRequest.getFileName()).getAbsolutePath());
                System.out.println("合并完成");
                //删除临时文件
                Utils.deleteFile(tempFolder);
                //清空
                Store.ApplyForTransmissionRequest = null;
                tv.setText("接收完成");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //返回MainActivity
    private void backMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    //注册事件总线
    private void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.return_btn:
                backMainActivity();
                break;
        }
    }


    //保存文件
    ReceivedFileRunnable saveFile = new ReceivedFileRunnable() {

        MessageRequest message;

        @Override
        public ReceivedFileRunnable setMessage(MessageRequest message) {
            this.message = message;
            return this;
        }

        @Override
        public void run() {

        }
    };
}