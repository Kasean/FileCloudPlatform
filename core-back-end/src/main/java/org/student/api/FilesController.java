package org.student.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/files")
public class FilesController {

    private static final Logger logger = LogManager.getLogger(FilesController.class);

    @PostMapping("/upload")
    public Mono<String> uploadFile(@RequestPart("file") Mono<FilePart> filePartMono) {
        return filePartMono.doOnNext(fp -> logger.info("Received file: " + fp.filename())).
                then(Mono.just("File uploaded successfully."));
    }

}
