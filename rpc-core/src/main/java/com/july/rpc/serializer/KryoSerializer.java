package com.july.rpc.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.july.rpc.entity.RpcRequest;
import com.july.rpc.entity.RpcResponse;
import com.july.rpc.enumeration.SerializerCode;
import com.july.rpc.exception.SerializeException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author july
 */
@Slf4j
public class KryoSerializer implements CommonSerializer {

    /**
     * Kryo是线程不安全的，创建ThreadLocal使得每个线程都有一个独立的Kryo对象
     */
    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RpcResponse.class);
        kryo.register(RpcRequest.class);
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            kryo.writeObject(output, obj);
            KRYO_THREAD_LOCAL.remove();
            return output.toBytes();
        } catch (Exception e) {
            log.error("序列化时出现异常：", e);
            throw new SerializeException("序列化时出现异常");
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            Object obj = kryo.readObject(input, clazz);
            KRYO_THREAD_LOCAL.remove();
            return obj;
        } catch (Exception e) {
            log.error("序列化时出现异常：", e);
            throw new SerializeException("序列化时出现异常");
        }
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("KRYO").getCode();
    }
}
