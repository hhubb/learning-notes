package netty.http练习.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author binbin
 * @Date 2024 04 22 14 40
 **/
public class HttpClient {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new HttpClientInitHandler());
        bootstrap.connect("localhost", 7777).sync();
    }
}
