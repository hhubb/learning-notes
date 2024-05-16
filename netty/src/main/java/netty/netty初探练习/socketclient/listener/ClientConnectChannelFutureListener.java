package netty.netty初探练习.socketclient.listener;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import lombok.extern.slf4j.Slf4j;
import netty.netty初探练习.socketclient.*;

import java.util.concurrent.TimeUnit;

/**
 * 监听连接
 * 是Netty中用于监听ChannelFuture的状态变化的接口。ChannelFuture表示异步的I/O操作，它通常与Channel相关联，用于表示一次I/O操作的结果或状态。
 * @Author binbin
 * @Date 2024 04 16 11 22
 **/
@Slf4j
public class ClientConnectChannelFutureListener implements ChannelFutureListener {
    private NettyClient nettyClient;

    public ClientConnectChannelFutureListener(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (channelFuture.isSuccess()) {
            log.info("连接成功！");
        } else {
            EventLoop loop = channelFuture.channel().eventLoop();
            log.warn("客户端已启动，与服务端建立连接失败,3s之后尝试重连!");
            loop.schedule(() -> nettyClient.connect(), 3, TimeUnit.SECONDS);
        }
    }
}
