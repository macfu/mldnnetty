package com.macfu.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;


/**
 * @Author: liming
 * @Date: 2018/11/21 17:16
 * @Description: 发送消息
 */
public class HttpServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            // 进行请求的接收
            HttpRequest request = (HttpRequest) msg;
            System.out.println("【Http接收请求】uri = " + request.uri() + "，method" + request.method());
            String content = "<html><head><title>netty开发框架</title></head><body><h1>www.google.com</h1>" +
                    "</body></html>";
            this.responseWrite(ctx, content);
        }
    }

    private void responseWrite(ChannelHandlerContext ctx, String content) {
        // 在netty里面如果需要进行传输处理则需要依靠ByteBuf来完成
        ByteBuf byteBuf = Unpooled.copiedBuffer(content, CharsetUtil.UTF_8);
        // 由于实现的是一个HTTP协议，那么在进行相应的时候除了数据的显示之外还需要考虑HTTP头信息内容
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        // 设置MIME响应类型
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(byteBuf.readableBytes()));
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

}
