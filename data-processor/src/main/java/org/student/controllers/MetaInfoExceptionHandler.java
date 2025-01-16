package org.student.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.student.exceptions.DataNotFoundException;
import org.student.exceptions.SaveDataException;

@RestControllerAdvice
public class MetaInfoExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> handleDataNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No content was found for the corresponding key");
    }

    @ExceptionHandler(SaveDataException.class)
    public ResponseEntity<String> handleSaveDataException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during saving, check your request");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred: " + ex.getMessage());
    }
}
