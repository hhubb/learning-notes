package netty.websocket练习.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Author binbin
 * @Date 2024 04 22 16 48
 **/
public class WebSocketServer {
    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap wsBootStrap = new ServerBootstrap();
        wsBootStrap.group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //添加HTTP服务数据编码解码器
                        socketChannel.pipeline().addLast(new HttpServerCodec());
                        // 对写大数据流的支持
                        socketChannel.pipeline().addLast(new ChunkedWriteHandler());
                        /**
                         * 我们通常接收到的是一个http片段，如果要想完整接受一次请求的所有数据，我们需要绑定HttpObjectAggregator，然后我们
                         * 就可以收到一个FullHttpRequest-是一个完整的请求信息。
                         *对httpMessage进行聚合，聚合成FullHttpRequest或FullHttpResponse
                         * 几乎在netty中的编程，都会使用到此hanler
                         */
                        socketChannel.pipeline().addLast(new HttpObjectAggregator(65536));
                        // ====================== 以上是用于支持http协议 , 以下是支持httpWebsocket   ======================

                        //添加WebSocket协议处理器，用于处理WebSocket握手、消息传输等操作。
                        socketChannel.pipeline().addLast(new WebSocketServerCompressionHandler());
                        socketChannel.pipeline().addLast(new WebSocketServerProtocolHandler("/src/main/java/websocket"));
                        socketChannel.pipeline().addLast(new WebSocketServerHandler());
                    }
                });
        wsBootStrap.bind(6666).sync();
    }
}
