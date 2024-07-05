package org.student.messaging.models;

import java.util.Arrays;

public class BodyArtifactMessage extends BaseArtifactMessage{

    private byte[] artifactBody;

    public byte[] getArtifactBody() {
        return artifactBody;
    }

    public void setArtifactBody(byte[] artifactBody) {
        this.artifactBody = artifactBody;
    }

    @Override
    public String toString() {
        return "BodyArtifactMessage{" +
                "artifactBody=" + Arrays.toString(artifactBody) +
                "} " + super.toString();
    }
}
