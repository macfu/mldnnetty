package com.macfu.client;

import com.macfu.client.handler.HttpClientHandler;
import com.macfu.commons.ServerInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpVersion;

/**
 * @Author: liming
 * @Date: 2018/11/23 14:11
 * @Description:
 */
public class HttpClient {
    // 启动客服端程序
    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap clientBootstrap = new Bootstrap();
            clientBootstrap.group(group);
            clientBootstrap.channel(NioSocketChannel.class);
            clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new HttpResponseDecoder());
                    socketChannel.pipeline().addLast(new HttpResponseEncoder());
                    socketChannel.pipeline().addLast(new HttpClientHandler());
                }
            });
            // 连接远程服务器
            ChannelFuture future = clientBootstrap.connect(ServerInfo.SERVER, ServerInfo.PORT).sync();
            // 像服务器端进行请求数据的发送
            String url = "http://" + ServerInfo.SERVER + ":" + ServerInfo.PORT; // 连接地址
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, url);
            request.headers().set(HttpHeaderNames.HOST, "127.0.0.1");
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE); // 设置长连接
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(request.content().readableBytes()));
            request.headers().set(HttpHeaderNames.COOKIE, "nothing");
            future.channel().writeAndFlush(request);    // 发送http.request请求
            future.channel().closeFuture().sync();      // 等待关闭，Handler里面关闭处理
        } catch (Exception e) {
            group.shutdownGracefully();
        }
    }
}
