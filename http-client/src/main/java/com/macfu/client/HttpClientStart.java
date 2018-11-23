package com.macfu.client;

/**
 * @Author: liming
 * @Date: 2018/11/23 14:30
 * @Description: 客户端程序启动类
 */
public class HttpClientStart {
    public static void main(String[] args) throws Exception {
        System.out.println("*****服务启动******");
        new HttpClient().run();
    }
}
