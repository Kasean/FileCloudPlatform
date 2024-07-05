package org.student.archiver;

import org.student.models.ArtifactMetaInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ArchiverIMStorage implements ArchiverRepository{

    private final Map<UUID, String> aliasCache = new ConcurrentHashMap<>();

    @Override
    public void saveArtifactAlias(ArtifactMetaInfo metaInfo, String alias) {
        aliasCache.put(metaInfo.getInternalKeyStoreId(), alias);
    }

    @Override
    public Optional<String> getArtifactAlias(ArtifactMetaInfo metaInfo) {
        var alias =  aliasCache.get(metaInfo.getInternalKeyStoreId());

        return alias == null ? Optional.empty() : Optional.of(alias);
    }
}
