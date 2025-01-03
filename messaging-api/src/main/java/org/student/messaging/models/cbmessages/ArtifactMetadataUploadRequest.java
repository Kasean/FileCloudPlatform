package org.student.messaging.models.cbmessages;

import java.util.Objects;
import java.util.UUID;

public class ArtifactMetadataUploadRequest {
    private UUID internalArtifactId;
    private String artifactName;
    private long artifactSize;

    public ArtifactMetadataUploadRequest() {
    }

    public UUID getInternalArtifactId() {
        return internalArtifactId;
    }

    public void setInternalArtifactId(UUID internalArtifactId) {
        this.internalArtifactId = internalArtifactId;
    }

    public String getArtifactName() {
        return artifactName;
    }

    public void setArtifactName(String artifactName) {
        this.artifactName = artifactName;
    }

    public long getArtifactSize() {
        return artifactSize;
    }

    public void setArtifactSize(long artifactSize) {
        this.artifactSize = artifactSize;
    }

    @Override
    public String toString() {
        return "ArtifactMetadataUploadRequest{" +
                "internalArtifactId=" + internalArtifactId +
                ", artifactName='" + artifactName + '\'' +
                ", artifactSize=" + artifactSize +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtifactMetadataUploadRequest that = (ArtifactMetadataUploadRequest) o;
        return artifactSize == that.artifactSize && Objects.equals(internalArtifactId, that.internalArtifactId) && Objects.equals(artifactName, that.artifactName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(internalArtifactId, artifactName, artifactSize);
    }
}
