package org.student.api.producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.student.messaging.models.BaseArtifactMessage;
import org.student.messaging.models.serializers.JsonSerializer;

import java.util.Properties;

public class MessageProducerImpl<T extends BaseArtifactMessage> implements MessageProducer<T> {

    private final KafkaProducer<String, T> producer;
    private final String topic;

    public MessageProducerImpl(String bootstrapServers, String topic, Class<T> messageType) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        properties.put("value.serializer.type", messageType);
        this.producer = new KafkaProducer<>(properties);
        this.topic = topic;
    }

    @Override
    public void send(String key, T message) {
        producer.send(new ProducerRecord<>(topic, 1, key, message));
    }

//    public MessageProducerImpl(String bootstrapServers, String topic) {
//        Properties properties = new Properties();
//        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
////        props.put("value.deserializer.type", MyObject.class);
////        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class
//        this.producer = new KafkaProducer<>(properties);
//        this.topic = topic;
//    }
}
