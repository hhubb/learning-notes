package netty.http2练习;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.DefaultHttp2Connection;
import io.netty.handler.codec.http2.HttpToHttp2ConnectionHandlerBuilder;
import io.netty.handler.codec.http2.InboundHttp2ToHttpAdapter;
import io.netty.handler.codec.http2.InboundHttp2ToHttpAdapterBuilder;
import io.netty.handler.ssl.ApplicationProtocolNegotiationHandler;

/**
 * @Author binbin
 * @Date 2024 04 24 15 23
 **/
public class CustProtocolNegotiationHandler extends ApplicationProtocolNegotiationHandler {
    protected CustProtocolNegotiationHandler(String fallbackProtocol) {
        super(fallbackProtocol);
    }

    @Override
    protected void configurePipeline(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        DefaultHttp2Connection connection = new DefaultHttp2Connection(true);
        InboundHttp2ToHttpAdapter listener = new InboundHttp2ToHttpAdapterBuilder(connection)
                .propagateSettings(true)
                .validateHttpHeaders(false)
                .maxContentLength(Integer.MAX_VALUE).build();
        channelHandlerContext.pipeline().addLast(new HttpToHttp2ConnectionHandlerBuilder().frameListener(listener).connection(connection).build());
        channelHandlerContext.pipeline().addLast(new Http2RequestHandler());
    }
}
