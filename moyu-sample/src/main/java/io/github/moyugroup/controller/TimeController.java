package io.github.moyugroup.controller;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

/**
 * TimeController
 * <p>
 * Created by fanfan on 2024/01/16.
 */
@Slf4j
@RestController
public class TimeController {

    @GetMapping("getTime")
    public Object getTime(LocalDateTime localDateTime, Date date) {
        HashMap<String, Object> returnMap = new HashMap<>();
        returnMap.put("localDateTime", localDateTime);
        returnMap.put("date", date);
        return returnMap;
    }

    @PostMapping("postTime")
    public Object postTime(@RequestBody Param param) {
        return param;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Param {
        LocalDateTime localDateTime;
        Date date;
    }
}
