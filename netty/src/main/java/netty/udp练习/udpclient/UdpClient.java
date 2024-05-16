package netty.udp练习.udpclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Author binbin
 * @Date 2024 04 18 15 47
 **/
public class UdpClient {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap client = new Bootstrap();
        client.group(new NioEventLoopGroup())
                .channel(NioDatagramChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                //SO_BROADCAST  广播 ,因为UDP是以广播的形式发送消息的
                .option(ChannelOption.SO_BROADCAST, true);
        ChannelFuture channelFuture = client.bind(0).sync();
    }
}
