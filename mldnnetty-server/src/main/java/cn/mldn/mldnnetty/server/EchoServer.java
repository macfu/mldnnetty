package cn.mldn.mldnnetty.server;

import cn.mldn.mldnnetty.server.handle.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import cn.mldn.commons.ServerInfo;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class EchoServer {
    public void run() throws Exception{     //程序的运行方法，异常全部抛出
        //1.在Netty里面服务器端的程序需要准备出两个线程池
        //1.1第一个线程池为接收用户请求的线程池
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        //1.2第二个线程池为进行数据请求处理的线程池
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            //所有服务端的程序需要需要通过ServerBootstrap类进行启动
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //为此服务端配置有线程池对象（配置连接线程池，工作线程池）
            serverBootstrap.group(boosGroup,workGroup);
            //指明当前服务器的运行方式，给予NIO的serverSocket实现
            serverBootstrap.channel(NioServerSocketChannel.class);
            //进行netty数据处理的过滤器配置（责任链设计模式）
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                    socketChannel.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                    socketChannel.pipeline().addLast(new EchoServerHandler());  //自定义程序处理逻辑
                }
            });
            //当前服务器主要实现的是一个TCP的回应处理程序，那么在这样的情况下就必须进行一些TCP属性配置
            serverBootstrap.option(ChannelOption.SO_BACKLOG,64);    //当处理线程全满时的最大等待队列长度
            //绑定服务器端口并进行服务的启动
            ChannelFuture future = serverBootstrap.bind(ServerInfo.PORT).sync();    //异步线程处理
            future.channel().closeFuture().sync();  //处理完成之后进行关闭
        }catch(Exception e){
            boosGroup.shutdownGracefully();     //关闭主线程池
            workGroup.shutdownGracefully();     //关闭子线程池
        }
    }
}
