package netty.chinafight练习.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author binbin
 * @Date 2024 04 18 10 39
 **/
@Slf4j
public class ChinaFightServerHandler extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        String content = (String) o;
        log.info("Client Message : " + content);
        if (content.equals("中国")) {
            Thread.sleep(3000);
            channelHandlerContext.channel().writeAndFlush("加油！");
        }
    }
}
