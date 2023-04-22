package tcpClient.handle;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PascalNameFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import tcpClient.pojo.BaseResponse;
import tcpClient.pojo.SocketClient;

import static tcpClient.pojo.MessageType.CONNECT_SERVER;

public class ConnectServerHandle {
    public static String Pack(ConnectServerRequest request) {
        return JSONObject.toJSONString(request,new PascalNameFilter(), SerializerFeature.WriteMapNullValue);
    }

    public static ConnectServerResponse UnPack(JSONObject jsonObject) {
        return JSONObject.toJavaObject(jsonObject, ConnectServerResponse.class);
    }

    public static class ConnectServerRequest extends SocketClient {
        public String Cmd = CONNECT_SERVER;
    }

    public static class ConnectServerResponse extends BaseResponse {
        private boolean Result;

        public boolean isResult() {
            return Result;
        }

        public void setResult(boolean result) {
            Result = result;
        }
    }
}
