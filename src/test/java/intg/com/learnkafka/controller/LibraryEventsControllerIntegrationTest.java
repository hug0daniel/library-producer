package com.learnkafka.controller;

import com.learnkafka.domain.EventType;
import com.learnkafka.domain.LibraryEventDTO;
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
                .expectBody(LibraryEventDTO.class)
                .value(event -> {
                    assertEquals(EventType.NEW, event.libraryEventType());
                    // mais asserts se quiseres
                });
    }

//    @Test
//    void postLibraryEvent(){
//
//        //given
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("content-type", MediaType.APPLICATION_JSON.toString());
//
//        var httpEntity = new HttpEntity<>(TestUtil.libraryEventRecord(),httpHeaders);
//
//        //when
//        var resposnseEntity = restTemplate
//                .exchange("/v1/libraryevent", HttpMethod.POST,httpEntity, LibraryEventDTO.class);
//
//        //then
//        assertEquals(HttpStatus.CREATED,resposnseEntity.getStatusCode());
//    }
}