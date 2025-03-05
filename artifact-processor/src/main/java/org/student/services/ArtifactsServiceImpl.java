package org.student.services;

import org.springframework.stereotype.Service;
import org.student.api.producers.MessageProducer;
import org.student.archiver.ArchiverService;
import org.student.archiver.ArchiverServiceImpl;
import org.student.configs.properties.KeyStoreProperties;
import org.student.messaging.models.BaseArtifactMessage;
import org.student.messaging.models.BodyArtifactMessage;
import org.student.messaging.models.ResponseCode;
import org.student.repositories.ArtifactsIMStorage;
import org.student.repositories.ArtifactsRepository;

import java.util.UUID;

@Service
public class ArtifactsServiceImpl implements ArtifactsService {

    private final MessageProducer<BaseArtifactMessage> producer;
    private final ArchiverService archiver;
    private final ArtifactsRepository repository = new ArtifactsIMStorage(); // TODO: add choice in config: real db or in memory storage


    public ArtifactsServiceImpl(MessageProducer<BaseArtifactMessage> messageProducer, KeyStoreProperties keyStoreProperties) {
        this.producer = messageProducer;
        this.archiver = new ArchiverServiceImpl(keyStoreProperties.getRootPassword().toCharArray(), keyStoreProperties.getPathToKeyStore(), keyStoreProperties.getDefaultRSAAlias());
    }

    @Override
    public void createArtifactMessage(String key, byte[] artifactMessage, String topic) {
        var artifact = archiver.encrypt(artifactMessage);

        var id = repository.saveArtifact(artifact);

        var artifactResponse = new BaseArtifactMessage();
        artifactResponse.setResponseCode(ResponseCode.CREATED);
        artifactResponse.setInternalId(id);


        producer.send(key, topic, artifactResponse);
    }

    @Override
    public void readArtifactMessage(String key, UUID id, String topic) {
        var encryptArtifact = repository.getArtifact(id).orElseThrow(() -> new IllegalArgumentException("Artifact with id " + id + " not founded."));

        var res =  archiver.decrypt(encryptArtifact).getArtifactData();

        var artifactResponse = new BodyArtifactMessage();
        artifactResponse.setArtifactBody(res);
        artifactResponse.setResponseCode(ResponseCode.READED);
        artifactResponse.setInternalId(id);

        producer.send(key, topic, artifactResponse);
    }

    @Override
    public void updateArtifactMessage(String key, UUID id, byte[] newArtifactMessage) { // Not implemented

    }

    @Override
    public void deleteArtifactMessage(String key, UUID id, String topic) {
        var encryptedDeletedArtifact = repository.deleteArtifact(id);

        var res = archiver.decrypt(encryptedDeletedArtifact).getArtifactData();

        var artifactResponse = new BodyArtifactMessage();
        artifactResponse.setArtifactBody(res);
        artifactResponse.setResponseCode(ResponseCode.DELETED);
        artifactResponse.setInternalId(id);

        producer.send(key, topic, artifactResponse);
    }

    @Override
    public void shutdown() {
        archiver.shutdown();
    }
}
