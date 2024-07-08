package org.student.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.student.api.models.ArtifactCreateRequest;
import org.student.api.models.ArtifactCreateResponce;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/artifacts")
public class ArtifactsController {

    private static final Logger logger = LogManager.getLogger(ArtifactsController.class);

    @PostMapping("/upload")
    public Mono<ArtifactCreateResponce> uploadFile(@RequestBody ArtifactCreateRequest request) {
        logger.info("Received artifact: {}", request.toString());
        return Mono.just(new ArtifactCreateResponce());
    }

}
