package com.enterprise.hotel.common.exception;

import org.springframework.http.HttpStatus;

/** Base type for all domain errors that map to a specific HTTP status. */
public class BusinessException extends RuntimeException {
    private final HttpStatus status;

    public BusinessException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
