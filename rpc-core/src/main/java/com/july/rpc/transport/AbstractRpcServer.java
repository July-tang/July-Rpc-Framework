package com.july.rpc.transport;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.july.rpc.annotation.Service;
import com.july.rpc.annotation.ServiceScan;
import com.july.rpc.enumeration.RpcError;
import com.july.rpc.exception.RpcException;
import com.july.rpc.provider.ServiceProvider;
import com.july.rpc.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * RPC服务器的抽象类
 *
 * @author july
 */
@Slf4j
public abstract class AbstractRpcServer implements RpcServer {

    protected String host;
    protected int port;

    protected ServiceRegistry registry;
    protected ServiceProvider provider;

    @Override
    public <T> void publishService(Object service, String serviceName) {
        provider.addServiceProvider(service, serviceName);
        registry.register(serviceName, new InetSocketAddress(host, port));
    }

    /**
     * 扫描服务
     *
     */
    @Override
    public void scanServices(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(ServiceScan.class)) {
            log.error("启动类缺少@ServiceScan注解！");
            throw new RpcException(RpcError.SERVICE_SCAN_PACKAGE_NOT_FOUND);
        }
        String[] path = clazz.getAnnotation(ServiceScan.class).value();
        if (ArrayUtil.isEmpty(path)) {
            return;
        }
        Set<Object> services = new LinkedHashSet<>();
        for (String basePackage : path) {
            Set<Class<?>> candidates = ClassUtil.scanPackageByAnnotation(basePackage, Service.class);
            for (Class<?> candidate : candidates) {
                Object obj;
                try {
                    obj = candidate.newInstance();
                    services.add(obj);
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("创建 " + candidate + " 时有错误发生");
                }
            }
        }
        for (Object service : services) {
            Class<?> serviceClass = service.getClass();
            String value = serviceClass.getAnnotation(Service.class).value();
            if (StrUtil.isEmpty(value)) {
                for (Class<?> inter : serviceClass.getInterfaces()) {
                    publishService(service, inter.getCanonicalName());
                }
            } else {
                publishService(service, value);
            }
        }
    }
}
