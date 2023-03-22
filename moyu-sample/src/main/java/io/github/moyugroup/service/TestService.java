package io.github.moyugroup.service;

import org.springframework.stereotype.Service;

/**
 * Created by fanfan on 2023/03/23.
 */
@Service
public class TestService {

    public String hello(String s) {
        return "helloWorld:" + s;
    }
}
