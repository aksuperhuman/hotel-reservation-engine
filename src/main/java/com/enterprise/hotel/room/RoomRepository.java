package com.enterprise.hotel.room;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByHotelId(Long hotelId);

    /**
     * PESSIMISTIC_WRITE acquires a row-level SELECT ... FOR UPDATE lock. Any other
     * transaction attempting to lock the same room row blocks until this transaction
     * commits. Used on the critical booking path to fully serialize inventory mutation
     * for a given room, guaranteeing consistency at the cost of throughput.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Room r where r.id = :id")
    Optional<Room> findByIdForUpdate(@Param("id") Long id);
}
