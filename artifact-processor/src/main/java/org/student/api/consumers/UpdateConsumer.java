package org.student.api.consumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.student.configs.ApplicationConfig;
import org.student.services.ArtifactsService;
import org.student.services.ArtifactsServiceImpl;

import java.util.Properties;

public class UpdateConsumer implements MessageConsumer{
    private final KafkaConsumer<String, byte[]> consumer;
    private final ArtifactsService artifactsService;

    public UpdateConsumer(ApplicationConfig config) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getKafka().getBootstrapServers());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, config.getKafka().getGroupId());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        this.consumer = new KafkaConsumer<>(properties);

        this.artifactsService = new ArtifactsServiceImpl(config);
    }

    @Override
    public void consume() { // Not implemented

    }
}
