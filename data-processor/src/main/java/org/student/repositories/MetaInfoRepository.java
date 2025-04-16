package org.student.repositories;

import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;

import java.util.Optional;
import java.util.UUID;

public interface MetaInfoRepository {
    @Deprecated
    UUID save(InternalMetaInfoDto internalMetaInfoDto);
    @Deprecated
    Optional<ExternalMetaInfoDto> getExternalMetaInfo(UUID externalId);
    @Deprecated
    Optional<InternalMetaInfoDto> getInternalMetaInfoDto(UUID externalId);
    @Deprecated
    boolean deleteByKey(UUID key);

    UUID save(UUID userId,InternalMetaInfoDto internalMetaInfoDto);
    Optional<ExternalMetaInfoDto> getExternalMetaInfo(UUID userId,UUID externalId);
    Optional<InternalMetaInfoDto> getInternalMetaInfoDto(UUID userId,UUID externalId);
    boolean deleteByKey(UUID userId,UUID externalId);
}
