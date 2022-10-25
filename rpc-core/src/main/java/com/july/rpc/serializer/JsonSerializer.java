package com.july.rpc.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.july.rpc.entity.RpcRequest;
import com.july.rpc.enumeration.SerializerCode;
import com.july.rpc.exception.SerializeException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author july
 */
@Slf4j
public class JsonSerializer implements CommonSerializer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            log.error("序列化过程中出现异常：{}", e.getMessage());
            throw new SerializeException("序列化过程中出现异常");
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try {
            Object obj = objectMapper.readValue(bytes, clazz);
            if (obj instanceof RpcRequest) {
                obj = handleRequest(obj);
            }
            return obj;
        } catch (IOException e) {
            log.error("反序列化过程中出现异常：{}", e.getMessage());
            throw new SerializeException("反序列化过程中出现异常");
        }
    }

    /**
     * 将反序列化后的request携带的参数类型处理为原实例类型
     * 因为JSON序列化时本质上只是转换成JSON字符串，会丢失对象类型信息
     *
     * @param obj
     * @return
     * @throws IOException
     */
    private Object handleRequest(Object obj) throws IOException {
        RpcRequest request = (RpcRequest) obj;
        for (int i = 0; i < request.getParamTypes().length; i++) {
            Class<?> clazz = request.getParamTypes()[i];
            if (!clazz.isAssignableFrom(request.getParameters()[i].getClass())) {
                byte[] bytes = objectMapper.writeValueAsBytes(request.getParameters()[i]);
                request.getParameters()[i] = objectMapper.readValue(bytes, clazz);
            }
        }
        return request;
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("JSON").getCode();
    }
}
