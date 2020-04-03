package com.wibo.server.controller;

import com.wibo.common.config.BusinessException;
import com.wibo.common.config.ErrorCode;
import com.wibo.common.pojo.Test;
import com.wibo.common.response.MySqlTestResponse;
import com.wibo.common.response.TestResponse;
import com.wibo.server.mapper.MysqlMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@Api(value = "数据分析接口", description = "数据分析接口", tags = "ServerController", consumes = "application/json",
        produces = "application/json")
public class ServerController {

    @Autowired
    private MysqlMapper mysqlMapper;

    @RequestMapping("hello")
    @ApiOperation(value = "hello", notes = "hello", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "返回", response = String.class)})
    public String hello() {
        return "hello";
    }

    @RequestMapping("exception")
    @ApiOperation(value = "exception", notes = "exception", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "返回", response = String.class)})
    public String exception() {
        int a = 10 / 0;
        return "ok!";
    }

    @RequestMapping("response")
    @ApiOperation(value = "response", notes = "response", response = TestResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "返回", response = TestResponse.class)})
    public TestResponse response() {
        TestResponse response = new TestResponse();
        response.setMessage("lalalalala");
        response.setName("Tom");
        return response;
    }

    @RequestMapping("message")
    @ApiOperation(value = "message", notes = "message", response = TestResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "返回", response = TestResponse.class)})
    public TestResponse message() {
        TestResponse response = new TestResponse();
        response.setMessage("lalalalala");
        response.setName("Tom");
        BusinessException.throwException(ErrorCode.Status.MESSAGE_IS_NULL);
        return response;
    }

    @RequestMapping("mysqlTest")
    @ApiOperation(value = "mysqlTest", notes = "mysqlTest", response = MySqlTestResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "返回", response = MySqlTestResponse.class)})
    public MySqlTestResponse mySqlTestResponse() {
        List<Test> testList = mysqlMapper.selectTest();
        MySqlTestResponse response = new MySqlTestResponse();
        response.setTestList(testList);
        return response;
    }

    @RequestMapping("header")
    @ApiOperation(value = "header", notes = "header", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "返回", response = String.class)})
    public String showHeader(HttpServletRequest request) {
        System.out.println(request.getContentType());
        return "string";
    }

    @RequestMapping("file")
    @ApiOperation(value = "file", notes = "file", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "返回", response = String.class)})
    public String file(@RequestParam("file") MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        System.out.println(sheet.getPhysicalNumberOfRows());
        System.out.println(sheet.getLastRowNum());
        System.out.println(sheet.getRow(3));
        return "string";
    }

    @RequestMapping("fileDetail")
    public String fileDetail(@RequestParam("file") List<MultipartFile> files) throws IOException {
        for (MultipartFile file : files) {
            System.out.println(file.getResource());
            System.out.println(file.getBytes());
            System.out.println(file.getName());
            System.out.println(file.getOriginalFilename());
            System.out.println(file.getContentType());
        }
        return "string";
    }

    @RequestMapping("thread")
    public String thread() throws InterruptedException {
        System.out.println(Thread.currentThread());
        Thread.sleep(3000);
        return "string";
    }

    @RequestMapping("thread2")
    public String thread2(@RequestParam("name") String name) throws InterruptedException {
        System.out.println(name);
        Thread.sleep(3000);
        return "string";
    }

    @RequestMapping("file2")
    public String file2(HttpServletRequest request) throws InterruptedException, IOException {
        System.out.println(request.getParameter("name"));
        return "string";
    }

    @RequestMapping("zuul")
    public String zuul(HttpServletRequest request) throws InterruptedException, IOException {
        System.out.println("8081");
        return "8081";
    }

    @RequestMapping("log")
    public String log(HttpServletRequest request) throws InterruptedException, IOException {
        log.error("this is log");
        return "8081";
    }
}
