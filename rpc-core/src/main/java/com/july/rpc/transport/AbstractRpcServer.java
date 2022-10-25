package com.july.rpc.transport;

import com.july.rpc.provider.ServiceProvider;
import com.july.rpc.registry.ServiceRegistry;

import java.net.InetSocketAddress;

/**
 * RPC服务器的抽象类
 *
 * @author july
 */
public abstract class AbstractRpcServer implements RpcServer {

    protected String host;
    protected int port;

    protected ServiceRegistry registry;
    protected ServiceProvider provider;

    @Override
    public <T> void publishService(Object service, Class<T> serviceClass) {
        provider.addServiceProvider(service);
        registry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
    }
}
