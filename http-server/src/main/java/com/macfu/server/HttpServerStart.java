package com.macfu.server;

import com.macfu.server.handler.server.HttpServer;

/**
 * @Author: liming
 * @Date: 2018/11/22 10:25
 * @Description: 服务启动主类
 */
public class HttpServerStart {
    public static void main(String[] args) throws Exception {
        System.out.println("*****服务启动******");
        new HttpServer().run();
    }
}
