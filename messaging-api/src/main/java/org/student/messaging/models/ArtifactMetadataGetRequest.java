package org.student.messaging.models;

import java.util.Objects;
import java.util.UUID;

public class ArtifactMetadataGetRequest {
    private UUID userId;
    private UUID artifactId;

    public ArtifactMetadataGetRequest() {
    }

    public ArtifactMetadataGetRequest(UUID userId, UUID artifactId) {
        this.userId = userId;
        this.artifactId = artifactId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(UUID artifactId) {
        this.artifactId = artifactId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ArtifactMetadataGetRequest that = (ArtifactMetadataGetRequest) object;
        return Objects.equals(userId, that.userId) && Objects.equals(artifactId, that.artifactId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, artifactId);
    }
}
