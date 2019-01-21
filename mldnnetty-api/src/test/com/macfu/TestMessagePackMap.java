package com.macfu;

import org.msgpack.MessagePack;
import org.msgpack.template.Template;
import org.msgpack.template.Templates;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: liming
 * @Date: 2019/01/21 11:15
 * @Description: TestMessagePackMap
 */
public class TestMessagePackMap {
    public static void main(String[] args) throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "mldn");
        map.put("url", "www.mldn.cn");
        MessagePack pack = new MessagePack();
        byte[] write = pack.write(map);
        {
            Map<String, String> read = pack.read(write, Templates.tMap(Templates.TString, pack.lookup(String.class)));
            System.out.println(read);
        }
    }
}
