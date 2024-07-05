package org.student.services;

import java.util.UUID;

public interface ArtifactsService {

    void createArtifactMessage(String key, byte[] artifactMessage, String topic);

    void readArtifactMessage(String key, UUID id, String topic);

    void updateArtifactMessage(String key, UUID id, byte[] newArtifactMessage); // TODO: not in alfa
    void deleteArtifactMessage(String key, UUID id, String topic);

    void shutdown();

}
