package org.student.messaging.artifact_processor;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.student.messaging.topics.KafkaTopics;

public class ArtifactProcessorProducer {
    private final KafkaTemplate<String, byte[]> template;

    ArtifactProcessorProducer(KafkaTemplate<String, byte[]> template) {
        this.template = template;
    }

    public void send(byte[] message) {
        template.send(new ProducerRecord<>(KafkaTopics.ARTIFACT_PROCESSOR_TOPIC, 0, null, message));
    }
}
