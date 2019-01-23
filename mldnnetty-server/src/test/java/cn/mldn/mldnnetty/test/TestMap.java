package cn.mldn.mldnnetty.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: liming
 * @Date: 2019/01/23 10:44
 * @Description:
 */
public class TestMap {
    public static void main(String[] args) {
        List<String> l1 = new ArrayList<>();
        l1.add("1");
        l1.add("2");
        List<String> l2 = new ArrayList<>();
        l1.add("3");
        l1.add("4");
        Map<String, List<String>> params = new HashMap<>();
        params.put("1",l1);
        params.put("2",l2);
        System.err.println(params.get("1").get(0));
    }
}
