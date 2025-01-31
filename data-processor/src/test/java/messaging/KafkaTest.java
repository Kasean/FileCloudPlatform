package messaging;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.student.Application;
import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.messaging.MessageProducer;
import org.student.messaging.models.ArtifactMetadataUploadRequest;
import org.student.messaging.topics.KafkaTopics;
import org.student.services.MetaInfoServiceImpl;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@Testcontainers
@SpringBootTest(classes = {Application.class})
public class KafkaTest {

    @SpyBean
    private MessageProducer messageProducer;

    @MockBean
    private MetaInfoServiceImpl metaInfoService;

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))
            .withEmbeddedZookeeper();

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @Test
    void saveTest() throws Exception {
        ArtifactMetadataUploadRequest request = new ArtifactMetadataUploadRequest(UUID.randomUUID(), "ArtifactName", 1234L);
        UUID mockUuid = UUID.randomUUID();

        when(metaInfoService.saveMetaInfo(any(ArtifactMetadataUploadRequest.class))).thenReturn(mockUuid);

        messageProducer.sendArtifact(request, KafkaTopics.CrudMeta.SAVE_META_INFO_TOPIC);

        await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(metaInfoService, times(1)).saveMetaInfo(any(ArtifactMetadataUploadRequest.class));

                    ArgumentCaptor<UUID> uuidCaptor = ArgumentCaptor.forClass(UUID.class);
                    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);

                    verify(messageProducer, times(1)).sendUUID(uuidCaptor.capture(), topicCaptor.capture());

                    String actualTopic = topicCaptor.getValue();
                    UUID actualUuid = uuidCaptor.getValue();

                    assertAll(
                            () -> assertEquals(KafkaTopics.ResponseMeta.SAVE_RESPONSE_TOPIC, actualTopic,
                                    () -> String.format("Topics mismatch! Expected: '%s', but got: '%s'",
                                            KafkaTopics.ResponseMeta.SAVE_RESPONSE_TOPIC, actualTopic)),
                            () -> assertNotNull(actualUuid, "Captured UUID is null!"),
                            () -> assertEquals(mockUuid, actualUuid,
                                    () -> String.format("UUID mismatch! Expected: '%s', but got: '%s'", mockUuid, actualUuid))
                    );
                });
    }

    @Test
    void getExternalInfoTest() throws Exception {
        UUID mockUuid = UUID.randomUUID();

        ExternalMetaInfoDto metaInfoDto = new ExternalMetaInfoDto(mockUuid,"artifactName",12345L);
        when(metaInfoService.readExternalMetaInfo(any(UUID.class))).thenReturn(Optional.of(metaInfoDto));

        messageProducer.sendUUID(mockUuid,KafkaTopics.CrudMeta.GET_EXT_META_INFO_TOPIC);

        await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(metaInfoService, times(1)).readExternalMetaInfo(any(UUID.class));

                    ArgumentCaptor<ExternalMetaInfoDto> externalInfoDto = ArgumentCaptor.forClass(ExternalMetaInfoDto.class);
                    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);

                    verify(messageProducer, times(1)).sendExternalMeta(externalInfoDto.capture(), topicCaptor.capture());

                    String actualTopic = topicCaptor.getValue();
                    ExternalMetaInfoDto actualMeta = externalInfoDto.getValue();

                    assertAll(
                            () -> assertEquals(KafkaTopics.ResponseMeta.GET_EXT_INFO_RESPONSE_TOPIC, actualTopic,
                                    () -> String.format("Topics mismatch! Expected: '%s', but got: '%s'",
                                            KafkaTopics.ResponseMeta.GET_EXT_INFO_RESPONSE_TOPIC, actualTopic)),
                            () -> assertNotNull(actualMeta, "Captured dto is null!"),
                            () -> assertEquals(metaInfoDto, actualMeta,
                                    () -> String.format("DTOs mismatch! Expected: '%s', but got: '%s'", metaInfoDto, actualMeta))
                    );
                });
    }

    @Test
    void getInternalInfoTest() throws Exception {

        InternalMetaInfoDto metaInfoDto = new InternalMetaInfoDto(UUID.randomUUID(),"artifactName",12345L);
        when(metaInfoService.readInternalMetaInfoDto(any(UUID.class))).thenReturn(Optional.of(metaInfoDto));

        messageProducer.sendUUID(UUID.randomUUID(),KafkaTopics.CrudMeta.GET_INT_META_INFO_TOPIC);

        await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(metaInfoService, times(1)).readInternalMetaInfoDto(any(UUID.class));

                    ArgumentCaptor<InternalMetaInfoDto> internalInfoDto = ArgumentCaptor.forClass(InternalMetaInfoDto.class);
                    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);

                    verify(messageProducer, times(1)).sendInternalMeta(internalInfoDto.capture(), topicCaptor.capture());

                    String actualTopic = topicCaptor.getValue();
                    InternalMetaInfoDto actualMeta = internalInfoDto.getValue();

                    assertAll(
                            () -> assertEquals(KafkaTopics.ResponseMeta.GET_INT_INFO_RESPONSE_TOPIC, actualTopic,
                                    () -> String.format("Topics mismatch! Expected: '%s', but got: '%s'",
                                            KafkaTopics.ResponseMeta.GET_INT_INFO_RESPONSE_TOPIC, actualTopic)),
                            () -> assertNotNull(actualMeta, "Captured dto is null!"),
                            () -> assertEquals(metaInfoDto, actualMeta,
                                    () -> String.format("DTOs mismatch! Expected: '%s', but got: '%s'", metaInfoDto, actualMeta))
                    );
                });
    }

    @Test
    void delDataInfoTest() throws Exception {
        when(metaInfoService.deleteMetaInfo(any(UUID.class))).thenReturn(true);

        messageProducer.sendUUID(UUID.randomUUID(),KafkaTopics.CrudMeta.DEL_META_INFO);

        await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(metaInfoService, times(1)).deleteMetaInfo(any(UUID.class));

                    ArgumentCaptor<Boolean> delResult = ArgumentCaptor.forClass(Boolean.class);
                    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);

                    verify(messageProducer, times(1)).sendBoolean(delResult.capture(), topicCaptor.capture());

                    String actualTopic = topicCaptor.getValue();
                    Boolean actualDelResult = delResult.getValue();

                    assertAll(
                            () -> assertEquals(KafkaTopics.ResponseMeta.DEL_RESPONSE_TOPIC, actualTopic,
                                    () -> String.format("Topics mismatch! Expected: '%s', but got: '%s'",
                                            KafkaTopics.ResponseMeta.DEL_RESPONSE_TOPIC, actualTopic)),
                            () -> assertEquals(actualDelResult, true,
                                    () -> String.format("Result mismatch! Expected: '%s', but got: '%s'", actualDelResult, true))
                    );
                });
    }
}



