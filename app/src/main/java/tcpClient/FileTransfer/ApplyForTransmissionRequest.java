package tcpClient.FileTransfer;

import tcpClient.pojo.SendFileStatusCode;

import static tcpClient.pojo.MessageType.FILETRANSFERREQUEST;

public class ApplyForTransmissionRequest {

    /// <summary>
    /// 申请传输
    /// </summary>
    private String Cmd = FILETRANSFERREQUEST;

    private SendFileStatusCode Code = SendFileStatusCode.ApplyForTransmissionRequest;

    public SendFileStatusCode getCode() {
        return Code;
    }

    public String getFileName() {
        return FileName;
    }

    public String getCmd() {
        return Cmd;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public long getTotalBlocks() {
        return TotalBlocks;
    }

    public void setTotalBlocks(long totalBlocks) {
        TotalBlocks = totalBlocks;
    }

    /// <summary>
    /// 文件名
    /// </summary>
    private String FileName;

    /// <summary>
    /// 总块数
    /// </summary>
    private long TotalBlocks;

}
