package com.wibo.server.controller;

import com.wibo.common.config.BusinessException;
import com.wibo.common.response.HelloResponse;
import com.wibo.common.response.TestResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("feign")
public class FeignController {

    /**
     * 无参访问Feign接口
     * @return
     */
    @RequestMapping(value = "hello", produces = {"application/json"},
            method = RequestMethod.GET)
    public TestResponse response() {
        TestResponse response = new TestResponse();
        response.setName("hello consumer");
        response.setMessage("lalalalalalalalalalalalala");
        return response;
    }

    /**
     * 异常访问Feign接口
     * @return
     */
    @RequestMapping(value = "exception", produces = {"application/json"},
            method = RequestMethod.GET)
    public TestResponse responseException() {
        TestResponse response = new TestResponse();
        response.setName("hello consumer");
        response.setMessage("lalalalalalalalalalalalala");
        BusinessException.throwExceptionByStatus("1001", "服务端的异常");
        return response;
    }

    /**
     * 无参访问Feign接口
     * @return
     */
    @RequestMapping(value = "normal", produces = {"application/json"},
            method = RequestMethod.GET)
    public HelloResponse normal() {
        HelloResponse response = new HelloResponse();
        response.setHello("hellooooooooooooo");
        return response;
    }
}
