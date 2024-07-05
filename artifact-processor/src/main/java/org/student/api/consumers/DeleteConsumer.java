package org.student.api.consumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.student.api.VoidBiFunction;

import java.util.Properties;
import java.util.UUID;

public class DeleteConsumer  implements MessageConsumer{
    private final KafkaConsumer<String, byte[]> consumer;
    private final VoidBiFunction<String, UUID> service;
    private final String topic;

    public DeleteConsumer(String bootstrapServers, String groupId, String topic, VoidBiFunction<String, UUID> service) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        this.consumer = new KafkaConsumer<>(properties);

        this.service = service;
        this.topic = topic;
    }

    @Override
    public void consume() {

    }
}
