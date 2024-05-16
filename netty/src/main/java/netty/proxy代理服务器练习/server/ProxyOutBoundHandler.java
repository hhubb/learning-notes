package netty.proxy代理服务器练习.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @Author binbin
 * @Date 2024 04 25 13 51
 **/
@Slf4j
public class ProxyOutBoundHandler extends SimpleChannelInboundHandler {
    private Channel inboundChannel;

    public ProxyOutBoundHandler(Channel inboundChannel) {
        this.inboundChannel = inboundChannel;
    }

    /**
     * 将服务端返回的消息通过inboundChannel发给客户端
     * @param channelHandlerContext
     * @param o
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        ByteBuf msg = (ByteBuf) o;
        log.info(channelHandlerContext.channel().remoteAddress().toString() + "回复的消息：" + msg.toString(CharsetUtil.UTF_8));
        if (inboundChannel.isActive()) {
            // 将outboundChannel中的消息读取，并写入到inboundChannel中
            inboundChannel.writeAndFlush(Unpooled.wrappedBuffer(("原始服务器返回的消息：" + msg.toString(CharsetUtil.UTF_8)).getBytes(StandardCharsets.UTF_8))).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    inboundChannel.read();
                } else {
                    inboundChannel.close();
                }
            });
        }
    }
}
