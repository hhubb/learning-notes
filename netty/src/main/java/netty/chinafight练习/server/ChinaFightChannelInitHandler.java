package netty.chinafight练习.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import netty.chinafight练习.code.DataByteDecoder;
import netty.chinafight练习.code.DataByteEncoder;

/**
 * @Author binbin
 * @Date 2024 04 18 10 38
 **/
public class ChinaFightChannelInitHandler extends ChannelInitializer {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        /**
         * 添加各种处理器是有顺序的
         * 需要先设置解码Decoder
         * 再设置编码Encoder
         * 再进行数据处理
         */
        channel.pipeline().addLast(new DataByteDecoder());
        channel.pipeline().addLast(new DataByteEncoder());

        channel.pipeline().addLast(new ChinaFightServerHandler());
    }
}
