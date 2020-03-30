package com.wibo.consumer.controller;

import com.wibo.common.response.HelloResponse;
import com.wibo.common.response.TestResponse;
import com.wibo.consumer.feign.ServerFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "server")
public class ConsumerController {

    // 服务端的Feign接口
    @Autowired
    private ServerFeign serverFeign;

    @RequestMapping(value = "feign", produces = {"application/json"}, method = RequestMethod.GET)
    public TestResponse response() {
        return serverFeign.hello();
    }

    // 业务异常调用Feign接口
    @RequestMapping(value = "exception", produces = {"application/json"}, method = RequestMethod.GET)
    public TestResponse responseException() {
        return serverFeign.exception();
    }

    // 普通调用Feign接口
    @RequestMapping(value = "normal", produces = {"application/json"}, method = RequestMethod.GET)
    public HelloResponse hello() {
        return serverFeign.normal();
    }
}
