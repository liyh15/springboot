package com.wibo.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka.produce")
@Data
public class KafkaProduceProperties {

    private String servers;

    private String retries;

    private String batchSize;

    private String bufferMemory;

    private String acks;
}
