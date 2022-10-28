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
     * Rpc过程调用错误
     */
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务未实现任何接口"),
    SERVICE_NOT_FOUND("找不到相对应的服务"),
    SERVICE_INVOCATION_FAILURE("调用服务失败"),
    /**
     * 协议相关错误
     */
    UNKNOWN_PROTOCOL("不识别的协议包"),
    UNKNOWN_SERIALIZER("不识别的(反)序列化器"),
    UNKNOWN_PACKAGE_TYPE("不识别的数据包类型"),
    SERIALIZER_NOT_FOUND("找不到序列化器"),
    /**
     * 服务注册中心相关错误
     */
    FAILED_TO_CONNECT_SERVICE_REGISTRY("连接服务注册中心失败"),
    REGISTER_SERVICE_FAILED("注册服务失败"),
    GET_SERVICE_FAILED("获取服务失败"),

    /**
     * Rpc启动类错误
     */
    SERVICE_SCAN_PACKAGE_NOT_FOUND("启动类ServiceScan注解缺失"),
    UNKNOWN_ERROR("出现未知错误");

    private final String message;

}
