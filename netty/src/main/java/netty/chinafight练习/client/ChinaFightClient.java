package netty.chinafight练习.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;

/**
 * @Author binbin
 * @Date 2024 04 18 10 46
 **/
public class ChinaFightClient {
    private static EventLoopGroup group;

    public static void main(String[] args) throws InterruptedException {
        ;
        ChannelFuture channelFuture = ClientConnect.getClient("localhost", 1949).connect();

    }
}
