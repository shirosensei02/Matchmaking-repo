package Matchmaking;

import Matchmaking.Controller.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.dao.DataAccessException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler handler;
    private WebRequest mockRequest;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        mockRequest = mock(WebRequest.class);
        when(mockRequest.getDescription(false)).thenReturn("test request");
    }

    @Test
    void testHandleGeneralException() {
        Exception ex = new Exception("Test exception");
        ResponseEntity<Object> response = handler.handleGeneralException(ex, mockRequest);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Test exception"));
    }

    @Test
    void testHandleIllegalStateException() {
        IllegalStateException ex = new IllegalStateException("Invalid round");
        ResponseEntity<Object> response = handler.handleIllegalStateException(ex, mockRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Invalid round number"));
    }

    @Test
    void testHandleJsonProcessingException() {
        JsonProcessingException ex = mock(JsonProcessingException.class);
        ResponseEntity<Object> response = handler.handleJsonProcessingException(ex, mockRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error processing JSON data"));
    }

    @Test
    void testHandleTypeCastingException() {
        ClassCastException ex = new ClassCastException("Type casting error");
        ResponseEntity<Object> response = handler.handleTypeCastingException(ex, mockRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Data type error"));
    }

    @Test
    void testHandleDatabaseException() {
        DataAccessException ex = mock(DataAccessException.class);
        ResponseEntity<Object> response = handler.handleDatabaseException(ex, mockRequest);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Database error"));
    }
}