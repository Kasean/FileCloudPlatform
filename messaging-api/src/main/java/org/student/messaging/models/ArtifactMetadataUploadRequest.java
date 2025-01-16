package org.student.messaging.models;

import java.util.Objects;
import java.util.UUID;

public class ArtifactMetadataUploadRequest {
    private String artefactName;
    private UUID artefactId;
    private Long artefactSize;

    public ArtifactMetadataUploadRequest(UUID artefactId, String artefactName, Long artefactSize) {
        this.artefactName = artefactName;
        this.artefactId = artefactId;
        this.artefactSize = artefactSize;
    }

    public ArtifactMetadataUploadRequest(){}

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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ArtifactMetadataUploadRequest that = (ArtifactMetadataUploadRequest) object;
        return Objects.equals(artefactName, that.artefactName) && Objects.equals(artefactId, that.artefactId) && Objects.equals(artefactSize, that.artefactSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artefactName, artefactId, artefactSize);
    }

}
