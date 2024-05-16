package websocket.core;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * @Author binbin
 * @Date 2024 04 02 15 39
 **/
@Slf4j
public class JavaClient extends WebSocketClient {
    private static String URL;
    private static JavaClient myWebSocketClient;

    public JavaClient(URI serverUri) {
        super(serverUri);
    }

    public static JavaClient initClient(String uri) {
        URL = uri;
        JavaClient client = new JavaClient(URI.create(uri));
        client.connect();
        int count = 5;
        //判断连接状态，0为请求中  1为已建立  其它值都是建立失败
        while (client.getReadyState().ordinal() == 0 && count > 0) {
            try {
                Thread.sleep(200);
                count--;
            } catch (Exception e) {
                log.warn("延迟操作出现问题，但并不影响功能");
            }
            log.info("WebSocketCenter连接中..................");
        }
        //连接状态不再是0请求中，判断建立结果是不是1已建立
        if (client.getReadyState().ordinal() == 1) {
            log.info("WebSocketCenter连接成功..................");
            myWebSocketClient = client;
        }
        return myWebSocketClient;

    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("开始连接...");
    }

    @Override
    public void onMessage(String s) {
        log.info("消息内容：" + s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("连接关闭 {}", s);
    }

    @Override
    public void onError(Exception e) {
        log.info("异常内容 {}", e);
    }
}