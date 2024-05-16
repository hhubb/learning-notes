package netty.websocket练习.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author binbin
 * @Date 2024 04 23 14 18
 **/
public class WebSocketClient {
    public static void main(String[] args) throws InterruptedException, URISyntaxException {
        Bootstrap bootstrap = new Bootstrap();
        URI websocketURI = new URI("ws://localhost:6666/websocket");
        HttpWebSocketClientHandler handler = new HttpWebSocketClientHandler(websocketURI);
        bootstrap.group(new NioEventLoopGroup(1))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new HttpServerCodec());
                        socketChannel.pipeline().addLast(new HttpObjectAggregator(65535));
                        socketChannel.pipeline().addLast("wsh", handler);
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(websocketURI.getHost(), websocketURI.getPort()).sync();
        handler.handshakeFuture.sync();
        channelFuture.channel().writeAndFlush(new TextWebSocketFrame("WebSocket_hbb_123"));
    }
}
