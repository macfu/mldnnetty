package cn.mldn.mldnnetty.main;

import cn.mldn.mldnnetty.client.ObjectClient;

public class EchoClientStart {
    public static void main(String[] args) throws Exception{
        new ObjectClient().run();
    }
}
