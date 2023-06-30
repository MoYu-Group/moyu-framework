package io.github.moyugroup.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.Duration;

/**
 * SpringApplicationRunListener 的默认空实现，便于子类按需实现感兴趣的方法
 * <p>
 * Created by fanfan on 2022/05/21.
 */
@Slf4j
public class ApplicationRunAdapter implements SpringApplicationRunListener {

    /**
     * 在 run 方法第一次启动时立即调用。可用于非常早的初始化
     *
     * @param bootstrapContext 引导上下文
     */
    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        log.info("ApplicationRunAdapter.starting");
    }

    /**
     * 环境初始化后，创建ApplicationContext之前调用
     *
     * @param bootstrapContext 引导上下文
     * @param environment      环境
     */
    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        log.info("ApplicationRunAdapter.environmentPrepared");
    }

    /**
     * 创建和准备ApplicationContext后，加载源前调用
     *
     * @param context 应用程序上下文
     */
    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        log.info("ApplicationRunAdapter.contextPrepared");
    }

    /**
     * 在加载应用程序上下文之后，但在刷新之前调用
     *
     * @param context 应用程序上下文
     */
    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        log.info("ApplicationRunAdapter.contextLoaded");
    }

    /**
     * 上下文已刷新，应用程序已启动，但尚未调用CommandLineRunners和ApplicationRunners
     *
     * @param context   应用程序上下文。
     * @param timeTaken 启动应用程序所用的时间，如果未知，则为null
     */
    @Override
    public void started(ConfigurableApplicationContext context, Duration timeTaken) {
        log.info("ApplicationRunAdapter.started");
    }

    /**
     * 在 run 方法完成之前立即调用，此时应用程序上下文已刷新并且所有 CommandLineRunners 和 ApplicationRunners 已被调用
     *
     * @param context   应用程序上下文
     * @param timeTaken 应用程序准备就绪所用的时间，如果未知，则为null
     */
    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        log.info("ApplicationRunAdapter.ready");
    }

    /**
     * 当运行应用程序发生故障时调用
     *
     * @param context   应用程序上下文或null如果在创建上下文之前发生故障
     * @param exception 异常信息
     */
    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        log.info("ApplicationRunAdapter.failed");
    }
}
