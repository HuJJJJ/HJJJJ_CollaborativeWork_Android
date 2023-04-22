package tcpClient.FileTransfer;

import tcpClient.pojo.SendFileStatusCode;

public class TransferCompletedRequest  extends FileTransferBaseRequest{
    /// <summary>
    /// 传输完成
    /// </summary>
    private   SendFileStatusCode Code  = SendFileStatusCode.TransferCompleted;

    @Override
    public SendFileStatusCode getCode() {
        return Code;
    }

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }

    /// <summary>
    /// 文件总MD5
    /// </summary>
    private String MD5 ;
}
