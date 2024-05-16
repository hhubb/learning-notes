package websocket.core;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author binbin
 * @Date 2024 04 02 14 39
 **/
//注册成组件
@Component
//定义websocket服务器端，它的功能主要是将目前的类定义成一个websocket服务器端。注解的值将被用于监听用户连接的终端访问URL地址
@ServerEndpoint("/ws/server/{param}")
//如果不想每次都写private  final Logger logger = LoggerFactory.getLogger(当前类名.class); 可以用注解@Slf4j;可以直接调用log.info
@Slf4j
public class WebSocket {
    //存储每一个客户端会话信息的线程安全的集合
    private static final CopyOnWriteArraySet<Session> sessions = new CopyOnWriteArraySet<>();
    private static final ConcurrentHashMap<String, Session> connectorSessionMap = new ConcurrentHashMap<>();
    //使用线程安全的计数器，记录在connector
    private static final AtomicInteger onlineConnectorCount = new AtomicInteger(0);

    //实例一个session，这个session是websocket的session
    private Session session;

    //存放websocket的集合（本次demo不会用到，聊天室的demo会用到）
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    //客户端请求时一个websocket时
    @OnOpen
    public void onOpen(Session session, @PathParam("param") String param) {
        //存储会话信息
        //存储会话信息
        connectorSessionMap.put(param, session);
        //打印日志
        log.info("有连接器：" + param + " 加入，当前连接数为：" + connectorSessionMap.size());

        //给客户端发消息
        this.sendMessage(session, "连接成功");


    }

    //客户端关闭时一个websocket时
    @OnClose
    public void onClose() {
        //删除会话信息
        sessions.remove(session);
        //计数-1
        int cnt = onlineConnectorCount.decrementAndGet();
        //打印日志
        log.info("有连接关闭，当前连接数为：" + cnt);
    }

    //收到客户端消息时调用的方法
    @OnMessage
    public void onMessage(Session session, String message) {
        //打印日志
        log.info("来自客户端的消息：" + message);

        //给客户端发消息
        //判断消息内容：如果是请求资源
        if (StringUtils.isEmpty(message)) {
            log.warn("接收消息为空");
            return;
        }
        //解析消息体
        WsMessage wsMessage = new WsMessage();
        try {
            wsMessage = JSONObject.parseObject(message, WsMessage.class);
            //给客户端发消息
            this.sendMessage(connectorSessionMap.get(wsMessage.getTargetConnectorId()), message);
            log.info("消息发送成功, targetConnectorId={}, message={}", wsMessage.getTargetConnectorId(), message);
        } catch (Exception e) {
            log.error("消息处理异常，连接器ID={}, flag={}", wsMessage.getConnectorId(), wsMessage.getFlag(), e);
        }

    }

    public static byte[] toByteArray(String filename) throws IOException {

        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length())) {
            BufferedInputStream in;
            in = new BufferedInputStream(new FileInputStream(f));
            int bufSize = 1024;
            byte[] buffer = new byte[bufSize];
            int len;
            while (-1 != (len = in.read(buffer, 0, bufSize))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 发送消息
     *
     * @param session
     * @param message
     */
    public void sendMessage(Session session, byte[] message) {
        try {
            //发送消息
            session.getBasicRemote().sendBinary(ByteBuffer.wrap(message));
        } catch (IOException e) {
            //打印日志
            log.error("发送消息出错：" + e.getMessage());
        }
    }

    public void sendMessage(Session session, String message) {
        try {
            //发送消息
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            //打印日志
            log.error("发送消息出错：" + e.getMessage());
        }
    }

}