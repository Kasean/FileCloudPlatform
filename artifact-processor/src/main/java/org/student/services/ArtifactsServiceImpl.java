package org.student.services;

import org.student.archiver.ArchiverService;
import org.student.archiver.ArchiverServiceImpl;
import org.student.configs.ApplicationConfig;
import org.student.api.MessageProducer;
import org.student.repositories.ArtifactsIMStorage;
import org.student.repositories.ArtifactsRepository;

import java.util.UUID;

public class ArtifactsServiceImpl implements ArtifactsService {

    private final ArchiverService archiver;
    private final ArtifactsRepository repository = new ArtifactsIMStorage(); // TODO: add choice in config: real db or in memory storage

    private final MessageProducer producer;

    public ArtifactsServiceImpl(ApplicationConfig config) {
        this.archiver = new ArchiverServiceImpl(config.getKeyStore());
        this.producer = new MessageProducer(config.getKafka().getBootstrapServers());
    }

    @Override
    public void createArtifactMessage(String key, byte[] artifactMessage) {
        var artifact = archiver.encrypt(artifactMessage);

        var id = repository.saveArtifact(artifact);

        // TODO: send to producer;
    }

    @Override
    public void readArtifactMessage(String key, UUID id) {
        var encryptArtifact = repository.getArtifact(id).orElseThrow(() -> new IllegalArgumentException("Artifact with id " + id + " not founded."));

        var res =  archiver.decrypt(encryptArtifact).getArtifactData();
        // TODO: send to producer;
    }

    @Override
    public void updateArtifactMessage(String key, UUID id, byte[] newArtifactMessage) { // Not implemented

    }

    @Override
    public void deleteArtifactMessage(String key, UUID id) {
        var encryptedDeletedArtifact = repository.deleteArtifact(id);

        var res = archiver.decrypt(encryptedDeletedArtifact).getArtifactData();

        // TODO: send to producer;
    }
}
