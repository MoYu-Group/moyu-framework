package io.github.moyugroup.controller;

import io.github.moyugroup.base.model.pojo.Result;
import io.github.moyugroup.enums.ErrorCodeEnum;
import io.github.moyugroup.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 异常测试
 * <p>
 * Created by fanfan on 2023/05/13.
 */
@Slf4j
@RestController
public class ExceptionTestController {

    @GetMapping("testBizException")
    public Result<?> testBizException() {
        throw new BizException(ErrorCodeEnum.APPLICATION_ERROR);
    }

    @GetMapping("testNullException")
    public Result<?> testNullException() {
        String test = null;
        return Result.success(test.trim());
    }

    @GetMapping("testByZeroException")
    public Result<?> testByZeroException() {
        int i = 1 / 0;
        return Result.success();
    }

}
