package io.github.moyugroup.auth.common.constant;

/**
 * SSO 参数常量
 * <p>
 * Created by fanfan on 2024/03/30.
 */
public class SSOLoginConstant {
    /**
     * 应用 AppId 参数名称
     */
    public final static String APP_ID = "appId";
    /**
     * 应用 AppSecret 参数名称
     */
    public final static String APP_SECRET = "appSecret";
    /**
     * 回调地址参数名称
     */
    public final static String BACK_URL = "backUrl";
    /**
     * SSO_TOKEN 参数名称
     */
    public final static String SSO_TOKEN = "ssoToken";
    /**
     * 默认登录页面路径
     */
    public final static String LOGIN_PAGE_PATH = "/ssoLogin.html";
    /**
     * 默认注销页面路径
     */
    public final static String LOGIN_OUT_PATH = "/ssoLogout.html";
    /**
     * 默认切换租户页面路径
     */
    public final static String SWITCH_TENANT_PATH = "/switchTenant.html";
}
