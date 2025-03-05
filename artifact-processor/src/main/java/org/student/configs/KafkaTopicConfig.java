package org.student.configs;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.student.configs.properties.KafkaProperties;
import org.student.configs.properties.TopicProperties;

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
    public NewTopic createArtifactTopics() {
        return TopicBuilder.name("tpc1")
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }

    @Bean
    public NewTopic readArtifactTopic() {
        return TopicBuilder.name("tpr1")
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }

    @Bean
    public NewTopic updateArtifactTopic() {
        return TopicBuilder.name("tru1")
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }

    @Bean
    public NewTopic deleteArtifactTopic() {
        return TopicBuilder.name("tpd1")
                .partitions(topicProperties.getPartitionCount())
                .replicas(topicProperties.getReplicaCount())
                .build();
    }
}
