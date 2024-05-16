package netty.http练习.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.*;

/**
 * @Author binbin
 * @Date 2024 04 22 14 41
 **/
public class HttpClientInitHandler extends ChannelInitializer {


    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(new HttpResponseDecoder());
        channel.pipeline().addLast(new HttpRequestEncoder());
        channel.pipeline().addLast(new HttpContentDecompressor());
//        channel.pipeline().addLast(new HttpContentCompressor());
        channel.pipeline().addLast(new HttpClientHandler());
    }
}
