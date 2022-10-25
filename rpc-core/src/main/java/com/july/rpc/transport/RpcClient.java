package com.july.rpc.transport;

import com.july.rpc.entity.RpcRequest;
import com.july.rpc.serializer.CommonSerializer;

/**
 * 客户端类通用接口
 *
 * @author july
 */
public interface RpcClient {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    /**
     * 发送RPC请求
     * @param request RPC请求
     * @return 请求结果
     */
    Object sendRequest(RpcRequest request);
}
