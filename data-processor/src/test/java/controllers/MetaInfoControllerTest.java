package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.student.controllers.MetaInfoController;
import org.student.controllers.MetaInfoExceptionHandler;
import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.messaging.models.ArtifactMetadataUploadRequest;
import org.student.services.MetaInfoServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {MetaInfoController.class, MetaInfoExceptionHandler.class})
public class MetaInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetaInfoServiceImpl metaInfoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void saveTest() throws Exception {
        ArtifactMetadataUploadRequest request =
                new ArtifactMetadataUploadRequest(UUID.randomUUID(),"artefact-name",12345L);

        UUID returnedId = UUID.randomUUID();
        when(metaInfoService.saveMetaInfo(any(ArtifactMetadataUploadRequest.class))).thenReturn(returnedId);

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                post("/meta-info/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + returnedId + "\""));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true,false})
    void deleteTest(boolean expectedResult) throws Exception {
        when(metaInfoService.deleteMetaInfo(any(UUID.class))).thenReturn(expectedResult);

        mockMvc.perform(
                delete("/meta-info/delete/"+UUID.randomUUID()))
                .andExpect(content().string(String.valueOf(expectedResult)));
    }

    @Test
    void getInternalInfoTest() throws Exception {
        var returnedInfo = new InternalMetaInfoDto(UUID.randomUUID(),"artefact-name",12345L);
        when(metaInfoService.readInternalMetaInfoDto(any(UUID.class)))
                .thenReturn(Optional.of(returnedInfo));

        String expectedJson = objectMapper.writeValueAsString(returnedInfo);

        mockMvc.perform(
                get("/meta-info/get-internal/"+UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getNonExistInternalInfoTest() throws Exception {
        when(metaInfoService.readInternalMetaInfoDto(any(UUID.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(
                        get("/meta-info/get-internal/"+UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getExternalInfoTest() throws Exception {
        var returnedInfo = new ExternalMetaInfoDto(UUID.randomUUID(),"artefact-name",12345L);
        when(metaInfoService.readExternalMetaInfo(any(UUID.class)))
                .thenReturn(Optional.of(returnedInfo));

        String expectedJson = objectMapper.writeValueAsString(returnedInfo);

        mockMvc.perform(
                        get("/meta-info/get-external/"+UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getNonExistExternalInfoTest() throws Exception {
        when(metaInfoService.readExternalMetaInfo(any(UUID.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(
                        get("/meta-info/get-external/"+UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}
