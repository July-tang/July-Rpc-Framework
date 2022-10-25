package com.july.rpc.registry;

import java.net.InetSocketAddress;

/**
 * 服务注册接口
 *
 * @author july
 */
public interface ServiceRegistry {

    /**
     * 注册服务
     *
     * @param serviceName 服务名称
     * @param inetSocketAddress 提供服务的地址
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);
}
