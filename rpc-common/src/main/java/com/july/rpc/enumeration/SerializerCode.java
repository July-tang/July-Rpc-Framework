package com.july.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字节流中标识序列化和反序列化器
 *
 * @author july
 */
@AllArgsConstructor
@Getter
public enum SerializerCode {

    /**
     * 序列化器对应编码
     */
    JSON(1);

    private final int code;

}