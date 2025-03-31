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
import org.student.messaging.topics.KafkaTopics;

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
        return TopicBuilder.name(KafkaTopics.CrudMeta.SAVE_META_INFO_TOPIC)
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }

    @Bean
    public NewTopic getExtInfoTopic() {
        return TopicBuilder.name(KafkaTopics.CrudMeta.GET_EXT_META_INFO_TOPIC)
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }

    @Bean
    public NewTopic getIntInfoTopic() {
        return TopicBuilder.name(KafkaTopics.CrudMeta.GET_INT_META_INFO_TOPIC)
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }

    @Bean
    public NewTopic delInfoTopic() {
        return TopicBuilder.name(KafkaTopics.CrudMeta.DEL_META_INFO)
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }

    @Bean
    public NewTopic saveResponseTopic() {
        return TopicBuilder.name(KafkaTopics.ResponseMeta.SAVE_RESPONSE_TOPIC)
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }

    @Bean
    public NewTopic delResponseTopic() {
        return TopicBuilder.name(KafkaTopics.ResponseMeta.DEL_RESPONSE_TOPIC)
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }

    @Bean
    public NewTopic getExtResponseTopic() {
        return TopicBuilder.name(KafkaTopics.ResponseMeta.GET_EXT_INFO_RESPONSE_TOPIC)
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }

    @Bean
    public NewTopic getIntResponseTopic() {
        return TopicBuilder.name(KafkaTopics.ResponseMeta.GET_INT_INFO_RESPONSE_TOPIC)
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }

    @Bean
    public NewTopic getErrorResponseTopic() {
        return TopicBuilder.name(KafkaTopics.ResponseMeta.ERROR_RESPONSE_TOPIC)
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }
}
