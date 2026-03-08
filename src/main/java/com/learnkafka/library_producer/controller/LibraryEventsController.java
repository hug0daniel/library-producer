package com.learnkafka.library_producer.controller;

import com.learnkafka.library_producer.domain.LibraryEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LibraryEventsController {


    @PostMapping("/v1/libraryevent")
    public ResponseEntity<LibraryEventDTO> postLibraryEvent(@RequestBody LibraryEventDTO libraryEventDTO) {

        log.info("libraryEvent: {}", libraryEventDTO);
        // invoke kafka producer

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEventDTO);
    }


}
