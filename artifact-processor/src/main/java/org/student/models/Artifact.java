package org.student.models;

public class Artifact {

    private final ArtifactMetaInfo metaInfo = new ArtifactMetaInfo();

    private byte[] artifactData;

    public Artifact(byte[] artifactData) {
        this.artifactData = artifactData;
    }

    public Artifact() {
    }

    public ArtifactMetaInfo getMetaInfo() {
        return metaInfo;
    }

    public byte[] getArtifactData() {
        return artifactData;
    }

    public void setArtifactData(byte[] artifactData) {
        this.artifactData = artifactData;
    }
}
