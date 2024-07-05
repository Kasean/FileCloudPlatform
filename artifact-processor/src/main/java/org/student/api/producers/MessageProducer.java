package org.student.api.producers;


import org.student.messaging.models.BaseArtifactMessage;

public interface MessageProducer<T extends BaseArtifactMessage> {
    void send(String key, T message);
}
