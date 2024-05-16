package netty.http练习.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @Author binbin
 * @Date 2024 04 22 14 37
 **/
@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler {
    private static final byte[] defaultContent = "欢迎来到www.hubinbin.com!".getBytes(StandardCharsets.UTF_8);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        if (msg instanceof HttpObject) {
            /**
             * 接收到的response会分解成两段：HttpRequest、HttpContent、LastHttpContent
             */
            if (msg instanceof LastHttpContent) {
                return;
            }
            if (msg instanceof HttpContent) {
                return;
            }
            HttpRequest req = (HttpRequest) msg;
            //在HTTP中有一个独特的功能叫做，100 (Continue) Status，就是说client在不确定server端是否会接收请求的时候，可以先发送一个请求头，并在这个头上加一个"100-continue"字段，但是暂时还不发送请求body。直到接收到服务器端的响应之后再发送请求body。
            // 如果服务器收到100Continue请求的话
            if (HttpUtil.is100ContinueExpected(req)) {
                send100Continue(channelHandlerContext);
                return;
            }
            boolean isKeepAlive = HttpUtil.isKeepAlive(req);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(req.protocolVersion(), HttpResponseStatus.OK, Unpooled.wrappedBuffer(defaultContent));
            response.headers().set("Content-Type", "text/plain;charset=utf-8")
                    .set("Content-Length", response.content().readableBytes());
            if (isKeepAlive) {
                if (!req.protocolVersion().isKeepAliveDefault()) {
                    response.headers().set("Connection", "keep-alive");
                }
            } else {
                response.headers().set("Connection", "close");
            }
            ChannelFuture future = channelHandlerContext.writeAndFlush(response);
            if (!isKeepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    private void send100Continue(ChannelHandlerContext channelHandlerContext) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.EMPTY_BUFFER);
        channelHandlerContext.writeAndFlush(response);
    }
}
