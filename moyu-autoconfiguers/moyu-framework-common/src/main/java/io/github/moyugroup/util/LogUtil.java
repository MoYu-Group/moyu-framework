package io.github.moyugroup.util;

import cn.hutool.core.util.StrUtil;
import io.github.moyugroup.base.model.enums.ExceptionLevel;
import io.github.moyugroup.constant.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 日志工具类
 * <p>
 * Created by fanfan on 2022/05/21.
 */
public class LogUtil {

    /**
     * 定义打印日志的 logger，该 logger 对象在框架默认 logback 里配置，如果未引入框架的日志配置则不生效
     */
    private static final Logger log = LoggerFactory.getLogger("MONITOR_LOG");

    /**
     * 打印格式化控制台日志
     * 格式：IP地址|httpMethod|请求地址|是否成功|响应耗时|类名|方法名|参数|返回值|错误码|错误日志
     *
     * @param log        控制台日志对象
     * @param flag       是否成功
     * @param ip         IP地址
     * @param httpMethod httpMethod
     * @param requestUri 请求地址
     * @param ms         请求耗时
     * @param className  类名
     * @param methodName 方法名
     * @param param      参数
     * @param resultStr  返回值
     * @param errCode    错误码
     * @param errMsg     错误信息
     * @param level      日志等级
     */
    public static void printConsoleLog(Logger log, Boolean flag, String ip, String httpMethod, String requestUri, Long ms, String className, String methodName, String param, String resultStr, String errCode, String errMsg, ExceptionLevel level) {
        String logInfo = StrUtil.format("{}|{}|RequestUri:{}|{}|{}ms|{}|{}|Param:{}|Result:{}|{}|{}",
                ip, httpMethod, requestUri, flag, ms, className, methodName, param, resultStr, errCode, errMsg);
        printLogWithLevel(log, level, logInfo);
    }

    /**
     * 打印格式化的监控日志
     * 格式：AppName|类名|方法名|真实IP|httpMethod|请求地址|是否成功|响应耗时|请求参数|返回值|错误码|错误日志
     * 如果引入了 MoYu 框架的日志配置，会单独将接口监控的日志输出到 ${logPath}/logs/monitor/operation.log 文件中，便于单独进行日志分析
     * 否则会将日志输出到 console，除非自己指定 loggerName=MONITOR_LOG 的 logback 配置
     *
     * @param className  类名
     * @param methodName 方法名
     * @param ip         真实IP
     * @param httpMethod httpMethod
     * @param requestUri 请求地址
     * @param isSuccess  是否成功
     * @param ms         响应耗时
     * @param methodArgs 请求参数
     * @param response   返回值
     * @param errorCode  错误码
     * @param errorMsg   错误日志
     * @param level      日志等级
     */
    public static void printMonitorLog(String className, String methodName, String ip, String httpMethod, String requestUri, Boolean isSuccess, Long ms,
                                       String methodArgs, String response, String errorCode, String errorMsg, ExceptionLevel level) {
        String appName = PropertyUtil.getProperty(CommonConstants.PROJECT_NAME, String.class, null);
        String logInfo = StrUtil.format("{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}",
                appName, className, methodName, ip, httpMethod, requestUri, isSuccess, ms, methodArgs, response, errorCode, errorMsg);
        printLogWithLevel(log, level, logInfo);
    }

    /**
     * 打印指定等级的日志
     *
     * @param log     日志logger
     * @param level   日志等级
     * @param logInfo 日志内容
     */
    public static void printLogWithLevel(Logger log, ExceptionLevel level, String logInfo) {
        switch (level) {
            case INFO -> log.info(logInfo);
            case DEBUG -> log.debug(logInfo);
            case WARN -> log.warn(logInfo);
            case ERROR -> log.error(logInfo);
        }
    }

}
