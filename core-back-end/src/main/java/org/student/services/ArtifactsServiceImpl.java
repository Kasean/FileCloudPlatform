package org.student.services;

import org.student.api.models.ArtifactCreateRequest;
import org.student.api.models.ArtifactLoadResponse;
import org.student.api.models.ArtifactResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class ArtifactsServiceImpl implements ArtifactsService {
    @Override
    public Mono<ArtifactResponse> upload(ArtifactCreateRequest request) {
        return null;
    }

    @Override
    public Flux<ArtifactResponse> getAllArtifacts() {
        return null;
    }

    @Override
    public Mono<ArtifactLoadResponse> getArtifactById(UUID id) {
        return null;
    }

    @Override
    public Mono<ArtifactResponse> deleteArtifact(UUID id) {
        return null;
    }
}
