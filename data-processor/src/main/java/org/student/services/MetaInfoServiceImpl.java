package org.student.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.messaging.models.ArtifactMetadataUploadRequest;
import org.student.repositories.MetaInfoStorage;
import org.student.utility.MetaInfoMapper;

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
}
