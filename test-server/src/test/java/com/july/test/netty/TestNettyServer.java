package com.july.test.netty;

import com.july.rpc.annotation.ServiceScan;
import com.july.rpc.api.ByeService;
import com.july.rpc.api.HelloService;
import com.july.rpc.transport.RpcServer;
import com.july.rpc.transport.netty.server.NettyServer;
import com.july.test.config.RpcConfig;
import com.july.test.impl.ByeServiceImpl;
import com.july.test.impl.HelloServiceImpl;
import org.junit.Test;

/**
 * @author july
 */
public class TestNettyServer {

    @Test
    public void server() {
        RpcServer server = new NettyServer("127.0.0.1", 9001);
        server.scanServices(RpcConfig.class);
        server.start();
    }
}
