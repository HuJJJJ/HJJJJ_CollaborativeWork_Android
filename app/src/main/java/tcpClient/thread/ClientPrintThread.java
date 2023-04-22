package tcpClient.thread;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.hjjjj_collborativework_client.utils.Utils;
import tcpClient.callback.ClientRecivedCallback;
import tcpClient.pojo.MessageRequest;

import java.io.*;
import java.net.Socket;

import static tcpClient.config.SocketConfig.HOST;
import static tcpClient.config.SocketConfig.PORT;

public class ClientPrintThread implements Runnable {


    private Socket socket;

    //// private ObjectInputStream ois;
    ////private ObjectOutputStream oos;

    private OutputStream writer;
    private DataInputStream dis;
    public Handler revHandler;

    public ClientPrintThread() {

    }

    public boolean IsConnect() {
        if (socket != null) {
            return socket.isConnected();
        } else {
            return false;
        }
    }


    @Override
    public void run() {
        new Thread(() -> {
            int payloadLength=0;
            int jsonLength=0;
            try {
                socket = new Socket(HOST, PORT);
                //输出流写数据
                writer = socket.getOutputStream();
                dis = new DataInputStream(socket.getInputStream());
                byte[] length = new byte[4];

                while (true) {
                    int bytesRead = 0;

                    while (bytesRead < length.length) {
                        int n = dis.read(length, bytesRead, length.length - bytesRead);
                        if (n == -1) {
                            System.out.println("x");
                        }
                        bytesRead += n;
                    }
                     jsonLength = Utils.bytesToInt(length, 0);

                    bytesRead = 0;
                    while (bytesRead < length.length) {
                        int n = dis.read(length, bytesRead, length.length - bytesRead);
                        if (n == -1) {
                            System.out.println("x");
                        }
                        bytesRead += n;
                    }
                     payloadLength = Utils.bytesToInt(length, 0);
                    byte[] fileBytes = new byte[payloadLength];

                    dis.readFully(fileBytes);
                    byte[] stringMessageBytes = new byte[jsonLength];
                    dis.readFully(stringMessageBytes);
                    String stringMessage = new String(stringMessageBytes);
                    System.out.println(stringMessage);

                    JSONObject jsonObject = JSONObject.parseObject(JSON.parse(stringMessage).toString(), JSONObject.class);
                    MessageRequest request = new MessageRequest();
                    request.setJsonStringLength(jsonLength);
                    request.setPayloadLength(payloadLength);
                    request.setJsonObject(jsonObject);
                    request.setPayload(fileBytes);
                    ClientRecivedCallback.receivedDataCenter(request);

                }
            } catch (Exception e) {
                try {
                    System.out.println("-----------------------------");
                    System.out.println(e.getMessage());
                    socket.close();
                    dis.close();
                    writer.close();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }).start();
 int a =0;
        // 为当前线程初始化Looper
        Looper.prepare();
        // 创建revHandler对象

        revHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                // 将用户在文本框输入的内容写入网络
                try {
                    byte[] bytes = (byte[]) msg.obj;
                    writer.write(bytes);
                    writer.flush();

                } catch (Exception e) {
                    try {
                        System.out.println(e.getMessage());
                        socket.close();
                        dis.close();
                        writer.close();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        };
        // 启动Looper
        Looper.loop();
    }
}
