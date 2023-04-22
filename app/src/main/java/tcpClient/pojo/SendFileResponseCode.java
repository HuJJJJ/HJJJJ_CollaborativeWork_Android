package tcpClient.pojo;

public enum SendFileResponseCode {

    /// <summary>
    /// 同意传输
    /// </summary>
    AcceptTransmission(0),

    /// <summary>
    /// 拒绝传输
    /// </summary>
    RejectTransmission(1),

    /// <summary>
    /// 传输确认
    /// </summary>
    TransferACK(2),

    /// <summary>
    /// 重传
    /// </summary>
    Retransmission(3),

    /// <summary>
    /// 传输成功
    /// </summary>
    TransferCompletedSuccess(4),

    /// <summary>
    /// 传输失败
    /// </summary>
    TransferFailed(5);

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

    SendFileResponseCode(int code) {
        this.code = code;
    }

    }
