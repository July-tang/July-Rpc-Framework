package com.july.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.july.rpc.enumeration.RpcError;
import com.july.rpc.exception.RpcException;
import com.july.rpc.util.NacosUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * Nacos服务注册中心
 *
 * @author july
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry{

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerService(serviceName, inetSocketAddress);
        } catch (NacosException e) {
            log.error("注册服务时出现异常：", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }
}
