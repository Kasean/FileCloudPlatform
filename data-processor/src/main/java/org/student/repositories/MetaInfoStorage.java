package org.student.repositories;

import org.springframework.stereotype.Repository;
import org.student.dto.ArtifactsStorage;
import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.utility.MetaInfoMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MetaInfoStorage implements MetaInfoRepository{
    private final Map<UUID, ArtifactsStorage> artifacts = new ConcurrentHashMap<>();
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
        ArtifactsStorage artifactsStorage = artifacts.computeIfAbsent(userId, key -> new ArtifactsStorage());

        return artifactsStorage.save(internalMetaInfoDto);
    }


    @Override
    public Optional<ExternalMetaInfoDto> getExternalMetaInfo(UUID userId, UUID externalId) {
        return Optional.ofNullable(artifacts.get(userId))
                .map(artifactsStorage -> artifactsStorage.getExternalMetaInfo(externalId));
    }


    @Override
    public Optional<InternalMetaInfoDto> getInternalMetaInfoDto(UUID userId, UUID externalId) {
        return Optional.ofNullable(artifacts.get(userId))
                .map(innerMap -> innerMap.getInternalMetaInfoDto(externalId));
    }

    @Override
    public List<ExternalMetaInfoDto> getAll(UUID userId) {
        ArtifactsStorage artifactsStorage = artifacts.get(userId);
        if (artifactsStorage == null) {
            return List.of();
        }

        return artifactsStorage.getArtifacts().entrySet().stream()
                .map(entry -> MetaInfoMapper.toExternalMetaInfoDto(entry.getValue(),entry.getKey()))
                .toList();
    }

    @Override
    public boolean deleteByKey(UUID userId, UUID externalId) {
        ArtifactsStorage userArtifacts = artifacts.get(userId);
        if (userArtifacts == null) {
            return false;
        }
        return userArtifacts.deleteByKey(externalId);
    }

}
