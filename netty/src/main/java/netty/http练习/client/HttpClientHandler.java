package netty.http练习.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.ClientCookieEncoder;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @Author binbin
 * @Date 2024 04 22 14 42
 **/
@Slf4j
public class HttpClientHandler extends SimpleChannelInboundHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/getServerName", Unpooled.wrappedBuffer("我来访问网站了".getBytes(StandardCharsets.UTF_8)));
        request.headers().set(HttpHeaderNames.HOST, "127.0.0.1");
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
        //设置cookie我们使用的是ClientCookieEncoder.encode方法，ClientCookieEncoder有两种encoder模式，一种是STRICT，一种是LAX。
        //在STRICT模式下，会对cookie的name和value进行校验和排序。
        //和encoder对应的就是ClientCookieDecoder，用于对cookie进行解析。
        //设置好我们所有的request之后就可以写入到channel中了。
        request.headers().set(HttpHeaderNames.COOKIE, ClientCookieEncoder.STRICT.encode(new DefaultCookie("name", "hubinbin"), new DefaultCookie("site", "www.hubinbin.com")));
        ctx.writeAndFlush(request);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        /**
         * 接收到的response会分解成三段：HttpResponse、HttpContent、LastHttpContent
         */
        if (msg instanceof HttpObject) {
            if (msg instanceof HttpResponse) {
                return;
            }
            if (msg instanceof LastHttpContent) {
                return;
            }
            HttpContent resp = (HttpContent) msg;
            ByteBuf byteBuf = resp.content();
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            log.info(new String(bytes, StandardCharsets.UTF_8));
        }
    }
}
