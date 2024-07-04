package org.student.messaging;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.student.configs.ApplicationConfig;
import org.student.services.ArtifactsService;
import org.student.services.ArtifactsServiceImpl;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class MessageConsumer {

    private final KafkaConsumer<String, byte[]> consumer;
    private final ArtifactsService artifactsService;

    public MessageConsumer(ApplicationConfig config) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getKafka().getBootstrapServers());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, config.getKafka().getGroupId());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        this.consumer = new KafkaConsumer<>(properties);

        this.artifactsService = new ArtifactsServiceImpl(config);
    }

    public void consume(String topic) {
        TopicPartition partition = new TopicPartition(topic, 0);
        consumer.assign(Collections.singletonList(partition));

        while (true) {
            ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                for (ConsumerRecord<String, byte[]> record : records) {
                    System.out.println("Consumed message: " + new String(record.value()));
                    artifactsService.saveArtifactMessage(record.key(), record.value());
                }
            } else
                System.out.println("No messages");
        }

    }
}
