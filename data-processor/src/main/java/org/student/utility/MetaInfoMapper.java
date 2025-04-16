package org.student.utility;

import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.messaging.models.ArtifactMetadataUploadRequest;
import org.student.messaging.models.UserArtifactMetadataUploadRequest;

import java.util.UUID;

public class MetaInfoMapper {
    private MetaInfoMapper(){}

    @Deprecated
    public static InternalMetaInfoDto toInternalMetaInfoDto(ArtifactMetadataUploadRequest request){
        return new InternalMetaInfoDto(
                request.getArtefactId(),
                request.getArtefactName(),
                request.getArtefactSize());
    }

    public static InternalMetaInfoDto toInternalMetaInfoDto(UserArtifactMetadataUploadRequest request){
        return new InternalMetaInfoDto(
                request.getArtefactId(),
                request.getArtefactName(),
                request.getArtefactSize());
    }

    public static ExternalMetaInfoDto toExternalMetaInfoDto(InternalMetaInfoDto internalMetaInfoDto, UUID externalId){
        return new ExternalMetaInfoDto(
                externalId,
                internalMetaInfoDto.artifactName(),
                internalMetaInfoDto.artifactSize());
    }
}
