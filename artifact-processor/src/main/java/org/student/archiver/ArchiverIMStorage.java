package org.student.archiver;

import org.student.models.ArtifactMetaInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArchiverIMStorage implements ArchiverRepository{

    private final Map<UUID, String> aliasCache = new HashMap<>();

    @Override
    public void saveArtifactAlias(ArtifactMetaInfo metaInfo, String alias) {
        aliasCache.put(metaInfo.getInternalKeyStoreId(), alias);
    }

    @Override
    public String getArtifactAlias(ArtifactMetaInfo metaInfo) {
        return aliasCache.get(metaInfo.getInternalKeyStoreId());
    }
}
