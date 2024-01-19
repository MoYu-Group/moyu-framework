package io.github.moyugroup.web.config;

import io.github.moyugroup.constant.CommonConstants;
import io.github.moyugroup.web.config.properties.ThreadPoolProperties;
import io.github.moyugroup.web.constant.FrameworkConstants;
import io.github.moyugroup.web.decorator.MdcTaskDecorator;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步线程池配置
 * 可以通过配置禁用默认实现，业务自行实现
 * <p>
 * Created by fanfan on 2023/04/05.
 */
@Slf4j
@EnableAsync
@Configuration
@EnableConfigurationProperties(ThreadPoolProperties.class)
@ConditionalOnProperty(value = FrameworkConstants.THREAD_POOL_DISABLED, havingValue = CommonConstants.CONDITIONAL_FALSE, matchIfMissing = true)
public class ThreadPoolConfig {

    @Resource
    private ThreadPoolProperties threadPoolProperties;

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 自定义线程池名称前缀
        executor.setThreadNamePrefix("async-task-");
        // 核心线程池数量
        executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        // 最大线程池数量
        executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        // 缓冲队列长度
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        // 空闲线程存活时间
        executor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 关闭线程池时，等待任务结束超时时间
        executor.setAwaitTerminationSeconds(60);
        // 线程池对拒绝任务(无线程可用)的处理策略：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 增加 MDC 适配器，用于传递线程的上下文
        executor.setTaskDecorator(new MdcTaskDecorator());
        log.info("init ThreadPoolTaskExecutor with corePoolSize:{} maxPoolSize:{} queueCapacity：{} keepAliveSeconds：{}",
                executor.getCorePoolSize(), executor.getMaxPoolSize(), threadPoolProperties.getQueueCapacity(), executor.getKeepAliveSeconds());
        return executor;
    }

}
