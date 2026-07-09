package com.enterprise.hotel.room;

import com.enterprise.hotel.common.BaseEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room extends BaseEntity {

    @Column(name = "hotel_id", nullable = false)
    private Long hotelId;

    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    private RoomType roomType;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RoomStatus status = RoomStatus.AVAILABLE;

    /**
     * Optimistic-locking version. Hibernate increments this on every update and
     * throws {@link org.springframework.orm.ObjectOptimisticLockingFailureException}
     * if two transactions try to mutate the same row concurrently.
     */
    @Version
    @Column(nullable = false)
    private Long version;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "room_features", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "feature")
    @Builder.Default
    private Set<String> features = new HashSet<>();
}
