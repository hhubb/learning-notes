package websocket.core;

import lombok.Data;

/**
 * @Author binbin
 * @Date 2024 04 10 15 33
 **/
@Data
public class WsMessage {
    private String errorMessage;
    private Integer code;
    private String requestId;
    private byte[] bytes;
    private String fileName;
    private String flag;
    private String remoteUrl;
    private String resourceId;
    private String connectorId;
    private String targetConnectorId;
    private RepresentationVO representationVO;
    private String privateKey;

}
