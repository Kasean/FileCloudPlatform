package org.student.api.models;

import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtifactCreateRequest that = (ArtifactCreateRequest) o;
        return Objects.equals(name, that.name) && Arrays.equals(artifactBody, that.artifactBody);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(artifactBody);
        return result;
    }
}
