package io.github.moyugroup.auth.common.context;


import io.github.moyugroup.auth.common.pojo.dto.UserInfo;

import java.util.Objects;

/**
 * 用户上下文
 * <p>
 * Created by fanfan on 2024/03/13.
 */
public class UserContext {

    /**
     * 使用线程变量储存当前登录用户上下文
     */
    private static final ThreadLocal<UserInfo> userThreadLocal = new ThreadLocal<>();

    /**
     * 设置当前登录用户上下文
     *
     * @param userInfo 用户上下文
     */
    public static void set(UserInfo userInfo) {
        userThreadLocal.set(userInfo);
    }

    /**
     * 获取当前登录用户上下文
     *
     * @return 用户上下文
     */
    public static UserInfo get() {
        return userThreadLocal.get();
    }

    /**
     * 清理当前登录用户上下文
     */
    public static void remove() {
        userThreadLocal.remove();
    }

    /**
     * 用户当前登录用户ID
     *
     * @return
     */
    public static String getCurUserId() {
        UserInfo userInfo = get();
        if (Objects.isNull(userInfo)) {
            return null;
        }
        return userInfo.getUserId();
    }

}
