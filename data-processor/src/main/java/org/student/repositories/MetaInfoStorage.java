package org.student.repositories;

import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.utility.MetaInfoMapper;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MetaInfoStorage implements MetaInfoRepository{

    private final Map<UUID, InternalMetaInfoDto> internalStorage = new ConcurrentHashMap<>();

    @Override
    public UUID save(InternalMetaInfoDto internalMetaInfoDto) {
        UUID externalId = UUID.randomUUID();
        internalStorage.put(externalId, internalMetaInfoDto);
        return externalId;
    }

    @Override
    public Optional<ExternalMetaInfoDto> getExternalMetaInfo(UUID externalId) {
        var result = internalStorage.get(externalId);
        return result == null ? Optional.empty() :
                Optional.of(
                        MetaInfoMapper.toExternalMetaInfoDto(result,externalId));
    }

    @Override
    public Optional<InternalMetaInfoDto> getInternalMetaInfoDto(UUID externalId) {
        var result = internalStorage.get(externalId);
        return result == null ? Optional.empty() :
                Optional.of(result);
    }

    @Override
    public boolean deleteByKey(UUID key) {
        return internalStorage.remove(key)!=null;
    }
}
