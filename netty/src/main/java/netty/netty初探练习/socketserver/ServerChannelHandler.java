package netty.netty初探练习.socketserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @Author binbin
 * @Date 2024 04 15 17 04
 **/
@Slf4j
public class ServerChannelHandler extends SimpleChannelInboundHandler {

    @Override
    //在ChannelHandlerContext中，调用channel（）获得绑定的channel。可以通过调用handler（）获得绑定的Handler。通过调用fire*方法来触发Channel的事件。
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {


//        //数据缓冲区中未读数据字节数
//        log.info("readableBytes" + byteBuf.readableBytes());
//        //数据缓冲区中未读数据起始字节index
//        log.info("readerIndex" + byteBuf.readerIndex());
//        //数据缓冲区中写区数据起始字节index
//        log.info("writerIndex" + byteBuf.writerIndex());
//        log.info(byteBuf.toString(CharsetUtil.US_ASCII));
//        byte[] read = new byte[1];
//        byteBuf.readBytes(read);
//        log.info(read.toString());
//        log.info(byteBuf.toString(CharsetUtil.US_ASCII));
//        //Discardable bytes空间删除，将多余的空间放到writable bytes中，但不会删除其余未读数据
//        byteBuf.discardReadBytes();
//        /**
//         *  +-------------------+------------------+------------------+
//         *     | discardable bytes |  readable bytes  |  writable bytes  |
//         *     +-------------------+------------------+------------------+
//         *     |                   |                  |                  |
//         *     0      <=      readerIndex   <=   writerIndex    <=    capacity
//         *
//         *     调用 discardReadBytes()之后：
//         *       +------------------+--------------------------------------+
//         *     |  readable bytes  |    writable bytes (got more space)   |
//         *     +------------------+--------------------------------------+
//         *     |                  |                                      |
//         * readerIndex (0) <= writerIndex (decreased)        <=        capacity
//         */
//        log.info("readableBytes" + byteBuf.readableBytes());
//        log.info("readerIndex" + byteBuf.readerIndex());
//        log.info("writerIndex" + byteBuf.writerIndex());
//        log.info(byteBuf.toString(CharsetUtil.US_ASCII));
//        //将readerIndex 和 writerIndex 清零，删除所有数据。
//        byteBuf.clear();
//        /**
//         *     +-------------------+------------------+------------------+
//         *     | discardable bytes |  readable bytes  |  writable bytes  |
//         *     +-------------------+------------------+------------------+
//         *     |                   |                  |                  |
//         *     0      <=      readerIndex   <=   writerIndex    <=    capacity
//         *     调用 clear()之后：
//         *         +---------------------------------------------------------+
//         *     |             writable bytes (got more space)             |
//         *     +---------------------------------------------------------+
//         *     |                                                         |
//         *     0 = readerIndex = writerIndex            <=            capacity
//         */
//        log.info("readableBytes" + byteBuf.readableBytes());
//        log.info("readerIndex" + byteBuf.readerIndex());
//        log.info("writerIndex" + byteBuf.writerIndex());
        ByteBuf byteBuf = (ByteBuf) msg;
        log.info(byteBuf.toString(CharsetUtil.UTF_8));

//        finally {
//            //在Netty中，引用计数（Reference Counting）是一种管理内存的技术，用于确保资源在不再被引用时可以被正确释放，从而避免内存泄漏。
//            //该接口定义了两个方法：retain()用于增加对象的引用计数，release()用于减少对象的引用计数。当对象的引用计数减少到0时，该对象会被释放。
//            ReferenceCountUtil.release(msg);
//        }
//        Channel channel = channelHandlerContext.channel();
//        Scanner scanner = new Scanner(System.in);
        String s = "我收到了！";
        channelHandlerContext.writeAndFlush(Unpooled.wrappedBuffer(("你好！我是原始Server").getBytes(StandardCharsets.UTF_8)));


    }
}
