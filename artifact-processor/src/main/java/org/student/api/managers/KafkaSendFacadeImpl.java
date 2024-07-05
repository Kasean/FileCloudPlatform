package org.student.api.managers;

import org.student.api.producers.MessageProducer;
import org.student.api.producers.MessageProducerImpl;
import org.student.messaging.models.BaseArtifactMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KafkaSendFacadeImpl implements KafkaSendFacade {

    private final Map<String, MessageProducer> producers = new ConcurrentHashMap<>();
    private final String bootstrapServer;


    public KafkaSendFacadeImpl(String bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
    }

    @Override
    public <T extends BaseArtifactMessage> void send(String topic, String key, Class<T> messageType, T message) {
        @SuppressWarnings("unchecked")
        MessageProducer<T> producer = (MessageProducer<T>) producers.computeIfAbsent(topic, t -> createProducer(messageType, topic));
        producer.send(key, message);
    }

    private <T extends BaseArtifactMessage> MessageProducer<T> createProducer(Class<T> messageType, String topic) {
        return new MessageProducerImpl<>(bootstrapServer, topic, messageType);
    }
}
