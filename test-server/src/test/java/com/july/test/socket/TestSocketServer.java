package com.july.test.socket;

import com.july.rpc.annotation.ServiceScan;
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
@ServiceScan
public class TestSocketServer {

    @Test
    public void server() {
        RpcServer rpcServer = new SocketServer("127.0.0.1", 9999);
        rpcServer.start();
    }
}
