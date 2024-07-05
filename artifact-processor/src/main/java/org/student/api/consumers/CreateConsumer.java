package org.student.api.consumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.student.api.VoidBiFunction;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class CreateConsumer implements MessageConsumer{
    private final KafkaConsumer<String, byte[]> consumer;
    private final VoidBiFunction<String, byte[]> service;
    private final String topic;

    public CreateConsumer(String bootstrapServers, String groupId, String topic, VoidBiFunction<String, byte[]> service) {
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
        TopicPartition partition = new TopicPartition(topic, 0);
        consumer.assign(Collections.singletonList(partition));

        while (true) {
            ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                for (ConsumerRecord<String, byte[]> record : records) {
                    System.out.println("Consumed message: " + new String(record.value()));
                    service.accept(record.key(), record.value());
                }
            } else
                System.out.println("No messages");
        }
    }
}
