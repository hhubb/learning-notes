package netty.netty初探练习.socketclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @Author binbin
 * @Date 2024 04 15 17 21
 **/
@Slf4j
public class ClientChannelHandler extends ChannelInboundHandlerAdapter {
    private ChannelHandlerContext ctx;
    private ByteBuf content;

    /**
     * 执行一些初始化操作或者发送一些初始消息
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("accepted channel: {}", ctx.channel());
        log.info("accepted channel parent: {}", ctx.channel().parent());
//        this.ctx = ctx;
//        content = ctx.alloc().directBuffer(256).writeBytes("Hello Server I am Client".getBytes(StandardCharsets.UTF_8));
//        ctx.writeAndFlush(content.retain());
    }

    // 处理接收到的消息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 处理接收到的消息
        String line = ((ByteBuf) msg).toString(StandardCharsets.UTF_8);
        System.out.println(line);
    }

}
