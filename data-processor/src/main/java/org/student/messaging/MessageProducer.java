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

    public void sendArtifact(ArtifactMetadataUploadRequest request, String topic) throws JsonProcessingException, ExecutionException, InterruptedException, KafkaSendException {
        sendWithToken(request,topic,"artifactMetadataUploadRequest");
    }

    public void sendUUID(UUID uuid, String topic) throws JsonProcessingException, ExecutionException, InterruptedException, KafkaSendException {
        sendWithToken(uuid,topic,"uuid");
    }

    public void sendExternalMeta(ExternalMetaInfoDto exMetaInfoDto, String topic) throws ExecutionException, JsonProcessingException, InterruptedException, KafkaSendException {
        sendWithToken(exMetaInfoDto,topic,"externalMeta");
    }

    public void sendInternalMeta(InternalMetaInfoDto inMetaInfoDto, String topic) throws ExecutionException, JsonProcessingException, InterruptedException, KafkaSendException {
        sendWithToken(inMetaInfoDto,topic,"internalMeta");
    }

    public void sendBoolean(Boolean bool, String topic) throws ExecutionException, JsonProcessingException, InterruptedException, KafkaSendException {
        sendWithToken(bool,topic,"delResult");
    }

    private <T> void sendWithoutToken(T data, String topic) throws JsonProcessingException, ExecutionException, InterruptedException, KafkaSendException {
        send(data,topic,null);
    }

    private <T> void sendWithToken(T data, String topic, String deserializerTokenName) throws JsonProcessingException, ExecutionException, InterruptedException, KafkaSendException {
        send(data,topic,Map.of("__TypeId__",deserializerTokenName));
    }

    private <T> void send(T data, String topic, Map<String,String> headers) throws JsonProcessingException, ExecutionException, InterruptedException, KafkaSendException {
        byte[] message = objectMapper.writeValueAsBytes(data);

        ProducerRecord<String, byte[]> record = recordFactory(message,topic);
        if (headers!=null){
            headers.forEach((key,value)->record.headers().add(key,value.getBytes()));
        }

        if (template.send(record).get()==null){
            throw new KafkaSendException("An error occurred while sending a message");
        }
    }

    private ProducerRecord<String, byte[]> recordFactory(byte[] data,String topic){
        return new ProducerRecord<>(
                topic,
                null,
                data
        );
    }
}
