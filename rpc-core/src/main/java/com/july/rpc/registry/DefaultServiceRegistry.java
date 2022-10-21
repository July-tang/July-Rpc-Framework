package com.july.rpc.registry;

import com.july.rpc.enumeration.RpcError;
import com.july.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册服务的默认实现
 *
 * @author july
 */
@Slf4j
public class DefaultServiceRegistry implements ServiceRegistry{

    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    @Override
    public <T> void register(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if (!serviceMap.containsKey(serviceName)) {
            Class<?>[] interfaces = service.getClass().getInterfaces();
            if (interfaces.length == 0) {
                throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
            }
            for (Class<?> i : interfaces) {
                serviceMap.put(i.getCanonicalName(), service);
            }
            serviceMap.put(serviceName, interfaces);
            log.info("向接口：{} 注册服务：{}", interfaces, serviceName);
        }
    }

    @Override
    public Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
