package com.learnkafka.producer;

import com.learnkafka.domain.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class LiveEventsProducer {

    private final KafkaTemplate<Integer,String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic}")
    public String topic;

    public LiveEventsProducer(KafkaTemplate<Integer, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    // Async approach with .send(); (Most realistic approach in real live services)
    public CompletableFuture<SendResult<Integer, String>> sendLibraryEvent_asynchApproach1(LibraryEvent libraryEvent){

        var key= libraryEvent.libraryEventId();
        var value = objectMapper.writeValueAsString(libraryEvent);

        // If this is the 1st call ever to the cluster: will happen blocking call - to get metadata about the kafka cluster
        // 2- Send message happens - Returns a Completable Future


        var completableFuture = kafkaTemplate.send(topic, key , value);

        return completableFuture.whenComplete((sendResult,throwable) ->{
            if(throwable != null) {
                handleFailure(key,value,throwable);
            } else {
                handleSuccess(key,value, sendResult);
            }
        });
    }

    public void sendLibraryEvent_asynchApproach2(LibraryEvent libraryEvent){

        var key= libraryEvent.libraryEventId();
        var value = objectMapper.writeValueAsString(libraryEvent);


        ProducerRecord<Integer,String> record = buildProducerRecord(key,value);
        // If this is the 1st call ever to the cluster: will happen blocking call - to get metadata about the kafka cluster
        // 2- Send message happens - Returns a Completable Future


        var completableFuture = kafkaTemplate.send(record);

         completableFuture.whenComplete((sendResult,throwable) ->{
            if(throwable != null) {
                handleFailure(key,value,throwable);
            } else {
                handleSuccess(key,value, sendResult);
            }
        });
    }

    private ProducerRecord<Integer, String > buildProducerRecord(Integer key, String value) {
        return new ProducerRecord<>(topic,key,value);
    }
    // testing messages
//    private ProducerRecord<Integer, String > buildProducerRecord_withHeaders(Integer key, String value) {
//        List<RecordHeader> recordHeaders = List.of(new RecordHeader("event-source","scanner".getBytes()));
//        return new ProducerRecord<>(topic,null,key,value,recordHeaders);
//    }

    //Synch approach with the .get();
    /*public SendResult<Integer, String> sendLibraryEvent_synchApproach(LibraryEvent libraryEvent) throws ExecutionException, InterruptedException, TimeoutException {

        var key= libraryEvent.libraryEventId();
        var value = objectMapper.writeValueAsString(libraryEvent);

        // 1- Blocking call - to get metadata about the kafka cluster
        // 2- Block and wait until the message is sent to the Kafka topic


        SendResult<Integer,String> sendResult = kafkaTemplate.send(topic, key , value)
                //.get();
                .get(3, TimeUnit.SECONDS);
        handleSuccess(key,value, sendResult);
        return sendResult;

    }*/

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> sendResult) {
        log.info("Message sent Successfully for the key: {} and the value: {} , partition is {} ", key,value,sendResult.getRecordMetadata().partition());
    }

    private void handleFailure(Integer key, String value, Throwable throwable) {
        log.error("Error sending the message [key: {}, value: {}] and the exception is {} ",key,value, throwable.getMessage(), throwable);
    }
}
