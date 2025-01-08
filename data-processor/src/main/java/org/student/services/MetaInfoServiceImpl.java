package org.student.services;

import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.messaging.models.ArtifactMetadataUploadRequest;
import org.student.repositories.MetaInfoStorage;
import org.student.utility.MetaInfoMapper;

import java.util.Optional;
import java.util.UUID;

public class MetaInfoServiceImpl implements MetaInfoService{

    private final MetaInfoStorage storage;

    public MetaInfoServiceImpl(MetaInfoStorage storage) { //Added a class constructor for easier writing of tests
        this.storage = storage;
    }

    @Override
    public UUID saveMetaInfo(ArtifactMetadataUploadRequest request) {
        return storage.save(
                MetaInfoMapper.toInternalMetaInfoDto(request));
    }

    @Override
    public Optional<ExternalMetaInfoDto> readExternalMetaInfo(UUID id) {
        return storage.getExternalMetaInfo(id);
    }

    @Override
    public Optional<InternalMetaInfoDto> readInternalMetaInfoDto(UUID id) {
        return storage.getInternalMetaInfoDto(id);
    }

    @Override
    public boolean deleteMetaInfo(UUID key) {
        return storage.deleteByKey(key);
    }
}
