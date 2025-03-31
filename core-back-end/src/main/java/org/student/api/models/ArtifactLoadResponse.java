package org.student.api.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.student.api.serializer.ByteArrayAsNumberArraySerializer;

import java.util.Arrays;
import java.util.UUID;

public class ArtifactLoadResponse extends ArtifactResponse{

    @JsonSerialize(using = ByteArrayAsNumberArraySerializer.class)
    private byte[] artifactBody;

    public ArtifactLoadResponse(byte[] artifactBody) {
        this.artifactBody = artifactBody;
    }

    public ArtifactLoadResponse(UUID id, ArtifactMateInfo metaInfo, byte[] artifactBody) {
        super(id, metaInfo);
        this.artifactBody = artifactBody;
    }


    public byte[] getArtifactBody() {
        return artifactBody;
    }

    public void setArtifactBody(byte[] artifactBody) {
        this.artifactBody = artifactBody;
    }

    @Override
    public String toString() {
        return "ArtifactLoadResponse{" +
                "artifactBody=" + Arrays.toString(artifactBody) +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtifactLoadResponse that = (ArtifactLoadResponse) o;
        return Arrays.equals(artifactBody, that.artifactBody);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(artifactBody);
    }
}
