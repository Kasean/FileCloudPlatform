package org.student.messaging.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic saveInfoTopic() {
        return TopicBuilder.name("save-info-topic")
                .partitions(2)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic getExtInfoTopic() {
        return TopicBuilder.name("get-ext-info-topic")
                .partitions(2)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic getIntInfoTopic() {
        return TopicBuilder.name("get-int-info-topic")
                .partitions(2)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic delInfoTopic() {
        return TopicBuilder.name("del-info-topic")
                .partitions(2)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic saveResponseTopic() {
        return TopicBuilder.name("save-response-topic")
                .partitions(2)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic delResponseTopic() {
        return TopicBuilder.name("del-response-topic")
                .partitions(2)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic getExtResponseTopic() {
        return TopicBuilder.name("get-ext-response-topic")
                .partitions(2)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic getIntResponseTopic() {
        return TopicBuilder.name("get-int-response-topic")
                .partitions(2)
                .replicas(1)
                .compact()
                .build();
    }
}
