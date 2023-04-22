package tcpClient.handle;

import com.example.hjjjj_collborativework_client.utils.Utils;

import java.nio.charset.StandardCharsets;

public class BuildOverallMessageHandle {

    public static byte[] Pack(String jsonString) {
        jsonString += "\n";
        byte[] jsonBytes = jsonString.getBytes(StandardCharsets.UTF_8);
        byte[] jsonBytesLength = Utils.intToBytes(jsonBytes.length);
        byte[] bytes = new byte[jsonBytes.length + 4 + jsonBytesLength.length];
        byte[] payloadLength = new byte[]{0, 0, 0, 0};
        System.arraycopy(jsonBytesLength, 0, bytes, 0, jsonBytesLength.length);
        System.arraycopy(payloadLength, 0, bytes, jsonBytesLength.length, payloadLength.length);
        System.arraycopy(jsonBytes, 0, bytes, jsonBytesLength.length + payloadLength.length, jsonBytes.length);
        return bytes;
    }

    public static byte[] Pack(String jsonString, byte[] payload) {
        jsonString += "\n";
        byte[] jsonBytes = jsonString.getBytes();
        byte[] jsonBytesLength = Utils.intToBytes(jsonBytes.length);
        byte[] payloadLength = Utils.intToBytes(payload.length);
        byte[] bytes = new byte[jsonBytes.length + payloadLength.length + jsonBytesLength.length + payload.length];
        System.arraycopy(jsonBytesLength, 0, bytes, 0, jsonBytesLength.length);
        System.arraycopy(payloadLength, 0, bytes, jsonBytesLength.length, payloadLength.length);
        System.arraycopy(payload, 0, bytes, jsonBytesLength.length + payloadLength.length, payload.length);
        System.arraycopy(jsonBytes, 0, bytes, jsonBytesLength.length + payloadLength.length + payload.length, jsonBytes.length);
        return bytes;
    }
}
