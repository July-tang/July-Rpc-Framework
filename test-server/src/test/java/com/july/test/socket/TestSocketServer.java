package com.july.test.socket;

import com.july.rpc.api.ByeService;
import com.july.rpc.api.HelloService;
import com.july.rpc.provider.ServiceProviderImpl;
import com.july.rpc.provider.ServiceProvider;
import com.july.rpc.transport.RpcServer;
import com.july.rpc.transport.socket.server.SocketServer;
import com.july.test.impl.ByeServiceImpl;
import com.july.test.impl.HelloServiceImpl;
import org.junit.Test;

/**
 * @author july
 */
public class TestSocketServer {

    @Test
    public void server() {
        HelloService helloService = new HelloServiceImpl();
        ByeService byeService = new ByeServiceImpl();
        RpcServer rpcServer = new SocketServer("127.0.0.1", 9999);
        rpcServer.publishService(helloService, HelloService.class);
        rpcServer.publishService(byeService, ByeService.class);
        rpcServer.start();
    }
}
