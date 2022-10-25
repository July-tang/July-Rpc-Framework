package com.july.test.netty;

import com.july.rpc.api.ByeService;
import com.july.rpc.api.HelloService;
import com.july.rpc.transport.RpcServer;
import com.july.rpc.transport.netty.server.NettyServer;
import com.july.test.impl.ByeServiceImpl;
import com.july.test.impl.HelloServiceImpl;
import org.junit.Test;

/**
 * @author july
 */
public class TestNettyServer {

    @Test
    public void server() {
        HelloService helloService = new HelloServiceImpl();
        ByeService byeService = new ByeServiceImpl();
        RpcServer server = new NettyServer("127.0.0.1", 9000);
        server.publishService(helloService, HelloService.class);
        server.publishService(byeService, ByeService.class);
        server.start();
    }
}
