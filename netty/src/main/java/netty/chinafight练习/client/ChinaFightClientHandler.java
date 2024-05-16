package netty.chinafight练习.client;

import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author binbin
 * @Date 2024 04 18 10 49
 **/
@Slf4j
public class ChinaFightClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("————————————————————————连接成功！——————————————————————");
        ctx.channel().writeAndFlush("中国");
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf message = (ByteBuf) msg;
        String content = (String) msg;
        log.info("Server Message : " + content);
        if (content.equals("加油！")) {
            Thread.sleep(3000);
            ctx.channel().writeAndFlush("中国");
        }
    }

    @Override
    //连接断开后触发
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接断开：" + ctx.channel().remoteAddress());
    }

    @Override
    //连接断开后,channelInactive()执行之后
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("尝试重连....");
        ctx.channel().eventLoop().schedule(() -> {
            log.info("重连中.....");
            try {
                ClientConnect.getReconnectClient().connect();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 3, TimeUnit.SECONDS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("........................服务端连接异常....................");
    }
}
