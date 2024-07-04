package org.student.repositories;

import org.student.models.Artifact;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ArtifactsIMStorage implements ArtifactsRepository{

    private final Map<UUID, Artifact> artifactStorage = new HashMap<>();

    @Override
    public UUID saveArtifact(Artifact artifact) {
        var id = UUID.randomUUID();
        artifactStorage.put(id, artifact);
        return id;
    }

    @Override
    public Optional<Artifact> getArtifact(UUID id) {
        var artifact = artifactStorage.get(id);

        return artifact == null ? Optional.empty() : Optional.of(artifact);
    }
}
