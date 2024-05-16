package websocket.core;

import lombok.Data;

/**
 * @Author binbin
 * @Date 2024 04 10 15 43
 **/
@Data
public class WsMessageResp {
    private String requestId;
    private byte[] bytes;
    private String fileName;
    private String flag;

}
