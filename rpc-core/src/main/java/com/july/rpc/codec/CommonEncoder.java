package com.july.rpc.codec;

import com.july.rpc.entity.RpcRequest;
import com.july.rpc.enumeration.PackageType;
import com.july.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 自定义协议编码器
 *
 * @author july
 */
public class CommonEncoder extends MessageToByteEncoder {

    /**
     * 四字节魔数，标识协议包
     */
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        out.writeInt(MAGIC_NUMBER);
        out.writeInt(msg instanceof RpcRequest ? PackageType.REQUEST_PACK.getCode()
                : PackageType.RESPONSE_PACK.getCode());
        out.writeInt(serializer.getCode());
        byte[] bytes = serializer.serialize(msg);
        out.writeInt(bytes.length)
                .writeBytes(bytes);
    }
}
