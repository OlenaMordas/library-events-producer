package com.learnkafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.domain.Book;
import com.learnkafka.domain.LibraryEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LibraryEventProducerUnitTest {

    @Mock
    KafkaTemplate<Integer, String> kafkaTemplate;

    //the kafkaTemplate mock will be injected in here
    @InjectMocks
    LibraryEventProducer eventProducer;

    // not mocking object mapper, using the actual one
    @Spy
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void sendLibraryEvent_Approach2_failure() throws JsonProcessingException, ExecutionException, InterruptedException {

        Book book = Book.builder()
                .bookId(null)
                .bookAuthor(null)
                .bookName("War and Peace")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .book(book)
                .build();

        SettableListenableFuture future = new SettableListenableFuture();
        future.setException(new RuntimeException("Exception calling Kafka"));

        when(kafkaTemplate.send(isA(ProducerRecord.class)))
        .thenReturn(future);
        assertThrows(Exception.class, ()->eventProducer.sendLibraryEvent_Approach2(libraryEvent).get());

    }
}
