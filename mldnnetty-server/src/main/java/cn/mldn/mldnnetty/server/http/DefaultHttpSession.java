package cn.mldn.mldnnetty.server.http;

import cn.mldn.commons.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: liming
 * @Date: 2019/01/21 17:36
 * @Description: 实现方法
 */
public class DefaultHttpSession implements HttpSession {

    private String sessionId;
    private Map<String, Object> attributes = new HashMap<String, Object>();

    public DefaultHttpSession() {
        this.sessionId = UUID.randomUUID().toString();
    }

    @Override
    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        this.attributes.put(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }

    @Override
    public String getId() {
        return this.sessionId;
    }

    @Override
    public void invalidate() {
        this.sessionId = null;
    }
}
