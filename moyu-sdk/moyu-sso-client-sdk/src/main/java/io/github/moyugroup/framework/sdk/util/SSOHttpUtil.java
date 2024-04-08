package io.github.moyugroup.framework.sdk.util;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.github.moyugroup.auth.common.constant.SSOLoginConstant;
import io.github.moyugroup.auth.common.pojo.dto.SSOUserDTO;
import io.github.moyugroup.base.model.pojo.Result;
import io.github.moyugroup.enums.ErrorCodeEnum;
import io.github.moyugroup.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * SSO 服务相关 Http 请求封装
 * <p>
 * Created by fanfan on 2024/04/05.
 */
@Slf4j
public class SSOHttpUtil {

    /**
     * 初始化 okHttpClient 客户端，设置超时时间为 5s
     */
    public static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            // 连接超时
            .connectTimeout(5, TimeUnit.SECONDS)
            // 写入超时
            .readTimeout(5, TimeUnit.SECONDS)
            // 读取超时
            .writeTimeout(5, TimeUnit.SECONDS)
            .build();

    /**
     * 请求登录中心 通过 ssoToken 获取登录用户信息
     *
     * @param appId
     * @param appSecret
     * @param ssoToken
     * @return
     */
    public static SSOUserDTO getLoginUserBySSOToken(String serverUrl, String appId, String appSecret, String ssoToken) {
        if (StringUtils.isAnyBlank(serverUrl, appId, appSecret, ssoToken)) {
            throw new BizException(ErrorCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        serverUrl = serverUrl + SSOLoginConstant.OAUTH2_GET_USER_ENDPOINT;
        JSONObject param = new JSONObject();
        param.set(SSOLoginConstant.APP_ID, appId);
        param.set(SSOLoginConstant.APP_SECRET, appSecret);
        param.set(SSOLoginConstant.SSO_TOKEN, ssoToken);
        Result<JSONObject> result = sendPostRequest(serverUrl, param);
        if (result.isSuccess()) {
            return JSONUtil.toBean(result.getContent(), SSOUserDTO.class);
        }
        return null;
    }

    /**
     * 使用 okHttp 客户端发送 Post 请求
     *
     * @param requestUrl
     * @param param
     * @return
     */
    public static Result<JSONObject> sendPostRequest(String requestUrl, JSONObject param) {
        log.info("SSO Server Request Url:{} Param:{}", requestUrl, param);
        // 创建 MediaType
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        // 创建 RequestBody
        RequestBody requestBody = RequestBody.create(param.toString(), mediaType);

        // 创建请求对象
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(requestBody)
                .build();

        // 发送请求并处理响应
        try (Response response = okHttpClient.newCall(request).execute()) {
            String body = null;
            if (Objects.nonNull(response.body())) {
                body = response.body().string();
            }
            log.info("SSO Server Response code:{} body:{}", response.code(), body);

            if (!response.isSuccessful()) {
                return Result.fail();
            }

            // 解析JSON响应
            Result<JSONObject> result = JSONUtil.toBean(body, new TypeReference<>() {
            }, false);
            if (result.isSuccess()) {
                return result;
            }
            return Result.fail();
        } catch (IOException e) {
            log.error("SSO Server Request Error:{}", e.getMessage());
            return Result.fail();
        }
    }

}
