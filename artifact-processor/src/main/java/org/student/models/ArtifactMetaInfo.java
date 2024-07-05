package org.student.models;

import java.util.UUID;

public class ArtifactMetaInfo {
    private final UUID internalKeyStoreId = UUID.randomUUID();

    public UUID getInternalKeyStoreId() {
        return internalKeyStoreId;
    }
}
