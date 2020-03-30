package com.wibo.consumer.feign;

import com.wibo.common.response.BaseResponse;
import com.wibo.common.response.HelloResponse;
import com.wibo.common.response.TestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ServerFeign接口调用示例
 * 本地工程按照BaseResponse<T>的规范表示返回参数
 * 外部工程直接
 */
@FeignClient(name = "server")
public interface ServerFeign {

    /**
     * 无参调用Serve
     * @return
     */
    @RequestMapping(value = "feign/hello", produces = {"application/json"}, method = RequestMethod.GET)
    TestResponse hello();


    /**
     * 业务异常调用Server
     * @return
     */
    @RequestMapping(value = "feign/exception", produces = {"application/json"}, method = RequestMethod.GET)
    TestResponse exception();

    /**
     * 业务正常调用Server
     * @return
     */
    @RequestMapping(value = "feign/normal", produces = {"application/json"}, method = RequestMethod.GET)
    HelloResponse normal();
}
