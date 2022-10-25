package com.july.rpc.handler;

import com.july.rpc.entity.RpcRequest;
import com.july.rpc.entity.RpcResponse;
import com.july.rpc.enumeration.ResponseCode;
import com.july.rpc.provider.ServiceProvider;
import com.july.rpc.provider.ServiceProviderImpl;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * RPC请求过程调用处理器
 *
 * @author july
 */
@Slf4j
public class RequestHandler {

    private static final ServiceProvider provider;

    static {
        provider = new ServiceProviderImpl();
    }

    public Object handle(RpcRequest rpcRequest) {
        Object service = provider.getServiceProvider(rpcRequest.getInterfaceName());
        try {
            return invokeTargetMethod(rpcRequest, service);
        } catch (Exception e) {
            log.error("Rpc调用过程中出现异常：", e);
            e.printStackTrace();
            return null;
        }
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) throws Exception {
        Method method;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            log.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException e) {
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND);
        }
        return method.invoke(service, rpcRequest.getParameters());
    }
}
