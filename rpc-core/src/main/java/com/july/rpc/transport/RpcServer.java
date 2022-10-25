package com.july.rpc.transport;

import com.july.rpc.serializer.CommonSerializer;

/**
 * 服务器类通用接口
 *
 * @author july
 */
public interface RpcServer {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    /**
     * 启动服务
     *
     */
    void start();

    /**
     * 发布服务
     *
     * @param service 服务实例
     * @param serviceClass
     * @param <T>
     */
    <T> void publishService(Object service, Class<T> serviceClass);
}
