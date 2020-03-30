package com.wibo.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaConfig {

    @Autowired
    private KafkaConsumerProperties kafkaProperties;

    @Autowired
    private KafkaProduceProperties kafkaProduceProperties;

    @Bean(name = "containerFactory")
    public ConcurrentKafkaListenerContainerFactory containerFactory() {
        ConcurrentKafkaListenerContainerFactory containerFactory = new ConcurrentKafkaListenerContainerFactory();
        containerFactory.setConcurrency(kafkaProperties.getConcurrency());
        containerFactory.setConsumerFactory(new DefaultKafkaConsumerFactory(consumerConfig()));
        containerFactory.getContainerProperties().setGroupId(kafkaProperties.getGroupId());
        containerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        containerFactory.setBatchListener(true);
        return containerFactory;
    }


        /**
         * 消费者的配置
         * @return
         */
    public Map<String, Object> consumerConfig() {
       Map<String, Object> consumerMap = new HashMap<>();
        consumerMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getServers());
        consumerMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaProperties.getAuto());
        consumerMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getOffset());
        consumerMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaProperties.getRecords());
        consumerMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafkaProperties.getMaxPollTime());
        consumerMap.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, kafkaProperties.getHeartTime());
        consumerMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaProperties.getSessionTime());
        return consumerMap;
    }

    @Bean("myKafkaTemplate")
    public KafkaTemplate<String, String> kafkaTemplate(@Qualifier("producerFactory") ProducerFactory producerFactory) {
        return new KafkaTemplate<String, String>(producerFactory);
    }

    @Bean(name = "producerFactory")
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<String, String>(produceProps());
    }

    /**
     * kafka生产者配置
     * @return
     */
    private Map<String, Object> produceProps() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProduceProperties.getServers());
        map.put(ProducerConfig.RETRIES_CONFIG, kafkaProduceProperties.getRetries());
        map.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProduceProperties.getBatchSize());
        map.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProduceProperties.getBufferMemory());
        map.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        map.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return map;
    }
}
