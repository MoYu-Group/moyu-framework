package io.github.moyugroup.controller;

import cn.hutool.json.JSONObject;
import io.github.moyugroup.base.model.pojo.PageInfo;
import io.github.moyugroup.base.model.pojo.Result;
import io.github.moyugroup.base.util.UUIDUtil;
import io.github.moyugroup.enums.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("get")
    public Result<HashMap<String, Object>> get(String a, String b) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("a", a);
        map.put("b", b);
        map.put("spring.application.name", name);
        return Result.success(map);
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
}
