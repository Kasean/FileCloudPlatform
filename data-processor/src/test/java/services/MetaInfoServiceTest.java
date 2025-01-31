package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.messaging.models.ArtifactMetadataUploadRequest;
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
        ArtifactMetadataUploadRequest request =
                new ArtifactMetadataUploadRequest(UUID.randomUUID(), "Artefact Name", 12345L);

        when(storage.save(any(InternalMetaInfoDto.class))).thenReturn(UUID.randomUUID());

        UUID result = metaInfoService.saveMetaInfo(request);

        assertNotNull(result);
        assertNotEquals(result, request.getArtefactId()); //Checking the internal key for non-disclosure
    }

    @Test
    void readExternalMetaInfoTest() {
        UUID externalId = UUID.randomUUID();
        ExternalMetaInfoDto mockDto = new ExternalMetaInfoDto(
                externalId, "Artefact Name", 12345L);

        when(storage.getExternalMetaInfo(externalId)).thenReturn(Optional.of(mockDto));

        Optional<ExternalMetaInfoDto> result = metaInfoService.readExternalMetaInfo(externalId);

        assertTrue(result.isPresent());
        assertEquals(mockDto, result.get());
    }

    @Test
    void readExternalMetaInfoNotFoundTest() {
        UUID externalId = UUID.randomUUID();

        when(storage.getExternalMetaInfo(externalId)).thenReturn(Optional.empty());

        Optional<ExternalMetaInfoDto> result = metaInfoService.readExternalMetaInfo(externalId);

        assertFalse(result.isPresent());
    }

    @Test
    void readInternalMetaInfoDtoTest() {
        UUID externalId = UUID.randomUUID();
        InternalMetaInfoDto mockDto = new InternalMetaInfoDto(
                UUID.randomUUID(), "Artefact Name", 12345L);

        when(storage.getInternalMetaInfoDto(externalId)).thenReturn(Optional.of(mockDto));

        Optional<InternalMetaInfoDto> result = metaInfoService.readInternalMetaInfoDto(externalId);

        assertTrue(result.isPresent());
        assertEquals(mockDto.internalId(), result.get().internalId());
    }

    @Test
    void readInternalMetaInfoDtoNotFoundTest() {
        UUID externalId = UUID.randomUUID();

        when(storage.getInternalMetaInfoDto(externalId)).thenReturn(Optional.empty());

        Optional<InternalMetaInfoDto> result = metaInfoService.readInternalMetaInfoDto(externalId);

        assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void deleteMetaInfoTest(boolean expectedResult) {
        UUID key = UUID.randomUUID();
        when(storage.deleteByKey(key)).thenReturn(expectedResult);

        boolean result = metaInfoService.deleteMetaInfo(key);

        assertEquals(expectedResult, result);
    }
}
