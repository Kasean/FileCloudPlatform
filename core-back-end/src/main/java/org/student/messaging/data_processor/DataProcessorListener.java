package org.student.messaging.data_processor;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;
import org.student.messaging.KafkaTopics;

@Service
public class DataProcessorListener {
    @KafkaListener(id = "data-processor-listener", topicPartitions = @TopicPartition(topic = KafkaTopics.DATA_PROCESSOR_TOPIC, partitions = {"1"}), groupId = "core-file-garbage-group")
    public void consume(byte[] message) {
        System.out.println("Consumed message: " + new String(message));
    }

}
