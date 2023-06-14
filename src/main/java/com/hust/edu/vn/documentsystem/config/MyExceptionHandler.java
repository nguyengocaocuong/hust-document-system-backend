package com.hust.edu.vn.documentsystem.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(value = {IllegalAccessException.class})
    protected ResponseEntity<Object> handle(){
        return ResponseEntity.badRequest().body(null);
    }
}
