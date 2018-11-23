package com.macfu.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.CharsetUtil;

/**
 * @Author: liming
 * @Date: 2018/11/23 11:37
 * @Description: 客户端接受消息
 */
public class HttpClientHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            System.out.println("【客户端】Content-Type" + response.headers().get(HttpHeaderNames.CONTENT_TYPE));
            System.out.println("【客户端】Content-Length" + response.headers().get(HttpHeaderNames.CONTENT_LENGTH));
            System.out.println("【客户端】Cookie" + response.headers().get(HttpHeaderNames.SET_COOKIE).toString());
        }
        ;
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            System.out.println(buf.toString(CharsetUtil.UTF_8));
        }
    }
}
