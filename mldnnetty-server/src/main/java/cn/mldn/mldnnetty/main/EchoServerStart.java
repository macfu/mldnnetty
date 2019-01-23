package cn.mldn.mldnnetty.main;

import cn.mldn.mldnnetty.server.EchoServer;
import cn.mldn.mldnnetty.server.HttpServer;

public class EchoServerStart {
    public static void main(String[] args) throws Exception{
        System.out.println("******服务正常启动******");
//        new EchoServer().run();
        new HttpServer().run();
    }

}
