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
    @GetMapping("/getAll")
    Flux<ArtifactResponse> getAllArtifacts();
    @Deprecated
    @GetMapping("/loadArtifact/{id}")
    Mono<ArtifactLoadResponse> loadArtifact(@PathVariable UUID id);
    @Deprecated
    @DeleteMapping("/deleteArtifact/{id}")
    Mono<ArtifactResponse> deleteArtifact(@PathVariable UUID id);

    @PostMapping("/upload-artifact")
    Mono<ArtifactResponse> uploadArtifact(@RequestParam UUID userId, @RequestBody ArtifactCreateRequest request);
    @GetMapping("/getAll-user-artifacts")
    Flux<ArtifactResponse> getAllArtifacts(@RequestParam UUID userId);
    @GetMapping("/load/{artifactId}")
    Mono<ArtifactLoadResponse> loadArtifact(@PathVariable UUID artifactId, @RequestParam UUID userId);
    @DeleteMapping("/del/{artifactId}")
    Mono<ArtifactResponse> deleteArtifact(@PathVariable UUID artifactId, @RequestParam UUID userId);
}
