package org.student.services;

import org.student.dto.MetaInfoDto;
import org.student.messaging.models.ArtifactMetadataUploadRequest;

import java.util.Optional;
import java.util.UUID;

public interface MetaInfoService {

    UUID saveMetaInfo(ArtifactMetadataUploadRequest request);

    Optional<MetaInfoDto> readMetaInfo(UUID key);

    MetaInfoDto deleteMetaInfo(UUID key);
}
