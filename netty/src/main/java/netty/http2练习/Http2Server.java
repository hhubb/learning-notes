package netty.http2练习;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http2.Http2SecurityUtil;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.*;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

/**
 * @Author binbin
 * @Date 2024 04 24 15 11
 **/
public class Http2Server {
    public static void main(String[] args) {

    }

    private ServerBootstrap buildIMageHttp1Server(NioEventLoopGroup boss, NioEventLoopGroup work) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new HttpRequestDecoder());
                        socketChannel.pipeline().addLast(new HttpRequestEncoder());
                        socketChannel.pipeline().addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
                        socketChannel.pipeline().addLast(new Http1ServerHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 1024);
        return serverBootstrap;
    }

    private ServerBootstrap buildIMageHttp2Server(NioEventLoopGroup boss) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        SslProvider provider = SslProvider.isAlpnSupported(SslProvider.OPENSSL) ? SslProvider.OPENSSL : SslProvider.JDK;
                        SslContext sslContext = SslContextBuilder.forClient()
                                .sslProvider(provider)
                                .ciphers(Http2SecurityUtil.CIPHERS, SupportedCipherSuiteFilter.INSTANCE)
                                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                                .applicationProtocolConfig(new ApplicationProtocolConfig(
                                        ApplicationProtocolConfig.Protocol.ALPN,
                                        ApplicationProtocolConfig.SelectorFailureBehavior.NO_ADVERTISE,
                                        ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT,
                                        ApplicationProtocolNames.HTTP_2,
                                        ApplicationProtocolNames.HTTP_1_1
                                )).build();
                        socketChannel.pipeline().addLast(sslContext.newHandler(socketChannel.alloc()));
                        socketChannel.pipeline().addLast(new CustProtocolNegotiationHandler(""));
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 1024);
        return serverBootstrap;
    }
}
