package com.july.rpc.transport.netty.server;

import cn.hutool.core.util.ClassUtil;
import com.july.rpc.codec.CommonDecoder;
import com.july.rpc.codec.CommonEncoder;
import com.july.rpc.provider.ServiceProvider;
import com.july.rpc.provider.ServiceProviderImpl;
import com.july.rpc.registry.NacosServiceRegistry;
import com.july.rpc.registry.ServiceRegistry;
import com.july.rpc.serializer.CommonSerializer;
import com.july.rpc.transport.AbstractRpcServer;
import com.july.rpc.transport.RpcServer;
import com.july.rpc.transport.netty.handler.NettyServerHandler;
import com.july.rpc.util.ShutdownHook;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author july
 */
@Slf4j
public class NettyServer extends AbstractRpcServer {

    private final CommonSerializer serializer;

    public NettyServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }

    public NettyServer(String host, int port, int code) {
        this.host = host;
        this.port = port;
        this.serializer = CommonSerializer.getByCode(code);
        provider = new ServiceProviderImpl();
        registry = new NacosServiceRegistry();
    }

    @Override
    public void start() {
        ShutdownHook.addClearAllHook();
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
                                    new CommonEncoder(serializer),
                                    new CommonDecoder(),
                                    new NettyServerHandler()
                            );
                        }
                    })
                    .bind(host, port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("启动服务器时发生错误： ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
