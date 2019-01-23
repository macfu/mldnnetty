package cn.mldn.mldnnetty.server;

import cn.mldn.commons.ServerInfo;
import cn.mldn.mldnnetty.server.handle.HttpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;

/**
 * @Author: liming
 * @Date: 2019/01/21 16:11
 * @Description:
 */
public class HttpServer {
    public void run() throws Exception {
        // 在netty里面服务端的程序需要准备出两个线程池
        // 第一个线程池为接受用户请求连接的线程池
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        // 第二个线程为进行数据请求处理的线程池
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            // 所有的服务端的程序需要通过ServerBootStrap类进行启动
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 为此服务器配置有线程池对象(配置连接线程池，工作线程池)
            serverBootstrap.group(boosGroup, workGroup);
            // 指明当前服务器运行的方式，基于NIO的ServerSocket实现
            serverBootstrap.channel(NioServerSocketChannel.class);
            // 进行netty数据处理的过滤器配置(责任链设计模式)
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    channel.pipeline().addLast(new HttpRequestEncoder());
                    channel.pipeline().addLast(new HttpRequestDecoder());
                    // 自定义程序处理
                    channel.pipeline().addLast(new HttpServerHandler());
                }
            });
            // 当前服务器主要实现的是一个TCP的回应处理程序，那么在这样的情况下就必须进行一些TCP属性配置
            // 当前线程全满时的最大等待队列长度
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 64);
            // 绑定服务端口并且进行服务的启动
            ChannelFuture sync = serverBootstrap.bind(ServerInfo.PORT).sync();
            // 处理完成之后进行关闭
            sync.channel().closeFuture().sync();
        } catch (Exception e) {
            boosGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }
    }
}
