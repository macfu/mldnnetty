package cn.mldn.mldnnetty.server.handle;

import cn.mldn.vo.Member;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ObjectServerHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Member member = (Member)msg;
        System.err.println("｛服务器｝" + member);
        Member echoMember = new Member();
        echoMember.setName("【echo】" + member.getName());
        echoMember.setAge(member.getAge() * 2);
        echoMember.setSalary(member.getSalary() * 10);
        ctx.writeAndFlush(echoMember);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("【服务器端-生命周期】服务器出现异常");
    }
}
