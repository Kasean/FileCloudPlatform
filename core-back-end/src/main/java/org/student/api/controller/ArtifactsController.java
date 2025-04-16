package org.student.api.controller;

import org.springframework.web.bind.annotation.*;
import org.student.api.models.ArtifactCreateRequest;
import org.student.api.models.ArtifactLoadResponse;
import org.student.api.models.ArtifactResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ArtifactsController {
    @Deprecated
    @PostMapping("/upload")
    Mono<ArtifactResponse> uploadArtifact(@RequestBody ArtifactCreateRequest request);

    @Deprecated
    @GetMapping("/loadArtifact/{id}")
    Mono<ArtifactLoadResponse> loadArtifact(@PathVariable UUID id);
    @Deprecated
    @DeleteMapping("/deleteArtifact/{id}")
    Mono<ArtifactResponse> deleteArtifact(@PathVariable UUID id);

    @PostMapping("/user/{userId}")
    Mono<ArtifactResponse> uploadArtifact(@PathVariable UUID userId, @RequestBody ArtifactCreateRequest request);

    @GetMapping("/user/{userId}")
    Flux<ArtifactResponse> getAllArtifacts(@PathVariable UUID userId);

    @GetMapping("/user/{userId}/{artifactId}")
    Mono<ArtifactLoadResponse> loadArtifact(@PathVariable UUID userId, @PathVariable UUID artifactId);

    @DeleteMapping("/user/{userId}/{artifactId}")
    Mono<ArtifactResponse> deleteArtifact(@PathVariable UUID userId, @PathVariable UUID artifactId);
}
