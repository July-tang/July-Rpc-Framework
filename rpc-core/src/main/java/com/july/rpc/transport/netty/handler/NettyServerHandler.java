package com.july.rpc.transport.netty.handler;

import com.july.rpc.entity.RpcRequest;
import com.july.rpc.entity.RpcResponse;
import com.july.rpc.handler.RequestHandler;
import com.july.rpc.provider.ServiceProvider;
import com.july.rpc.util.ThreadPoolFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * @author july
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private final RequestHandler requestHandler;

    private static final String THREAD_NAME_PREFIX = "netty-server-handler";

    private final ExecutorService threadPool;

    public NettyServerHandler() {
        requestHandler = new RequestHandler();
        threadPool = ThreadPoolFactory.createDefaultThreadPool(THREAD_NAME_PREFIX);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        threadPool.execute(() -> {
            try {
                log.info("服务器收到请求：{}", msg);
                Object result = requestHandler.handle(msg);
                ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result));
                future.addListener(ChannelFutureListener.CLOSE);
            } finally {
                ReferenceCountUtil.release(msg);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时出现异常：{}", cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }
}
