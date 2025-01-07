package org.student.repositories;

import org.student.dto.MetaInfoWIthExternalIdDto;
import org.student.dto.MetaInfoWithInternalIdDto;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MetaInfoStorage implements MetaInfoRepository{

    private final Map<UUID, MetaInfoWithInternalIdDto> internalStorage = new ConcurrentHashMap<>();

    @Override
    public UUID save(MetaInfoWithInternalIdDto metaInfoWithInternalIdDto) {
        UUID externalId = UUID.randomUUID();
        internalStorage.put(externalId, metaInfoWithInternalIdDto);
        return externalId;
    }

    @Override
    public Optional<MetaInfoWIthExternalIdDto> findByKey(UUID externalId) {
        var result = internalStorage.get(externalId);
        return result == null ? Optional.empty() :
                Optional.of(new MetaInfoWIthExternalIdDto(
                        externalId,
                        result.artifactName(),
                        result.artifactSize()));
    }

    @Override
    public boolean deleteByKey(UUID key) {
        return internalStorage.remove(key)!=null;
    }
}
