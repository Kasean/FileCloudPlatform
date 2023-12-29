package org.student.messaging.artifact_processor;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;
import org.student.messaging.KafkaTopics;

@Service
public class ArtifactProcessorListener {
    @KafkaListener(id = "artifact-processor-listener", topicPartitions = @TopicPartition(topic = KafkaTopics.ARTIFACT_PROCESSOR_TOPIC, partitions = {"1"}), groupId = "core-file-garbage-group")
    public void consume(byte[] message) {
        System.out.println("Consumed message: " + new String(message));
    }

}
