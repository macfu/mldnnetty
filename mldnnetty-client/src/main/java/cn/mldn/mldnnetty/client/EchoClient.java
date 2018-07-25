package cn.mldn.mldnnetty.client;

import cn.mldn.commons.ServerInfo;
import cn.mldn.mldnnetty.client.handler.EchoClientHandler;
import com.sun.corba.se.internal.CosNaming.BootstrapServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.util.concurrent.Future;

public class EchoClient {
    public void run() throws Exception{
        //创建一个进行数据交互的处理线程池
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap clientBootStrap = new Bootstrap();        //创建客户端连接
            clientBootStrap.group(group);       //设置连接池
            clientBootStrap.channel(NioSocketChannel.class);    //设置通道类型
            clientBootStrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                    socketChannel.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                    socketChannel.pipeline().addLast(new EchoClientHandler());      //自定义程序处理逻辑
                }
            });
            //连接远程服务端
            ChannelFuture future = clientBootStrap.connect(ServerInfo.HOSTNAME,ServerInfo.PORT).sync();
            future.channel().closeFuture().sync();      //等待关闭，handler里面关闭处理
        }catch (Exception e){
            group.shutdownGracefully();     //关闭线程池
        }
    }
}
