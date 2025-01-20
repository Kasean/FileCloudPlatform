package org.student.api;

import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.exceptions.DataNotFoundException;
import org.student.exceptions.SaveDataException;
import org.student.messaging.models.ArtifactMetadataUploadRequest;

import java.util.Optional;
import java.util.UUID;

public interface MetaInfoApi {
    UUID save(ArtifactMetadataUploadRequest request) throws SaveDataException;

    ExternalMetaInfoDto getExternalMeta(UUID id) throws DataNotFoundException;

    InternalMetaInfoDto getInternalMeta(UUID id) throws DataNotFoundException;


    boolean delete(UUID id);
}
