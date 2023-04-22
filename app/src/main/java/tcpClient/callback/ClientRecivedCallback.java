package tcpClient.callback;

import com.alibaba.fastjson.JSONObject;
import org.greenrobot.eventbus.EventBus;
import tcpClient.FileTransfer.ApplyForTransmissionRequest;
import tcpClient.FileTransfer.FileTransferResponse;
import tcpClient.handle.*;
import tcpClient.pojo.ClientDisConnectedNotification;
import tcpClient.pojo.MessageRequest;

import static tcpClient.pojo.MessageType.*;

public class ClientRecivedCallback {

    public static void receivedDataCenter(MessageRequest message) {
        JSONObject response = message.getJsonObject();
        String cmd = response.getString("Cmd");
//        JSONObject jsonObject = JSONObject.parseObject(JSON.parse(message).toString(), JSONObject.class);
//        String cmd = jsonObject.getString("Cmd");
        switch (cmd) {
            case CONNECT_SERVER:
                connServerReceivedHandle(response);
                break;
            case APPLY_CONNECT_CLIENT:
                applyConnectClientReceivedHandle(response);
                break;
            case REPLY_CONNECT_CLIENT:
                replyConnectClientReceivedHandle(response);
                break;
            case GETONLINEDEVICES:
                getOnlineDevicesReceivedHandle(response);
                break;
            case EXCHANGECLIPBOARD:
                exChangeClipboardReceivedHandle(response);
                break;
            case FILETRANSFERREQUEST:
                fileTransferRequestReceivedHandle(response);
                break;
            case FILETRANSFERRESPONSE:
                fileTransferResponseReceivedHandle(response);
            case FILETRANSFERRING:
                fileTransferringReceivedHandle(message);
                break;
            case CLIENTDISCONNECTED:
                clientDisConnectReceivedHandle();
                break;
            case PING_REQ:

                break;
        }
    }

    private static void clientDisConnectReceivedHandle(){
        EventBus.getDefault().post(new ClientDisConnectedNotification());
    }
    private static void fileTransferringReceivedHandle(MessageRequest message)
    {
        EventBus.getDefault().post(message);
    }
    private static void fileTransferResponseReceivedHandle(JSONObject msg) {
        FileTransferResponse response = JSONObject.toJavaObject(msg, FileTransferResponse.class);
        EventBus.getDefault().post(response);
    }

    private static void fileTransferRequestReceivedHandle(JSONObject message) {
        EventBus.getDefault().post(JSONObject.toJavaObject(message, ApplyForTransmissionRequest.class));
    }

    private static void exChangeClipboardReceivedHandle(JSONObject jsonObject) {
        ExchangeClipboardHandle.ExchangeClipboardResponse response = ExchangeClipboardHandle.UnPack(jsonObject);
        EventBus.getDefault().post(response);
    }

    private static void getOnlineDevicesReceivedHandle(JSONObject jsonObject) {
        GetOnlineDevicesHandle.GetOnlineDevicesResponse response = GetOnlineDevicesHandle.UnPack(jsonObject);
        EventBus.getDefault().post(response);
    }

    private static void replyConnectClientReceivedHandle(JSONObject jsonObject) {
        ReplyConnectClientHandle.ReplyConnectClientResponse response = ReplyConnectClientHandle.UnPack(jsonObject);
        EventBus.getDefault().post(response);
    }

    private static void applyConnectClientReceivedHandle(JSONObject jsonObject) {
        ApplyConnectClientHandle.ApplyConnectClientResponse response = ApplyConnectClientHandle.UnPack(jsonObject);
        EventBus.getDefault().post(response);
    }

    private static void connServerReceivedHandle(JSONObject jsonObject) {
        ConnectServerHandle.ConnectServerResponse response = ConnectServerHandle.UnPack(jsonObject);
        EventBus.getDefault().post(response);
    }

}
