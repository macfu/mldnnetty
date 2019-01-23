package cn.mldn.mldnnetty.server.handle;

import cn.mldn.mldnnetty.server.page.RequestPageUtil;
import cn.mldn.mldnnetty.server.page.RequestParameterUtil;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @Author: liming
 * @Date: 2019/01/21 15:48
 * @Description:
 */
public class HttpServerHandler extends ChannelHandlerAdapter {

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        if (msg instanceof HttpRequest) {
//            HttpRequest request = (HttpRequest) msg;
//            System.out.println("【Http请求】uri = " + request.uri() +  ", method = " + request.method());
//            String content = "<html><head><title>MLDN_NETTY开发框架</title></head>" + "<body><h1>www.mldn.cn></h1>"
//                    + "<h1>good good study, day day up</h1></body></html>";
//            this.responseWrite(ctx, content);
//        }
//    }
//
//    private void responseWrite(ChannelHandlerContext ctx, String content) {
//        ByteBuf buf = Unpooled.copiedBuffer(content, CharsetUtil.UTF_8);
//        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
//        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=utf-8");
//        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(buf.readableBytes()));
//        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        cause.printStackTrace();
//    }

    private HttpRequest request;
    private HttpContent content;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            this.request = (HttpRequest) msg;
            RequestPageUtil rpu = new RequestPageUtil(this.request, this.content, ctx);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
