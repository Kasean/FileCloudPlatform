package org.student.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.student.api.models.ArtifactCreateRequest;
import org.student.api.models.ArtifactLoadResponse;
import org.student.api.models.ArtifactMateInfo;
import org.student.api.models.ArtifactResponse;
import org.student.exceptions.messaging.UploadDataException;
import org.student.services.ArtifactsService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ArtifactsControllerImpl.class)
class FilesControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ArtifactsService artifactsService;

    @Test
    @WithMockUser(username = "user")
    void testArtifactUploading(){

        var testBody = new byte[0];
        var testName = "Test name";

        ArtifactCreateRequest request = new ArtifactCreateRequest();
        request.setArtifactBody(testBody);
        request.setName(testName);

        ArtifactMateInfo metaInfo = new ArtifactMateInfo(request.getName(), request.getArtifactBody().length);
        UUID id = UUID.randomUUID();

        ArtifactResponse response = new ArtifactResponse(id, metaInfo);

        Mockito.when(artifactsService.upload(request)).thenReturn(Mono.just(response));

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf()).post().uri("http://localhost:8888/api/v1/artifacts/upload")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ArtifactResponse.class).isEqualTo(response);

        Mockito.verify(artifactsService).upload(request);
    }

    @Test
    @WithMockUser(username = "user")
    void testGetAllMetaInfo() {

        var testBody1 = new byte[0];
        var testName1 = "Test name1";

        var testBody2 = new byte[0];
        var testName2 = "Test name2";

        ArtifactMateInfo metaInfo1 = new ArtifactMateInfo(testName1, testBody1.length);
        UUID id1 = UUID.randomUUID();

        ArtifactResponse response1 = new ArtifactResponse(id1, metaInfo1);
        ArtifactMateInfo metaInfo2 = new ArtifactMateInfo(testName2, testBody2.length);
        UUID id2 = UUID.randomUUID();

        ArtifactResponse response2 = new ArtifactResponse(id2, metaInfo2);

        Mockito.when(artifactsService.getAllArtifacts()).thenReturn(Flux.just(response1, response2));

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf()).get().uri("http://localhost:8888/api/v1/artifacts/getAll")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ArtifactResponse.class).isEqualTo(List.of(response1, response2));

        Mockito.verify(artifactsService).getAllArtifacts();
    }

    @Test
    @WithMockUser(username = "user")
    void testArtLoading() {
        UUID id = UUID.randomUUID();
        String name = "Test name";
        byte[] body = new byte[0];

        ArtifactLoadResponse response = new ArtifactLoadResponse(id, new ArtifactMateInfo(name, body.length), body);

        Mockito.when(artifactsService.getArtifactById(id)).thenReturn(Mono.just(response));

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf()).get().uri("http://localhost:8888/api/v1/artifacts/loadArtifact/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ArtifactLoadResponse.class).isEqualTo(response);

        Mockito.verify(artifactsService).getArtifactById(id);
    }
}
