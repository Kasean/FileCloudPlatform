package org.student.messaging.models;

public class BodyArtifactMessage extends BaseArtifactMessage{

    private byte[] artifactBody;

    public byte[] getArtifactBody() {
        return artifactBody;
    }

    public void setArtifactBody(byte[] artifactBody) {
        this.artifactBody = artifactBody;
    }
}
