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
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务未实现接口"),
    SERVICE_NOT_FOUND("找不到对应的服务");

    private final String message;

}
