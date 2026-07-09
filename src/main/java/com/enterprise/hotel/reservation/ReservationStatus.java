package com.enterprise.hotel.reservation;

/**
 * Reservation lifecycle:
 * PENDING -> LOCK_ROOM -> PAYMENT_PENDING -> CONFIRMED -> CHECKED_IN -> CHECKED_OUT -> COMPLETED.
 * CANCELLED is a terminal state reachable from any pre-CONFIRMED state and releases inventory.
 */
public enum ReservationStatus {
    PENDING,
    LOCK_ROOM,
    PAYMENT_PENDING,
    CONFIRMED,
    CHECKED_IN,
    CHECKED_OUT,
    COMPLETED,
    CANCELLED
}
