package tcpClient.handle;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PascalNameFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import tcpClient.pojo.BaseResponse;

import static tcpClient.pojo.MessageType.EXCHANGECLIPBOARD;

public class ExchangeClipboardHandle {

    public static String Pack(String str) {
        ExchangeClipboardRequest request = new ExchangeClipboardRequest();
        request.setData(str);
        return JSONObject.toJSONString(request,new PascalNameFilter(), SerializerFeature.WriteMapNullValue);
    }

    public static ExchangeClipboardResponse UnPack(JSONObject jsonObject) {
        return JSONObject.toJavaObject(jsonObject, ExchangeClipboardResponse.class);
    }

    public static class ExchangeClipboardRequest{
        private String Cmd =EXCHANGECLIPBOARD;
        private String Data;

        public String getCmd() {
            return Cmd;
        }

        public String getData() {
            return Data;
        }

        public void setData(String data) {
            Data = data;
        }
    }
    public static class ExchangeClipboardResponse extends BaseResponse {
        private String Cmd;

        @Override
        public String getCmd() {
            return Cmd;
        }

        @Override
        public void setCmd(String cmd) {
            Cmd = cmd;
        }

        public String getData() {
            return Data;
        }

        public void setData(String data) {
            Data = data;
        }

        private String Data;
    }

}
