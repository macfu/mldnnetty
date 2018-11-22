package com.macfu.server.handler.server;

import com.macfu.commons.ServerInfo;
import com.macfu.server.handler.HttpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @Author: liming
 * @Date: 2018/11/21 18:55
 * @Description:
 */
public class HttpServer {
    public void run() throws Exception {
        // 在netty里面服务器端的程序需要准备出两个线程池
        // 第一次线程池为接收用户请求连接的线程池，第二个线程池为进行数据请求处理的线程池
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            // 所有的服务端的程序需要通过ServerBootstrap类进行启动
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup, workGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new HttpResponseEncoder());
                    socketChannel.pipeline().addLast(new HttpResponseDecoder());
                    socketChannel.pipeline().addLast(new HttpServerHandler());
                }
            });
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 64);
            ChannelFuture future = serverBootstrap.bind(ServerInfo.PORT).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
