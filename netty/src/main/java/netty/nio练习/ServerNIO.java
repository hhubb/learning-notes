package netty.nio练习;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author binbin
 * @Date 2024 04 17 15 55
 **/
@Slf4j
public class ServerNIO {
    //Channel是指客户端与服务器之间的通信通道
    //selector是一个多路复用器，存放对应通道的SelectionKey对象的集合
    public static void main(String[] args) throws IOException, InterruptedException {
        //生命一个socket协议为基础的server，并绑定地址和端口
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.bind(new InetSocketAddress("localhost", 8888));
        //设置是否为阻塞模式  false为非阻塞
        channel.configureBlocking(false);

        //声明一个selector，并将selector注册入channel，并将响应的类型设置为接受连接
        Selector selector = Selector.open();
        //将channel和事件类型注册到selector中，一旦有对应的请求，selector就可通过select()方法获取到
        channel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            //select()是阻塞的，只有等到有连接、读取、写、关闭事件加入到selector中发生才会继续
            selector.select();
            //获取selector中存在的SelectionKey.selectionKey可以有四种状态，分别是isReadable,isWritable,isConnectable和isAcceptable
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //SelectionKey 是否出去需要连接的状态（对应客户端的连接请求事件），如果是，可以进行读写了。这里先只注册读。
                if (key.isAcceptable()) {
                    register(selector, channel);
                }
                //SelectionKey 处于可读状态（对应客户端的消息请求事件），说明发起了读的请求，开始处理请求
                if (key.isReadable()) {
                    serverResponse(key);
                }
                iterator.remove();
            }
            Thread.sleep(1000);
        }

    }

    //从SelectionKey获取到channel，处理客户端发来的数据，并将回复的内容发送回去
    private static void serverResponse(SelectionKey key) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        SocketChannel socketChannel = (SocketChannel) key.channel();
        socketChannel.read(byteBuffer);
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);
        String message = new String(bytes).trim();
        log.info(message);
        if (message.equals("q") || message.equals("exit")) {
            log.info("发回去：Bye Bye");
            socketChannel.write(ByteBuffer.wrap("Bye Bye".getBytes(StandardCharsets.UTF_8)));
        } else {
            log.info("鹦鹉学舌：" + message);
            socketChannel.write(ByteBuffer.wrap(("鹦鹉学舌：" + message).getBytes(StandardCharsets.UTF_8)));
        }
    }

    //再次将selector注册到channel中，监听后续客户端发来的请求信息
    private static void register(Selector selector, ServerSocketChannel channel) throws IOException {
        log.info(channel.toString());
        SocketChannel acceptChannel = channel.accept();
        acceptChannel.configureBlocking(false);
        acceptChannel.register(selector, SelectionKey.OP_READ);
    }
}
