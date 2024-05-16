package netty.netty初探练习.socketclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.netty初探练习.socketclient.listener.ClientConnectChannelFutureListener;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @Author binbin
 * @Date 2024 04 15 17 27
 **/
public class NettyClient {
    private static EventLoopGroup group;
    private static Channel channel;

    private static void inti() {
        group = new NioEventLoopGroup();
    }

    public void connect() {
        inti();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ClientChannelHandler());
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8888);
        /**
         * 为什么不调用ChannelFuture 对象的sync 方法就无法继续执行？
         * connect 是异步非阻塞，main线程发起调用之后，因为并不是主线程去做连接服务器，所有主线程会向下执行立即返回，真正执行底层connect 的是另一个线程，是一个NIO线程，如果此时没有调用sync 主线程无阻塞的会继续向下执行，下面的channel 对象会建立成功，但实际上的tcp连接并没有建立成功所以没有爆空指针错误，而是代码执行无效。
         * 主线程因为异步非阻塞，其实并不知道channel 下的连接是否建立好，因为建立连接的是NIO线程而不是主线程
         * 这里connection 的建立需要一些事件，必须阻塞直到连接建立成功，再去继续执行才能执行成功。
         * 正确处理：
         * 问题的关键在于如何让主线程等待EventLoop中的子线程的tcp连接建立后，主线程正确的拿到channel对象(一般带有future/promise 都是异步方法配套使用的)
         * 方法一：channelFuture.sync() 同步处理结果
         * 发起的连接建立请求的线程会等待去建立的线程，知道它建立好了才会继续向下运行
         * 可以通过使用sync方法来同步结果
         * 阻塞住当前线程（调用sync方法的线程），直到nio线程连接建立完毕
         * 相当于主线程主动的去等待建立结果，实际执行针对连接建立后对象操作的还是主线程
         * 方法二：channelFuture.addListener(回调对象) 异步处理结果
         * 等结果的也不是主线程，而是将等待连接建立，等一系列的问题交给其他线程来处理
         * 回调对象传递给NIO线程，NIO线程的连接建立好了，NIO线程就会主动去调用operationComplete 方法，进行主线程分给的剩下的操作
         * 这里的ChannelFuture 对象和调用时是同一个
         */
        //sync()方法会阻塞直至连接完成
        //channelFuture.sync();
        //在future执行结束之后，被通知。不需要自己再去调用get等待future结束。这里实际上就是异步IO概念的实现，不需要主动去调用。
        /**
         * ChannelFutureListener 的operationComplete在连接建立成功后执行。（晚于channelActive）
         */
        channelFuture.addListener(new ClientConnectChannelFutureListener(this));
        channel = channelFuture.channel();
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            new NettyClient().connect();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String s = scanner.nextLine();
                if (s == null) {
                    continue;
                }
                if (s.equals("exit") || s.equals("q")) {
                    break;
                }
                byte[] bytes = ("Client: " + s + "\r\n").getBytes(StandardCharsets.UTF_8);
                channel.writeAndFlush(Unpooled.wrappedBuffer(bytes)).sync();
            }
            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

    }
}
