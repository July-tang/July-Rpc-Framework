package com.july.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RPC调用过程中的错误
 *
 * @author july
 */
@AllArgsConstructor
@Getter
public enum RpcError {

    /**
     * 可能出现的Rpc调用错误
     */
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务未实现任何接口"),
    SERVICE_NOT_FOUND("找不到相对应的服务"),
    /**
     * 协议相关错误
     */
    UNKNOWN_PROTOCOL("不识别的协议包"),
    UNKNOWN_SERIALIZER("不识别的(反)序列化器"),
    UNKNOWN_PACKAGE_TYPE("不识别的数据包类型"),
    SERIALIZER_NOT_FOUND("找不到序列化器");

    private final String message;

}
