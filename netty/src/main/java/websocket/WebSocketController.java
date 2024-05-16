package websocket;

import websocket.core.JavaClient;
import org.java_websocket.client.WebSocketClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author binbin
 * @Date 2024 04 02 14 41
 **/
@RestController
@RequestMapping(value = "/centre")
public class WebSocketController {


    @GetMapping(value = "/register")
    public void register(String str){
        WebSocketClient client = JavaClient.initClient("ws://127.0.0.1:6666/websocket");
        if (client != null){
            client.send(str);
        }

    }
}
