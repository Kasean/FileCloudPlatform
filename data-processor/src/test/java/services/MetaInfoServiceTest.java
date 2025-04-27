package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.messaging.models.ArtifactMetadataGetRequest;
import org.student.messaging.models.UserArtifactMetadataUploadRequest;
import org.student.repositories.MetaInfoStorage;
import org.student.services.MetaInfoServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class MetaInfoServiceTest {

    private MetaInfoStorage storage;
    private MetaInfoServiceImpl metaInfoService;

    @BeforeEach
    void setUp() {
        storage = Mockito.mock(MetaInfoStorage.class);
        metaInfoService = new MetaInfoServiceImpl(storage);
    }

    @Test
    void saveMetaInfoTest() {
        var request =
                new UserArtifactMetadataUploadRequest("artifactName",UUID.randomUUID(),12345L,UUID.randomUUID());

        when(storage.save(any(UUID.class),any(InternalMetaInfoDto.class))).thenReturn(UUID.randomUUID());

        UUID result = metaInfoService.saveMetaInfo(request);

        assertNotNull(result);
        assertNotEquals(result, request.getArtefactId()); //Checking the internal key for non-disclosure
    }

    @Test
    void readExternalMetaInfoTest() {
        UUID externalId = UUID.randomUUID();
        ExternalMetaInfoDto mockDto = new ExternalMetaInfoDto(
                externalId, "Artefact Name", 12345L);

        when(storage.getExternalMetaInfo(any(UUID.class),any(UUID.class))).thenReturn(Optional.of(mockDto));

        var request = new ArtifactMetadataGetRequest(UUID.randomUUID(),externalId);

        Optional<ExternalMetaInfoDto> result = metaInfoService.readExternalMetaInfo(request);

        assertTrue(result.isPresent());
        assertEquals(mockDto, result.get());
    }

    @Test
    void readExternalMetaInfoNotFoundTest() {
        UUID externalId = UUID.randomUUID();

        when(storage.getExternalMetaInfo(any(UUID.class),any(UUID.class))).thenReturn(Optional.empty());

        var request = new ArtifactMetadataGetRequest(UUID.randomUUID(),externalId);
        Optional<ExternalMetaInfoDto> result = metaInfoService.readExternalMetaInfo(request);

        assertFalse(result.isPresent());
    }

    @Test
    void readInternalMetaInfoDtoTest() {
        UUID externalId = UUID.randomUUID();
        InternalMetaInfoDto mockDto = new InternalMetaInfoDto(
                UUID.randomUUID(), "Artefact Name", 12345L);

        when(storage.getInternalMetaInfoDto(any(UUID.class),any(UUID.class))).thenReturn(Optional.of(mockDto));

        var request = new ArtifactMetadataGetRequest(UUID.randomUUID(),externalId);
        Optional<InternalMetaInfoDto> result = metaInfoService.readInternalMetaInfoDto(request);

        assertTrue(result.isPresent());
        assertEquals(mockDto.internalId(), result.get().internalId());
    }

    @Test
    void readInternalMetaInfoDtoNotFoundTest() {
        UUID externalId = UUID.randomUUID();

        when(storage.getInternalMetaInfoDto(any(UUID.class),any(UUID.class))).thenReturn(Optional.empty());

        var request = new ArtifactMetadataGetRequest(UUID.randomUUID(),externalId);
        Optional<InternalMetaInfoDto> result = metaInfoService.readInternalMetaInfoDto(request);

        assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void deleteMetaInfoTest(boolean expectedResult) {
        when(storage.deleteByKey(any(UUID.class),any(UUID.class))).thenReturn(expectedResult);

        var request = new ArtifactMetadataGetRequest(UUID.randomUUID(),UUID.randomUUID());
        boolean result = metaInfoService.deleteMetaInfo(request);

        assertEquals(expectedResult, result);
    }
}
