package org.student.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.student.api.models.ArtifactCreateRequest;
import org.student.api.models.ArtifactLoadResponse;
import org.student.api.models.ArtifactResponse;
import org.student.exceptions.messaging.UploadDataException;
import org.student.services.ArtifactsService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/artifacts")
public class ArtifactsControllerImpl implements ArtifactsController {

    private static final Logger logger = LogManager.getLogger("ArtifactsControllerImpl");

    private final ArtifactsService artifactsService;

    public ArtifactsControllerImpl(ArtifactsService artifactsService) {
        this.artifactsService = artifactsService;
    }

    @Override
    @PostMapping("/upload")
    public Mono<ArtifactResponse> uploadArtifact(@RequestBody ArtifactCreateRequest request) {
        logger.info("Creating artifact from request: {}", request);
        return artifactsService.upload(request)
                .doOnNext(artifactResponse -> logger.info("Created artifact: {}", artifactResponse))
                .onErrorResume(UploadDataException.class, e -> {
                    logger.error("Upload failed: {}", e.getMessage());
                    return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
                });
    }

    @Override
    @GetMapping("/getAll")
    public Flux<ArtifactResponse> getAllArtifacts() {
        return artifactsService.getAllArtifacts()
                .doOnNext(artifact -> logger.info("Received artifact info from storage: {}", artifact));
    }

    @Override
    @GetMapping("/loadArtifact/{id}")
    public Mono<ArtifactLoadResponse> loadArtifact(@PathVariable UUID id) {
        return artifactsService.getArtifactById(id)
                .doOnNext(artifact -> logger.info("Loading artifact with id {}", id))
                .onErrorResume(IllegalArgumentException.class, e -> {
                    logger.error("load failed: {}", e.getMessage());
                    return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
                });
    }

    @Override
    public Mono<ArtifactResponse> deleteArtifact(UUID id) {
        return artifactsService.deleteArtifact(id).doOnNext(artifactResponse -> logger.info("Artifact {} deleted", artifactResponse));
    }

}
