package netty.dns练习;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.dns.*;
import io.netty.util.NetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author binbin
 * @Date 2024 04 28 14 00
 **/
@Slf4j
public class Do53Handler extends SimpleChannelInboundHandler {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        if (o instanceof DefaultDnsResponse) {
            DefaultDnsResponse response = (DefaultDnsResponse) o;
            readMsg(response);
        }

    }

    private static void readMsg(DefaultDnsResponse msg) {
        if (msg.count(DnsSection.QUESTION) > 0) {
            DnsQuestion question = msg.recordAt(DnsSection.QUESTION, 0);
            log.info("question is :{}", question);
        }
        int i = 0, count = msg.count(DnsSection.ANSWER);
        while (i < count) {
            DnsRecord record = msg.recordAt(DnsSection.ANSWER, i);
            if (record.type() == DnsRecordType.A) {
                DnsRawRecord raw = (DnsRawRecord) record;
                log.info("ip address is: {}", NetUtil.bytesToIpAddress(ByteBufUtil.getBytes(raw.content())));
            }
            i++;
        }

    }
}
