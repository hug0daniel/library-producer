package com.learnkafka.controller;

import com.learnkafka.domain.EventType;
import com.learnkafka.domain.LibraryEvent;
import com.learnkafka.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = "library-events")
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"})
class LibraryEventsControllerIntegrationTest {

    private RestTestClient restTestClient;

    @Autowired
    private WebApplicationContext context;  // ← this + RestTestClient replaces TestRestTemplate

    @BeforeEach
    void setUp() {
        this.restTestClient = RestTestClient.bindToApplicationContext(context).build();
    }

    //TODO: test with a kafkaConsumer

    @Test
    void postLibraryEvent() {
        // given
        var libraryEvent = TestUtil.libraryEventRecord();

        // when/then (fluent!)
        restTestClient.post()
                .uri("/v1/libraryevent")  // ← URI relativa (bind resolve)
                .contentType(MediaType.APPLICATION_JSON)
                .body(libraryEvent)  // ← Direto, sem HttpEntity!
                .exchange()
                .expectStatus().isCreated()  // ← 201 automático
                .expectBody(LibraryEvent.class)
                .value(event -> {
                    assert event != null;
                    assertEquals(EventType.NEW, event.libraryEventType());
                    // mais asserts se quiseres
                });
    }
}