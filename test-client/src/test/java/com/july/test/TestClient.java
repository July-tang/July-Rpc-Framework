package com.july.test;

import com.july.rpc.api.ByeService;
import com.july.rpc.api.HelloObject;
import com.july.rpc.api.HelloService;
import com.july.rpc.transport.RpcClientProxy;
import org.junit.Test;

/**
 * @author july
 */
public class TestClient {

    @Test
    public void helloClient() {
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(777, "This is a message");
        System.out.println(helloService.hello(object));
    }

    @Test
    public void byeClient() {
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
        ByeService byeService = proxy.getProxy(ByeService.class);
        System.out.println(byeService.bye("777"));
    }
}
