package org.student.api.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.student.messaging.models.BaseArtifactMessage;

import java.nio.charset.StandardCharsets;

@Component
public class MessageProducerImpl<T extends BaseArtifactMessage> implements MessageProducer<T> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaTemplate<String,byte[]> kafkaTemplate;

    public MessageProducerImpl(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String key, String topic, T message) {
        try {
            ProducerRecord<String,byte[]> producerRecord = new ProducerRecord<>(topic,key,objectMapper.writeValueAsBytes(message));
            producerRecord.headers().add("messageType","response".getBytes(StandardCharsets.UTF_8));
            kafkaTemplate.send(producerRecord);
            System.out.println("message "+producerRecord+" was sent");
        }catch (JsonProcessingException e){
            throw new RuntimeException(e.getMessage());
        }

    }
}
