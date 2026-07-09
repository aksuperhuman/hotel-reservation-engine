package com.enterprise.hotel.reservation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * One row per (room, date) that is held or booked. The composite primary key gives us a
 * hard database-level uniqueness guarantee: two concurrent bookings for the same room-night
 * cannot both insert, so double-booking is impossible even if every application lock fails.
 */
@Entity
@Table(name = "room_date_locks")
@IdClass(RoomDateLock.PK.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDateLock {

    @Id
    @Column(name = "room_id")
    private Long roomId;

    @Id
    @Column(name = "booked_date")
    private LocalDate bookedDate;

    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PK implements Serializable {
        private Long roomId;
        private LocalDate bookedDate;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PK pk)) return false;
            return Objects.equals(roomId, pk.roomId) && Objects.equals(bookedDate, pk.bookedDate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(roomId, bookedDate);
        }
    }
}
