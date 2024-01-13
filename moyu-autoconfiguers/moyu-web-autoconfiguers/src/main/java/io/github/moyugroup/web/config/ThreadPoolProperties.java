package io.github.moyugroup.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 线程池配置类
 * <p>
 * Created by fanfan on 2023/04/06.
 */
@Data
@ConfigurationProperties(prefix = "moyu.thread-pool")
public class ThreadPoolProperties {

    /**
     * 关闭自定义线程池
     */
    private Boolean disabled;

    /**
     * 核心线程池数量
     */
    private Integer corePoolSize = 4;

    /**
     * 最大线程池数量
     */
    private Integer maxPoolSize = 10;

    /**
     * 缓冲队列长度
     */
    private Integer queueCapacity = 1000;

    /**
     * 空闲线程存活时间
     */
    private Integer keepAliveSeconds = 60;
}
