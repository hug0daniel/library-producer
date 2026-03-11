package com.learnkafka.util;

import com.learnkafka.domain.BookDTO;
import com.learnkafka.domain.LibraryEventDTO;
import com.learnkafka.domain.EventType;
import tools.jackson.databind.ObjectMapper;

public class TestUtil {

    public static BookDTO bookRecord(){

        return new BookDTO(123, "Dilip","Kafka Using Spring Boot" );
    }

    public static BookDTO bookRecordWithInvalidValues(){

        return new BookDTO(null, "","Kafka Using Spring Boot" );
    }

    public static LibraryEventDTO libraryEventRecord(){

        return
                new LibraryEventDTO(null,
                        EventType.NEW,
                        bookRecord());
    }

    public static LibraryEventDTO newLibraryEventRecordWithLibraryEventId(){

        return
                new LibraryEventDTO(123,
                        EventType.NEW,
                        bookRecord());
    }

    public static LibraryEventDTO libraryEventRecordUpdate(){

        return
                new LibraryEventDTO(123,
                        EventType.UPDATE,
                        bookRecord());
    }

    public static LibraryEventDTO libraryEventRecordUpdateWithNullLibraryEventId(){

        return
                new LibraryEventDTO(null,
                        EventType.UPDATE,
                        bookRecord());
    }

    public static LibraryEventDTO libraryEventRecordWithInvalidBook(){

        return
                new LibraryEventDTO(null,
                        EventType.NEW,
                        bookRecordWithInvalidValues());
    }

    public static LibraryEventDTO parseLibraryEventRecord(ObjectMapper objectMapper , String json){

        return  objectMapper.readValue(json, LibraryEventDTO.class);


    }
}
