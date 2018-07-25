package cn.mldn.mldnnetty.server;

import cn.mldn.mldnnetty.server.handle.UDPServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UDPServer {
    public void run() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap serverBootstrap = new Bootstrap();
            serverBootstrap.group(group);
            serverBootstrap.channel(NioDatagramChannel.class);
            serverBootstrap.handler(new UDPServerHandler());
            serverBootstrap.option(ChannelOption.SO_BROADCAST,true);
            ChannelFuture future = serverBootstrap.bind(0).sync();
            future.channel().closeFuture().sync();
        }catch(Exception e){
            group.shutdownGracefully();
        }
    }
}
