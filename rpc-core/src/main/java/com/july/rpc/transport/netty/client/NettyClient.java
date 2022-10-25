package com.july.rpc.transport.netty.client;

import com.july.rpc.codec.CommonDecoder;
import com.july.rpc.codec.CommonEncoder;
import com.july.rpc.entity.RpcRequest;
import com.july.rpc.entity.RpcResponse;
import com.july.rpc.serializer.CommonSerializer;
import com.july.rpc.serializer.JsonSerializer;
import com.july.rpc.transport.RpcClient;
import com.july.rpc.transport.netty.handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author july
 */
@Slf4j
public class NettyClient implements RpcClient {

    private final String host;

    private final int port;

    private static final Bootstrap bootstrap;

    private static int SERIALIZER_CODE = 0;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public NettyClient(String host, int port, int code) {
        this.host = host;
        this.port = port;
        SERIALIZER_CODE = code;
    }

    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                new CommonDecoder(),
                                new CommonEncoder(CommonSerializer.getByCode(SERIALIZER_CODE)),
                                new NettyClientHandler());
                    }
                });
    }

    @Override
    public Object sendRequest(RpcRequest request) {
        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();
            log.info("客户端连接到服务器 {}:{}", host, port);
            Channel channel = future.channel();
            if (channel != null) {
                channel.writeAndFlush(request).addListener(f -> {
                    if (f.isSuccess()) {
                        log.info(String.format("客户端发送消息: %s", request.toString()));
                    } else {
                        log.error("客户端发送RPC请求时发生错误：", f.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = channel.attr(key).get();
                return rpcResponse.getData();
            }
        } catch (InterruptedException e) {
            log.error("客户端发送RPC请求时发生错误：", e);
        }
        return null;
    }
}
