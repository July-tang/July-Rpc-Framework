package com.july.rpc.registry;

import java.net.InetSocketAddress;

/**
 * 服务发现接口
 *
 * @author july
 */
public interface ServiceDiscovery {

    /**
     * 发现服务
     *
     * @param serviceName
     * @return
     */
    InetSocketAddress lookupService(String serviceName);
}
