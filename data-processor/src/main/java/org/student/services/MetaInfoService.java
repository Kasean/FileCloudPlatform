package org.student.services;

import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.messaging.models.ArtifactMetadataGetRequest;
import org.student.messaging.models.ArtifactMetadataUploadRequest;
import org.student.messaging.models.UserArtifactMetadataUploadRequest;

import java.util.Optional;
import java.util.UUID;

public interface MetaInfoService {
    @Deprecated
    UUID saveMetaInfo(ArtifactMetadataUploadRequest request);
    @Deprecated
    Optional<ExternalMetaInfoDto> readExternalMetaInfo(UUID externalId);
    @Deprecated
    Optional<InternalMetaInfoDto> readInternalMetaInfoDto(UUID externalId);
    @Deprecated
    boolean deleteMetaInfo(UUID key);

    UUID saveMetaInfo(UserArtifactMetadataUploadRequest request);
    Optional<ExternalMetaInfoDto> readExternalMetaInfo(ArtifactMetadataGetRequest request);
    Optional<InternalMetaInfoDto> readInternalMetaInfoDto(ArtifactMetadataGetRequest request);
    boolean deleteMetaInfo(ArtifactMetadataGetRequest request);
}
