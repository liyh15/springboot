package com.wibo.controller;

import com.wibo.common.response.TestResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping(value = "response")
    public TestResponse response() {
        return new TestResponse();
    }
}
