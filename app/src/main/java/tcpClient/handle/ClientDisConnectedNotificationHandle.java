package tcpClient.handle;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PascalNameFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import static tcpClient.pojo.MessageType.CLIENTDISCONNECTED;

public class ClientDisConnectedNotificationHandle {
    public static String Pack(ClientDisConnectedRequest request) {
        return JSONObject.toJSONString(request, new PascalNameFilter(), SerializerFeature.WriteMapNullValue);
    }

    public static void Unpack() {
    }


    public static class ClientDisConnectedRequest {
        public String Cmd = CLIENTDISCONNECTED;
    }

    public class ClientDisConnectedResponse {
    }
}
