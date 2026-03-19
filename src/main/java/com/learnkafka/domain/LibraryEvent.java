package com.learnkafka.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record LibraryEvent(
        Integer libraryEventId,
        EventType libraryEventType,
        @NotNull(message = "Book cannot be null")
        @Valid
        Book book) {

}
