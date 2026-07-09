package com.enterprise.hotel.common.exception;

import org.springframework.http.HttpStatus;

/** Thrown when a room cannot be booked for the requested dates (already held/booked). */
public class RoomUnavailableException extends BusinessException {
    public RoomUnavailableException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
