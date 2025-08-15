package io.github.moyugroup.constant;

/**
 * 通用常量
 * <p>
 * Created by fanfan on 2022/05/21.
 */
public class CommonConstants {

    /**
     * 框架的base包
     */
    public static final String BASE_PACKAGE = "io.github.moyugroup";
    /**
     * 配置的项目名称属性
     */
    public static final String PROJECT_NAME = "spring.application.name";
    /**
     * 项目常用profile环境
     */
    public static final String TEST_ENV = "test";
    public static final String DEV_ENV = "dev";
    public static final String PRE_ENV = "pre";
    public static final String PROD_ENV = "prod";

    /**
     * 逻辑删除标识：已删除
     */
    public static final int DELETED_FLAG_YES = 1;
    /**
     * 逻辑删除标识：未删除
     */
    public static final int DELETED_FLAG_NO = 0;
    /**
     * 英文逗号
     */
    public static final String COMMA = ",";
    /**
     * UTF-8编码
     */
    public static final String CHARSET_UTF8 = "UTF-8";


    /**
     * Conditional常量
     */
    public static final String CONDITIONAL_TRUE = "true";
    public static final String CONDITIONAL_FALSE = "false";

    /**
     * traceId
     */
    public final static String TRACE_ID = "traceId";
    /**
     * spanId
     */
    public final static String SPAN_ID = "spanId";
    /**
     * traceId Header
     */
    public final static String TRACE_ID_HEADER = "X-Trace-Id";
}
