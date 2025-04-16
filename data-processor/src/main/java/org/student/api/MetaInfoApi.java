package org.student.api;

import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.exceptions.DataNotFoundException;
import org.student.exceptions.SaveDataException;
import org.student.messaging.models.ArtifactMetadataGetRequest;
import org.student.messaging.models.ArtifactMetadataUploadRequest;
import org.student.messaging.models.UserArtifactMetadataUploadRequest;

import java.util.UUID;

public interface MetaInfoApi {
    @Deprecated
    UUID save(ArtifactMetadataUploadRequest request) throws SaveDataException;
    @Deprecated
    ExternalMetaInfoDto getExternalMeta(UUID id) throws DataNotFoundException;
    @Deprecated
    InternalMetaInfoDto getInternalMeta(UUID id) throws DataNotFoundException;
    @Deprecated
    boolean delete(UUID id);

    UUID save(UserArtifactMetadataUploadRequest request) throws SaveDataException;
    ExternalMetaInfoDto getExternalMeta(ArtifactMetadataGetRequest request) throws DataNotFoundException;
    InternalMetaInfoDto getInternalMeta(ArtifactMetadataGetRequest request) throws DataNotFoundException;
    boolean delete(ArtifactMetadataGetRequest request);
}
