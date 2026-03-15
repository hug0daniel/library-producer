package com.learnkafka.controller;

import com.learnkafka.domain.LibraryEventDTO;
import com.learnkafka.producer.LiveEventsProducer;
import com.learnkafka.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

// Aplicattion Context sliced due to WebMvcTest annotation
@WebMvcTest(LibraryEventsController.class)
class LibraryEventsControllerUnitTest {

    private RestTestClient restTestClient;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    LiveEventsProducer liveEventsProducer;


    @BeforeEach
    void setUp() {
        this.restTestClient = RestTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void postLibraryEvent() {
        //given
        when(liveEventsProducer.sendLibraryEvent_asynchApproach1(any(LibraryEventDTO.class))).thenReturn(null);

        // when

        restTestClient.post()
                .uri("/v1/libraryevent")
                .contentType(MediaType.APPLICATION_JSON)
                .body(TestUtil.libraryEventRecord())
                .exchange()
                .expectStatus().isCreated();



    }
}