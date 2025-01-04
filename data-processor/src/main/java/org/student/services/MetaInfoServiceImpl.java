package org.student.services;

import org.student.dto.MetaInfoDto;
import org.student.messaging.models.ArtifactMetadataUploadRequest;
import org.student.repositories.MetaInfoStorage;

import java.util.Optional;
import java.util.UUID;

public class MetaInfoServiceImpl implements MetaInfoService{

    private final MetaInfoStorage storage = new MetaInfoStorage();

    @Override
    public UUID saveMetaInfo(ArtifactMetadataUploadRequest request) {
        UUID artefactId = request.getArtefactId();
        if (storage.findByKey(artefactId).isEmpty()){
            return storage.save(
                    artefactId,
                    request.getArtefactName(),
                    request.getArtefactSize());
        }
        return null; //or throw exception?
    }

    @Override
    public Optional<MetaInfoDto> readMetaInfo(UUID key) {
        return storage.findByKey(key);
    }

    @Override
    public MetaInfoDto deleteMetaInfo(UUID key) {
        return storage.deleteByKey(key);
    }
}
