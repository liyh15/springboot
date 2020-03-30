package com.wibo.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.wibo")
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.wibo")
@EnableConfigurationProperties
public class KafkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaApplication.class, args);
    }
}
