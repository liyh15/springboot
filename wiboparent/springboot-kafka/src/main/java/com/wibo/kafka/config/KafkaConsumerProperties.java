package com.wibo.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Configuration
@ConfigurationProperties(prefix = "kafka.consumer")
@Data
public class KafkaConsumerProperties implements Serializable {

    private String servers;

    private String auto;

    private String offset;

    private String records;

    private String heartTime;

    private String sessionTime;

    private String maxPollTime;

    private int concurrency;

    private String groupId;
}
