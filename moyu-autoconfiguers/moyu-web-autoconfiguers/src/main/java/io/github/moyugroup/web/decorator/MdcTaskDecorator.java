package io.github.moyugroup.web.decorator;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * MDC 任务适配器
 * <p>
 * Created by fanfan on 2023/04/05.
 */
public class MdcTaskDecorator implements TaskDecorator {

    /**
     * 使异步线程池获得主线程的上下文
     *
     * @param runnable
     * @return
     */
    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> map = MDC.getCopyOfContextMap();
        return () -> {
            try {
                MDC.setContextMap(map);
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

}
