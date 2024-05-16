package netty.proxy代理服务器练习.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;

/**
 * @Author binbin
 * @Date 2024 04 25 13 49
 **/
public class ProxyClient {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap proxyClient = new Bootstrap();
        proxyClient.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ProxyClientHandler());
                    }
                }).option(ChannelOption.SO_BACKLOG, 1024);
        ChannelFuture channelFuture = proxyClient.connect("localhost", 4444).sync();
        //等待三秒钟是连接完全建立
        Thread.sleep(3000);
        //开始通信
        channelFuture.channel().writeAndFlush(Unpooled.wrappedBuffer("你好！我是原始Client！".getBytes(StandardCharsets.UTF_8)));
    }
}
