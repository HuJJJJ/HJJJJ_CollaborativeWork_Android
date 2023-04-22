package tcpClient.pojo;

public enum ReplyConnectResponseCode {
    /// <summary>
    /// 获取组内在线列表
    /// </summary>
    Accept(1),

    /// <summary>
    /// 申请连接某个客户端
    /// </summary>
    Refuse(2),

    /// <summary>
    /// 回复连接申请
    /// </summary>
    Busy(3);

    private int code;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static ReplyConnectResponseCode convert(int value) {
        for (ReplyConnectResponseCode cmd : ReplyConnectResponseCode.values()) {
            if (cmd.getCode() == value) {
                return cmd;
            }
        }
        return null;
    }

    ReplyConnectResponseCode(int code) {
        this.code = code;
    }

}
