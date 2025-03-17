package org.student.api.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.student.services.ArtifactsServiceImpl;

import java.io.IOException;
import java.util.UUID;

@Service
public class Consumer {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ArtifactsServiceImpl artifactsService;

    public Consumer(ArtifactsServiceImpl artifactsService) {
        this.artifactsService = artifactsService;
    }

    @KafkaListener(id="artifact-upload-listener",topicPartitions = @TopicPartition(topic = "tpc1", partitions = {"0","1"}),groupId = "upload-group-id")
    public void uploadListen(ConsumerRecord<String,byte[]> record, @Header("messageType") String messageType){
        if (!"request".equalsIgnoreCase(messageType)){
            return;
        }
        artifactsService.createArtifactMessage(record.key(), record.value(), record.topic());
    }

    @KafkaListener(id="artifact-delete-listener",topicPartitions = @TopicPartition(topic = "tpd1", partitions = {"0","1"}),groupId = "delete-group-id")
    public void deleteListen(ConsumerRecord<String,byte[]> record, @Header("messageType") String messageType) throws IOException {
        if (!"request".equalsIgnoreCase(messageType)){
            return;
        }
        artifactsService.deleteArtifactMessage(record.key(), objectMapper.readValue(record.value(), UUID.class),record.topic());
    }

    @KafkaListener(id="artifact-read-listener",topicPartitions = @TopicPartition(topic = "tpr1", partitions = {"0","1"}),groupId = "read-group-id")
    public void readListen(ConsumerRecord<String,byte[]> record, @Header("messageType") String messageType) throws IOException {
        if (!"request".equalsIgnoreCase(messageType)){
            return;
        }
        artifactsService.readArtifactMessage(record.key(), objectMapper.readValue(record.value(), UUID.class),record.topic());
    }
}
