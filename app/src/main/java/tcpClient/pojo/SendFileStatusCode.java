package tcpClient.pojo;

public enum SendFileStatusCode {


    ApplyForTransmissionRequest(1),


    Transferring(2),


    TransferCompleted(3);

    private int code;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static SendFileStatusCode convert(int value) {
        for (SendFileStatusCode cmd : SendFileStatusCode.values()) {
            if (cmd.getCode() == value) {
                return cmd;
            }
        }
        return null;
    }

    SendFileStatusCode(int code) {
        this.code = code;
    }
}
