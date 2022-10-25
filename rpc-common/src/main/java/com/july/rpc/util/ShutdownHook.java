package com.july.rpc.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author july
 */

@Slf4j
public class ShutdownHook {

    public static void addClearAllHook() {
        log.info("关闭后将自动注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtil.clearRegistry();
            ThreadPoolFactory.shutDownAll();
        }));
    }
}
