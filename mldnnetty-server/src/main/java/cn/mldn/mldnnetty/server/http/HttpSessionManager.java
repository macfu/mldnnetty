package cn.mldn.mldnnetty.server.http;

import cn.mldn.commons.http.HttpSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: liming
 * @Date: 2019/01/21 17:39
 * @Description:
 */
public class HttpSessionManager {
    // 用户所有的session都在此集合中进行保存，使用并发访问处理的map结合
    private static final Map<String, HttpSession> SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * 一个新的用户，需用通过其创建一个SessionID，创建后的SessionId在集合中保存
     * @return
     */
    public static String createSession() {
        HttpSession session = new DefaultHttpSession();
        String sessionId = session.getId();
        SESSION_MAP.put(sessionId, session);
        return sessionId;
    }

    /**
     * 当用户发送过来请求的时候需要判断该用户是否已经登录过了
     * @param sessionId
     * @return
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
     * 删除指定的SessionId数据
     * @param sessionId
     */
    public static void invalidate(String sessionId) {
        SESSION_MAP.remove(sessionId);
    }

    /**
     * 获取指定的Session对象信息
     * @param sessionId
     * @return
     */
    public static HttpSession getSession(String sessionId) {
        return SESSION_MAP.get(sessionId);
    }
}
