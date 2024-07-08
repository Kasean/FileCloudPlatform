package org.student.api.models;

import java.util.Objects;

public class ArtifactMateInfo {
    private String artifactName;
    private long artifactSize;

    public ArtifactMateInfo(String artifactName, long artifactSize) {
        this.artifactName = artifactName;
        this.artifactSize = artifactSize;
    }

    public ArtifactMateInfo() {
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
        return "ArtifactMateInfo{" +
                "artifactName='" + artifactName + '\'' +
                ", artifactSize=" + artifactSize +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtifactMateInfo that = (ArtifactMateInfo) o;
        return artifactSize == that.artifactSize && Objects.equals(artifactName, that.artifactName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artifactName, artifactSize);
    }
}
