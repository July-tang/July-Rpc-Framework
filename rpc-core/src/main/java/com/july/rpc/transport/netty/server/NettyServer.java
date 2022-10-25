package com.july.rpc.transport.netty.server;

import com.july.rpc.codec.CommonDecoder;
import com.july.rpc.codec.CommonEncoder;
import com.july.rpc.registry.ServiceRegistry;
import com.july.rpc.serializer.JsonSerializer;
import com.july.rpc.transport.RpcServer;
import com.july.rpc.transport.netty.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author july
 */
@Slf4j
public class NettyServer implements RpcServer {

    private final ServiceRegistry registry;

    public NettyServer(ServiceRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void start(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ChannelFuture future = new ServerBootstrap().group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new CommonEncoder(new JsonSerializer()),
                                    new CommonDecoder(),
                                    new NettyServerHandler(registry)
                            );
                        }
                    })
                    .bind(port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("启动服务器时发生错误： ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}