package io.github.moyugroup.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by fanfan on 2023/03/23.
 */
@Slf4j
@Service
public class TestService {

    public String hello(String s) {
        return "helloWorld:" + s;
    }

    @Async("threadPoolTaskExecutor")
    public void asyncTest(String a) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("helloWorld asyncTest a:{}", a);
    }
}
