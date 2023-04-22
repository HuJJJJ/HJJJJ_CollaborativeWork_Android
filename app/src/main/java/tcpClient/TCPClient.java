package tcpClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;
import com.example.hjjjj_collborativework_client.utils.Utils;
import tcpClient.handle.BuildOverallMessageHandle;
import tcpClient.handle.ConnectServerHandle;
import tcpClient.pojo.SocketClient;
import tcpClient.thread.ClientPrintThread;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static tcpClient.config.SocketConfig.*;

public class TCPClient {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);

    private ClientPrintThread printThread;
    private Context context;
    private boolean IsConnectServer;
    private boolean IsConnectClient;

    //当服务器连接状态发生改变时
    public static OnChangeListener onConnectServerStateChangeCall;

    //当客户端连接发生改变时
    public static OnChangeListener onConnectClientStateChangeCall;

    public boolean isConnectServer() {
        return IsConnectServer;
    }

    public void setConnectServer(boolean connectServer) {
        IsConnectServer = connectServer;
        onConnectServerStateChangeCall.onChange(connectServer);
    }

    public boolean isConnectClient() {
        return IsConnectClient;
    }

    public void setConnectClient(boolean connectClient) {
        IsConnectClient = connectClient;
        onConnectClientStateChangeCall.onChange(connectClient);
    }


    public String ConnectClientID = null;
    @SuppressLint("StaticFieldLeak")
    private static volatile TCPClient mInstance = null;

    public TCPClient(Context context) throws Exception {
        this.context = context;
        printThread = new ClientPrintThread();
        //开启业务线程
        executorService.execute(printThread);
    }

    public static TCPClient getInstance(Context context) {
        try {
            if (mInstance == null) {
                synchronized (TCPClient.class) {
                    if (mInstance == null) {
                        mInstance = new TCPClient(context);
                        return mInstance;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("获取单例失败,原因：" + ex.getMessage());
        }
        return mInstance;
    }

    public boolean IsConnect() {
        return printThread.IsConnect();
    }


    ///上报服务器加入组
    public void Connect() throws Exception {
        ConnectServerHandle.ConnectServerRequest request = new ConnectServerHandle.ConnectServerRequest();
        request.setClientID(Utils.getDeviceNo(mInstance.context));
        request.setPassword(PASSWORD);
        request.setGroupName(GROUPNAME);
        request.setUserName(USERNAME);
        String jsonString = ConnectServerHandle.Pack(request);

        Send(jsonString);
    }


    ///发送数据
    public void Send(String jsonString) throws IOException {
        Message message = new Message();
        byte[] bytes = BuildOverallMessageHandle.Pack(jsonString);
        message.obj = bytes;
        printThread.revHandler.sendMessage(message);
    }

    public void Send(String jsonString, byte[] payload) {
        Message message = new Message();
        byte[] bytes = BuildOverallMessageHandle.Pack(jsonString, payload);
        message.obj = bytes;
        printThread.revHandler.sendMessage(message);
    }


    //监听属性值变化
    public interface OnChangeListener {    // 创建interface类
        void onChange(boolean state);    // 值改变
    }

    public static void setConnectServerStateChangeCall(OnChangeListener onChange) {    // 创建setListener方法
        onConnectServerStateChangeCall = onChange;
    }


    public static void setConnectClientStateChangeCall(OnChangeListener onChange) {    // 创建setListener方法
        onConnectClientStateChangeCall = onChange;
    }
}
