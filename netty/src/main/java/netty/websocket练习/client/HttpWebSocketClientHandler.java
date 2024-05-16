package netty.websocket练习.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

/**
 * @Author binbin
 * @Date 2024 04 23 14 21
 **/
@Slf4j
public class HttpWebSocketClientHandler extends SimpleChannelInboundHandler {
    WebSocketClientHandshaker handshaker;
     ChannelPromise handshakeFuture;

    HttpWebSocketClientHandler(URI uri) {
        handshaker = WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        handshaker.handshake(ctx.channel());

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            if (!handshaker.isHandshakeComplete()) {
                try {
                    handshaker.finishHandshake(channelHandlerContext.channel(), (FullHttpResponse) msg);
                    handshakeFuture.setSuccess();
                    log.info("websocket Handshake 完成!");
                } catch (Exception exception) {
                    log.error(exception.getMessage());
                    handshakeFuture.setFailure(exception);
                }
                return;
            }
        }
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame frame = (TextWebSocketFrame) msg;
            log.info(frame.text());
        }

    }
}
