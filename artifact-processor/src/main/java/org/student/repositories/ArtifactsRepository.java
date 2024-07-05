package org.student.repositories;

import org.student.models.Artifact;

import java.util.Optional;
import java.util.UUID;

public interface ArtifactsRepository {

    UUID saveArtifact(Artifact artifact);

    Optional<Artifact> getArtifact(UUID id);

    Artifact deleteArtifact(UUID id);

}
