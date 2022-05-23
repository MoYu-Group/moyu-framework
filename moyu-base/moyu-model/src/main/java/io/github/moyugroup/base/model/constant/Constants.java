package io.github.moyugroup.base.model.constant;

/**
 * 常量集合
 * <p>
 * Created by fanfan on 2022/05/23.
 */
public class Constants {

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
     * HTTP的POST请求方法
     */
    public static final String METHOD_POST = "POST";
    /**
     * HTTP的POST请求方法
     */
    public static final String METHOD_GET = "GET";
    /**
     * Conditional常量
     */
    public static final String CONDITIONAL_TRUE = "true";
    public static final String CONDITIONAL_FALSE = "false";

    /**
     * 项目常用profile环境
     */
    public static final String TEST_ENV = "test";
    public static final String DEV_ENV = "dev";
    public static final String PRE_ENV = "pre";
    public static final String PROD_ENV = "prod";

}
