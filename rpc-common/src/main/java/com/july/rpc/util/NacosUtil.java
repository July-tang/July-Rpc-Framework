package com.july.rpc.util;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.july.rpc.enumeration.RpcError;
import com.july.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

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

    public static NamingService getNacosNamingService() {
        try {
            return NamingFactory.createNamingService(SERVER_ADDRESS);
        } catch (NacosException e) {
            log.error("连接到Nacos时有错误发生: ", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_SERVICE_REGISTRY);
        }
    }

    public static void registerService(String serviceName, InetSocketAddress address) throws NacosException {
        NAMING_SERVICE.registerInstance(serviceName, address.getHostName(), address.getPort());
    }

    public static List<Instance> getAllInstance(String serviceName) throws NacosException {
        return NAMING_SERVICE.getAllInstances(serviceName);
    }
}
