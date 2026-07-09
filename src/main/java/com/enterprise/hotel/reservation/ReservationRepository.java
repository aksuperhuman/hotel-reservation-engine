package com.enterprise.hotel.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByStatus(ReservationStatus status);

    /** Holds whose payment window has elapsed; the scheduler cancels these to free inventory. */
    @Query("select r from Reservation r where r.status = :status and r.holdExpiresAt < :now")
    List<Reservation> findExpiredHolds(@Param("status") ReservationStatus status, @Param("now") Instant now);

    long countByStatus(ReservationStatus status);
}
