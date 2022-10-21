package com.july.test;

import com.july.rpc.api.HelloService;
import com.july.rpc.transport.RpcServer;
import org.junit.Test;

/**
 * @author july
 */
public class TestServer {

    @Test
    public void server() {
        HelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer();
        rpcServer.register(helloService, 9000);
    }
}
