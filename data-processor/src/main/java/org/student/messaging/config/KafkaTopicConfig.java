package org.student.messaging.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.student.messaging.config.properties.KafkaProperties;
import org.student.messaging.config.properties.TopicProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(TopicProperties.class)
public class KafkaTopicConfig {
    private final KafkaProperties kafkaProperties;
    private final TopicProperties topicProperties;

    public KafkaTopicConfig(KafkaProperties kafkaProperties, TopicProperties topicProperties) {
        this.kafkaProperties = kafkaProperties;
        this.topicProperties = topicProperties;
    }

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic saveInfoTopic() {
        return TopicBuilder.name("save-info-topic")
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }

    @Bean
    public NewTopic getExtInfoTopic() {
        return TopicBuilder.name("get-ext-info-topic")
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }

    @Bean
    public NewTopic getIntInfoTopic() {
        return TopicBuilder.name("get-int-info-topic")
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }

    @Bean
    public NewTopic delInfoTopic() {
        return TopicBuilder.name("del-info-topic")
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }

    @Bean
    public NewTopic saveResponseTopic() {
        return TopicBuilder.name("save-response-topic")
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }

    @Bean
    public NewTopic delResponseTopic() {
        return TopicBuilder.name("del-response-topic")
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }

    @Bean
    public NewTopic getExtResponseTopic() {
        return TopicBuilder.name("get-ext-response-topic")
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }

    @Bean
    public NewTopic getIntResponseTopic() {
        return TopicBuilder.name("get-int-response-topic")
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }
}
