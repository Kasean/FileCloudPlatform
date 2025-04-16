package org.student.services;

import org.springframework.stereotype.Service;
import org.student.api.models.ArtifactCreateRequest;
import org.student.api.models.ArtifactLoadResponse;
import org.student.api.models.ArtifactResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public interface ArtifactsService {
    @Deprecated
    Mono<ArtifactResponse> upload(ArtifactCreateRequest request);
    @Deprecated
    Flux<ArtifactResponse> getAllArtifacts();
    @Deprecated
    Mono<ArtifactLoadResponse> getArtifactById(UUID id);
    @Deprecated
    Mono<ArtifactResponse> deleteArtifact(UUID id);

    Mono<ArtifactResponse> upload(UUID userId, ArtifactCreateRequest request);
    Flux<ArtifactResponse> getAllArtifacts(UUID userId);
    Mono<ArtifactLoadResponse> getArtifactById(UUID artifactId, UUID userId);
    Mono<ArtifactResponse> deleteArtifact(UUID artifactId, UUID userId);
}
