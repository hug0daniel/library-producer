package com.learnkafka.controller;

import com.learnkafka.domain.EventType;
import com.learnkafka.domain.LibraryEvent;
import com.learnkafka.producer.LiveEventsProducer;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class LibraryEventsController {

    private final LiveEventsProducer liveEventsProducer;

    public LibraryEventsController(LiveEventsProducer liveEventsProducer) {
        this.liveEventsProducer = liveEventsProducer;
    }


    @PostMapping("/v1/libraryevent")
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) {
        liveEventsProducer.sendLibraryEvent_asynchApproach2(libraryEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    @PutMapping("/v1/libraryevent")
    public ResponseEntity<?> updateLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) {

        //Initial request validation
        ResponseEntity<String> badRequest = validateLibraryEvent(libraryEvent);

        if (badRequest != null) return  badRequest;

        liveEventsProducer.sendLibraryEvent_asynchApproach2(libraryEvent);

        return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
    }

    public ResponseEntity<String> validateLibraryEvent(LibraryEvent libraryEvent) {

        if (libraryEvent.libraryEventId() == null ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the library event ID");
        }

        if (!libraryEvent.libraryEventType().equals(EventType.UPDATE)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only UPDATE Event Type is supported");
        }
        return null;
    }



}
