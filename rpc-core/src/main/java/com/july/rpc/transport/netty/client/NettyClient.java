package com.july.rpc.transport.netty.client;

import com.july.rpc.entity.RpcRequest;
import com.july.rpc.entity.RpcResponse;
import com.july.rpc.loadbalancer.LoadBalancer;
import com.july.rpc.loadbalancer.RoundRobinLoadBalancer;
import com.july.rpc.registry.NacosServiceDiscovery;
import com.july.rpc.registry.ServiceDiscovery;
import com.july.rpc.serializer.CommonSerializer;
import com.july.rpc.transport.RpcClient;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * Netty客户端类
 *
 * @author july
 */
@Slf4j
public class NettyClient implements RpcClient {

    private CommonSerializer serializer;

    private final ServiceDiscovery discovery;

    public NettyClient() {
        this(DEFAULT_SERIALIZER, new RoundRobinLoadBalancer());
    }

    public NettyClient(int code) {
        this(code, new RoundRobinLoadBalancer());
    }

    public NettyClient(LoadBalancer loadBalancer) {
        this(DEFAULT_SERIALIZER, loadBalancer);
    }

    public NettyClient(int code, LoadBalancer loadBalancer) {
        this.discovery = new NacosServiceDiscovery(loadBalancer);
        serializer = CommonSerializer.getByCode(code);
    }

    @Override
    public Object sendRequest(RpcRequest request) {
        try {
            InetSocketAddress inetSocketAddress = discovery.lookupService(request.getInterfaceName());
            Channel channel = NettyClientUtils.getChannel(inetSocketAddress, serializer);
            if(channel != null && channel.isActive()) {
                channel.writeAndFlush(request).addListener(future1 -> {
                    if (future1.isSuccess()) {
                        log.info(String.format("客户端发送消息: %s", request));
                    } else {
                        log.error("发送消息时有错误发生: ", future1.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = channel.attr(key).get();
                return rpcResponse.getData();
            } else {
                System.exit(0);
            }
        } catch (InterruptedException e) {
            log.error("发送消息时有错误发生: ", e);
        }
        return null;
    }
}
