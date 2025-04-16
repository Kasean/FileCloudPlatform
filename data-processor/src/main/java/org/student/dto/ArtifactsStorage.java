package org.student.dto;

import org.student.utility.MetaInfoMapper;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ArtifactsStorage {
    private final Map<UUID, InternalMetaInfoDto> artifacts = new ConcurrentHashMap<>();

    public UUID save(InternalMetaInfoDto internalMetaInfoDto) {
        UUID externalId = UUID.randomUUID();
        artifacts.put(externalId, internalMetaInfoDto);
        return externalId;
    }

    public ExternalMetaInfoDto getExternalMetaInfo(UUID id) {
        return Optional.ofNullable(artifacts.get(id))
                .map(result -> MetaInfoMapper.toExternalMetaInfoDto(result, id))
                .orElse(null);
    }

    public InternalMetaInfoDto getInternalMetaInfoDto(UUID id) {
        return artifacts.get(id);
    }

    public boolean deleteByKey(UUID artifactId) {
        return artifacts.remove(artifactId)!=null;
    }

    public Map<UUID, InternalMetaInfoDto> getArtifacts() {
        return artifacts;
    }
}
