package com.learnkafka.domain;

public record LibraryEventDTO(Integer libraryEventId, EventType libraryEventType, BookDTO bookDTO) {

}
