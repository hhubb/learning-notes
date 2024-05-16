package netty.http练习.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;

/**
 * @Author binbin
 * @Date 2024 04 22 14 38
 **/
public class HttpServerInitHandler extends ChannelInitializer {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        //当客户端向服务器端发送HTTP请求之后，服务器端需要把接收到的数据使用解码器解码成为可以被应用程序使用的各种HttpObject对象，从而能够在应用程序中对其解析。
        channel.pipeline().addLast(new HttpServerCodec());
        //在HTTP中有一个独特的功能叫做，100 (Continue) Status，就是说client在不确定server端是否会接收请求的时候，可以先发送一个请求头，
        //并在这个头上加一个"100-continue"字段，但是暂时还不发送请求body。直到接收到服务器端的响应之后再发送请求body。
        //为了处理这种请求，netty提供了一个HttpServerExpectContinueHandler对象，用来处理100 Status的情况。
        channel.pipeline().addLast(new HttpServerExpectContinueHandler());
        channel.pipeline().addLast(new HttpContentCompressor());
        //跨域处理
        CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin().allowNullOrigin().allowCredentials().build();
        channel.pipeline().addLast(new CorsHandler(corsConfig));
        channel.pipeline().addLast(new HttpServerHandler());
    }
}
