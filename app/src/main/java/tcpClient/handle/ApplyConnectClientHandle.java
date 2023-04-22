package tcpClient.handle;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PascalNameFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.hjjjj_collborativework_client.utils.Utils;
import tcpClient.pojo.BaseResponse;

import static tcpClient.config.SocketConfig.CLIENTID;
import static tcpClient.pojo.MessageType.APPLY_CONNECT_CLIENT;

public class ApplyConnectClientHandle {

    public static String Pack(String targetClientID,String sourceClientID) {
        ApplyConnectClientRequest request = new ApplyConnectClientRequest();
        request.setTargetClientID(targetClientID);
        request.setSourceClientID(sourceClientID);

        return JSONObject.toJSONString(request,new PascalNameFilter(), SerializerFeature.WriteMapNullValue);
    }

    public static ApplyConnectClientResponse UnPack(JSONObject jsonObject) {
        return JSONObject.toJavaObject(jsonObject, ApplyConnectClientResponse.class);
    }

    public static class ApplyConnectClientRequest {
        public String Cmd = APPLY_CONNECT_CLIENT;
        private String TargetClientID;
        private String SourceClientID;

        public String getSourceClientID() {
            return SourceClientID;
        }

        public void setSourceClientID(String sourceClientID) {
            SourceClientID = sourceClientID;
        }

        public String getTargetClientID() {
            return TargetClientID;
        }

        public void setTargetClientID(String targetClientID) {
            TargetClientID = targetClientID;
        }


    }

    public static class ApplyConnectClientResponse extends BaseResponse {
        private String TargetClientID;
        private String SourceClientID;

        public String getSourceClientID() {
            return SourceClientID;
        }

        public void setSourceClientID(String sourceClientID) {
            SourceClientID = sourceClientID;
        }

        public String getTargetClientID() {
            return TargetClientID;
        }

        public void setTargetClientID(String targetClientID) {
            TargetClientID = targetClientID;
        }

    }
}
