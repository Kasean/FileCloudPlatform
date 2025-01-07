package org.student.services;

import org.student.dto.MetaInfoWIthExternalIdDto;
import org.student.messaging.models.ArtifactMetadataUploadRequest;

import java.util.Optional;
import java.util.UUID;

public interface MetaInfoService {

    UUID saveMetaInfo(ArtifactMetadataUploadRequest request);

    Optional<MetaInfoWIthExternalIdDto> readMetaInfo(UUID key);

    boolean deleteMetaInfo(UUID key);
}
