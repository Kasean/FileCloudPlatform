package org.student.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.student.api.models.ArtifactCreateRequest;
import org.student.api.models.ArtifactLoadResponse;
import org.student.api.models.ArtifactResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ArtifactsController {
    @PostMapping("/upload")
    Mono<ArtifactResponse> uploadArtifact(@RequestBody ArtifactCreateRequest request);

    @GetMapping("/getAll")
    Flux<ArtifactResponse> getAllArtifacts();

    @GetMapping("/loadArtifact/{id}")
    Mono<ArtifactLoadResponse> loadArtifact(@PathVariable UUID id);
}
