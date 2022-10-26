package com.july.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 负载均衡通用接口
 *
 * @author july
 */
public interface LoadBalancer {

    /**
     * 负载均衡
     *
     * @param instances
     * @return 返回选择的服务实例
     */
    Instance select(List<Instance> instances);
}
