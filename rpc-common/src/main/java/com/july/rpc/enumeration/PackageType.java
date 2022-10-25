package com.july.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author july
 */
@AllArgsConstructor
@Getter
public enum PackageType {

    /**
     * 协议中RPC请求编码
     */
    REQUEST_PACK(0),
    /**
     * 协议中RPC响应编码
     */
    RESPONSE_PACK(1);

    private final int code;

}
