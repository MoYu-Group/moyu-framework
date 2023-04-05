package io.github.moyugroup.web.util;

import cn.hutool.core.util.IdUtil;

/**
 * TraceId Mdc 工具类
 * <p>
 * Created by fanfan on 2023/04/05.
 */
public class TraceIdMdcUtil {

    /**
     * 日志跟踪标识 TraceId
     */
    public final static String TRACE_ID = "TraceId";

    /**
     * 获取 TraceId
     *
     * @return TraceId
     */
    public static String getRequestId() {
        return IdUtil.fastSimpleUUID();
    }

}
