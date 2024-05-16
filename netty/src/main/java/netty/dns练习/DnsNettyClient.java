package netty.dns练习;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.dns.*;

/**
 * @Author binbin
 * @Date 2024 04 28 13 57
 **/
public class DnsNettyClient {
    //在netty中使用Do53/TCP协议，进行DNS查询
    //选择使用阿里的IPv4:223.5.5.5为例。
    //有了IP地址，我们还需要指定netty的连接端口号，这里默认的是53。
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //因为我们向channel中写入的是DnsQuery,所以需要一个encoder将DnsQuery编码为ByteBuf,这里使用的是netty提供的TcpDnsQueryEncoder：
                        socketChannel.pipeline().addLast(new TcpDnsQueryEncoder());
                        socketChannel.pipeline().addLast(new TcpDnsResponseDecoder());
                        socketChannel.pipeline().addLast(new Do53Handler());
                    }
                }).option(ChannelOption.SO_BACKLOG, 1024);
        ChannelFuture channelFuture = bootstrap.connect("223.5.5.5", 53).sync();
        int randomID = (int) (System.currentTimeMillis() / 1000);
        String queryDomain = "www.flydean.com";
        DnsQuery query = new DefaultDnsQuery(randomID, DnsOpCode.QUERY)
                .setRecord(DnsSection.QUESTION, new DefaultDnsQuestion(queryDomain, DnsRecordType.A));
        channelFuture.channel().writeAndFlush(query);
    }
}
