package tcpClient.pojo;

public class MessageType {
    public static final String CONNECT_SERVER = "connect_server";
    public static final String APPLY_CONNECT_CLIENT = "apply_connect_client";
    public static final String REPLY_CONNECT_CLIENT = "reply_connect_client";
    public static final String GETONLINEDEVICES = "get_online_devices";
    public static final String FILETRANSFERREQUEST = "file_transfer_request";
    public static final String FILETRANSFERRESPONSE = "file_transfer_response";

    public static final String CLIENTDISCONNECTED  = "client_disconnected";
    public static final String FILETRANSFERRING = "file_transferring";
    public static final String EXCHANGECLIPBOARD = "exchange_clipboard";
    public static final String PING_REQ = "ping_req";

    public static final String PING_RESP = "ping_resp";
}
