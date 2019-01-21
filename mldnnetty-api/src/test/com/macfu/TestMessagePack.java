package com.macfu;

import cn.mldn.vo.Member;
import org.msgpack.MessagePack;
import org.msgpack.template.Template;
import org.msgpack.template.Templates;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: liming
 * @Date: 2019/01/21 11:08
 * @Description:
 */
public class TestMessagePack {
    public static void main(String[] args) throws Exception {
        ArrayList<Member> all = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Member member = new Member();
            member.setName("弱-");
            member.setAge(10);
            member.setSalary(123.33 );
            all.add(member);
        }
        MessagePack pack = new MessagePack();
        byte[] data = pack.write(all);
        {
            List<Member> result = pack.read(data, Templates.tList(pack.lookup(Member.class)));
            System.out.println(result);
        }
    }
}
