package tcpClient.pojo;

import com.alibaba.fastjson.JSONObject;

public class MessageRequest {

    private int JsonStringLength;
    private int PayloadLength;
    private JSONObject JsonObject;
    private byte[] Payload;

    public int getJsonStringLength() {
        return JsonStringLength;
    }

    public void setJsonStringLength(int jsonStringLength) {
        JsonStringLength = jsonStringLength;
    }

    public int getPayloadLength() {
        return PayloadLength;
    }

    public void setPayloadLength(int payloadLength) {
        PayloadLength = payloadLength;
    }

    public JSONObject getJsonObject() {
        return JsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.JsonObject = jsonObject;
    }

    public byte[] getPayload() {
        return Payload;
    }

    public void setPayload(byte[] payload) {
        Payload = payload;
    }
}
