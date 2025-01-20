package org.student.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.student.api.MetaInfoApi;
import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.exceptions.DataNotFoundException;
import org.student.exceptions.SaveDataException;
import org.student.messaging.models.ArtifactMetadataUploadRequest;
import org.student.services.MetaInfoServiceImpl;

import java.util.UUID;

@RestController
@RequestMapping("/meta-info")
public class MetaInfoController implements MetaInfoApi {
    private static final Logger logger = LogManager.getLogger(MetaInfoController.class);

    private final MetaInfoServiceImpl metaInfoService;

    public MetaInfoController(MetaInfoServiceImpl metaInfoService) {
        this.metaInfoService = metaInfoService;
    }

    @GetMapping("/get-internal/{id}")
    @Override
    public InternalMetaInfoDto getInternalMeta(@PathVariable UUID id) throws DataNotFoundException {
        logger.info("A GET request to get internal meta-info was received");
        return metaInfoService.readInternalMetaInfoDto(id)
                .orElseThrow(()->{
                    logger.error("No internal metadata found for ID: {}", id);
                    return new DataNotFoundException("Error getting internal meta-information by id");
                });
    }

    @GetMapping("/get-external/{id}")
    @Override
    public ExternalMetaInfoDto getExternalMeta(@PathVariable UUID id) throws DataNotFoundException {
        logger.info("A GET request to get external meta-info was received");
        return metaInfoService.readExternalMetaInfo(id)
                .orElseThrow(()->{
                    logger.error("No external metadata found for ID: {}", id);
                    return new DataNotFoundException("Error getting external meta-information by id");
                });
    }

    @PostMapping("/save")
    @Override
    public UUID save(@RequestBody ArtifactMetadataUploadRequest request) throws SaveDataException {
        logger.info("A POST request was received to store the artifact's meta-information: {}",request);
        try {
            return metaInfoService.saveMetaInfo(request);
        }catch (Exception e){
            logger.error("Error occurred while saving metadata: {}", e.getMessage(), e);
            throw new SaveDataException("An error occurred during saving, check your request");
        }
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public boolean delete(@PathVariable UUID id){
        logger.info("A DELETE request has been received to delete the artifact's meta-information");
        return metaInfoService.deleteMetaInfo(id);
    }
}

