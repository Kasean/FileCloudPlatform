package org.student.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "archiver-topic", groupId = "core-file-garbage-group")
    public void consume(byte[] message) {
        System.out.println("Consumed message: " + new String(message));
    }

}
