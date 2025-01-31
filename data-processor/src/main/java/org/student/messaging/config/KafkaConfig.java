package org.student.messaging.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.student.messaging.config.properties.KafkaProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConfig {
    private final KafkaProperties kafkaProperties;

    public KafkaConfig(KafkaProperties kafkaProperties){
        this.kafkaProperties = kafkaProperties;
    }


    /**
     * To ensure proper deserialisation, the ‘__TypeId__’ header should be included when posting to a topic.
     * <p>
     * This helps the consumer to identify the type of the object being deserialized.
     * </p>
     *
     * <pre>
     * {@code
     *  ProducerRecord<String, byte[]> record = recordFactory(message,topic);
     *  record.headers().add(new RecordHeader("__TypeId__", deserializerTokenName.getBytes()));
     *  kafkaTemplate.send(record);
     * }
     * </pre>
     */
    @Bean
    public ProducerFactory<String, byte[]> producerFactory() {
        Map<String, Object> configProp = new HashMap<>();
        configProp.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configProp.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProp.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        return new DefaultKafkaProducerFactory<>(configProp);
    }


    /**
     * Configures the Kafka ConsumerFactory with the specified properties.
     * <p>
     * This configuration utilizes the {@link JsonDeserializer} for deserializing values.
     * The {@code TYPE_MAPPINGS} property defines the mapping between tokens and their corresponding class names.
     * In this configuration, the tokens {@code uuid} and {@code artifactMetadataUploadRequest} are mapped to
     * {@link java.util.UUID} and {@link org.student.messaging.models.ArtifactMetadataUploadRequest} respectively.
     * </p>
     */
    @Bean
    public ConsumerFactory<String, byte[]> consumerFactory() {
        Map<String, Object> configProp = new HashMap<>();
        configProp.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configProp.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProp.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProp.put(JsonDeserializer.TYPE_MAPPINGS,"uuid:java.util.UUID, artifactMetadataUploadRequest:org.student.messaging.models.ArtifactMetadataUploadRequest");
        configProp.put(JsonDeserializer.TRUSTED_PACKAGES,"java.util, java.lang, org.student.messaging.models");
        configProp.put(ConsumerConfig.GROUP_ID_CONFIG,"data-processor-group");
        return new DefaultKafkaConsumerFactory<>(configProp);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, byte[]> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public KafkaTemplate<String, byte[]> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}

