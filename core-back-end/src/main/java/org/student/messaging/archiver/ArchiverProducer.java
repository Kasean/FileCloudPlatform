package org.student.messaging.archiver;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.student.messaging.topics.KafkaTopics;

@Service
public class ArchiverProducer {

    private final KafkaTemplate<String, byte[]> template;

    ArchiverProducer(KafkaTemplate<String, byte[]> template) {
        this.template = template;
    }

    public void send(byte[] message) {
        template.send(new ProducerRecord<>(KafkaTopics.ARCHIVER_TOPIC, 0, null, message));
    }

}
