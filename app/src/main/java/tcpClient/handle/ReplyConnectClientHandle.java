package tcpClient.handle;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.PascalNameFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import tcpClient.pojo.BaseResponse;
import tcpClient.pojo.ReplyConnectResponseCode;

import static tcpClient.pojo.MessageType.GETONLINEDEVICES;
import static tcpClient.pojo.MessageType.REPLY_CONNECT_CLIENT;

public class ReplyConnectClientHandle {


    public static String Pack(ReplyConnectResponseCode code, String targetClientID, String sourceClientID) {
        ReplyConnectClientRequest request = new ReplyConnectClientRequest();
        request.ResponseCode = code;
        request.TargetClientID = targetClientID;
        request.SourceClientID = sourceClientID;
        return JSONObject.toJSONString(request, new PascalNameFilter(), SerializerFeature.WriteMapNullValue);
    }

    public static ReplyConnectClientResponse UnPack(JSONObject jsonObject) {
        ReplyConnectClientHandle.ReplyConnectClientResponse response = new ReplyConnectClientResponse();
        ReplyConnectResponseCode code = ReplyConnectResponseCode.convert(jsonObject.getInteger("ResponseCode"));
        jsonObject.remove("ResponseCode");
        response= JSONObject.toJavaObject(jsonObject, ReplyConnectClientHandle.ReplyConnectClientResponse.class);
        response.ResponseCode = code;
        return response;
    }

    public static class ReplyConnectClientRequest {
        public String Cmd = REPLY_CONNECT_CLIENT;
        public String TargetClientID;
        public String SourceClientID;
        public ReplyConnectResponseCode ResponseCode;
    }

    public static class ReplyConnectClientResponse extends BaseResponse {
        public String TargetClientID;
        public String SourceClientID;

        @JSONField(serialize = false)
        public ReplyConnectResponseCode ResponseCode;
    }

}
