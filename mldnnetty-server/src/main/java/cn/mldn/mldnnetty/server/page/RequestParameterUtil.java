package cn.mldn.mldnnetty.server.page;

import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: liming
 * @Date: 2019/01/23 10:32
 * @Description: 参数处理程序类
 */
public class RequestParameterUtil {
    private HttpRequest request;
    private HttpContent content;
    // 不管使用何种模式进行了参数的处理，全部的操作都是通过map集合保存
    private Map<String, List<String>> params = new HashMap<String, List<String>>();

    public RequestParameterUtil(HttpRequest request, HttpContent content) {
        this.request = request;
        this.content = content;
        // 进行参数的解析，解析后的内容会保存到paras集合中
        this.parse();
    }

    public String getParameter(String paramName) {
        try {
            return this.params.get(paramName).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> getParamaeterValues(String paramName) {
        return this.params.get(paramName);
    }

    private void parse() {
        // GET请求
        if (HttpMethod.GET.equals(this.request.method())) {
            // 处理GET请求参数
            QueryStringDecoder decoder = new QueryStringDecoder(this.request.uri());
            // 保存所有的GET请求参数
            this.params.putAll(decoder.parameters());
            // POST请求
        } else if (HttpMethod.POST.equals(this.request.method())) {
            if (this.content != null) {
                // POST解码
                HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(this.request);
                // 修改解码的开始位置
                decoder.offer(this.content);
                // 获取所有的Http请求参数
                List<InterfaceHttpData> datas = decoder.getBodyHttpDatas();
                // 循环处理所有的参数
                datas.forEach(param -> {
                    Attribute attribute = (Attribute) param;
                    try {
                        List<String> paramValue = null;
                        // 该内容已经保存过了
                        if (this.params.containsKey(attribute.getName())) {
                            paramValue = this.params.get(attribute.getName());
                        } else {
                            paramValue = new ArrayList<>();
                        }
                        // 获取一个数据进行保存
                        paramValue.add(attribute.getValue());
                        this.params.put(attribute.getName(), paramValue);
                    } catch (Exception e) {}
                });
            }
        }
    }

}
