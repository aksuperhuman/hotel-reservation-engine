package com.enterprise.hotel.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RoomDateLockRepository extends JpaRepository<RoomDateLock, RoomDateLock.PK> {

    /** Any existing rows here mean at least one requested night is already taken. */
    List<RoomDateLock> findByRoomIdAndBookedDateIn(Long roomId, List<LocalDate> dates);

    void deleteByReservationId(Long reservationId);
}
