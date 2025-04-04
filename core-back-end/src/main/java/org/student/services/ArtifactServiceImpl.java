package org.student.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.student.api.models.*;
import org.student.exceptions.messaging.UploadDataException;
import org.student.messaging.models.ArtifactMetadataUploadRequest;
import org.student.messaging.models.BaseArtifactMessage;
import org.student.messaging.models.BodyArtifactMessage;
import org.student.messaging.models.ResponseCode;
import org.student.messaging.topics.KafkaTopics;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Service
public class ArtifactServiceImpl implements ArtifactsService{

    private final KafkaRequestReplyService kafkaRequestReplyService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LogManager.getLogger("ArtifactServiceImpl");

    public ArtifactServiceImpl(KafkaRequestReplyService kafkaRequestReplyService) {
        this.kafkaRequestReplyService = kafkaRequestReplyService;
    }

    @Override
    public Mono<ArtifactResponse> upload(ArtifactCreateRequest request){
       var baseArtifactMessage = send("tpc1",request.getArtifactBody(), BaseArtifactMessage.class,null);
       if (baseArtifactMessage.getResponseCode().equals(ResponseCode.CREATED)){
           UUID baseArtifactInternalId = baseArtifactMessage.getInternalId();
           int requestLength = request.getArtifactBody().length;
           var metaData = new ArtifactMetadataUploadRequest(
                           baseArtifactInternalId,
                           request.getName(),
                           (long)requestLength);
           var externalArtefactId = send(
                   KafkaTopics.CrudMeta.SAVE_META_INFO_TOPIC,
                   metaData,
                   UUID.class,
                   Map.of("__TypeId__","artifactMetadataUploadRequest".getBytes(StandardCharsets.UTF_8)));
           if (externalArtefactId!=null){
               return Mono.just(new ArtifactResponse(externalArtefactId,new ArtifactMateInfo(request.getName(),requestLength)));
           }else {
               rollbackArtifact(baseArtifactInternalId);
               logger.error("Error after sending a request to " + KafkaTopics.CrudMeta.SAVE_META_INFO_TOPIC +" topic");
           }
       } else {
           logger.error("Error after sending a request to 'tpc1' topic");
       }
       return Mono.error(()-> new UploadDataException("An error occurred during artifact creation"));
    }

    @Async
    protected void rollbackArtifact(UUID baseArtifactInternalId){
        var bodyArtifactMessage = send("tpd1",baseArtifactInternalId, BodyArtifactMessage.class,null);
        if (bodyArtifactMessage.getResponseCode().equals(ResponseCode.DELETED)){
            System.out.println("DELETED");
        }
    }

    private <T,F> T send(String requestTopic, F requestObject, Class<T> responseClass, Map<String,byte[]> headers){
        String correlationId = UUID.randomUUID().toString();
        try {
            byte[] valueBytes = objectMapper.writeValueAsBytes(requestObject);

            ProducerRecord<String,byte[]> producerRecord =
                    createProducerRecord(requestTopic,valueBytes,headers,correlationId);

            var resultBytes = kafkaRequestReplyService.sendAndReceive(producerRecord,correlationId);
            return objectMapper.readValue(
                    resultBytes,
                    responseClass
            );
        }catch (IOException e){
        throw new RuntimeException(e.getMessage());
        }

    }

    private <T> T send(String requestTopic, byte[] valueBytes, Class<T> responseClass, Map<String,byte[]> headers){
        String correlationId = UUID.randomUUID().toString();
        try {
            ProducerRecord<String,byte[]> producerRecord =
                    createProducerRecord(requestTopic,valueBytes,headers,correlationId);

            var resultBytes = kafkaRequestReplyService.sendAndReceive(producerRecord,correlationId);
            return objectMapper.readValue(
                    resultBytes,
                    responseClass
            );
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private ProducerRecord<String,byte[]> createProducerRecord(String requestTopic, byte[] valueBytes, Map<String,byte[]> headers, String correlationId){
        ProducerRecord<String,byte[]> producerRecord =
                new ProducerRecord<>(requestTopic,correlationId,valueBytes);

        if (headers!=null){
            headers.forEach((key,value)->producerRecord.headers().add(key,value));
        }
        return producerRecord;
    }
    @Override
    public Flux<ArtifactResponse> getAllArtifacts() {
        return null;
    }

    @Override
    public Mono<ArtifactLoadResponse> getArtifactById(UUID id) {
        var internalMetaInfoDto = send(KafkaTopics.CrudMeta.GET_INT_META_INFO_TOPIC,id,InternalMetaInfoDto.class,
                Map.of("__TypeId__","uuid".getBytes(StandardCharsets.UTF_8)));
        if(internalMetaInfoDto!=null){
            var bodyArtifactMessage = send("tpr1",internalMetaInfoDto.internalId(), BodyArtifactMessage.class,null);
            return Mono.just(
                    new ArtifactLoadResponse(
                            id,
                            new ArtifactMateInfo(
                                    internalMetaInfoDto.artifactName(),
                                    internalMetaInfoDto.artifactSize()),
                            bodyArtifactMessage.getArtifactBody()));
        }
        return Mono.error(()-> new IllegalArgumentException("There was an error, check id"));
    }

    @Override
    public Mono<ArtifactResponse> deleteArtifact(UUID id) {
        var internalMetaInfo = send(KafkaTopics.CrudMeta.GET_INT_META_INFO_TOPIC,id, InternalMetaInfoDto.class,Map.of("__TypeId__","uuid".getBytes(StandardCharsets.UTF_8)));
        if (internalMetaInfo!=null){
            boolean metaInfoDeleted = send(KafkaTopics.CrudMeta.DEL_META_INFO,id,Boolean.class,Map.of("__TypeId__","uuid".getBytes(StandardCharsets.UTF_8)));
            if (metaInfoDeleted){
                UUID internalArtifactId = internalMetaInfo.internalId();
                String artifactName = internalMetaInfo.artifactName();
                long artifactSize = internalMetaInfo.artifactSize();

                var bodyArtifactMessage = send("tpd1",internalArtifactId, BodyArtifactMessage.class,null);
                if (bodyArtifactMessage.getResponseCode().equals(ResponseCode.DELETED)){
                    return Mono.just(new ArtifactResponse(id,new ArtifactMateInfo(artifactName, artifactSize)));
                }else {
                    rollBackDeletedMetaInfo(internalArtifactId,artifactName,artifactSize);
                }
            }
        }
        return Mono.error(()->new IllegalArgumentException("There was an error, check id"));
    }

    @Async
    protected void rollBackDeletedMetaInfo(UUID internalId,String artefactName,long artefactSize){
        send(KafkaTopics.CrudMeta.SAVE_META_INFO_TOPIC,
                new ArtifactMetadataUploadRequest(internalId,artefactName,artefactSize),
                UUID.class,
                Map.of("__TypeId__","artifactMetadataUploadRequest".getBytes(StandardCharsets.UTF_8)));
        logger.info("METAINFORMATION RECOVERED");
    }


}
