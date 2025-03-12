package org.student.repositories;

import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;

import java.util.Optional;
import java.util.UUID;

public interface MetaInfoRepository {
    UUID save(InternalMetaInfoDto internalMetaInfoDto);

    Optional<ExternalMetaInfoDto> getExternalMetaInfo(UUID externalId);

    Optional<InternalMetaInfoDto> getInternalMetaInfoDto(UUID externalId);


    boolean deleteByKey(UUID key);
}
