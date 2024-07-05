package org.student.messaging.models;

import java.util.UUID;

public class BaseArtifactMessage {

    private UUID internalId;

    private ResponseCode responseCode;

    public UUID getInternalId() {
        return internalId;
    }

    public void setInternalId(UUID internalId) {
        this.internalId = internalId;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String toString() {
        return "BaseArtifactMessage{" +
                "internalId=" + internalId +
                ", responseCode=" + responseCode +
                '}';
    }
}
