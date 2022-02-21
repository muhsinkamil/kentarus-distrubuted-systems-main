package com.kentarus.distributed_systems.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CommonException {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception ex, WebRequest req) {
        return new ResponseEntity<>("Oh no error occured. Please check the request again",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
