package netty.chinafight练习.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Author binbin
 * @Date 2024 04 18 10 35
 **/
public class ChinaFightServer {
    private static EventLoopGroup boss;
    private static EventLoopGroup child;

    public static void main(String[] args) throws InterruptedException {
        boss = new NioEventLoopGroup();
        child = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, child)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChinaFightChannelInitHandler())
                .option(ChannelOption.SO_BACKLOG, 1000)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
       ChannelFuture channelFuture= serverBootstrap.bind(1949).sync();
    }

}
