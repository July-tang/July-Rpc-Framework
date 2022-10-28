package com.july.rpc.provider;

import com.july.rpc.enumeration.RpcError;
import com.july.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的服务提供方，保存服务端本地服务
 *
 * @author july
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {

    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    @Override
    public <T> void addServiceProvider(T service, String serviceName) {
        if (!serviceMap.containsKey(serviceName)) {
            Class<?>[] interfaces = service.getClass().getInterfaces();
            if (interfaces.length == 0) {
                throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
            }
            for (Class<?> i : interfaces) {
                serviceMap.put(i.getCanonicalName(), service);
            }
            log.info("向接口：{} 注册服务：{}", interfaces, serviceName);
        }
    }

    @Override
    public Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
