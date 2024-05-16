package netty.chinafight练习.code;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

/**
 * @Author binbin
 * @Date 2024 04 18 16 49
 **/
public class DataByteEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        String message = (String) o;
        byte[] dataBytes = message.getBytes(StandardCharsets.UTF_8);
        //起始标识符长度是一个字节
        byteBuf.writeByte('B');
        //数据数组长度是四个字节
        byteBuf.writeInt(dataBytes.length);
        byteBuf.writeBytes(dataBytes);
    }
}
