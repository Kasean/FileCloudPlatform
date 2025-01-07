package org.student.repositories;

import org.student.dto.MetaInfoWIthExternalIdDto;
import org.student.dto.MetaInfoWithInternalIdDto;

import java.util.Optional;
import java.util.UUID;

public interface MetaInfoRepository {
    UUID save(MetaInfoWithInternalIdDto metaInfoWithInternalIdDto);

    Optional<MetaInfoWIthExternalIdDto> findByKey(UUID key);

    boolean deleteByKey(UUID key);
}
