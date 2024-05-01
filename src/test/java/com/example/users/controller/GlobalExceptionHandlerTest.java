package com.example.users.controller;

import com.example.users.controller.config.GlobalExceptionHandler;
import com.example.users.controller.config.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void userNotFoundExceptionReturnsNotFoundTest() {

        //Given
        UserNotFoundException ex = new UserNotFoundException();

        //When
        ResponseEntity<Map<String, List<String>>> responseEntity = handler.userNotFoundException(ex);

        //Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().size());
        assertTrue(responseEntity.getBody().containsKey("errors"));
        assertEquals(1, responseEntity.getBody().get("errors").size());
        assertEquals("User is not found!", responseEntity.getBody().get("errors").get(0));
    }

}