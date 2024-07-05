package org.student.messaging.models.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

public class JsonSerializer<T> implements Serializer<T> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public byte[] serialize(String s, T baseArtifactMessage) {
        try {
            return objectMapper.writeValueAsBytes(baseArtifactMessage);
        } catch (Exception e) {
            throw new SerializationException("Error serializing value", e);
        }
    }
}
