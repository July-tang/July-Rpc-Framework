package com.july.rpc.provider;

/**
 * 服务提供接口
 *
 * @author july
 */
public interface ServiceProvider {

    /**
     * 添加服务提供方
     *
     * @param service 服务实例
     * @param serviceName 服务名
     * @param <T>
     */
    <T> void addServiceProvider(T service, String serviceName);

    /**
     * 获取服务提供方
     *
     * @param serviceName
     * @return
     */
    Object getServiceProvider(String serviceName);
}
