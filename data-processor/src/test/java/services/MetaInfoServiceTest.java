package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.student.dto.MetaInfoWIthExternalIdDto;
import org.student.dto.MetaInfoWithInternalIdDto;
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

        when(storage.save(any(MetaInfoWithInternalIdDto.class))).thenReturn(UUID.randomUUID());

        UUID result = metaInfoService.saveMetaInfo(request);

        assertNotNull(result);
        assertNotEquals(result, request.getArtefactId()); //Checking the internal key for non-disclosure
    }

    @Test
    void readMetaInfoTest() {
        UUID externalId = UUID.randomUUID();
        MetaInfoWIthExternalIdDto mockDto = new MetaInfoWIthExternalIdDto(
                externalId, "Artefact Name", 12345L);

        when(storage.findByKey(externalId)).thenReturn(Optional.of(mockDto));

        Optional<MetaInfoWIthExternalIdDto> result = metaInfoService.readMetaInfo(externalId);

        assertTrue(result.isPresent());
        assertEquals(mockDto, result.get());
    }

    @Test
    void readMetaInfoNotFoundTest() {
        UUID externalId = UUID.randomUUID();

        when(storage.findByKey(externalId)).thenReturn(Optional.empty());

        Optional<MetaInfoWIthExternalIdDto> result = metaInfoService.readMetaInfo(externalId);

        assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @CsvSource({
            "true",
            "false"
    })
    void deleteMetaInfoTest(boolean expectedResult) {
        UUID key = UUID.randomUUID();
        when(storage.deleteByKey(key)).thenReturn(expectedResult);

        boolean result = metaInfoService.deleteMetaInfo(key);

        assertEquals(expectedResult, result);
    }
}
