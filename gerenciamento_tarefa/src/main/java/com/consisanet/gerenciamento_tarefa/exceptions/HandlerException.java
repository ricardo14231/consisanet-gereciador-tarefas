package com.consisanet.gerenciamento_tarefa.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.UnexpectedTypeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class HandlerException {

    @ExceptionHandler(ResponseBadRequestException.class)
    protected ResponseEntity<ExceptionDetails> handleBadRequest(ResponseBadRequestException ex) {
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .title("Bad Request Exception. " + ex.getLocalizedMessage())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .timestamp(LocalDateTime.now())
                        .details(ex.getMessage())
                        .developerMessage(ex.getClass().getName())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ResponseNotFoundException.class)
    protected ResponseEntity<ExceptionDetails> handleNotFoundException(ResponseNotFoundException ex) {
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .title("Not Found Exception. " + ex.getLocalizedMessage())
                        .status(HttpStatus.NOT_FOUND.value())
                        .timestamp(LocalDateTime.now())
                        .details(ex.getMessage())
                        .developerMessage(ex.getClass().getName())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ExceptionDetails> handleIllegalArgumentException
            (RuntimeException ex, WebRequest request) {

        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .title("Illegal Argument Exception. " + ex.getLocalizedMessage())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .timestamp(LocalDateTime.now())
                        .details(ex.getMessage())
                        .developerMessage(ex.getClass().getName())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid
            (MethodArgumentNotValidException ex) {

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String fields = fieldErrors.stream().map(field -> field.getField()).collect(Collectors.joining(", "));
        String fieldMessage = fieldErrors.stream().map(field -> field.getDefaultMessage()).collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                ValidationExceptionDetails.builder()
                        .title("Bad Request Exception. Invalid Field(s).")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .timestamp(LocalDateTime.now())
                        .details("Verique os campo(s).")
                        .developerMessage(ex.getClass().getName())
                        .fields(fields)
                        .fieldsMessage(fieldMessage)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    protected ResponseEntity<ExceptionDetails> handleUnexpectedTypeException
            (RuntimeException ex) {

        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .title("Unexpected Type Exception. " + ex.getLocalizedMessage())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .timestamp(LocalDateTime.now())
                        .details(ex.getMessage())
                        .developerMessage(ex.getClass().getName())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ResponseUnprocessableException.class)
    protected ResponseEntity<ExceptionDetails> handleResponseUnprocessableException
            (RuntimeException ex) {

        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .title("Unprocessable Entity Exception. " + ex.getLocalizedMessage())
                        .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .timestamp(LocalDateTime.now())
                        .details(ex.getMessage())
                        .developerMessage(ex.getClass().getName())
                        .build(),
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }
}
