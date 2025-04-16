package org.student.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.messaging.models.ArtifactMetadataGetRequest;
import org.student.messaging.models.ArtifactMetadataUploadRequest;
import org.student.messaging.models.UserArtifactMetadataUploadRequest;
import org.student.repositories.MetaInfoStorage;
import org.student.utility.MetaInfoMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MetaInfoServiceImpl implements MetaInfoService{

    private static final Logger logger = LogManager.getLogger("MetaInfoServiceImpl");
    private final MetaInfoStorage storage;

    public MetaInfoServiceImpl(MetaInfoStorage storage) { //Added a class constructor for easier writing of tests
        this.storage = storage;
    }

    @Override
    public UUID saveMetaInfo(ArtifactMetadataUploadRequest request) {
        logger.info("Method saveMetaInfo was called");
        return storage.save(
                MetaInfoMapper.toInternalMetaInfoDto(request));
    }

    @Override
    public Optional<ExternalMetaInfoDto> readExternalMetaInfo(UUID id) {
        Optional<ExternalMetaInfoDto> info = storage.getExternalMetaInfo(id);
        if (info.isEmpty()){
            logger.warn("No information about external meta-info was found");
        }
        return info;
    }

    @Override
    public Optional<InternalMetaInfoDto> readInternalMetaInfoDto(UUID id) {
        Optional<InternalMetaInfoDto> info = storage.getInternalMetaInfoDto(id);
        if (info.isEmpty()){
            logger.warn("No information about internal meta-info was found");
        }
        return info;
    }

    @Override
    public boolean deleteMetaInfo(UUID key) {
        logger.info("Method deleteMetaInfo was called");
        return storage.deleteByKey(key);
    }

    @Override
    public UUID saveMetaInfo(UserArtifactMetadataUploadRequest request) {
        logger.info("Method saveMetaInfo was called");
        return storage.save(request.getUserId(),
                MetaInfoMapper.toInternalMetaInfoDto(request));
    }

    @Override
    public Optional<ExternalMetaInfoDto> readExternalMetaInfo(ArtifactMetadataGetRequest request) {
        Optional<ExternalMetaInfoDto> info = storage.getExternalMetaInfo(request.getUserId(),request.getArtifactId());
        if (info.isEmpty()){
            logger.warn("No information about external meta-info was found");
        }
        return info;
    }

    @Override
    public Optional<InternalMetaInfoDto> readInternalMetaInfoDto(ArtifactMetadataGetRequest request) {
        Optional<InternalMetaInfoDto> info = storage.getInternalMetaInfoDto(request.getUserId(),request.getArtifactId());
        if (info.isEmpty()){
            logger.warn("No information about internal meta-info was found");
        }
        return info;
    }

    @Override
    public List<ExternalMetaInfoDto> getAll(UUID userId) {
        List<ExternalMetaInfoDto> userArtifacts = storage.getAll(userId);
        if (userArtifacts.isEmpty()){
            logger.info("The user's artifact list is empty");
        }
        return userArtifacts;
    }

    @Override
    public boolean deleteMetaInfo(ArtifactMetadataGetRequest request) {
        logger.info("Method deleteMetaInfo was called");
        return storage.deleteByKey(request.getUserId(),request.getArtifactId());
    }
}
