package com.example.hjjjj_collborativework_client;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PascalNameFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.hjjjj_collborativework_client.utils.Utils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import tcpClient.FileTransfer.*;
import tcpClient.TCPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class SendFileActivity extends AppCompatActivity {

    private TCPClient client = TCPClient.getInstance(this);
    private int bufferLength = 63 * 1024;
    private File file;
    //   private FileInputStream fis;
    private InputStream fis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_file);

        findViewById(R.id.return_main).setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        //注册事件总线
        registerEventBus();
        Intent itnIn = getIntent();
        Bundle extras = itnIn.getExtras();
        String action = itnIn.getAction();
        if (Intent.ACTION_SEND.equals(action)) {
            if (extras.containsKey(Intent.EXTRA_STREAM)) {
                try {

                    //获取文件
                    Uri uri2 = extras.getParcelable(Intent.EXTRA_STREAM);
                    String path = getRealPathFromURI(SendFileActivity.this, uri2);
                    path = java.net.URLDecoder.decode(path, "utf-8");
                    file = new File(path);
                    fis = getContentResolver().openInputStream(uri2);
                    //  fis = new FileInputStream(file);

                    if (file == null) {
                        Toast.makeText(this, "找不到该文件", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //获取文件名
                    String fileName = Utils.splitFileName(path);
                    //获取总块数
                    long blocks = Utils.getFileTotalBlocks(file, bufferLength);

                    //构建文件传输请求
                    ApplyForTransmissionRequest request = new ApplyForTransmissionRequest();
                    request.setFileName(fileName);
                    request.setTotalBlocks(blocks);
                    String payload = JSONObject.toJSONString(request, new PascalNameFilter(), SerializerFeature.WriteMapNullValue);
                    client.Send(payload);

                } catch (Exception e) {
                    Log.e(this.getClass().getName(), e.toString());
                }
            }
        }

    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            if (client == null) {
                client = TCPClient.getInstance(this);
            }
            if (!client.isConnectServer()) {
                client.Connect();
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //获取文件路径
    public String getRealPathFromURI(Activity act, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = act.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) {
            String path = contentUri.getPath();
            return path;
        }
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    ///请求文件传输回复回调
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void ReceivedDataHandle(FileTransferResponse response) {
        switch (response.Code) {
            case AcceptTransmission:
            case RejectTransmission:
                ApplyForTransmissionReceivedHandle(response);
                break;
            case TransferACK:
            case Retransmission:
                FileTransferringReceivedHandle(response);
                break;
            case TransferCompletedSuccess:
            case TransferFailed:
                FileTransCompletedReceivedHandle(response);
                break;
        }
    }


    ///请求传输回调
    private void ApplyForTransmissionReceivedHandle(FileTransferResponse response) {
        try {
            switch (response.Code) {
                case AcceptTransmission:
                    //发送文件
                    SendFile();
//                    //提示客户端传输完成
//                    TransferCompletedRequest completedRequest = new TransferCompletedRequest();
//                    completedRequest.setMD5("");
//                    String jsonString = JSONObject.toJSONString(completedRequest, new PascalNameFilter(), SerializerFeature.WriteMapNullValue);
//                    jsonString += "\n";
//                    client.Send(jsonString);
                    break;
                case RejectTransmission:
                    Toast.makeText(this, "客户端已拒绝传输请求", Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (Exception ex) {
            int a = 0;
        }

    }

    private void SendFile() {
        try {
            //传输文件
            // String md5 = MD5.asHex(MD5.getHash(file));
            byte[] sendBytes = new byte[bufferLength];
            int length = 0;
            int count = 1;
            TransferringRequest request = new TransferringRequest();
            while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
                long begin = System.currentTimeMillis();
                request.setCurrentBlock(count);
                request.setMD5("");
                String jsonString = JSONObject.toJSONString(request, new PascalNameFilter(), SerializerFeature.WriteMapNullValue);
                jsonString += "\n";
                client.Send(jsonString, sendBytes);
                count++;
                long end = System.currentTimeMillis();
                System.out.println("一片：" + (end - begin) + "ms。" + count + "片");
            }
            fis.close();
        } catch (Exception ex) {
            int a = 0;
        }


    }

    //传输中回调
    private void FileTransferringReceivedHandle(FileTransferResponse response) {

    }

    //传输完成回调
    private void FileTransCompletedReceivedHandle(FileTransferResponse response) {

    }


    private void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

}