package org.student.configs;

public class ApplicationConfig {

    private KeyStoreConfig keyStore;

    private KafkaConfig kafka;
    private ArtifactsConfig artifacts;

    public KeyStoreConfig getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(KeyStoreConfig keyStore) {
        this.keyStore = keyStore;
    }

    public KafkaConfig getKafka() {
        return kafka;
    }

    public void setKafka(KafkaConfig kafka) {
        this.kafka = kafka;
    }

    public ArtifactsConfig getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(ArtifactsConfig artifacts) {
        this.artifacts = artifacts;
    }
}
