package com.macfu.server.handler.server.http;

import com.google.common.collect.Maps;
import com.macfu.commons.HttpSession;

import java.util.Map;
import java.util.UUID;

/**
 * @Author: liming
 * @Date: 2018/11/22 11:14
 * @Description: Session工具类
 */
public class DefaultHttpSession implements HttpSession {
    private String sessionId;
    private Map<String, Object> attribute = Maps.newHashMap();

    // 第一次访问时创建sessionId
    public DefaultHttpSession() {
        this.sessionId = UUID.randomUUID().toString();
    }

    @Override
    public Object getAttribute(String name) {
        return this.attribute.get(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        this.attribute.put(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        this.attribute.remove(name);
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
