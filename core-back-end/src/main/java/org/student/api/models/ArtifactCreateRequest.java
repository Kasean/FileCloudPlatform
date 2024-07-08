package org.student.api.models;

import java.util.Arrays;

public class ArtifactCreateRequest {

    private String name;

    private byte[] artifactBody;

    public ArtifactCreateRequest(String name, byte[] artifactBody) {
        this.name = name;
        this.artifactBody = artifactBody;
    }

    public ArtifactCreateRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getArtifactBody() {
        return artifactBody;
    }

    public void setArtifactBody(byte[] artifactBody) {
        this.artifactBody = artifactBody;
    }

    @Override
    public String toString() {
        return "ArtifactCreateRequest{" +
                "name='" + name + '\'' +
                ", artifactBody=" + Arrays.toString(artifactBody) +
                '}';
    }
}
