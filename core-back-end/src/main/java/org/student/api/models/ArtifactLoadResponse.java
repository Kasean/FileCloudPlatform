package org.student.api.models;

import java.util.Arrays;
import java.util.UUID;

public class ArtifactLoadResponse extends ArtifactResponse{

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
}
