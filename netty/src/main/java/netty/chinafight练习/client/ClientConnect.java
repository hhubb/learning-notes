package netty.chinafight练习.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.chinafight练习.code.DataByteDecoder;
import netty.chinafight练习.code.DataByteEncoder;

/**
 * @Author binbin
 * @Date 2024 04 18 14 41
 **/
public class ClientConnect {
    private EventLoopGroup group;
    private Integer port;
    private String host;
    private static ClientConnect clientConnect;

    private ClientConnect() {
    }

    private ClientConnect(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public static ClientConnect getClient(String host, Integer port) {
        if (clientConnect == null) {
            clientConnect = new ClientConnect(host, port);
        }
        return clientConnect;
    }

    public static ClientConnect getReconnectClient() {
        if (clientConnect == null) {
            clientConnect = new ClientConnect();
        }
        return clientConnect;
    }

    public ChannelFuture connect() throws InterruptedException {
        group = new NioEventLoopGroup();
        Bootstrap clientBootStrap = new Bootstrap();
        clientBootStrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new DataByteDecoder());
                        socketChannel.pipeline().addLast(new DataByteEncoder());

                        socketChannel.pipeline().addLast(new ChinaFightClientHandler());
                    }
                });
        return clientBootStrap.connect(host, port).sync();
    }
}
