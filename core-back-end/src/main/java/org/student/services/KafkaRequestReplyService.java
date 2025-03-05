package org.student.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.student.messaging.topics.KafkaTopics;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class KafkaRequestReplyService {
    private final KafkaTemplate<String,byte[]> kafkaTemplate;
    private final Map<String, CompletableFuture<byte[]>> futures = new ConcurrentHashMap<>();
    private static final Logger logger = LogManager.getLogger("KafkaRequestReplyService");

    public KafkaRequestReplyService(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public byte[] sendAndReceive(ProducerRecord<String,byte[]> record, String correlationId){
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        futures.put(correlationId,future);

        record.headers().add("messageType","request".getBytes(StandardCharsets.UTF_8));
        try {
            kafkaTemplate.send(record).get();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

        logger.info(record+" was sent");

        try {
            return future.get(120, TimeUnit.SECONDS);
        }catch (TimeoutException | InterruptedException | ExecutionException e){
            throw new RuntimeException("Did not receive a response from the service within 120 seconds");
        }finally {
            futures.remove(correlationId);
        }
    }

    @KafkaListener(topics = {
            "tpc1",
            KafkaTopics.ResponseMeta.SAVE_RESPONSE_TOPIC,
            KafkaTopics.ResponseMeta.GET_INT_INFO_RESPONSE_TOPIC,
            KafkaTopics.ResponseMeta.ERROR_RESPONSE_TOPIC,
            "tpd1",
            "tpr1"
    },groupId = "response-upload-group")
    private void listen(ConsumerRecord<String,String> record, @Header("messageType") String messageType){
        if (!"response".equalsIgnoreCase(messageType)){
            return;
        }
        logger.info("CONSUMED: "+record);
        String key = record.key();
        byte[] body = record.value().getBytes(StandardCharsets.UTF_8);

        CompletableFuture<byte[]> future = futures.remove(key);
        if (future!=null){
            future.complete(body);
        }
    }
}
