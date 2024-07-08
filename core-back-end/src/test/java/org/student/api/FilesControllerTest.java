package org.student.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ArtifactsControllerImpl.class)
public class FilesControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @WithMockUser(username = "user")
    public void testFileUploading(){
        ClassPathResource resource = new ClassPathResource("testFile.csv");
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", resource);

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf()).post().uri("http://localhost:8888/api/v1/files/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("File uploaded successfully.");
    }
}
