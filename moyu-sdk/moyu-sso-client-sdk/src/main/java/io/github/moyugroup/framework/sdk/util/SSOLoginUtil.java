package io.github.moyugroup.framework.sdk.util;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.json.JSONUtil;
import io.github.moyugroup.auth.common.constant.SSOLoginConstant;
import io.github.moyugroup.auth.common.pojo.dto.SSOUserDTO;
import io.github.moyugroup.auth.common.pojo.dto.UserInfo;
import io.github.moyugroup.auth.common.util.AESCipherUtil;
import io.github.moyugroup.auth.common.util.CookieUtil;
import io.github.moyugroup.base.model.pojo.Result;
import io.github.moyugroup.framework.sdk.config.SSOClientProperties;
import io.github.moyugroup.framework.sdk.constant.enums.EnvironmentEnum;
import io.github.moyugroup.framework.sdk.constant.enums.SSOLoginErrorEnum;
import io.github.moyugroup.util.AssertUtil;
import io.github.moyugroup.web.util.WebUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * SSO用户工具类
 * <p>
 * Created by fanfan on 2024/03/30.
 */
@Slf4j
public class SSOLoginUtil {

    /**
     * 开发环境服务器地址
     */
    public static final String devServerUrl = "http://server.ffis.me:9001";
    /**
     * 生产环境服务器地址
     */
    public static final String prodServerUrl = "https://moyu-login.ffis.me";

    /**
     * 从 Cookie 中获取并解析用户信息
     *
     * @param request   request 对象
     * @param appId     appId
     * @param appSecret appSecret
     * @return UserInfo
     */
    public static UserInfo getUserFromCookie(HttpServletRequest request, String appId, String appSecret) {
        // 获取应用 Cookie
        String cookieValue = CookieUtil.getCookieByAppId(request, appId);
        if (StringUtils.isBlank(cookieValue)) {
            return null;
        }
        // 从 Cookie 中解析用户信息
        String userJsonStr = AESCipherUtil.decryptData(appSecret, cookieValue);
        if (StringUtils.isBlank(userJsonStr)) {
            return null;
        }
        return JSONUtil.toBean(userJsonStr, UserInfo.class);
    }

    /**
     * 获取完整访问地址，包含path中的请求参数
     *
     * @param request request
     * @return 完整访问地址
     */
    public static String getFullUrl(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        if (Objects.nonNull(request.getQueryString())) {
            url.append("?").append(request.getQueryString());
        }
        return url.toString();
    }

    /**
     * 获取请求的 host
     *
     * @param request 请求对象
     * @return host
     */
    public static String getRequestHost(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        return getHostByUrl(url.toString());
    }

    /**
     * 获取网址的 host
     *
     * @param url 网址
     * @return host
     */
    public static String getHostByUrl(String url) {
        UrlBuilder urlBuilder = UrlBuilder.of(url);
        return urlBuilder.getHost();
    }

    /**
     * 通过 ssoToken 获取用户信息，建立登录态
     *
     * @param ssoClientProperties
     * @param ssoToken
     * @param request
     * @param response
     */
    public static void handleSSOToken(SSOClientProperties ssoClientProperties, String ssoToken, HttpServletRequest request, HttpServletResponse response) {
        SSOUserDTO ssoUserDTO = SSOHttpUtil.getLoginUserBySSOToken(ssoClientProperties.getServerUrl(), ssoClientProperties.getAppId(), ssoClientProperties.getAppSecret(), ssoToken);
        AssertUtil.notNull(ssoUserDTO, SSOLoginErrorEnum.GET_SSO_USER_ERROR);
        // 检查 ssoToken 是否是当前应用生成的
        AssertUtil.isTrue(ssoClientProperties.getAppId().equals(ssoUserDTO.getAppId()), SSOLoginErrorEnum.SSO_USER_APP_ID_ERROR);
        // 用户登录数据加密
        String userJsonStr = JSONUtil.toJsonStr(ssoUserDTO.getUser());
        String encryptData = AESCipherUtil.encryptData(ssoClientProperties.getAppSecret(), userJsonStr);
        // 用户登录信息写入 Cookie，有效期30分钟
        CookieUtil.writeSSOLoginCookie(encryptData,
                ssoClientProperties.getAppId(),
                30 * 60,
                SSOLoginConstant.INDEX_PAGE_PATH,
                response);
        log.debug("user sso login success ssoUserDTO:{}", userJsonStr);
    }

    /**
     * 检查配置项是否正确
     *
     * @param ssoClientProperties
     */
    public static void ssoClientPropertiesCheck(SSOClientProperties ssoClientProperties) {
        AssertUtil.hasText(ssoClientProperties.getAppId(), "appId 不能为空");
        AssertUtil.hasText(ssoClientProperties.getAppSecret(), "appSecret 不能为空");
        AssertUtil.hasText(ssoClientProperties.getEnvironment(), "environment 不能为空");
        EnvironmentEnum environmentEnum = EnvironmentEnum.getByCode(ssoClientProperties.getEnvironment());
        AssertUtil.notNull(environmentEnum, "environment 配置错误，可配置字段详见：io.github.moyugroup.auth.demo.constant.enums.EnvironmentEnum");
        // 如果未自定义 sso 服务器地址，则取默认配置
        if (StringUtils.isBlank(ssoClientProperties.getServerUrl())) {
            if (EnvironmentEnum.DEVELOP.equals(environmentEnum)) {
                ssoClientProperties.setServerUrl(devServerUrl);
            }
            if (EnvironmentEnum.PRODUCTION.equals(environmentEnum)) {
                ssoClientProperties.setServerUrl(prodServerUrl);
            }
        }
    }

    /**
     * 处理退出登录
     *
     * @param properties
     * @param response
     * @param backUrl
     */
    public static void handleSSOLogout(SSOClientProperties properties, HttpServletResponse response, String backUrl) throws IOException {
        // 1.删除登录相关cookie
        CookieUtil.removeSSOLoginCookie(properties.getAppId(), SSOLoginConstant.INDEX_PAGE_PATH, response);
        // 2.如果有backUrl，则重定向到登录中心退出登录，携带appId和backUrl参数
        if (StringUtils.isNoneBlank(backUrl)) {
            String sb = properties.getServerUrl() + SSOLoginConstant.LOGOUT_ENDPOINT + "?" + SSOLoginConstant.APP_ID + "=" + properties.getAppId() +
                    "&" + SSOLoginConstant.BACK_URL + "=" + URLEncoder.encode(backUrl, StandardCharsets.UTF_8);
            response.sendRedirect(sb);
        } else {
            // 否则返回默认操作成功的json
            WebUtil.writeJsonResponse(Result.success());
        }
    }
}
