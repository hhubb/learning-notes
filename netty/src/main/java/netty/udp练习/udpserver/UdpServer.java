package netty.udp练习.udpserver;

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
public class UdpServer {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioDatagramChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.SO_BROADCAST,true);
        //因为UDP是面向无连接的，不需要在通信开始之前建立连接，所以服务端存在地址绑定一说。
        //UDP发送的数据报文中包含目标地址和端口信息，因此在发送数据时就已经确定了目标地址，无需额外的绑定操作。
      ChannelFuture channelFuture= bootstrap.bind(0).sync();
    }
}
