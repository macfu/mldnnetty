package cn.mldn.util;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * @Author: liming
 * @Date: 2019/01/21 11:26
 * @Description: MessagePack编码器
 */
public class MessagePackEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
//        MessagePack pack = new MessagePack();
//        byte[] raw = pack.write(o);
//        byteBuf.writeBytes(raw);

        byte[] bytes = JSONObject.toJSONString(o).getBytes();
        byteBuf.writeBytes(bytes);
    }
}
