package io.github.moyugroup.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 监控日志工具类
 * <p>
 * Created by fanfan on 2022/05/21.
 */
public class MonitorLogUtil {

    /**
     * 定义打印日志的 logger，该 logger 对象在框架默认 logback 里配置，如果未引入框架的日志配置则不生效
     */
    private static final Logger log = LoggerFactory.getLogger("MONITOR_LOG");

    /**
     * 打印格式化的监控日志
     * AppName|服务名称|是否成功|响应耗时|真实IP|请求参数|返回值|错误日志
     *
     * @param appName     AppName
     * @param serviceName 服务名称
     * @param isSuccess   是否成功
     * @param ms          响应耗时
     * @param ip          真实IP
     * @param methodArgs  请求参数
     * @param response    返回值
     * @param errorMsg    错误日志
     */
    public static void printLog(String appName, String serviceName, boolean isSuccess, long ms, String ip, String methodArgs, String response, String errorMsg) {
        if (isSuccess) {
            log.info("{}|{}|{}|{}|{}|{}|{}|{}", appName, serviceName, isSuccess, ms, ip, methodArgs, response, errorMsg);
        } else {
            log.error("{}|{}|{}|{}|{}|{}|{}|{}", appName, serviceName, isSuccess, ms, ip, methodArgs, response, errorMsg);
        }
    }

}
