package com.wibo.fastdfs;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import(FdfsClientConfig.class)
@SpringBootApplication
@ComponentScan(basePackages = {"com.wibo"})
@EnableEurekaClient
public class FastDfsApplication {
    public static void main(String[] args) {
        SpringApplication.run(FastDfsApplication.class, args);
    }
}
