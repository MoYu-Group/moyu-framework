package io.github.moyugroup.web.aop;

import io.github.moyugroup.constant.CommonConstants;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 方法运行监控日志 切面实现
 * <p>
 * Created by fanfan on 2022/05/21.
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(MethodTimeProperties.class)
@ConditionalOnProperty(value = AopConstants.METHOD_TIME_ASPECT_ENABLED, havingValue = CommonConstants.CONDITIONAL_TRUE, matchIfMissing = true)
public class MethodTimeAspect {

    /**
     * 加载框架的默认切点配置
     */
    @Value("${framework.method-time.exclusions}")
    private String frameworkExclusions;
    /**
     * 用户自定义配置
     */
    @Resource
    private MethodTimeProperties methodTimeProperties;

    /**
     * 切面拦截规则
     */
    @Bean
    @ConditionalOnExpression(" !''.equals('${moyu.method-time.pointcut-expression:}') ")
    public AspectJExpressionPointcutAdvisor methodPointcutAdvisor() {
    // 组装切面

        AspectJExpressionPointcutAdvisor methodPointcutAdvisor = new AspectJExpressionPointcutAdvisor();
        methodPointcutAdvisor.setAdvice(new MethodTimeAopInterceptor(frameworkExclusions, methodTimeProperties));
        methodPointcutAdvisor.setExpression(methodTimeProperties.getPointcutExpression());
        return methodPointcutAdvisor;
    }

}
