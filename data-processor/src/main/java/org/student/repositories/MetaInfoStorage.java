package org.student.repositories;

import org.springframework.stereotype.Repository;
import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.utility.MetaInfoMapper;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MetaInfoStorage implements MetaInfoRepository{
    private final Map<UUID, Map<UUID, InternalMetaInfoDto>> artifacts = new ConcurrentHashMap<>();
    private final Map<UUID, InternalMetaInfoDto> internalStorage = new ConcurrentHashMap<>();

    @Override
    public UUID save(InternalMetaInfoDto internalMetaInfoDto) {
        UUID externalId = UUID.randomUUID();
        internalStorage.put(externalId, internalMetaInfoDto);
        return externalId;
    }

    /**
     * Reads artifact metadata and returns a DTO with data and external id.
     *
     * @param id external metadata identifier.
     * @return {@link Optional} containing {@link ExternalMetaInfoDto} if found,
     * or an empty {@link Optional} if no metadata is associated with this identifier
     */
    @Override
    public Optional<ExternalMetaInfoDto> getExternalMetaInfo(UUID id) {
        var result = internalStorage.get(id);
        return result == null ? Optional.empty() :
                Optional.of(
                        MetaInfoMapper.toExternalMetaInfoDto(result,id));
    }

    /**
     * Reads artifact metadata and returns a DTO with data and internal id.
     *
     * @param id external metadata identifier.
     * @return {@link Optional} containing {@link InternalMetaInfoDto} if found,
     * or an empty {@link Optional} if no metadata is associated with this identifier
     */
    @Override
    public Optional<InternalMetaInfoDto> getInternalMetaInfoDto(UUID id) {
        var result = internalStorage.get(id);
        return result == null ? Optional.empty() :
                Optional.of(result);
    }

    @Override
    public boolean deleteByKey(UUID key) {
        return internalStorage.remove(key)!=null;
    }

    @Override
    public UUID save(UUID userId, InternalMetaInfoDto internalMetaInfoDto) {
        UUID externalId = UUID.randomUUID();

        Map<UUID, InternalMetaInfoDto> userArtifacts =
                artifacts.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());

        userArtifacts.put(externalId, internalMetaInfoDto);

        return externalId;
    }

    @Override
    public Optional<ExternalMetaInfoDto> getExternalMetaInfo(UUID userId, UUID externalId) {
        return Optional.ofNullable(artifacts.get(userId))
                .map(innerMap -> innerMap.get(externalId))
                .flatMap(dto -> Optional.of(MetaInfoMapper.toExternalMetaInfoDto(dto, externalId)));
    }


    @Override
    public Optional<InternalMetaInfoDto> getInternalMetaInfoDto(UUID userId, UUID externalId) {
        return Optional.ofNullable(artifacts.get(userId))
                .map(innerMap -> innerMap.get(externalId));
    }

    @Override
    public boolean deleteByKey(UUID userId, UUID externalId) {
        Map<UUID, InternalMetaInfoDto> userArtifacts = artifacts.get(userId);
        if (userArtifacts == null) {
            return false;
        }
        return userArtifacts.remove(externalId) != null;
    }

}
