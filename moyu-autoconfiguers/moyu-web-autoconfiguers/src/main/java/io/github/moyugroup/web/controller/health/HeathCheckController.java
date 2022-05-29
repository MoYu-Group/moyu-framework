package io.github.moyugroup.web.controller.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查操作
 * <p>
 * Created by fanfan on 2022/05/19.
 */
@RestController
public class HeathCheckController {

    @GetMapping("/health")
    public String health() {
        return "success";
    }

}
