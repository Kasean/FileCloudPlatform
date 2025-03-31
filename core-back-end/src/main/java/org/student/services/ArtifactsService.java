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

    Mono<ArtifactResponse> upload(ArtifactCreateRequest request);

    Flux<ArtifactResponse> getAllArtifacts();

    Mono<ArtifactLoadResponse> getArtifactById(UUID id);

    Mono<ArtifactResponse> deleteArtifact(UUID id);
}
