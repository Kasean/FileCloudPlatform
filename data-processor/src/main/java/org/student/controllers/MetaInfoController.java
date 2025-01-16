package org.student.controllers;

import org.springframework.web.bind.annotation.*;
import org.student.api.MetaInfoApi;
import org.student.dto.ExternalMetaInfoDto;
import org.student.dto.InternalMetaInfoDto;
import org.student.exceptions.DataNotFoundException;
import org.student.exceptions.SaveDataException;
import org.student.messaging.models.ArtifactMetadataUploadRequest;
import org.student.services.MetaInfoServiceImpl;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/meta-info")
public class MetaInfoController implements MetaInfoApi {
    private final MetaInfoServiceImpl metaInfoService;

    public MetaInfoController(MetaInfoServiceImpl metaInfoService) {
        this.metaInfoService = metaInfoService;
    }

    @GetMapping("/get-internal/{id}")
    @Override
    public Optional<InternalMetaInfoDto> getInternalMeta(@PathVariable UUID id) throws DataNotFoundException {
        var result = metaInfoService.readInternalMetaInfoDto(id);
        if (result.isEmpty()){
            throw new DataNotFoundException();
        }else return result;
    }

    @GetMapping("/get-external/{id}")
    @Override
    public Optional<ExternalMetaInfoDto> getExternalMeta(@PathVariable UUID id) throws DataNotFoundException {
        var result = metaInfoService.readExternalMetaInfo(id);
        if (result.isEmpty()){
            throw new DataNotFoundException();
        }return result;
    }

    @PostMapping("/save")
    @Override
    public UUID save(@RequestBody ArtifactMetadataUploadRequest request) throws SaveDataException {
        try {
            return metaInfoService.saveMetaInfo(request);
        }catch (Exception e){
            throw new SaveDataException();
        }
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public boolean delete(@PathVariable UUID id){
        return metaInfoService.deleteMetaInfo(id);
    }
}

