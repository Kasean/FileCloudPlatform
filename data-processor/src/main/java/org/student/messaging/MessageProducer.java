package org.student.messaging;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.student.messaging.topics.KafkaTopics;

@Service
public class MessageProducer {
    private final KafkaTemplate<String, byte[]> template;

    MessageProducer(KafkaTemplate<String, byte[]> template) {
        this.template = template;
    }

    public void send(byte[] message) {
        template.send(new ProducerRecord<>(KafkaTopics.DATA_PROCESSOR_TOPIC, 1, null, message));
    }
}
