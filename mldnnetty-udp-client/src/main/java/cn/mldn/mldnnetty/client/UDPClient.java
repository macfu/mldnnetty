package cn.mldn.mldnnetty.client;

import cn.mldn.commons.ServerInfo;
import cn.mldn.mldnnetty.client.handler.UDPClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UDPClient {
    public void run() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap clientBootstrap = new Bootstrap();
            clientBootstrap.group(group);
            clientBootstrap.channel(NioDatagramChannel.class);
            clientBootstrap.handler(new UDPClientHandler());
            clientBootstrap.option(ChannelOption.SO_BROADCAST,true);
            clientBootstrap.option(ChannelOption.SO_RCVBUF,2048);   //追加缓冲区配置
            clientBootstrap.bind(ServerInfo.PORT).sync().channel().closeFuture().await();
            //连接远程服务器
            ChannelFuture future = clientBootstrap.connect(ServerInfo.HOSTNAME,ServerInfo.PORT).sync();
            future.channel().closeFuture().sync();      //等待关闭，Handle里面关闭处理
        }catch(Exception e){
            group.shutdownGracefully();
        }
    }
}
