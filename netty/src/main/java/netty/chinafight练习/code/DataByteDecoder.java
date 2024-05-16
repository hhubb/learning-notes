package netty.chinafight练习.code;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Author binbin
 * @Date 2024 04 18 14 03
 **/
public class DataByteDecoder extends ByteToMessageDecoder {
    /**
     * 将接收到的数据在这里转换成服务需要的数据类型，以及检验数据的合法性，后续channelRead()可以不需要处理
     * ByteToMessageDecoder内置了一个缓存装置，这里的byteBuf是一个缓存集合
     *
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //起始标识符长度是一个字节，数据数组长度是四个字节
        if (byteBuf.readableBytes() < 5) {
            return;
        }
        //开始记录读取数据的位置
        byteBuf.markReaderIndex();
        //获取第一个字符，判断是否是数据开始的标识符
        byte identifier = byteBuf.readByte();
        if (identifier != 'B') {
            return;
        }
        //获取本次数据长度
        int dataLength = byteBuf.readInt();
        //如果后续数据字节长度不够，表示数据未接收完成
        if (byteBuf.readableBytes() < dataLength) {
            //还原已经读取过的数据，等后续数据到来
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[dataLength];
        byteBuf.readBytes(bytes);
        String message = new String(bytes, StandardCharsets.UTF_8);
        list.add(message);
    }
}
