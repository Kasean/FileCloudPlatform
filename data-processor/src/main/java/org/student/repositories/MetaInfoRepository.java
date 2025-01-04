package org.student.repositories;

import org.student.dto.MetaInfoDto;

import java.util.Optional;
import java.util.UUID;

public interface MetaInfoRepository {
    UUID save(UUID artefactId, String artefactName, long artefactSize);

    Optional<MetaInfoDto> findByKey(UUID key);

    MetaInfoDto deleteByKey(UUID key);
}
