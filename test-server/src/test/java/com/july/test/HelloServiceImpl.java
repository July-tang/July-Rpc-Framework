package com.july.test;

import com.july.rpc.api.HelloObject;
import com.july.rpc.api.HelloService;
import lombok.extern.slf4j.Slf4j;


/**
 * @author july
 */
@Slf4j
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(HelloObject object) {
        log.info("接收到：{}", object.getMessage());
        return "这里调用的返回值， id = " + object.getId();
    }
}
