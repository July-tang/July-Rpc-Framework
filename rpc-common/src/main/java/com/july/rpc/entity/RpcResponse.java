package com.july.rpc.entity;

import com.july.rpc.enumeration.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 服务提供方执行后向消费者返回的结果对象
 *
 * @author july
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse<T> implements Serializable {
    /**
     * 响应状态码
     */
    private Integer status;
    /**
     * 响应状态信息
     */
    private String message;
    /**
     * 响应数据（方法返回值）
     */
    private T data;

    public static<T> RpcResponse<T> success(T data) {
        return new RpcResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }

    public static<T> RpcResponse<T> fail(ResponseCode code) {
        return new RpcResponse<>(code.getCode(), code.getMessage(), null);
    }
}
