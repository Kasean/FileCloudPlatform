package org.student.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.student.api.MetaInfoApi;
import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.exceptions.DataNotFoundException;
import org.student.exceptions.SaveDataException;
import org.student.messaging.models.ArtifactMetadataUploadRequest;
import org.student.messaging.topics.KafkaTopics;
import org.student.services.MetaInfoServiceImpl;

import java.util.Optional;
import java.util.UUID;

@Service
public class MessageConsumer implements MetaInfoApi {

    private final MetaInfoServiceImpl metaInfoService;

    public MessageConsumer(MetaInfoServiceImpl metaInfoService) {
        this.metaInfoService = metaInfoService;
    }

    @KafkaListener(id = "data-processor-save-listener", topicPartitions = @TopicPartition(topic = KafkaTopics.CrudMeta.SAVE_META_INFO_TOPIC, partitions = {"0","1"}), groupId = "save-meta-group")
    @SendTo(KafkaTopics.ResponseMeta.SAVE_RESPONSE_TOPIC)
    @Override
    public UUID save(ArtifactMetadataUploadRequest request) throws SaveDataException {
        try {
            return metaInfoService.saveMetaInfo(request);
        }catch (Exception e){
            throw new SaveDataException(); //It's a temporary solution
        }
    }

    @KafkaListener(id = "data-processor-get-ext-listener", topicPartitions = @TopicPartition(topic = KafkaTopics.CrudMeta.GET_EXT_META_INFO_TOPIC, partitions = {"0","1"}), groupId = "get-ext-meta-group")
    @SendTo(KafkaTopics.ResponseMeta.GET_EXT_INFO_RESPONSE_TOPIC)
    @Override
    public Optional<ExternalMetaInfoDto> getExternalMeta(UUID id) throws DataNotFoundException {
        var result = metaInfoService.readExternalMetaInfo(id);
        if (result.isEmpty()){
            throw new DataNotFoundException(); //It's a temporary solution
        }return result;
    }

    @KafkaListener(id = "data-process-get-int-listener", topicPartitions = @TopicPartition(topic = KafkaTopics.CrudMeta.GET_INT_META_INFO_TOPIC, partitions = {"0","1"}), groupId = "get-int-meta-group")
    @SendTo(KafkaTopics.ResponseMeta.GET_INT_INFO_RESPONSE_TOPIC)
    @Override
    public Optional<InternalMetaInfoDto> getInternalMeta(UUID id) throws DataNotFoundException {
        var result = metaInfoService.readInternalMetaInfoDto(id);
        if (result.isEmpty()){
            throw new DataNotFoundException(); //It's a temporary solution
        }else return result;
    }

    @KafkaListener(id = "data-processor-del-listener", topicPartitions = @TopicPartition(topic = KafkaTopics.CrudMeta.DEL_META_INFO, partitions = {"0","1"}), groupId = "del-meta-group")
    @SendTo(KafkaTopics.ResponseMeta.DEL_RESPONSE_TOPIC)
    @Override
    public boolean delete(UUID id) {
        return metaInfoService.deleteMetaInfo(id);
    }
}
