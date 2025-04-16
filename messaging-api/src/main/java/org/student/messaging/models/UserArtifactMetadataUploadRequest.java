package org.student.messaging.models;

import java.util.Objects;
import java.util.UUID;

public class UserArtifactMetadataUploadRequest {
    private String artefactName;
    private UUID artefactId;
    private Long artefactSize;
    private UUID userId;

    public UserArtifactMetadataUploadRequest() {
    }

    public UserArtifactMetadataUploadRequest(String artefactName, UUID artefactId, Long artefactSize, UUID userId) {
        this.artefactName = artefactName;
        this.artefactId = artefactId;
        this.artefactSize = artefactSize;
        this.userId = userId;
    }

    public String getArtefactName() {
        return artefactName;
    }

    public void setArtefactName(String artefactName) {
        this.artefactName = artefactName;
    }

    public UUID getArtefactId() {
        return artefactId;
    }

    public void setArtefactId(UUID artefactId) {
        this.artefactId = artefactId;
    }

    public Long getArtefactSize() {
        return artefactSize;
    }

    public void setArtefactSize(Long artefactSize) {
        this.artefactSize = artefactSize;
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
        UserArtifactMetadataUploadRequest that = (UserArtifactMetadataUploadRequest) object;
        return Objects.equals(artefactName, that.artefactName) && Objects.equals(artefactId, that.artefactId) && Objects.equals(artefactSize, that.artefactSize) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artefactName, artefactId, artefactSize, userId);
    }
}
