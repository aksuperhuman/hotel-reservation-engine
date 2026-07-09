package com.enterprise.hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application entry point for the High-Concurrency Hotel Reservation Engine.
 *
 * <p>{@code @EnableScheduling} powers the background jobs (releasing expired holds,
 * clearing stale locks, occupancy/revenue reports). {@code @EnableRetry} enables the
 * declarative retry used by the optimistic-locking path of the reservation engine.</p>
 */
@SpringBootApplication
@EnableScheduling
@EnableRetry
public class HotelReservationApplication {
    public static void main(String[] args) {
        SpringApplication.run(HotelReservationApplication.class, args);
    }
}
