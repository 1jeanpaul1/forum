package com.fcgl.madrid.dev;
import com.fcgl.madrid.forum.model.InternalStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.lang.IllegalArgumentException;


@Service
public class DevService {

    public ResponseEntity<InternalStatus> failure() {
        throw new IllegalArgumentException("TESTING TESTING TESTING");
    }

    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback")
    public ResponseEntity<InternalStatus> failureWithFallback() {
        return failure();
    }

    private ResponseEntity<InternalStatus> fallback(IllegalArgumentException ex) {
        String message = "Fallback: " + ex.getMessage();
        InternalStatus internalStatus = new InternalStatus(-1, 400, message);
        return new ResponseEntity<InternalStatus>(internalStatus, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<InternalStatus> fallback(CallNotPermittedException ex) {
        String message = "Fallback: " + ex.getMessage();
        InternalStatus internalStatus = new InternalStatus(-1, 500, message);
        return new ResponseEntity<InternalStatus>(internalStatus, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

