package com.macfu.commons;

/**
 * @Author: liming
 * @Date: 2018/11/22 11:09
 * @Description:
 */
public interface HttpSession {
    static final String SESSIONID = "MACFUSESSIONID";

    Object getAttribute(String name);

    void setAttribute(String name, Object value);

    void removeAttribute(String name);

    String getId();

    void invalidate();
}
