package io.github.moyugroup.framework.sdk.filter;

import io.github.moyugroup.auth.common.constant.SSOLoginConstant;
import io.github.moyugroup.auth.common.context.UserContext;
import io.github.moyugroup.auth.common.pojo.dto.UserInfo;
import io.github.moyugroup.auth.common.util.PathUtil;
import io.github.moyugroup.enums.ErrorCodeEnum;
import io.github.moyugroup.exception.BizException;
import io.github.moyugroup.framework.sdk.config.SSOClientProperties;
import io.github.moyugroup.framework.sdk.constant.enums.SSOLoginErrorEnum;
import io.github.moyugroup.framework.sdk.util.SSOLoginUtil;
import io.github.moyugroup.util.AssertUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 应用端 SSO 登录过滤器
 * <p>
 * Created by fanfan on 2024/03/29.
 */
@Slf4j
public class MoYuClientSSOLoginFilter implements Filter {

    /**
     * 默认拦截路径
     */
    private static final List<String> protectedPaths = List.of("/**");

    /**
     * 白名单路径，不拦截
     * todo 后续可从应用配置读取
     */
    private static final List<String> whitePathList = Arrays.asList(
            "/css/**",
            "/js/**",
            "/static/**",
            "/favicon.ico",
            "/oauth2",
            "/open/**"
    );

    private final SSOClientProperties properties;

    public MoYuClientSSOLoginFilter(SSOClientProperties ssoClientProperties) {
        // 检查配置项是否正确
        SSOLoginUtil.ssoClientPropertiesCheck(ssoClientProperties);
        this.properties = ssoClientProperties;
    }

    /**
     * 判断请求路径是否是不需要拦截的路径
     *
     * @param requestPath
     * @return
     */
    private static boolean isUnMatchRequest(String requestPath) {
        return !PathUtil.isMatch(protectedPaths, requestPath) || PathUtil.isMatch(whitePathList, requestPath);
    }

    public static void main(String[] args) {
        System.out.println(isUnMatchRequest("/"));
        System.out.println(new AntPathMatcher().match("/**", "/"));
    }

    /**
     * 过滤器初始化逻辑
     *
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.debug("MoYuClientSSOLoginFilter init...");
    }

    /**
     * 过滤逻辑
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 获取请求的路径
        String requestPath = request.getRequestURI();

        // 不需要 SDK 拦截的请求
        if (isUnMatchRequest(requestPath)) {
            log.debug("doFilter not match path: {}", requestPath);
            // 对于不匹配的路径，直接继续请求
            filterChain.doFilter(request, response);
            return;
        }

        // 处理SSO登陆成功后的回调，通过 ssoToken 重建登录态
        if (PathUtil.isMatch(SSOLoginConstant.SSO_VERIFY_ENDPOINT, requestPath)) {
            // 获取必要参数
            String ssoToken = request.getParameter(SSOLoginConstant.SSO_TOKEN);
            String backUrl = request.getParameter(SSOLoginConstant.BACK_URL);
            AssertUtil.isFalse(StringUtils.isAnyBlank(ssoToken, backUrl), ErrorCodeEnum.SSO_LOGIN_PARAM_ERROR);
            log.debug("doFilter match path: {} with ssoToken={} backUrl={}", requestPath, ssoToken, backUrl);

            // 如果登录回调的 host 与当前应用 host 不一致，则说明有钓鱼风险，提示登录失败
            String requestHost = SSOLoginUtil.getRequestHost(request);
            String backUrlHost = SSOLoginUtil.getHostByUrl(backUrl);
            if (!StringUtils.equals(requestHost, backUrlHost)) {
                log.error("SSO 登录回调的 host:{} 与当前应用 host:{} 不一致", backUrlHost, requestHost);
                throw new BizException(SSOLoginErrorEnum.SSO_HOST_CHANGE);
            }
            // 根据ssoToken获取用户信息，建立登录态
            SSOLoginUtil.handleSSOToken(properties, ssoToken, request, response);

            // 登录成功后，重定向回 backUrl
            response.sendRedirect(backUrl);
            return;
        }

        // 处理注销登录请求
        if (PathUtil.isMatch(SSOLoginConstant.LOGOUT_ENDPOINT, requestPath)) {
            String backUrl = request.getParameter(SSOLoginConstant.BACK_URL);
            log.debug("doFilter match path: {} with backUrl={}", requestPath, backUrl);
            SSOLoginUtil.handleSSOLogout(properties, response, backUrl);
            return;
        }

        log.debug("doFilter match path: {}", requestPath);
        // 匹配到登录保护路径，进行登录检查
        userLoginCheck(request, response, filterChain);
    }

    /**
     * 用户登录检查
     *
     * @param httpRequest
     * @param httpResponse
     * @param filterChain
     */
    private void userLoginCheck(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain filterChain) throws ServletException, IOException {
        UserInfo userInfo = SSOLoginUtil.getUserFromCookie(httpRequest, properties.getAppId(), properties.getAppSecret());
        if (Objects.nonNull(userInfo)) {
            // 已登录，建立用户登录上下文
            buildLoginContext(userInfo);
            // 继续执行业务
            filterChain.doFilter(httpRequest, httpResponse);
        } else {
            // 用户未登录，跳转到sso登录页面进行登录
            String backUrl = SSOLoginUtil.getFullUrl(httpRequest);
            String appId = properties.getAppId();
            String redirectUrl = properties.getServerUrl() + SSOLoginConstant.LOGIN_PAGE_PATH
                    + "?" + SSOLoginConstant.APP_ID + "=" + appId
                    + "&" + SSOLoginConstant.BACK_URL + "=" + URLEncoder.encode(backUrl, StandardCharsets.UTF_8);
            log.debug("user is not logged in, redirect to {}", redirectUrl);
            httpResponse.sendRedirect(redirectUrl);
        }
    }

    /**
     * 建立用户登录上下文
     *
     * @param userInfo 用户信息
     */
    private void buildLoginContext(UserInfo userInfo) {
        log.debug("user is logged in with {}", userInfo);
        UserContext.set(userInfo);
    }

    /**
     * 过滤器销毁逻辑
     */
    @Override
    public void destroy() {
        Filter.super.destroy();
    }

}
