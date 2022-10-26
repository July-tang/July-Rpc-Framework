package com.july.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡
 *
 * @author july
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    private final AtomicInteger idx = new AtomicInteger(0);

    @Override
    public Instance select(List<Instance> instances) {
        Instance instance = instances.get(idx.get());
        if (idx.incrementAndGet() >= instances.size()) {
            idx.set(0);
        }
        return instance;
    }
}
