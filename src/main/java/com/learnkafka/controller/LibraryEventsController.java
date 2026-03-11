package com.learnkafka.controller;

import com.learnkafka.domain.LibraryEventDTO;
import com.learnkafka.producer.LiveEventsProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LibraryEventsController {

    private final LiveEventsProducer liveEventsProducer;

    public LibraryEventsController(LiveEventsProducer liveEventsProducer) {
        this.liveEventsProducer = liveEventsProducer;
    }


    @PostMapping("/v1/libraryevent")
    public ResponseEntity<LibraryEventDTO> postLibraryEvent(@RequestBody LibraryEventDTO libraryEventDTO) {

        log.info("libraryEvent: {}", libraryEventDTO);

        // invoke kafka producer
        //liveEventsProducer.sendLibraryEvent_asynchApproach1(libraryEventDTO);
        liveEventsProducer.sendLibraryEvent_asynchApproach2(libraryEventDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEventDTO);
    }


}
