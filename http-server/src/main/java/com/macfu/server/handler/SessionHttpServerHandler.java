package com.macfu.server.handler;

import com.macfu.commons.HttpSession;
import com.macfu.server.handler.server.http.SessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.ServerCookieDecoder;
import io.netty.handler.codec.http.ServerCookieEncoder;
import io.netty.util.CharsetUtil;

import java.util.Iterator;
import java.util.Set;

/**
 * @Author: liming
 * @Date: 2018/11/22 15:38
 * @Description: handler处理程序类
 */
public class SessionHttpServerHandler extends ChannelHandlerAdapter {
    private HttpRequest request;
    private HttpResponse response;
    private HttpSession session;

    /**
     * 进行session数据的创建
     *
     * @param exists 请求Cookie是否存在有session的判断，如果有session传递时true，否则传递的时false
     */
    private void setSessionId(boolean exists) {
        if (!exists) {  // 此时发送的request请求之中没有指定的Cookie内容
            // 根据自定义的session管理器创建有一个新的sessionId内容，并且利用定义的常量作为Cookie名称
            String encode = ServerCookieEncoder.encode(HttpSession.SESSIONID, SessionManager.createSession());
            this.request.headers().set(HttpHeaderNames.SET_COOKIE, encode);
        }
    }

    /**
     * 通过请求的Cookie之中分析是否存在有指定名称的Cookie存在
     *
     * @return 如果有返回true，否则返回false
     */
    private boolean isHasSessionId() {
        // 获取全部的请求Cookie数据
        String cookieStr = (String) this.request.headers().get("Cookie");
        if (cookieStr == null || "".equals(cookieStr)) {
            return false;
        }
        // 通过字符串解析出全部的Cookie数据
        Set<Cookie> set = ServerCookieDecoder.decode(cookieStr);
        Iterator<Cookie> iter = set.iterator();
        while (iter.hasNext()) {
            Cookie cookie = iter.next();    // 获取每一个Cookie的内容
            if (HttpSession.SESSIONID.equals(cookie.name())) {
                if (SessionManager.isExists(cookie.value())) {
                    this.session = SessionManager.getSession(cookie.value());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 现在发送的时request请求
        if (msg instanceof HttpRequest) {
            this.request = (HttpRequest) msg;
            System.out.println("uri" + request.uri() + "; method" + request.method());
            String content = "www.google.com";
            this.responseWrite(ctx, content);
        }
    }

    private void responseWrite(ChannelHandlerContext ctx, String msg) throws Exception {
        ByteBuf buf = Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8);
        this.response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        this.setSessionId(this.isHasSessionId());
        this.response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/json;charset=UTF-8");
        this.response.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(buf.readableBytes()));
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
