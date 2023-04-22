package tcpClient.handle;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PascalNameFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import tcpClient.pojo.BaseResponse;
import tcpClient.pojo.SocketClient;

import java.util.List;

import static tcpClient.pojo.MessageType.GETONLINEDEVICES;

public class GetOnlineDevicesHandle {

    public static String Pack() {
        return JSONObject.toJSONString(new GetOnlineDevicesRequest(),new PascalNameFilter(), SerializerFeature.WriteMapNullValue);
    }

    public static GetOnlineDevicesResponse UnPack(JSONObject jsonObject) {
        return JSONObject.toJavaObject(jsonObject, GetOnlineDevicesResponse.class);
    }

    public static class GetOnlineDevicesRequest {
        public String Cmd = GETONLINEDEVICES;
    }

    public static class GetOnlineDevicesResponse extends BaseResponse {
        private List<SocketClient> Clients;

        public List<SocketClient> getClients() {
            return Clients;
        }

        public void setClients(List<SocketClient> clients) {
            Clients = clients;
        }
    }

}
