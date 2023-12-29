package org.student.messaging.archiver;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;
import org.student.messaging.topics.KafkaTopics;

@Service
public class ArchiverListener {

    @KafkaListener(id = "archiver-listener", topicPartitions = @TopicPartition(topic = KafkaTopics.ARCHIVER_TOPIC, partitions = {"1"}), groupId = "core-file-garbage-group")
    public void consume(byte[] message) {
        System.out.println("Consumed message: " + new String(message));
    }

}
