package com.july.rpc.registry;

/**
 * 服务注册接口
 *
 * @author july
 */
public interface ServiceRegistry {

    /**
     * 注册一个服务
     *
     * @param service
     * @param <T>
     */
    <T> void register(T service);

    /**
     * 获取服务
     *
     * @param serviceName
     * @return
     */
    Object getService(String serviceName);
}
