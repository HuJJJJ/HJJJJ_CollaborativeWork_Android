package com.example.hjjjj_collborativework_client;

import android.app.Activity;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import tcpClient.pojo.SocketClient;

import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class OnlineDeviceListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_device_list);

        ListView lv = findViewById(R.id.DevicesLv);
        List<SocketClient> devices = (List<SocketClient>) getIntent().getSerializableExtra("devices");
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, devices.stream().map(e -> e.getUserName()).collect(Collectors.toList()));
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = this.getIntent();
            intent.putExtra("device", devices.get(i));
            //携带数据回到源页面
            setResult(Activity.RESULT_OK, intent);
            //已经回去了
            finish();
        });
    }
}