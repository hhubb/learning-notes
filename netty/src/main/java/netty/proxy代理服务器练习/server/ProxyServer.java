package netty.proxy代理服务器练习.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Author binbin
 * @Date 2024 04 25 13 42
 **/
public class ProxyServer {
    private static final String REMOTE_HOST = "localhost";
    private static final Integer REMOTE_POST = 8888;

    public static void main(String[] args) throws InterruptedException {

        ServerBootstrap proxyServer = new ServerBootstrap();
        proxyServer.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ProxyInBoundHandler(REMOTE_HOST, REMOTE_POST));
                    }
                }).option(ChannelOption.SO_BACKLOG, 1024).childOption(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture channelFuture = proxyServer.bind(4444).sync();


    }

}
