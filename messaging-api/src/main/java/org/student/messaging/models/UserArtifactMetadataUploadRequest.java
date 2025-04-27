package org.student.messaging.models;

import java.util.Objects;
import java.util.UUID;

public class UserArtifactMetadataUploadRequest extends ArtifactMetadataUploadRequest{
    private UUID userId;

    public UserArtifactMetadataUploadRequest() {
    }

    public UserArtifactMetadataUploadRequest(String artefactName, UUID artefactId, Long artefactSize, UUID userId) {
        super(artefactId,artefactName,artefactSize);
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        UserArtifactMetadataUploadRequest that = (UserArtifactMetadataUploadRequest) object;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId);
    }
}
