package com.july.rpc.util;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.july.rpc.enumeration.RpcError;
import com.july.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Nacos相关工具类
 *
 * @author july
 */
@Slf4j
public class NacosUtil {

    /**
     * Nacos服务中心地址
     */
    private static final String SERVER_ADDRESS = "127.0.0.1:8848";

    private static final NamingService NAMING_SERVICE = getNacosNamingService();

    private static Set<String> serviceNames = new HashSet<>();

    private static InetSocketAddress address;

    public static NamingService getNacosNamingService() {
        try {
            return NamingFactory.createNamingService(SERVER_ADDRESS);
        } catch (NacosException e) {
            log.error("连接到Nacos时有错误发生: ", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_SERVICE_REGISTRY);
        }
    }

    /**
     * 注册服务
     *
     * @param serviceName 服务名
     * @param address 服务器地址
     * @throws NacosException
     */
    public static void registerService(String serviceName, InetSocketAddress address) throws NacosException {
        NAMING_SERVICE.registerInstance(serviceName, address.getHostName(), address.getPort());
        NacosUtil.address = address;
        serviceNames.add(serviceName);
    }

    /**
     * 获取所有服务实例
     *
     * @param serviceName
     * @return
     * @throws NacosException
     */
    public static List<Instance> getAllInstance(String serviceName) throws NacosException {
        return NAMING_SERVICE.getAllInstances(serviceName);
    }

    /**
     * 注销已关闭的服务
     *
     */
    public static void clearRegistry() {
        if (serviceNames.isEmpty() || address == null) {
            return;
        }
        for (String serviceName : serviceNames) {
            try {
                NAMING_SERVICE.deregisterInstance(serviceName, address.getHostName(), address.getPort());
            } catch (NacosException e) {
                log.error("注册服务 {} 失败", serviceName, e);
            }
        }
    }
}
