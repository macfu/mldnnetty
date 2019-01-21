package cn.mldn.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;
import cn.mldn.vo.Member;

import java.util.List;

/**
 * @Author: liming
 * @Date: 2019/01/21 14:08
 * @Description: MessagePack解码
 */
public class MessagePackDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int len = byteBuf.readableBytes();
        byte[] data = new byte[len];
        byteBuf.getBytes(byteBuf.readerIndex(), data, 0, len);
        MessagePack pack = new MessagePack();
//        // 明确指定使用的是Member类(不应该指定特定的类，所以应该使用下面的那个)
//        list.add(pack.read(data, pack.lookup(Member.class)));
        // 设置一系列的的数据读取处理
        list.add(pack.read(data));
    }
}
