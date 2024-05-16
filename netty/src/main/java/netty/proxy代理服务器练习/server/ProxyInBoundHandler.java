package netty.proxy代理服务器练习.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @Author binbin
 * @Date 2024 04 25 13 45
 **/
@Slf4j
public class ProxyInBoundHandler extends SimpleChannelInboundHandler {
    private static String remoteHost;
    private static Integer remotePost;
    private Channel outboundChannel;
    private Channel inboundChannel;

    public ProxyInBoundHandler(String remoteHost, Integer remotePost) {
        this.remoteHost = remoteHost;
        this.remotePost = remotePost;
    }

    //使用Bootstrap创建一个client，用来连接远程要代理的服务器，我们将这个client端的创建放在channelActive方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        inboundChannel = ctx.channel();
        // 开启outbound连接
        Bootstrap proxyClient = new Bootstrap();
        proxyClient.group(inboundChannel.eventLoop())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ProxyOutBoundHandler(inboundChannel));
                    }
                }).option(ChannelOption.AUTO_READ, false);
        ChannelFuture channelFuture = proxyClient.connect(remoteHost, remotePost);
        outboundChannel = channelFuture.channel();
        channelFuture.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                // 连接建立完毕，读取inbound数据
                inboundChannel.read();
            } else {
                // 关闭inbound channel
                inboundChannel.close();
            }
        });
    }

    /**
     * 将客户端发来的的消息通过outboundChannel发给服务端
     *
     * @param channelHandlerContext
     * @param o
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        ByteBuf msg = (ByteBuf) o;

        log.info(channelHandlerContext.channel().remoteAddress().toString() + "发来的消息：" + msg.toString(CharsetUtil.UTF_8));
        if (!outboundChannel.isActive()) {
            log.error("远程服务已断开！");
            return;
        }
        log.info("开始转发........");
        outboundChannel.writeAndFlush(Unpooled.wrappedBuffer(("代理后的消息：" + msg.toString(CharsetUtil.UTF_8)).getBytes(StandardCharsets.UTF_8))).addListener((ChannelFutureListener) future -> {
            //当outboundChannel写成功之后，再继续inboundChannel的读取工作。
            if (future.isSuccess()) {
                // flush成功，读取下一个消息
//                channelHandlerContext.channel().read();
                outboundChannel.read();
            } else {
//                channelHandlerContext.channel().close();
                outboundChannel.close();
            }
        });
    }

}
