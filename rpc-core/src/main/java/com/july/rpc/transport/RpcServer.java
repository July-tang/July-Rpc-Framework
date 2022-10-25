package com.july.rpc.transport;

/**
 * 服务器类通用接口
 *
 * @author july
 */
public interface RpcServer {

    /**
     * 启动服务
     * @param port 端口号
     */
    void start(int port);
}
