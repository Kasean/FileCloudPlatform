package org.student.repositories;

import org.student.dto.MetaInfoDto;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MetaInfoStorage implements MetaInfoRepository{

    private final Map<UUID, MetaInfoDto> internalStorage = new ConcurrentHashMap<>();

    @Override
    public UUID save(UUID artefactId, String artefactName, long artefactSize) {
        internalStorage.put(artefactId, new MetaInfoDto(artefactName, artefactSize));
        return artefactId;
    }

    @Override
    public Optional<MetaInfoDto> findByKey(UUID key) {
        var result = internalStorage.get(key);
        return result == null ? Optional.empty() : Optional.of(result);
    }

    @Override
    public MetaInfoDto deleteByKey(UUID key) {
        return internalStorage.remove(key);
    }
}
