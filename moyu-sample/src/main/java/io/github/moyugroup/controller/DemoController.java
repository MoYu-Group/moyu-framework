package io.github.moyugroup.controller;

import cn.hutool.json.JSONObject;
import io.github.moyugroup.base.model.pojo.PageInfo;
import io.github.moyugroup.base.model.pojo.Result;
import io.github.moyugroup.base.util.UUIDUtil;
import io.github.moyugroup.enums.ErrorCodeEnum;
import io.github.moyugroup.exception.BizException;
import io.github.moyugroup.pojo.vo.ParamVO;
import io.github.moyugroup.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;

/**
 * demo controller
 * <p>
 * Created by fanfan on 2022/05/21.
 */
@Slf4j
@RestController
public class DemoController {

    @Value("${spring.application.name}")
    private String name;

    @Resource
    private TestService testService;

    @GetMapping("get")
    public Result<HashMap<String, Object>> get(String a, String b) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("a", a);
        map.put("b", b);
        map.put("spring.application.name", name);
        return Result.success(map);
    }

    @GetMapping("testService")
    public Result<?> testService(String a) {
        String hello = testService.hello(a);
        return Result.success(hello);
    }

    @GetMapping("getError")
    public Result<HashMap<String, Object>> getError() {
        return Result.fail(ErrorCodeEnum.REQUEST_ERROR);
    }

    @GetMapping("page")
    public Result<?> getPage() {
        PageInfo<Object> pageInfo = new PageInfo<>();
        pageInfo.setCurrentPage(1);
        pageInfo.setPageSize(10);
        pageInfo.setTotal(99);
        pageInfo.setList(null);
        return Result.success(pageInfo);
    }

    @PostMapping("post")
    public JSONObject post(@RequestBody(required = false) JSONObject jsonObject) {
        return jsonObject;
    }

    @PostMapping("upload")
    public Object upload(@RequestParam(value = "a", required = false) String a,
                         @RequestParam(value = "file", required = false) MultipartFile file) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("a", a);
        if (Objects.nonNull(file)) {
            map.put("file", file.getName());
        }
        return map;
    }

    @GetMapping("getUUID")
    public Result<Long> getUUID() {
        return Result.success(UUIDUtil.generateId());
    }

    @GetMapping("exception")
    public Result<?> exception() {
        throw new BizException(ErrorCodeEnum.REQUEST_ERROR);
    }

    @PostMapping("param")
    public Result<?> param(@RequestBody @Validated ParamVO paramVO) {
        return Result.success(paramVO);
    }
}
