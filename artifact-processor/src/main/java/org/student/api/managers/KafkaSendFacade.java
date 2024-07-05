package org.student.api.managers;

import org.student.messaging.models.BaseArtifactMessage;

public interface KafkaSendFacade {
    <T extends BaseArtifactMessage> void send(String topic, String key, Class<T> messageType, T message);
}
