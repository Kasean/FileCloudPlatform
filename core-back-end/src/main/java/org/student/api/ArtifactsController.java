package org.student.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.student.api.models.ArtifactCreateRequest;
import org.student.api.models.ArtifactResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface ArtifactsController {
    @PostMapping("/upload")
    Mono<ArtifactResponse> uploadFile(@RequestBody ArtifactCreateRequest request);

    @GetMapping("/getAll")
    Mono<List<ArtifactResponse>> getAllFiles();

    @GetMapping("/loadArtifact/{id}")
    Mono<ArtifactResponse> loadArtifact(@PathVariable UUID id);
}
