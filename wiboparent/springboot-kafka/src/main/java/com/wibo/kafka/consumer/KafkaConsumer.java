package com.wibo.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(containerFactory = "containerFactory", topics = "hello")
    public void consumerServer(List<ConsumerRecord<String, String>> recordList, Acknowledgment acknowledgment) {
        log.error("size={}", recordList.size());
        for (ConsumerRecord consumerRecord : recordList) {
            log.error("**************" + consumerRecord.value());
        }
        acknowledgment.acknowledge();
    }
}
