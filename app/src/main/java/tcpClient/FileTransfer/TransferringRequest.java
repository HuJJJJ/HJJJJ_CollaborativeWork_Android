package tcpClient.FileTransfer;

import tcpClient.pojo.SendFileStatusCode;

import static tcpClient.pojo.MessageType.FILETRANSFERRING;

public class TransferringRequest {


    private String Cmd = FILETRANSFERRING;
    private SendFileStatusCode Code = SendFileStatusCode.Transferring;


    /// <summary>
    /// 当前块号
    /// </summary>
    private int CurrentBlock;

    /// <summary>
    /// 当前块号MD5值
    /// </summary>
    private String MD5;

    public String getCmd() { return Cmd;}

    public SendFileStatusCode getCode() {
        return Code;
    }

    public int getCurrentBlock() {
        return CurrentBlock;
    }

    public void setCurrentBlock(int currentBlock) {
        CurrentBlock = currentBlock;
    }

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }
}
