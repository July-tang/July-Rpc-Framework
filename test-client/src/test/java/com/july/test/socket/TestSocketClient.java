package com.july.test.socket;

import com.july.rpc.api.ByeService;
import com.july.rpc.api.HelloObject;
import com.july.rpc.api.HelloService;
import com.july.rpc.transport.RpcClient;
import com.july.rpc.transport.RpcClientProxy;
import com.july.rpc.transport.socket.client.SocketClient;
import org.junit.Test;

/**
 * @author july
 */
public class TestSocketClient {

    @Test
    public void helloClient() {
        RpcClient client = new SocketClient();
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(777, "This is a message");
        System.out.println(helloService.hello(object));
    }

    @Test
    public void byeClient() {
        RpcClient client = new SocketClient();
        RpcClientProxy proxy = new RpcClientProxy(client);
        ByeService byeService = proxy.getProxy(ByeService.class);
        System.out.println(byeService.bye("777"));
    }
}
