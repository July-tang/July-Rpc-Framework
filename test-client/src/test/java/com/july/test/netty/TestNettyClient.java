package com.july.test.netty;

import com.july.rpc.api.ByeService;
import com.july.rpc.api.HelloObject;
import com.july.rpc.api.HelloService;
import com.july.rpc.transport.RpcClient;
import com.july.rpc.transport.RpcClientProxy;
import com.july.rpc.transport.netty.client.NettyClient;
import org.junit.Test;

/**
 * @author july
 */
public class TestNettyClient {

    @Test
    public void helloClient() {
        RpcClient client = new NettyClient("127.0.0.1", 9000);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(777, "This is a message");
        System.out.println(helloService.hello(object));
    }

    @Test
    public void byeClient() {
        RpcClient client = new NettyClient("127.0.0.1", 9000);
        RpcClientProxy proxy = new RpcClientProxy(client);
        ByeService byeService = proxy.getProxy(ByeService.class);
        System.out.println(byeService.bye("777"));
    }
}
