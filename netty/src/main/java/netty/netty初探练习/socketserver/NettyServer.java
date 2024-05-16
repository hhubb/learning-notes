package netty.netty初探练习.socketserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Author binbin
 * @Date 2024 04 15 17 02
 **/
public class NettyServer {
    private static EventLoopGroup bossGroup;
    private static EventLoopGroup childGroup;

    private static void init() {
        bossGroup = new NioEventLoopGroup();
        childGroup = new NioEventLoopGroup();
    }

    public static void main(String[] args) throws InterruptedException {
        init();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //EventLoopGroup，主要用来进行channel的注册和遍历
        //parent EventLoopGroup来接受连接，然后使用child EventLoopGroup来执行具体的命令。所以在ServerBootstrap中多了一个childGroup和对应的childHandler:
        //如果group只写一个参数，那么selector处理accept和client事件都由这个NioEventLoopGroup处理，如果是两个，则分开。
        serverBootstrap.group(bossGroup, childGroup)
                //channel或者ChannelFactory，用来指定Bootstrap中使用的channel的类型。
                .channel(NioServerSocketChannel.class)
                //ChannelHandler,用来指定具体channel中消息的处理逻辑。
                // handler在初始化时就会执行
                .handler(new LoggingHandler(LogLevel.INFO))
                // 客户端连接成功后执行，主要处理后续传输的数据
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        /**
                         * 注意，Inbound和outbound的处理顺序是相反的，比如下面的例子：
                         *
                         *     ChannelPipeline p = ...;
                         *    p.addLast("1", new InboundHandlerA());
                         *    p.addLast("2", new InboundHandlerB());
                         *    p.addLast("3", new OutboundHandlerA());
                         *    p.addLast("4", new OutboundHandlerB());
                         *    p.addLast("5", new InboundOutboundHandlerX());
                         *
                         *
                         * 上面的代码中我们向ChannelPipeline添加了5个handler，其中2个InboundHandler,2个OutboundHandler和一个同时处理In和Out的Handler。
                         *
                         * 那么当channel遇到inbound event的时候，就会按照1，2，3，4，5的顺序进行处理，但是只有InboundHandler才能处理Inbound事件，所以，真正执行的顺序是1，2，5。
                         *
                         * 同样的当channel遇到outbound event的时候，会按照5,4,3,2,1的顺序进行执行，但是只有outboundHandler才能处理Outbound事件，所以真正执行的顺序是5，4，3.
                         */
                        channel.pipeline().addLast(new ServerChannelHandler());
                    }
                })
                //ChannelOptions,表示使用的channel对应的属性信息。
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        //SocketAddress,bootstrap启动是绑定的ip和端口信息。
        ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();
    }
}
