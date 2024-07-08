package org.student.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.student.api.models.ArtifactCreateRequest;
import org.student.api.models.ArtifactResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/artifacts")
public class ArtifactsControllerImpl implements ArtifactsController {

    private static final Logger logger = LogManager.getLogger(ArtifactsControllerImpl.class);

    @Override
    @PostMapping("/upload")
    public Mono<ArtifactResponse> uploadFile(@RequestBody ArtifactCreateRequest request) {
        logger.info("Received artifact: {}", request.toString());
        return Mono.just(new ArtifactResponse());
    }

    @Override
    @GetMapping("/getAll")
    public Mono<List<ArtifactResponse>> getAllFiles() {

        List<ArtifactResponse> res = Collections.emptyList();
        logger.info("Received {} artifacts info from storage.", res.size());

        return Mono.just(res);
    }

    @Override
    @GetMapping("/loadArtifact/{id}")
    public Mono<ArtifactResponse> loadArtifact(@PathVariable UUID id) {
        logger.info("loading artifact with id {}", id);

        return Mono.just(new ArtifactResponse());
    }

}
