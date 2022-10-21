package com.july.rpc.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 测试用的api的实体类
 *
 * @author july
 */
@Data
@AllArgsConstructor
public class HelloObject implements Serializable {

    private Integer id;

    private String message;
}
