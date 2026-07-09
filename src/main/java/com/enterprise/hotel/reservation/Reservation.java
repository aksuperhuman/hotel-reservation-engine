package com.enterprise.hotel.reservation;

import com.enterprise.hotel.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String reference;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false)
    private int guests;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    /** When a PAYMENT_PENDING hold expires; used by the scheduler to release inventory. */
    @Column(name = "hold_expires_at")
    private Instant holdExpiresAt;

    @Version
    @Column(nullable = false)
    private Long version;
}
