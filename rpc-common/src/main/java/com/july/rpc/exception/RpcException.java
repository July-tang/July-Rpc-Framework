package com.july.rpc.exception;

import com.july.rpc.enumeration.RpcError;

/**
 * 自定义Rpc调用异常
 *
 * @author july
 */
public class RpcException extends RuntimeException{

    public RpcException(RpcError error, String detail) {
        super(error.getMessage() + ": " + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcError error) {
        super(error.getMessage());
    }
}
