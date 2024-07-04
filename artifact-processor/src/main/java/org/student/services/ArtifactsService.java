package org.student.services;

import java.util.UUID;

public interface ArtifactsService {

    void saveArtifactMessage(String key, byte[] artifactMessage);

    byte[] getArtifactMessage(String key, UUID id);

}
