package org.student.messaging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.student.api.MetaInfoApi;
import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.exceptions.DataNotFoundException;
import org.student.exceptions.SaveDataException;
import org.student.messaging.models.ArtifactMetadataUploadRequest;
import org.student.messaging.topics.KafkaTopics;
import org.student.services.MetaInfoServiceImpl;

import java.util.UUID;

@Service
public class MessageConsumer implements MetaInfoApi {

    private final MetaInfoServiceImpl metaInfoService;

    private final MessageProducer messageProducer;

    private static final Logger logger = LogManager.getLogger("MessageConsumer");

    public MessageConsumer(MetaInfoServiceImpl metaInfoService, MessageProducer messageProducer) {
        this.metaInfoService = metaInfoService;
        this.messageProducer = messageProducer;
    }

    /**
     * Sends a message to the topic with the mapping key: uuid
     * */
    @KafkaListener(id = "data-processor-save-listener", topicPartitions = @TopicPartition(topic = KafkaTopics.CrudMeta.SAVE_META_INFO_TOPIC, partitions = {"0","1"}), groupId = "save-meta-group")
    @Override
    public UUID save(@Payload ArtifactMetadataUploadRequest request) throws SaveDataException {
        logger.info("Received request to save metadata: {}", request);
        try {
            UUID id = metaInfoService.saveMetaInfo(request);
            logger.info("Metadata saved successfully with ID: {}", id);
            messageProducer.sendUUID(id,KafkaTopics.ResponseMeta.SAVE_RESPONSE_TOPIC);
            return id;
        } catch (Exception e) {
            logger.error("Error occurred while saving metadata: {}", e.getMessage(), e);
            throw new SaveDataException("An error occurred during saving, check your request");
        }
    }

    /**
     * Sends a message to the topic with the mapping key: externalMeta
     * */
    @KafkaListener(id = "data-processor-get-ext-listener", topicPartitions = @TopicPartition(topic = KafkaTopics.CrudMeta.GET_EXT_META_INFO_TOPIC, partitions = {"0","1"}), groupId = "get-ext-meta-group")
    @Override
    public ExternalMetaInfoDto getExternalMeta(@Payload UUID id) throws DataNotFoundException {
        logger.info("Received request to get external metadata for ID: {}", id);
        ExternalMetaInfoDto metaInfoDto = metaInfoService.readExternalMetaInfo(id)
                .orElseThrow(() -> {
                    logger.warn("No external metadata found for ID: {}", id);
                    return new DataNotFoundException("Error getting external meta-information by id");
                });
        try {
            messageProducer.sendExternalMeta(metaInfoDto,KafkaTopics.ResponseMeta.GET_EXT_INFO_RESPONSE_TOPIC);
        }catch (Exception e){
            logger.error("Error occurred while sending ex-metadata: {}", e.getMessage(), e);
            throw new DataNotFoundException("An unexpected error occurred while getting external meta-information");
        }
        return metaInfoDto;
    }

    /**
     * Sends a message to the topic with the mapping key: internalMeta
     * */
    @KafkaListener(id = "data-process-get-int-listener", topicPartitions = @TopicPartition(topic = KafkaTopics.CrudMeta.GET_INT_META_INFO_TOPIC, partitions = {"0","1"}), groupId = "get-int-meta-group")
    @Override
    public InternalMetaInfoDto getInternalMeta(@Payload UUID id) throws DataNotFoundException {
        logger.info("Received request to get internal metadata for ID: {}", id);
        InternalMetaInfoDto metaInfoDto = metaInfoService.readInternalMetaInfoDto(id)
                .orElseThrow(() -> {
                    logger.warn("No internal metadata found for ID: {}", id);
                    return new DataNotFoundException("Error getting internal meta-information by id");
                });
        try {
            messageProducer.sendInternalMeta(metaInfoDto,KafkaTopics.ResponseMeta.GET_INT_INFO_RESPONSE_TOPIC);
        }catch (Exception e){
            logger.error("Error occurred while sending in-metadata: {}", e.getMessage(), e);
            throw new DataNotFoundException("An unexpected error occurred while getting external meta-information");
        }
        return metaInfoDto;
    }

    /**
     * Sends a message to the topic with the mapping key: delResult
     * */
    @KafkaListener(id = "data-processor-del-listener", topicPartitions = @TopicPartition(topic = KafkaTopics.CrudMeta.DEL_META_INFO, partitions = {"0","1"}), groupId = "del-meta-group")
    @Override
    public boolean delete(@Payload UUID id) {
        logger.info("Received request to delete metadata for ID: {}", id);
        boolean result = metaInfoService.deleteMetaInfo(id);
        if (result) {
            logger.info("Metadata deleted successfully for ID: {}", id);
        } else {
            logger.warn("Failed to delete metadata for ID: {}", id);
        }
        try {
            messageProducer.sendBoolean(result,KafkaTopics.ResponseMeta.DEL_RESPONSE_TOPIC);
        } catch (Exception e) {
            logger.error("Error occurred while sending result of del-metadata: {}", e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while deleting meta-information");
        }

        return result;
    }
}
