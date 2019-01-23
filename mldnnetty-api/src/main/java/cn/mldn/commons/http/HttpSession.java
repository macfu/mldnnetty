package cn.mldn.commons.http;

/**
 * @Author: liming
 * @Date: 2019/01/21 17:33
 * @Description: HttpSession接口
 */
public interface HttpSession {
    public static final String SESSIONID = "MLDNSESSION";

    Object getAttribute(String name);

    void setAttribute(String name, Object value);

    void removeAttribute(String name);

    String getId();

    void invalidate();
}
