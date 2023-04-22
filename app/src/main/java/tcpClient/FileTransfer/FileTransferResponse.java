package tcpClient.FileTransfer;

import tcpClient.pojo.SendFileResponseCode;

import static tcpClient.pojo.MessageType.FILETRANSFERREQUEST;
import static tcpClient.pojo.MessageType.FILETRANSFERRESPONSE;

public class FileTransferResponse {
    public String Cmd = FILETRANSFERRESPONSE;
    public SendFileResponseCode Code;
}
