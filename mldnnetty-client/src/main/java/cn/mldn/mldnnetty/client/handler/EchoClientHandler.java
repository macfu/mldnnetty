package cn.mldn.mldnnetty.client.handler;

import cn.mldn.commons.DefaultNettyInfo;
import cn.mldn.util.InputUtil;
import cn.mldn.vo.Member;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

public class EchoClientHandler extends ChannelHandlerAdapter {
    private static final int REPEAT = 500;      //消息重复发送500次
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {     //客户端连接激活
        /*
        String data = "userid:mldnjava";
//        ByteBuf buf = Unpooled.buffer(data.length());
//        buf.writeBytes(data.getBytes());
        ctx.writeAndFlush(data);
        */

        /*
        String inputStr = InputUtil.getString("请输入要发送的消息");
        for(int x = 0 ; x < REPEAT ; x++){
            ctx.writeAndFlush(inputStr + "-" + x + DefaultNettyInfo.SEPARATOR);      //发送数据
        }
        */



        for(int x = 0 ; x < REPEAT ; x++){
            Member member = new Member();
            member.setName("小明-" + x);
            member.setAge(x);
            member.setSalary(x + 0.0);
            ctx.writeAndFlush(member);  //发送数据
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

//        ByteBuf buf = (ByteBuf) msg;        //接收远程发回的数据消息
//        String content = buf.toString(CharsetUtil.UTF_8);       //接收数据
//        if("quit".equalsIgnoreCase(content)){   //服务器端要结束处理
//            System.out.println("####本次操作结束，已推出####");
//            ctx.close();        //关闭连接通道，和服务端端口
//        }else{      //需要进行进一步的处理
//            System.out.println("{客户端}" + content);      //服务器端的回应消息
//            String inputStr = InputUtil.getString("请输入要发送的消息：");
//            ByteBuf newBuf = Unpooled.buffer(inputStr.length());
//            newBuf.writeBytes(inputStr.getBytes());     //写入数据
//            ChannelFuture future = ctx.writeAndFlush(newBuf);  //发送数据
//            future.addListener(new ChannelFutureListener() {
//                @Override
//                public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                    if(future.isSuccess()){
//                        System.out.println("*********客户端回信息发送成功");
//                    }
//                }
//            });
//        }
//        String content = (String)msg;
//        if("quit".equalsIgnoreCase(content)){
//            System.out.println("###########本次操作结束，已退出###########");
//            ctx.close();
//        }else{
//            System.out.println("｛客户端｝" + content);
//            String inputStr = InputUtil.getString("请输入要发送的数据：");
//            ctx.writeAndFlush(inputStr);    //发送数据
//        }
        /*
            String content = (String)msg;
            System.out.println("｛客户端｝" + content);
         */

        Member member = (Member)msg;
        System.out.println("｛客户端｝" + member);
        TimeUnit.MILLISECONDS.sleep(10);

    }
}
