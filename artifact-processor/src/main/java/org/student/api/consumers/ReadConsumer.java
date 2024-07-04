package org.student.api.consumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.student.api.VoidBiFunction;
import org.student.configs.ApplicationConfig;
import org.student.services.ArtifactsService;
import org.student.services.ArtifactsServiceImpl;

import java.util.Properties;
import java.util.UUID;

public class ReadConsumer implements MessageConsumer{
    private final KafkaConsumer<String, byte[]> consumer;
    private final VoidBiFunction<String, UUID> service;

    public ReadConsumer(String bootstrapServers, String groupId, VoidBiFunction<String, UUID> service) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        this.consumer = new KafkaConsumer<>(properties);

        this.service = service;
    }

    @Override
    public void consume(String topic) {

    }
}
