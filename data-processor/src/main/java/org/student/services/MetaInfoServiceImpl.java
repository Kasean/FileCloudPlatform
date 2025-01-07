package org.student.services;

import org.student.dto.MetaInfoWIthExternalIdDto;
import org.student.dto.MetaInfoWithInternalIdDto;
import org.student.messaging.models.ArtifactMetadataUploadRequest;
import org.student.repositories.MetaInfoStorage;

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
                new MetaInfoWithInternalIdDto(
                        request.getArtefactId(),
                        request.getArtefactName(),
                        request.getArtefactSize()));
    }

    @Override
    public Optional<MetaInfoWIthExternalIdDto> readMetaInfo(UUID externalId) {
        return storage.findByKey(externalId);
    }

    @Override
    public boolean deleteMetaInfo(UUID key) {
        return storage.deleteByKey(key);
    }
}
