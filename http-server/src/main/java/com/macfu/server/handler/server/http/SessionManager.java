package com.macfu.server.handler.server.http;

import com.macfu.commons.HttpSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: liming
 * @Date: 2018/11/22 13:55
 * @Description:
 */
public class SessionManager {
    // 用户所有的SessionId都在此集合之中进行保存
    private static final Map<String, HttpSession> SESSION_MAP = new ConcurrentHashMap<String, HttpSession>();

    /**
     * 一个新的用户，需要通过其创建一个SessionId，创建后的SessionId在集合中保存
     *
     * @return
     */
    public static String createSession() {
        DefaultHttpSession defaultHttpSession = new DefaultHttpSession();
        String id = defaultHttpSession.getId();
        SESSION_MAP.put(id, defaultHttpSession);
        return id;
    }

    /**
     * 当用户发送请求过来之后需要判断该用户是否连接过
     *
     * @param sessionId 用户通过Cookie传过来的SessionId
     * @return 如果用户存在返回true，否则返回false
     */
    public static boolean isExists(String sessionId) {
        if (SESSION_MAP.containsKey(sessionId)) {
            HttpSession session = SESSION_MAP.get(sessionId);
            if (session.getId() == null) {
                SESSION_MAP.remove(sessionId);
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 删除指定的Session数据
     *
     * @param sessionId 要删除的sessionId
     */
    public static void invalidate(String sessionId) {
        SESSION_MAP.remove(sessionId);
    }

    /**
     * 获取指定的Session对象信息
     *
     * @param sessionId 要查询的SessionId内容
     * @return 存在的HttpSesion对象
     */
    public static HttpSession getSession(String sessionId) {
        return SESSION_MAP.get(sessionId);
    }

}
