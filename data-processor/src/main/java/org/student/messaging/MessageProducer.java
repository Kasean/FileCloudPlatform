package org.student.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.exceptions.messaging.KafkaSendException;
import org.student.messaging.models.ArtifactMetadataUploadRequest;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class MessageProducer {
    private final KafkaTemplate<String, byte[]> template;
    private final ObjectMapper objectMapper = new ObjectMapper();

    MessageProducer(KafkaTemplate<String, byte[]> template) {
        this.template = template;
}

    public void sendArtifact(ArtifactMetadataUploadRequest request, String topic, String key) throws JsonProcessingException, ExecutionException, InterruptedException, KafkaSendException {
        sendWithToken(request,topic,key,"artifactMetadataUploadRequest");
    }

    public void sendUUID(UUID uuid, String topic, String key) throws JsonProcessingException, ExecutionException, InterruptedException, KafkaSendException {
        sendWithToken(uuid,topic,key,"uuid");
    }

    public void sendExternalMeta(ExternalMetaInfoDto exMetaInfoDto, String topic, String key) throws ExecutionException, JsonProcessingException, InterruptedException, KafkaSendException {
        sendWithToken(exMetaInfoDto,topic,key,"externalMeta");
    }

    public void sendInternalMeta(InternalMetaInfoDto inMetaInfoDto, String topic, String key) throws ExecutionException, JsonProcessingException, InterruptedException, KafkaSendException {
        sendWithToken(inMetaInfoDto,topic,key,"internalMeta");
    }

    public void sendBoolean(Boolean bool, String topic, String key) throws ExecutionException, JsonProcessingException, InterruptedException, KafkaSendException {
        sendWithToken(bool,topic,key,"delResult");
    }

    public void sendError(String topic,String key){
        try {
            sendWithoutToken(null,topic,key);
        }catch (ExecutionException | JsonProcessingException | InterruptedException | KafkaSendException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private <T> void sendWithoutToken(T data, String topic,String key) throws JsonProcessingException, ExecutionException, InterruptedException, KafkaSendException {
        send(data,topic,key,null);
    }

    private <T> void sendWithToken(T data, String topic, String recordKey, String deserializerTokenName) throws JsonProcessingException, ExecutionException, InterruptedException, KafkaSendException {
        send(data,topic,recordKey,
                Map.of("__TypeId__",deserializerTokenName));
    }

    private <T> void send(T data, String topic, String recordKey, Map<String,String> headers) throws JsonProcessingException, ExecutionException, InterruptedException, KafkaSendException {
        byte[] message = objectMapper.writeValueAsBytes(data);

        ProducerRecord<String, byte[]> record = recordFactory(message,topic,recordKey);
        if (headers!=null){
            headers.forEach((key,value)-> record.headers().add(key,value.getBytes()));
        }
        record.headers().add("messageType","response".getBytes(StandardCharsets.UTF_8));

        System.out.println(record);
        if (template.send(record).get()==null){
            throw new KafkaSendException("An error occurred while sending a message");
        }
    }

    private ProducerRecord<String, byte[]> recordFactory(byte[] data,String topic,String key){
        return new ProducerRecord<>(
                topic,
                key,
                data
        );
    }
}
