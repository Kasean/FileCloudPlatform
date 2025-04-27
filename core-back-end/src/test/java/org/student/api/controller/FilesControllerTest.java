package org.student.api.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.student.CoreApplication;
import org.student.api.models.ArtifactCreateRequest;
import org.student.api.models.ArtifactLoadResponse;
import org.student.api.models.ArtifactMateInfo;
import org.student.api.models.ArtifactResponse;
import org.student.services.ArtifactsService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@WebFluxTest(ArtifactsControllerImpl.class)
@ContextConfiguration(classes = CoreApplication.class)
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
        UUID userId = UUID.randomUUID();

        ArtifactResponse response = new ArtifactResponse(id, metaInfo);

        Mockito.when(artifactsService.upload(userId,request)).thenReturn(Mono.just(response));

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf()).post().uri("http://localhost:8888/api/v1/artifacts/user/"+userId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ArtifactResponse.class).isEqualTo(response);

        Mockito.verify(artifactsService).upload(userId,request);
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

        UUID userId = UUID.randomUUID();

        Mockito.when(artifactsService.getAllArtifacts(userId)).thenReturn(Flux.just(response1, response2));

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf()).get().uri("http://localhost:8888/api/v1/artifacts/user/"+userId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ArtifactResponse.class).isEqualTo(List.of(response1, response2));

        Mockito.verify(artifactsService).getAllArtifacts(userId);
    }

    @Test
    @WithMockUser(username = "user")
    void testArtLoading() {
        UUID id = UUID.randomUUID();
        String name = "Test name";
        byte[] body = new byte[0];

        ArtifactLoadResponse response = new ArtifactLoadResponse(id, new ArtifactMateInfo(name, body.length), body);

        UUID userId = UUID.randomUUID();

        Mockito.when(artifactsService.getArtifactById(id,userId)).thenReturn(Mono.just(response));

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf()).get().uri("http://localhost:8888/api/v1/artifacts/user/" + userId + "/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ArtifactLoadResponse.class).isEqualTo(response);

        Mockito.verify(artifactsService).getArtifactById(id,userId);
    }

    @Test
    @WithMockUser(username = "user")
    void testArtDeleting() {
        UUID id = UUID.randomUUID();
        String name = "Test name";
        byte[] body = new byte[0];

        ArtifactResponse response = new ArtifactResponse(id, new ArtifactMateInfo(name, body.length));

        UUID userId = UUID.randomUUID();

        Mockito.when(artifactsService.deleteArtifact(id,userId)).thenReturn(Mono.just(response));

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf()).delete().uri("http://localhost:8888/api/v1/artifacts/user/" + userId + "/"+ id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ArtifactResponse.class).isEqualTo(response);

        Mockito.verify(artifactsService).deleteArtifact(id,userId);
    }
}
