package org.student.api.models;

import java.util.UUID;

public class ArtifactCreateResponce {
    private UUID id;

    private ArtifactMateInfo metaInfo;

    public ArtifactCreateResponce(UUID id, ArtifactMateInfo metaInfo) {
        this.id = id;
        this.metaInfo = metaInfo;
    }

    public ArtifactCreateResponce() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ArtifactMateInfo getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(ArtifactMateInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    @Override
    public String toString() {
        return "ArtifactCreateResponce{" +
                "id=" + id +
                ", metaInfo=" + metaInfo +
                '}';
    }
}
