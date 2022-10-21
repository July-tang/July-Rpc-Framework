package com.july.test;

import com.july.rpc.api.ByeService;
import com.july.rpc.api.HelloService;
import com.july.rpc.registry.DefaultServiceRegistry;
import com.july.rpc.registry.ServiceRegistry;
import com.july.rpc.transport.RpcServer;
import com.july.test.impl.ByeServiceImpl;
import com.july.test.impl.HelloServiceImpl;
import org.junit.Test;

/**
 * @author july
 */
public class TestServer {

    @Test
    public void server() {
        HelloService helloService = new HelloServiceImpl();
        ByeService byeService = new ByeServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        registry.register(byeService);
        RpcServer rpcServer = new RpcServer(registry);
        rpcServer.start(9000);
    }
}
