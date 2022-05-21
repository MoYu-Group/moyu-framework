package io.github.moyugroup.web.aop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 方法运行监控日志配置
 * <p>
 * Created by fanfan on 2022/05/20.
 */
@Data
@ConfigurationProperties(prefix = "moyu.method-time")
public class MethodTimeProperties {

    /**
     * 排除的列表
     */
    private String exclusions;

    /**
     * 切面表达式
     */
    private String pointcutExpression;

    /**
     * 参数截取长度，默认取1000
     */
    private int paramsMaxLength = 1000;

}
