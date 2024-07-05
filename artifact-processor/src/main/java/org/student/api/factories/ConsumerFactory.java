package org.student.api.factories;

import org.student.api.utils.OperationType;
import org.student.api.consumers.CreateConsumer;
import org.student.api.consumers.DeleteConsumer;
import org.student.api.consumers.MessageConsumer;
import org.student.api.consumers.ReadConsumer;
import org.student.configs.KafkaConfig;
import org.student.services.ArtifactsService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ConsumerFactory {

    public static List<? extends MessageConsumer> createConsumers(KafkaConfig config, ArtifactsService service){
        return Arrays.stream(OperationType.values()).map(operationType -> createConsumers(config, operationType, service)).flatMap(Collection::stream).toList();
    }

    private static List<? extends MessageConsumer> createConsumers(KafkaConfig config, OperationType operationType, ArtifactsService service){
        switch (operationType){
            case READ -> {
                return createReadConsumers(config, service);
            }
            case CREATE -> {
                return createCreateConsumers(config, service);
            }
            case DELETE -> {
                return createDeleteConsumers(config, service);
            }
            case UPDATE -> {
                return createUpdateConsumers(config, service);
            }
        }

        return Collections.emptyList();
    }

    private static List<? extends MessageConsumer> createReadConsumers(KafkaConfig config, ArtifactsService service) {
        return config.getTopicsForRead().stream().map(topic -> new ReadConsumer(config.getBootstrapServers(), config.getGroupId(), topic, service::readArtifactMessage)).toList();
    }

    private static List<? extends MessageConsumer> createCreateConsumers(KafkaConfig config, ArtifactsService service) {
        return config.getTopicsForCreate().stream().map(topic -> new CreateConsumer(config.getBootstrapServers(), config.getGroupId(), topic, service::createArtifactMessage)).toList();
    }

    private static List<? extends MessageConsumer> createDeleteConsumers(KafkaConfig config, ArtifactsService service) {
        return config.getTopicsForDelete().stream().map(topic -> new DeleteConsumer(config.getBootstrapServers(), config.getGroupId(), topic, service::deleteArtifactMessage)).toList();
    }

    private static List<? extends MessageConsumer> createUpdateConsumers(KafkaConfig config, ArtifactsService service) {
        return Collections.emptyList();
    }

}
