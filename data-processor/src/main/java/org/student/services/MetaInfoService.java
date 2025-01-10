package org.student.services;

import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.messaging.models.ArtifactMetadataUploadRequest;

import java.util.Optional;
import java.util.UUID;

public interface MetaInfoService {

    UUID saveMetaInfo(ArtifactMetadataUploadRequest request);

    Optional<ExternalMetaInfoDto> readExternalMetaInfo(UUID externalId);

    Optional<InternalMetaInfoDto> readInternalMetaInfoDto(UUID externalId);

    boolean deleteMetaInfo(UUID key);
}
