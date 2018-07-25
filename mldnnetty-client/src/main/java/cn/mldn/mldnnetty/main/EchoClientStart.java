package cn.mldn.mldnnetty.main;

import cn.mldn.mldnnetty.client.EchoClient;

public class EchoClientStart {
    public static void main(String[] args) throws Exception{
        new EchoClient().run();
    }
}
