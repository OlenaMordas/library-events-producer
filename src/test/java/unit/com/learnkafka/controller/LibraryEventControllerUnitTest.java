package com.learnkafka.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.domain.Book;
import com.learnkafka.domain.LibraryEvent;
import com.learnkafka.producer.LibraryEventProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryEventsController.class)
@AutoConfigureMockMvc
public class LibraryEventControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    //with webMvcTest we only have the controller layer scanned
    //so the event producer bean will not be created, we have to mock it
    @MockBean
    LibraryEventProducer libraryEventProducer;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void postLibraryEvent() throws Exception {
        Book book = Book.builder()
                .bookId(123)
                .bookAuthor("L.Tolstoy")
                .bookName("War and Peace")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .book(book)
                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);

        //mock a method of libraryEventProducer
        when(libraryEventProducer.sendLibraryEvent_Approach2(isA(LibraryEvent.class))).thenReturn(null);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/libraryevent")
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    void updateLibraryEvent() throws Exception {
        Book book = Book.builder()
                .bookId(123)
                .bookAuthor("L.Tolstoy")
                .bookName("War and Peace")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(123)
                .book(book)
                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);

        //mock a method of libraryEventProducer
        when(libraryEventProducer.sendLibraryEvent_Approach2(isA(LibraryEvent.class))).thenReturn(null);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/v1/libraryevent")
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void postLibraryEvent_4xx() throws Exception {
        Book book = Book.builder()
                .bookId(null)
                .bookAuthor(null)
                .bookName("War and Peace")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .book(book)
                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);

        //mock a method of libraryEventProducer
        when(libraryEventProducer.sendLibraryEvent_Approach2(isA(LibraryEvent.class))).thenReturn(null);
        String expectedErrorMessage = "book.bookAuthor-must not be null,book.bookId-must not be null";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/libraryevent")
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(expectedErrorMessage));

    }

    @Test
    void updateLibraryEvent_withNullLibraryEventId() throws Exception {
        Book book = Book.builder()
                .bookId(123)
                .bookAuthor("L.Tolstoy")
                .bookName("War and Peace")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .book(book)
                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);

        //mock a method of libraryEventProducer
        when(libraryEventProducer.sendLibraryEvent_Approach2(isA(LibraryEvent.class))).thenReturn(null);
        String expectedMessage = "Please pass the library event id";
        mockMvc.perform(
                MockMvcRequestBuilders.put("/v1/libraryevent")
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedMessage));

    }
}