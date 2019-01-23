package cn.mldn.mldnnetty.server.page;

import cn.mldn.commons.http.HttpSession;
import cn.mldn.mldnnetty.server.http.HttpSessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.Iterator;
import java.util.Set;

/**
 * @Author: liming
 * @Date: 2019/01/23 11:22
 * @Description: RequestPageUtil
 */
public class RequestPageUtil {
    private HttpResponse response;
    private HttpRequest request;
    private ChannelHandlerContext ctx;
    private HttpSession session;
    private HttpContent content;

    public RequestPageUtil(HttpRequest request, HttpContent content, ChannelHandlerContext ctx) {
        this.request = request;
        this.content = content;
        this.ctx = ctx;
        // 根据路径选择要处理的方法
        this.handlerUrl(this.request.uri());
    }

    private void handlerUrl(String uri) {
        System.out.println("uri = " + uri + ".method = " + this.request.method());
        // 现在做的是一个表单路径
        if ("/form".equals(uri)) {
            if (HttpMethod.GET.equals(this.request.method())) {
                this.form();
            }
            // 处理param请求操作
        } else if (uri.startsWith("/param")) {
            if (HttpMethod.POST.equals(this.request.method())) {
                if (this.content != null) {
                    // 进行所有参数的接收
                    this.param();
                }
            }
        }
    }

    private void param() {
        RequestParameterUtil parameterUtil = new RequestParameterUtil(this.request, this.content);
        String content = "<html><head><title>NETYT开发框架</title></head>"
                 + "<body><h1>www.google.com></h1>"
                + "<h1>【请求参数】msg = " + parameterUtil.getParameter("msg") + "</h1>"
                + "<h1>【请求参数】inst = " + parameterUtil.getParamaeterValues("inst") + "</h1>"
                + "</body></html>";
        this.responseWrite(content);
    }

    private void form() {
        String content = "<html><head><title>NETTY开发框架></title></head>"
                + "<body><h1>www.google.com</h1>"
                + "<form method='post' action='/param'>"
                + "信息: <input type='text' name='msg' id='msg' value='科技技术'><br>"
                + "兴趣: <input type='checkbox' name='inst' id='inst' value='唱歌' checked>唱歌"
                + "      <input type='checkbox' name='inst' id='inst' value='看书' checked>看书"
                + "      <input type='checkbox' name='inst' id='inst' value='学习' checked>学习"
                + "<input type='submit' value='提交'>"
                + "<input type='reset' value='重置'>"
                + "</form>"
                + "</body></html>";
        this.responseWrite(content);
    }

    /**
     * 进行session数据的创建
     * @param exists 请求Cookie是否存在有session的判断，如果有session传递时tru，否则传递是false
     */
    private void setSessionId(boolean exists) {
        // 此时发送的request请求之中没有指定的Cookie内容
        // 根据自定义的session管理器创建有一个新的sessionid的内容并且利用定义的常亮作为cookie名称
        if (!exists) {
            String encode = ServerCookieEncoder.encode(HttpSession.SESSIONID, HttpSessionManager.createSession());
            // HttpSessionManager类里面需要保留有一个Map集合进行全部Session数据的存储
            this.response.headers().set(HttpHeaderNames.SET_COOKIE, encode);
        }
    }

    private boolean isHasSessionId() {
        // 获取全部的请求Cookie数据
        String cookieStr = (String) this.request.headers().get("Cookie");
        if (cookieStr == null || "".equals(cookieStr)) {
            // 没有指定内容存在
            return false;
        }
        try {
            // 通过字符串解析出全部的Cookie数据
            Set<Cookie> set = ServerCookieDecoder.decode(cookieStr);
            Iterator<Cookie> iterator = set.iterator();
            while (iterator.hasNext()) {
                // 获取每一个Cookie内容
                Cookie cookie = iterator.next();
                // 存在有指定名称的Cookie
                if (HttpSession.SESSIONID.equals(cookie.name())) {
                    // 该session存在
                    if (HttpSessionManager.isExists(cookie.value())) {
                        this.session = HttpSessionManager.getSession(cookie.value());
                        return true;
                    }
                }
            }
        } catch (Exception e) {

        }
        return false;
    }

    private void responseWrite(String content) {
        // 在Netty里面如果要进行传输处理则需要依靠ByteBuf来完成
        ByteBuf buf = Unpooled.copiedBuffer(content, CharsetUtil.UTF_8);
        // 由于实现的是一个Http协议，那么在进行相应的时候除了数据显示之外还需要考虑Http头信息内容
        this.response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        // 判断Session是否存在，不存在创建新的
        this.setSessionId(this.isHasSessionId());
        // 设置响应MIME类型
        this.response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        // 设置响应数据长度
        this.response.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(buf.readableBytes()));
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

}
