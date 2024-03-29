package com.july.rpc.serializer;

/**
 * 通用的序列化反序列化接口
 *
 * @author july
 */
public interface CommonSerializer {

    Integer KRYO_SERIALIZER = 0;
    Integer JSON_SERIALIZER = 1;

    /**
     * 序列化
     *
     * @param obj 序列化对象
     * @return 序列化后的字节数组
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     *
     * @param bytes
     * @param clazz
     * @return
     */
    Object deserialize(byte[] bytes, Class<?> clazz);

    /**
     * 获取序列器编号
     *
     * @return
     */
    int getCode();

    /**
     * 根据编号获取对应的序列器
     *
     * @param code
     * @return
     */
    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
